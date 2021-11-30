package com.greenhouse;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SeedActivity extends AppCompatActivity {

    private NumberPicker[] pickers = new NumberPicker[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed);

        this.pickers[0] = findViewById(R.id.min_temp_picker);
        this.pickers[1] = findViewById(R.id.max_temp_picker);
        this.pickers[2] = findViewById(R.id.min_humidity_picker);
        this.pickers[3] = findViewById(R.id.max_humidity_picker);
        this.pickers[4] = findViewById(R.id.min_light_picker);
        this.pickers[5] = findViewById(R.id.max_light_picker);

        for (NumberPicker picker : this.pickers) {
            picker.setMaxValue(100);
            picker.setMinValue(0);
        }

        this.pickers[0].setValue(MainActivity.getSEED().getMinTemp());
        this.pickers[1].setValue(MainActivity.getSEED().getMaxTemp());
        this.pickers[2].setValue(MainActivity.getSEED().getMinHum());
        this.pickers[3].setValue(MainActivity.getSEED().getMaxHum());
        this.pickers[4].setValue(MainActivity.getSEED().getMinLight());
        this.pickers[5].setValue(MainActivity.getSEED().getMaxLight());
    }

    /**
     * Called when user presses the save_new_seed_button.
     * Save in memory the new seed's values, but before it checks if values are correct
     * @param v
     */
    public void saveNewSeed(View v){

        //Checking if values are correct (Min are less than Maxes)
        if(this.pickers[0].getValue() > this.pickers[1].getValue()){
            Toast.makeText(this.getApplicationContext(), "Min temperature greater than max...", Toast.LENGTH_SHORT).show();
        }else if(this.pickers[2].getValue() > this.pickers[3].getValue()){
            Toast.makeText(this.getApplicationContext(), "Min humidity greater than max...", Toast.LENGTH_SHORT).show();
        }else if(this.pickers[4].getValue() > this.pickers[5].getValue()){
            Toast.makeText(this.getApplicationContext(), "Min light greater than max...", Toast.LENGTH_SHORT).show();
        }else {

            MainActivity.getSEED().setMinTemp(this.pickers[0].getValue());
            MainActivity.getSEED().setMaxTemp(this.pickers[1].getValue());
            MainActivity.getSEED().setMinHum(this.pickers[2].getValue());
            MainActivity.getSEED().setMaxHum(this.pickers[3].getValue());
            MainActivity.getSEED().setMinLight(this.pickers[4].getValue());
            MainActivity.getSEED().setMaxLight(this.pickers[5].getValue());

            MainActivity.getSEED().saveNewSeed(this.getApplicationContext());

            this.finish();
        }
    }

    public void dismissNewSeed(View v){
        this.finish();
    }
}