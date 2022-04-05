package com.greenhouse.cloud;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.greenhouse.MainActivity;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.settings.Settings;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * It provides authentication sending credentials to RESTFul Server and waiting 'till is responds
 * if they're valid. If they're, MainActivity will be started
 *
 * @author Gabriele-P03
 */

public class LoginRequest extends Task {


    public LoginRequest(Context context){
        super(context);
    }


    @Override
    protected Integer doInBackground(String... strings) {

        //Super-call opens the connection with the server
        int responseCode = super.doInBackground(strings);

        //200 in HTTP means all ok, then it is gonna starting MainActivity after have saved new credentials
        if(responseCode == 200){
            Settings.username = strings[0];
            Settings.password = strings[1];
        }

        return responseCode;
    }




    /**
     * Called once that Login process is over.
     * Once response message has been received, it starts the {@link MainActivity} after have added, in according to documentation,
     * FLAG_ACTIVITY_NEW_TASK 'cause it is gonna be started by an Async-Task.
     * Otherwise, in case of error, if credentials are not valid and they've been saved, they will be deleted, instead for
     * any other errors, status code will be printed
     * @param result
     */
    @Override
    protected void onPostExecute(Integer result) {

        if(result.intValue() == 200) {
            if(Settings.rememberLogin) {
                try { Settings.saveConf(this.context); } catch (IOException e) { e.printStackTrace(); }
            }
            context.startActivity(new Intent(context, MainActivity.class)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("grade", Integer.parseInt(this.result)));
        }
        else if (result.intValue() == 401){
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, Settings.IP + ":" + Settings.port + " response status code: " + result.intValue() + " - " + this.result, Toast.LENGTH_LONG).show();
        }
    }
}
