/**
 * As already said in Readme file, this application manages a greenhouse. A feature of this project is that
 * to insert GreenHouse's data inside a database. This in order to analysis how the place and a certain terrain
 * can improve life of a plant.
 *
 * Of course this app doesn't provide an AI for counting new plants or leaves. User has to do it, but once done,
 * this app thus will provide to insert data inside a database.
 *
 * Database is MySQL-based
 * As we already know, Android does not provide any method for query MySQL by itself as most of stuff.
 * Then we're gonna using a PHP RESTFul server which will be hosted on the same MySQL's server.
 *
 * When the user press Send Button, it will call as daemon the php script via HttpRequest
 *
 * Every time this activity is started, it will check for connection to Database.
 * Otherwise, it will be done on send button click
 */

package com.greenhouse.cloud;

import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.greenhouse.R;
import com.greenhouse.cloud.HttpRest.HttpRestConnection;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DataBaseActivity extends AppCompatActivity {

    private String ip;

    private EditText DB_ip;
    private TextView DB_state;
    private Switch ghSwitch;
    private NumberPicker[] pickers;
    private TextView[] tvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        this.loadComponents();
        this.loadIP();
    }


    /**
     * Called when user presses send button.
     * It will send data to the connected database
     * @param v
     */
    public void sendData(View v) {
        this.ip = this.DB_ip.getText().toString();
        if (this.isValidIP(this.ip)) {
            this.saveNewIP();
            new HttpRestConnection(this.ip, this.DB_state, this.getApplicationContext(), this.getRequestParameter())
                    .execute();

        }
    }


    /**
     * Called once user presses Dismiss button
     * It will close layout and start the Main one
     *
     * Also it will disconnect from database
     * @param v
     */
    public void dismissCloudActivity(View v){
        this.saveNewIP();
        this.finish();
    }


    /**
     * @return the values to have to be insert into database as a unique string,
     * every parameters as separated by '&'
     */
    private String getRequestParameter(){
        String params = "";

        params += "type_gh=" + (this.ghSwitch.isChecked() ? "o" : "i") + "&";
        params += "date='2022/01/01'&";
        params += "max_height=" + this.pickers[0].getValue() + "&";
        params += "plants=" + this.pickers[1].getValue() + "&";
        params += "leaves=" + this.pickers[2].getValue();

        if(this.ghSwitch.isChecked()){
            params += "&temperature=" + this.pickers[3].getValue() + "&humidity=" + this.pickers[4].getValue();
        }

        Toast.makeText(this.getApplicationContext(), params, Toast.LENGTH_LONG).show();
        return params;
    }

    /**
     * Called when this activity is closed. It will save the IP in memory as a TXT file.
     *
     * Before it will check, splitting by '.' if all 4 integer are correct
     */
    private void saveNewIP(){

        String text = this.DB_ip.getText().toString();

        if(this.isValidIP(text)){
            try {
                FileWriter fw = new FileWriter(new File(this.getApplicationContext().getFilesDir(), "ip.conf"));
                fw.write("ip="+text);
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this.getApplicationContext(), "Could not save IP...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Called before trying connecting to DB
     * It read last valid IP from memory
     */
    private void loadIP(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.getApplicationContext().getFilesDir(), "ip.conf")));
            String buffer = br.readLine();

            buffer = buffer.substring(3);
            this.ip = buffer;
            this.DB_ip.setText(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            new File(this.getApplicationContext().getFilesDir(), "ip.conf");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), "Could not read last valid IP...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param ip
     * @return if the given IP is valid
     */
    public boolean isValidIP(String ip){
        if(ip != null) {
            String[] single_byte = ip.split("\\.");

            if (single_byte.length == 4) {
                for (String sb : single_byte) {
                    try {
                        Integer.parseInt(sb);
                    } catch (Exception e) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    private void loadComponents(){
        this.DB_state = findViewById(R.id.db_state_tv);
        this.DB_ip = findViewById(R.id.db_ip_et);

        this.tvs = new TextView[2];
        this.tvs[0] = new TextView(this.getApplicationContext());
        this.tvs[0].setText("Temperatura");
        this.tvs[0].setTextColor(Color.WHITE);
        this.tvs[0].setId(View.generateViewId());
        this.tvs[1] = new TextView(this.getApplicationContext());
        this.tvs[1].setText("Umidità");
        this.tvs[1].setTextColor(Color.WHITE);
        this.tvs[1].setId(View.generateViewId());

        this.pickers = new NumberPicker[5];
        this.pickers[0] = findViewById(R.id.db_max_height_picker);
        this.pickers[1] = findViewById(R.id.db_plants_picker);
        this.pickers[2] = findViewById(R.id.db_leaves_picker);

        this.pickers[3] = new NumberPicker(this.getApplicationContext());
        this.pickers[3].setId(View.generateViewId());
        this.pickers[4] = new NumberPicker(this.getApplicationContext());
        this.pickers[4].setId(View.generateViewId());

        for(int i = 0; i < 5; i++){
            this.pickers[i].setMaxValue( (i >= 3 ? 100 : 500) );
            this.pickers[i].setMinValue(0);
        }

        this.ghSwitch = findViewById(R.id.out_in_switch);
        this.ghSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConstraintLayout currentLayout = findViewById(R.id.db_layout);

            //If checked, means that we're gonna sending outside greenhouse's data
            if(isChecked){
                ConstraintSet set = new ConstraintSet();

                currentLayout.addView(this.tvs[0], 0);
                currentLayout.addView(this.tvs[1], 0);
                currentLayout.addView(this.pickers[3], 0);
                currentLayout.addView(this.pickers[4], 0);
                set.clone(currentLayout);

                set.connect(this.tvs[0].getId(), ConstraintSet.TOP, this.pickers[0].getId(), ConstraintSet.BOTTOM, 20);
                set.connect(this.tvs[0].getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
                set.connect(this.pickers[3].getId(), ConstraintSet.TOP, this.tvs[0].getId(), ConstraintSet.BOTTOM, 5);
                set.connect(this.pickers[3].getId(), ConstraintSet.LEFT, this.tvs[0].getId(), ConstraintSet.LEFT);

                set.connect(this.tvs[1].getId(), ConstraintSet.TOP, this.pickers[2].getId(), ConstraintSet.BOTTOM, 20);
                set.connect(this.tvs[1].getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
                set.connect(this.pickers[4].getId(), ConstraintSet.TOP, this.tvs[1].getId(), ConstraintSet.BOTTOM, 5);
                set.connect(this.pickers[4].getId(), ConstraintSet.RIGHT, this.tvs[1].getId(), ConstraintSet.RIGHT);

                set.applyTo(currentLayout);
            }else{
                for(TextView tv : this.tvs)
                    currentLayout.removeView(tv);

                currentLayout.removeView(this.pickers[3]);
                currentLayout.removeView(this.pickers[4]);
            }
        });
    }
}