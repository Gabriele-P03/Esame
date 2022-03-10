package com.greenhouse.cloud;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.greenhouse.LoginActivity;
import com.greenhouse.MainActivity;
import com.greenhouse.settings.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * It provides authentication sending credentials to RESTFul Server and waiting 'till is responds
 * if they're valid. If they're, MainActivity will be started
 *
 * @author Gabriele-P03
 */

public class LoginRequest extends AsyncTask<String, String, Integer> {

    private URL url;
    private HttpURLConnection httpURLConnection;
    private Context context;

    private String result;

    public LoginRequest(Context context){
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {

            this.url = new URL("http://" + Settings.IP + ":" + Settings.port + "/Login.php?usr=" +
                    strings[0] +
                    "&psw=" + strings[1]);
            this.httpURLConnection = (HttpURLConnection) this.url.openConnection();

            int statusCode = this.httpURLConnection.getResponseCode();

            //Wait the response from RESTFul Server for checking if credentials are valid
            InputStream is = this.httpURLConnection.getInputStream();
            byte[] bytes = new byte[256];
            is.read(bytes);
            this.result = new String(bytes, StandardCharsets.UTF_8);

            //200 in HTTP means all ok, then it is gonna starting MainActivity after have saved new credentials
            if(statusCode == 200) {
                Settings.username = strings[0];
                Settings.password = strings[1];

                Settings.saveConf(this.context);
            }
            return statusCode;

        } catch (IOException e) {
            e.printStackTrace();
            this.publishProgress(e.getMessage());
            return -2;
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
    }

    /**
     * Called once that Login process is over.
     * Once response message has been received, it starts the {@link MainActivity} after have added, in according to documentation,
     * FLAG_ACTIVITY_NEW_TASK 'cause it is gonna be started by an Async-Task.
     * Otherwise, in case of error, if credentials are not valid and they've been saved, they will be deleted, instead for
     * any other errors, status code will be printed
     * @param statusCode
     */
    @Override
    protected void onPostExecute(Integer statusCode) {

        if(statusCode.intValue() == 200)
            context.startActivity(new Intent(context, MainActivity.class)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("grade", this.result));
        else if (statusCode.intValue() == 401){
            /*
                RESTFul Server returns -1 if username or password are invalid
                It reset username and password
             */
            try {
                Settings.username = "";
                Settings.password = "";
                Settings.rememberLogin = false;
                Settings.saveConf(context);
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(context, Settings.IP + ":" + Settings.port + " response status code: " + statusCode.intValue() + " - " + this.result, Toast.LENGTH_LONG).show();
        }
    }
}
