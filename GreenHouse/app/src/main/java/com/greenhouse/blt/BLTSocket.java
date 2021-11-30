package com.greenhouse.blt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.greenhouse.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Activity for showing all bluetooth devices already bonded, even if they're not available now.
 * Their availability is checked once a device is clicked. If yes, smartphone will connect to bluetooth
 * device - HC05 in this case.
 *
 * Once connected @isBtConnected is set to true and its stream are retrieved
 *
 * If HC05 turns off, a few seconds later, due to not being able to receive other data, the try-catch block
 * at {@link BLTSocket#survey()} will throw an {@link IOException} which close socket and streams, disconnect device and set @isBtConnected false
 *
 * @author GABRIELE-P03
 */
public class BLTSocket extends Service {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static boolean isBtConnected = false;
    private static BluetoothAdapter adapter;
    private static BluetoothSocket socket;
    private static BluetoothDevice device;
    private static InputStream is;
    private static OutputStream os;

    /**
     * Constructor called when a bluetooth device has been clicked in the layout
     * If the phone's bluetooth socket has not already connected , it tries to initialize
     * the new connection with the one clicked.
     * Get the stream and set @isBtConnected as true
     *
     * @param address - MAC address of the bluetooth device
     */
    public BLTSocket(String address, Context context){
        if (!isBtConnected || !socket.isConnected()) {
            try {
                adapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                device = adapter.getRemoteDevice(address);
                socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                socket.connect();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                isBtConnected = true;
                Toast.makeText(context, "Device Selected: " + device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();

            }catch (IOException e) {

                try {
                    socket.close();
                    device = null;
                    adapter = null;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                e.printStackTrace();
                Toast.makeText(context, "Device actually not available. Check if it is on...", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "You have already connected to another bluetooth device", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Being a service, this class must have a default constructor with no parameters
     */
    public BLTSocket() {
    }

    @Override
    public void onCreate() {
        HandlerThread handlerThread = new HandlerThread("SURVEYS_SERVICE", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        survey();
        return START_STICKY;
    }

    /**
     * This handler keeps updating the three surveys reading their current value
     * from the bluetooth module's input stream.
     * Set the the circular progress bar and the own text view below as the relative value
     */
    Handler handler = new Handler();
    Runnable runnable;
    private void survey() {

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                if(!socket.isConnected()){
                    handler.removeCallbacks(runnable);
                    BLTSocket.this.onDestroy();
                    return;
                }

                byte[] survey = new byte[12];
                try {
                    /*
                        Once got the current values of the three surveys,
                        it sets the relative progress bar and the text view
                     */
                    if(is.read(survey, 0, 12) >= 0){
                        for(int i = 0; i < 3; i++){

                            int x = survey[i];

                            MainActivity.progressBars[i].setProgress(x);
                            MainActivity.textViews[i].setText(String.valueOf(x) + (i == 0 ? "°C" : '%'));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    //For closing socket, streams and isBtConnected false
                    BLTSocket.this.onDestroy();

                    Toast.makeText(getApplicationContext(), "Error during receiving data. Seems like device is disconnected...", Toast.LENGTH_SHORT).show();
                    return;//Return for not run handler again
                }
                handler.postDelayed(this, 1000);
            }
        };
        runnable.run();

    }

    /**
     * Called when the user click on the UPDATE SEED button
     * on the popup menu of the ViewSeed layout
     *
     * Send to the HC-05 the new recommended values
     *
     * @param string
     */
    public void updateSeed(String string, Context context){

        if(socket.isConnected()) {

            //Remove names and split with regex '-'
            String[] lines = string
                    .replace("Temperature: ", "")
                    .replace("Humidity: ", "")
                    .replace("Light: ", "")
                    .replace("\n", "-")
                    .replace(" ", "")
                    .split("-");

            if (lines.length == 6) {
                try {
                    handler.removeCallbacks(runnable);

                    for (String value : lines) {
                        byte[] bytes = new byte[2];
                        bytes[0] = (byte) (Integer.parseInt(value) >> 8);
                        bytes[1] = (byte) (Integer.parseInt(value) & 0xFF);
                        os.write(bytes);
                    }
                    os.flush();
                    Toast.makeText(context, "Values have been sent successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Values have not been sent successfully", Toast.LENGTH_SHORT).show();
                } finally {
                    handler.postDelayed(runnable, 2000);
                }
            }else {
                Toast.makeText(context, "Invalid values:\n" + string, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Close the socket and set @isBtConnected as false when the app is closed or crashes
     */
    @Override
    public void onDestroy() {
        try {
            is.close();
            os.close();
            socket.close();
            isBtConnected = false;
            BluetoothActivity.bltSocket = null;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean isBltConnected() {
        return isBtConnected;
    }
}