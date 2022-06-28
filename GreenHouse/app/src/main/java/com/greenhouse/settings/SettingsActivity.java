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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SettingsActivity extends AppCompatActivity {

    private EditText ip, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.ip = findViewById(R.id.settings_ip);
        this.ip.setText(Settings.IP);
        this.port = findViewById(R.id.settings_port);
        this.port.setText(Settings.port);
    }


    /**
     * Called when user presses on Save button
     * @param v
     */
    public void saveSettings(View v){
        try {
            Settings.IP = this.ip.getText().toString();
            Settings.port = this.port.getText().toString();
            Settings.saveConf(this.getApplicationContext());
            this.dismissActivitySettings(this.getCurrentFocus());
        } catch (IOException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
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