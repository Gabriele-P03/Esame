package com.greenhouse.cloud.collector;

import android.content.Context;
import android.widget.Toast;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.json.JSONObject;
import com.greenhouse.settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is a collector of every users retrieved by the SQL Query
 *
 * They're stored inside an ArrayList
 * The users are transported via JSON API
 */

public class UserCollector {

    private ArrayList<User> USERS;
    private UserCollectorTask collectorTask;

    /**
     * The default constructor retrieves all users available in the database
     */
    public UserCollector(Context context) {
        this.collectorTask = new UserCollectorTask(context);
        this.USERS = new ArrayList<>();
    }

    public UserCollector(ArrayList<User> USERS) { this.USERS = USERS; }
    public UserCollector(JSONObject jsonObject) { }

    public ArrayList<User> getUSERS() {
        return USERS;
    }



    private class UserCollectorTask extends Task {

        JSONObject jsonObject;

        public UserCollectorTask(Context context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            //Super-call opens the connection with the server
            int responseCode = super.doInBackground(strings);
            if(responseCode == 200){
                try {
                    this.URL = new URL("http://" + Settings.IP + ":" + Settings.port + "employer/GetEmployee.php");
                    this.httpURLConnection = (HttpURLConnection) this.URL.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));

                    String buffer = "";
                    this.result = "";
                    while( (buffer = br.readLine()) != null){
                        this.result += buffer;
                    }

                    return this.httpURLConnection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer s) {

            if(s.intValue() != 200) {
                Toast.makeText(this.context, s.intValue() + " : " + this.result, Toast.LENGTH_SHORT).show();
            }else{
                //Let's decode the json retrieved...
                this.jsonObject = new JSONObject(this.result);
            }

        }
    }
}
