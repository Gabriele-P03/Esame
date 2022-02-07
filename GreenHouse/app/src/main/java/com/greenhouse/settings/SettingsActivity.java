package com.greenhouse.settings;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.greenhouse.LoginActivity;
import com.greenhouse.MainActivity;
import com.greenhouse.R;
import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

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
            String buffer = this.psw.getText().toString();
            for(int i = 0; i < buffer.length(); i++){
                for(int j = 0; j < buffer.length(); j++){
                    if(i != j){
                        if(buffer.charAt(i) == buffer.charAt(j)) {
                            Toast.makeText(this.getApplicationContext(), "Password cannot have characters repeated", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
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
        if(this.getIntent().getBooleanExtra("login", true)){
            startActivity(new Intent(this.getApplicationContext(), LoginActivity.class));
        }else{
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
        }
    }
}