package com.greenhouse.cloud;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
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

public class LoginRequest extends AsyncTask<String, Integer, String> {

    private URL url;
    private HttpURLConnection httpURLConnection;
    private Context context;

    public LoginRequest(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            if(strings.length != 2){
                return "-1";
            }

            this.url = new URL("http://" + Settings.IP + ":" + Settings.port + "/Login.php?usr=" +
                    strings[0] +
                    "&psw=" + strings[1]);
            this.httpURLConnection = (HttpURLConnection) this.url.openConnection();

            //Wait the response from RESTFul Server for checking if credentials are valid
            InputStream is = this.httpURLConnection.getInputStream();
            byte[] bytes = new byte[256];
            is.read(bytes);
            String result = new String(bytes, StandardCharsets.UTF_8);

            //200 in HTTP means all ok, then it is gonna starting MainActivity after have saved new credentials
            if(result.trim().equals("200")) {
                Settings.username = strings[0];
                Settings.password = strings[1];

                //If user has chose to save credentials...
                if(Settings.rememberLogin)
                    Settings.saveConf(this.context, Settings.IP, Settings.port, strings[0], strings[1]);

                return "200";
            }else{
                return result;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "-1";
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.trim().equals("200"))
            context.startActivity(new Intent(context, MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
    }
}
