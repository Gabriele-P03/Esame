/**
 * As already said in Readme file, this application manages a greenhouse. A feature of this project is that
 * to insert GreenHouse's data inside a database. This in order to analysis how the place and a certain terrain
 * can improve life of a plant.
 *
 * Of course this app doesn't provide an AI for counting new plants or leaves. User has to do it, but once done,
 * this app thus will provide to insert data inside a database.
 *
 * Database is MySQL-based
 *
 * Every time this activity is started, it will check for connection to Database.
 * Otherwise, it will be done on send button click
 */

package com.greenhouse.cloud;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.greenhouse.R;

import java.io.*;

public class DataBaseActivity extends AppCompatActivity {

    private String ip;
    private MySQLConnection mySQLConnection;

    private EditText DB_ip;
    private TextView DB_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        this.DB_state = findViewById(R.id.db_state_tv);
        this.DB_ip = findViewById(R.id.db_ip_et);
        this.loadIP();
        this.connect();
    }

    /**
     * Called for initializing connection with a db
     */
    private void connect(){
        if(!this.isConnected()){
            this.settingNewDBState("Connecting...");
            if(this.isValidIP(this.ip)){
                this.mySQLConnection = new MySQLConnection(this.ip, "3306", "GH");
            }else{
                this.settingNewDBState("IP invalid...");
            }
        }
    }

    /**
     * Called when user presses send button.
     * It will send data to the connected database
     * @param v
     */
    public void sendData(View v){
        if(!this.isConnected()){
            this.connect();
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
        this.disconnect();
        this.saveNewIP();
        this.finish();
    }

    /**
     * Called for disconnecting from db
     */
    private void disconnect(){
        if(this.isConnected()){
            this.settingNewDBState("Disconnecting...");
        }
    }

    /**
     * @return if it is connected to a db
     */
    public boolean isConnected(){
        return this.mySQLConnection == null ? false : this.mySQLConnection.isConnected();
    }

    private void settingNewDBState(String info){
        this.DB_state.setText(info);
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
}