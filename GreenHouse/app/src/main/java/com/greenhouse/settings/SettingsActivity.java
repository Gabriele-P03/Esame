package com.greenhouse.settings;

import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.greenhouse.R;
import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private File file;
    private EditText ip, port, usr, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.ip = findViewById(R.id.settings_ip);
        this.ip.setText(Settings.IP);
        this.port = findViewById(R.id.settings_port);
        this.port.setText(Settings.port);
        this.usr = findViewById(R.id.settings_usr);
        this.usr.setText(Settings.username);
        this.psw = findViewById(R.id.settings_psw);
        this.psw.setText(Settings.password);
    }


    /**
     * Called when user presses on Save button
     * @param v
     */
    public void saveSettings(View v){
        try {
            Settings.saveConf(this.getApplicationContext(), this.ip.getText().toString(), this.port.getText().toString(), this.usr.getText().toString(), this.psw.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when user presses on Dismiss button
     * @param v
     */
    public void dismissActivitySettings(View v){
        this.finish();
    }
}