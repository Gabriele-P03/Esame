package com.greenhouse.blt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.greenhouse.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Activity started when the user clicks on the Bluetooth button in the main layout
 * Once activated own bluetooth radio, it searches the already compared device which are available
 *
 * When the user clicks on one, the application instances a new BLTSocket to set the connection,
 * passing to the constructor the MAC address of the bluetooth device clicked
 *
 * @see BLTSocket
 */
public class BluetoothActivity extends AppCompatActivity {


    private BluetoothAdapter adapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView listBLTDevice;
    private TextView bltTV;

    private BluetoothDevice deviceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        this.bltTV = findViewById(R.id.bltTV);
        this.listBLTDevice = findViewById(R.id.listBLTDevices);
        setBlt();
    }

    public static BLTSocket bltSocket;

    private void setBlt(){
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        //If the adapter is null, the user's phone hasn't a bluetooth radio
        if(adapter != null){
            if(!adapter.isEnabled()){
                //If the bluetooth is disabled, it asks to the user to enable it

                registerForActivityResult(
                        new StartActivityForResult(),
                        result -> {
                            if(result.getResultCode() != RESULT_OK){
                                BluetoothActivity.this.adapter = null;
                                BluetoothActivity.this.finish();
                            }
                        }
                ).launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }

            //The bonded devices are those ones which have already been compared
            pairedDevices = adapter.getBondedDevices();
            this.bltTV.setText(pairedDevices.size() + " Devices Found");

            if(pairedDevices.size() > 0){

                /*
                 *  Create a new list of strings which every one contains the name of one compared
                 *  bluetooth radio. The array list is given to the adapter.
                 */
                final ArrayList<String> arrayList = new ArrayList<>();
                for (BluetoothDevice device : pairedDevices){
                    arrayList.add(device.getName());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_whitetext_layout, arrayList);
                this.listBLTDevice.setAdapter(adapter);

                /*
                    When an item (string that represents a bluetooth radio) has been clicked,
                    its MAC address is given to the BLTSocket constructor, which will set
                    the opportune variable, objects and connects our radio with the selected one
                 */
                this.listBLTDevice.setOnItemClickListener( (parent, v, pos, id) -> {
                    if(deviceSelected == null) {
                        deviceSelected = (BluetoothDevice) pairedDevices.toArray()[pos];
                        bltSocket = new BLTSocket(deviceSelected.getAddress(), this.getApplicationContext());
                        if (bltSocket.isBltConnected() && getSystemServiceName(BLTSocket.class) == null) {
                            //startService(new Intent(this, BLTSocket.class));
                        }else{
                            deviceSelected = null;
                        }
                    }else{
                        Toast.makeText(this.getApplicationContext(), "You have already connected to another device", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), "Seems like your device hasn't any bluetooth adapter...", Toast.LENGTH_SHORT).show();
        }
    }

}