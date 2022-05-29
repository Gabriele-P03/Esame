/**
 *
 * The main abstraction of a task which will be sent as SQL-Query to the RESTFul Server
 *
 * As HTTP 1.1 is a pipeline-based protocol, it is also asynchronous. Packet must be sent via
 * AsyncTask
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud.jobs;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.greenhouse.settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, String, Integer> {

    //It contains the url of the page
    //e.g. https://IP_SERVER:PORT/page.php
    protected java.net.URL URL;
    protected String urlAsString;
    protected Context context;

    protected HttpURLConnection httpURLConnection;
    protected String result;

    public Task(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        try {

                //For security reasons, before every query, even if the user is already authenticated, it must be done again
                this.URL = new URL("http://" + Settings.IP + ":" + Settings.port + "/login.php?usr=" + strings[0] + "&psw=" + strings[1]);
                this.httpURLConnection = (HttpURLConnection) this.URL.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));

                String buffer = "";
                this.result = "";
                while( (buffer = br.readLine()) != null){
                    this.result += buffer;
                }

                return this.httpURLConnection.getResponseCode();

        }catch (MalformedURLException e){
            e.printStackTrace();
            this.result = "URL invalid: " + this.urlAsString;
        }catch (IOException e) {
            e.printStackTrace();
            this.result = "Connection could not be opened: " + e;
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer s) {

        //If no errors occurred, it doesn't matter to print any toast
        if(s.intValue() != 200)
            Toast.makeText(this.context, " " + s.intValue() + ": " + this.result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, (values.length > 0 ? values[0]  : ""), Toast.LENGTH_SHORT).show();
    }
}
