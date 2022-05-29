package com.greenhouse.cloud.collector;

import android.content.Context;
import android.widget.Toast;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.json.JSONArray;
import com.greenhouse.json.JSONObject;
import com.greenhouse.json.JSONReader;
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

    /**
     * The default constructor retrieves all users available in the database
     */
    public UserCollector(Context context) {
        this.USERS = new ArrayList<>();
        new UserCollectorTask(context).execute();
    }


    public ArrayList<User> getUSERS() {
        return USERS;
    }



    private class UserCollectorTask extends Task {

        private JSONArray usersArrayJSON;

        public UserCollectorTask(Context context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(String... strings) {

                try {
                    this.URL = new URL("http://" + Settings.IP + ":" + Settings.port + "/employer/GetEmployee.php");
                    this.httpURLConnection = (HttpURLConnection) this.URL.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));

                    String buffer = "";
                    this.result = "";
                    while( (buffer = br.readLine()) != null){
                        this.result += buffer;
                    }

                    return this.httpURLConnection.getResponseCode();
                } catch (IOException e) {
                    this.result = e.getMessage();
                    return -1;
                }
            }

        @Override
        protected void onPostExecute(Integer s) {

            if(s.intValue() != 200) {
                Toast.makeText(this.context, s.intValue() + " : " + this.result, Toast.LENGTH_SHORT).show();
            }else{
                //Let's decode the json retrieved...
                this.usersArrayJSON = new JSONReader(this.result).getRootArray();
                if(this.usersArrayJSON != null) {
                    for (JSONObject obj : this.usersArrayJSON.getObjects()) {
                        if(obj.getMaps().size() == 8)   //Without password's field, it is composed of 8 fields
                            USERS.add(new User(obj));
                    }
                }
            }

        }
    }
}
