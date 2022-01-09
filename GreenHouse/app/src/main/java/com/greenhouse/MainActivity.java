/**
 * This is the main activity of my secondary school final exam which consists of a greenhouse automatically controlled
 * by Arduino, which checks temperature, humidity and light inside it and controls fans, and by this android app which
 * receives from HC-05 (Bluetooth device for Arduino) values defined before (temp, hum and light).
 *
 * Min and Max values of temperature, humidity and light are set via this application.
 * User, once have written values into @valuesTV, should press @seedUpdateButton which will send to HC-05 those values.
 * Arduino will receive them and a function will save them into AtMega328P's EEPROM and set relative arrays.
 * As I said in Arduino program's documentation, writing EEPROM is limited: about 2000 times...
 *
 * For more info about arduino part, check gitHub:
 *
 * @author Gabriele-P03
 *
 *
 */

package com.greenhouse;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.greenhouse.blt.BluetoothActivity;
import com.greenhouse.cloud.DataBaseActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * progressBar[0]: temperaturePB
     * progressBar[1]: humidityPB
     * progressBar[2]: lightPB;
     */
    public static ProgressBar[] progressBars = new ProgressBar[3];

    /**
     * textViews[0]: temperature's textView
     * textViews[1]: humidity's textView
     * textViews[2]: light's textView
     */
    public static TextView[] textViews = new TextView[3];

    private TextView valuesTV;
    private ImageButton bltButton, seedUpdateButton;

    private static Seed SEED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.progressBars[0] = findViewById(R.id.temperature_progress_bar);
        this.progressBars[1] = findViewById(R.id.humidity_progress_bar);
        this.progressBars[2] = findViewById(R.id.light_progress_bar);

        this.textViews[0] = findViewById(R.id.temperature_tv);
        this.textViews[1] = findViewById(R.id.humidity_tv);
        this.textViews[2] = findViewById(R.id.light_tv);

        this.valuesTV = findViewById(R.id.values_tv);
        this.bltButton = findViewById(R.id.blt_button);
        this.seedUpdateButton = findViewById(R.id.update_seed_button);

        SEED = Seed.readSeedFromFile(this.getApplicationContext());
    }

    /**
     * Called once user presses bluetooth button.
     * Open paired bluetooth device available popup window.
     * Socket will connect to the clicked one.
     *
     * @param v
     */
    public void bltButtonOnClick(View v){
        startActivity(new Intent(this.getApplicationContext(), BluetoothActivity.class));
    }

    /**
     * Called once user presses cloud button
     * @param v
     */
    public void cloudButtonOnClick(View v){
        startActivity(new Intent(this.getApplicationContext(), DataBaseActivity.class));
    }

    /**
     * Called once user presses seed update button.
     * It will run a little daemon function which sends to HC-05 new
     * min and max values about temp, humidity and light.
     *
     * @param v
     */
    public void seedUpdateButton(View v) {
        if (BluetoothActivity.bltSocket != null && BluetoothActivity.bltSocket.isBltConnected()) {
                BluetoothActivity.bltSocket.updateSeed(this.valuesTV.getText().toString(), this.getApplicationContext());
        }else{
            Toast.makeText(this.getApplicationContext(), "No bluetooth device connected...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when user presses on current values text view (even if it is not a button).
     * It will open a popup window which let user inserts new values.
     * Every time a new seed is set, its values will be saved in a text file
     * @param v
     */
    public void valuesTVPressed(View v){
        startActivity(new Intent(this.getApplicationContext(), SeedActivity.class));
    }

    public static Seed getSEED() {
        return SEED;
    }


    /**
     * Called when this activity is resumed, it reloads seed's values
     */
    @Override
    protected void onResume() {

        super.onResume();
        if (SEED != null) {
            this.valuesTV.setText(
                    "Temperature: " + SEED.getMinTemp() + " - " + SEED.getMaxTemp() + "\n" +
                    "Humidity: " + SEED.getMinHum() + " - " + SEED.getMaxHum() + "\n" +
                    "Light: " + SEED.getMinLight() + " - " + SEED.getMaxLight());
        }else{
            this.valuesTV.setText("Tap here to insert a new seed");
        }
    }
}