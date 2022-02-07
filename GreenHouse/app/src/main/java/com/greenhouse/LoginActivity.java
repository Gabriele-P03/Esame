package com.greenhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import com.greenhouse.cloud.LoginRequest;
import com.greenhouse.settings.Settings;
import com.greenhouse.settings.SettingsActivity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * This is the first layout which is tried to be loaded.
 * It is loaded only if user has not selected the check box which allows
 * application to remember last credentials used by the user.
 * It is loaded even once credentials are invalid
 *
 * Otherwise the MainActivity is started...
 *
 * @author Gabriele-P03
 */

public class LoginActivity extends Activity {

    private EditText usr, psw;
    private ImageButton settingsBT;
    private Button loginBT;
    private CheckBox rememberLoginCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
            Before log-in layout is shown, it is gonna check if the check box which
            allows to remember last credentials has been selected previously
         */
        try {
            Settings.loadConf(this.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If previously user didn't chose to remember credentials, it will load activity's views and then show it
        //It doesn't mean that user will never be able to modify own credentials, in fact it can do it via Settings Button
        //from MainActivity's layout
        if(!Settings.rememberLogin)
            this.loadCompos();
        else
            new LoginRequest(this.getApplicationContext()).execute(Settings.username, Settings.password);
    }

    /**
     * Called for loading all components of this layout
     */
    private void loadCompos(){
        this.usr = findViewById(R.id.login_usr);
        this.psw = findViewById(R.id.login_psw);
        this.settingsBT = findViewById(R.id.settings_login_button);
        this.loginBT = findViewById(R.id.login_button);
        this.rememberLoginCB = findViewById(R.id.remember_login);

        this.settingsBT.setOnClickListener(l -> {
            Intent i = new Intent(this.getApplicationContext(), SettingsActivity.class);
            /*
                Put true if the intent is started from LoginActivity.
                This boolean value will be used once user comes out from settings activity.
                If it is true, it will be redirect into LoginLayout otherwise in MainLayout
             */
            i.putExtra("login", true);

            startActivity(i);
        });

        this.loginBT.setOnClickListener(l -> {
            Settings.rememberLogin = this.rememberLoginCB.isChecked();
            new LoginRequest(this.getApplicationContext()).execute(Settings.username, Settings.password);
        });
    }
}