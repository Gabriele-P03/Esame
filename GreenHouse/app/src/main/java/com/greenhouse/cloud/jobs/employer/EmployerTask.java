/**
 * As asked in the technical specifications, the employer is enabled to:
 * - Watch data
 * - Edit Data
 * - Remove Data
 * - Apply a new employee
 * - Lay-off an employee
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud.jobs.employer;

import android.content.Context;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EmployerTask extends Task {

    public EmployerTask(Context context) {
        super(context);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int statusCode = super.doInBackground(strings);

        //Re-authentication successful
        if(statusCode == 200){
            try {
                this.urlAsString = "http://" + Settings.IP + ":" + Settings.port + "/employer/Select.php" +
                        "?usr=" + Settings.username + "&psw=" + Settings.password;

                for(String string : strings){
                    this.urlAsString += "&" + string;
                }

                this.URL = new URL(this.urlAsString);
                this.httpURLConnection = (HttpURLConnection) this.URL.openConnection();
                this.result = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream())).readLine();
                return this.httpURLConnection.getResponseCode();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return statusCode;
    }
}
