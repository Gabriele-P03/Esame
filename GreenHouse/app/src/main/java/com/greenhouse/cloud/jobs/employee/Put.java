/**
 * As already known, Android does not provide any method for querying MySQL without any API.
 * Thus this is an interface for communicating with the RESTFul server hosted on the same MySQL's server.
 *
 * It is gonna using HttpRequest
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud.jobs.employee;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.settings.Settings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Put extends Task {

    public Put(Context context) {
        super(context);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        //Super-call opens the connection with the server
        int responseCode = super.doInBackground(strings);
        if(responseCode == 200){
            try {
                this.URL = new URL("http://" + Settings.IP + ":" + Settings.port + "/employee/Put.php?" + strings[0]);
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
        Toast.makeText(this.context, s.intValue() + " : " +this.result, Toast.LENGTH_SHORT).show();
    }
}
