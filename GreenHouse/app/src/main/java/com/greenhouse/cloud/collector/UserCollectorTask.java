package com.greenhouse.cloud.collector;

import android.content.Context;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.settings.Settings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This task provides retrieving users already filtered by the POST-Http
 * Users retrieved are stored inside an instance of {@UserCollector}
 */

public class UserCollectorTask extends Task {

    public UserCollectorTask(Context context) {
        super(context);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        //The login
        int login_status_code = super.doInBackground(strings);
        if(login_status_code == 200){

            //Login correct
            try {
                if(strings.length > 0) {
                    this.URL = new URL("http://" + Settings.IP + ":" + Settings.port + "/get_users.php?" + strings[0]);
                    this.httpURLConnection = (HttpURLConnection) this.URL.openConnection();
                }else{
                    throw new RuntimeException("Not enough parameters...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            //Error
            super.onPostExecute(login_status_code);
        }

        return 0;
    }
}
