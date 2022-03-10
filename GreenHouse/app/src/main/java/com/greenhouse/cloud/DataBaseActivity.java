/**
 * As already said in Readme file, this application manages a greenhouse. A feature of this project is that
 * to insert GreenHouse's data inside a database. This in order to analysis how the place and a certain terrain
 * can improve life of a plant.
 *
 * Of course this app doesn't provide an AI for counting new plants or leaves. User has to do it, but once done,
 * this app thus will provide to insert data inside a database.
 *
 * Database is MySQL-based
 * As we already know, Android does not provide any method for query MySQL by itself as most of stuff.
 * Then we're gonna using a PHP RESTFul server which will be hosted on the same MySQL's server.
 *
 * When the user press Send Button, it will call as daemon the php script via HttpRequest
 *
 * Every time this activity is started, it will check for connection to Database.
 * Otherwise, it will be done on send button click
 */

package com.greenhouse.cloud;

import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import com.greenhouse.MainActivity;
import com.greenhouse.R;
import com.greenhouse.SeedActivity;
import com.greenhouse.TestFragment;
import com.greenhouse.cloud.HttpRest.HttpRestConnection;
import com.greenhouse.settings.Settings;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DataBaseActivity extends AppCompatActivity {

    private TextView DB_state;
    private Switch ghSwitch;
    private NumberPicker[] pickers;
    private TextView[] tvs;
    private DatePicker datePicker;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        this.loadComponents();
    }

    /**
     * Called when user presses send button.
     * It will send data to the connected database
     * @param v
     */
    public void sendData(View v) {
        new HttpRestConnection(this.DB_state, this.getApplicationContext(), this.getRequestParameter()).execute();
    }


    /**
     * Called once user presses Dismiss button
     * It will close layout and start the Main one
     *
     * Also it will disconnect from database
     * @param v
     */
    public void dismissCloudActivity(View v){
        this.finish();
    }


    /**
     * @return the values to have to be insert into database as a unique string,
     * every parameters as separated by '&'
     */
    private String getRequestParameter(){
        String params =
               "usr=" + Settings.username + "&"
             + "psw=" + Settings.password + "&"
             + "type_gh=" + (this.ghSwitch.isChecked() ? "o" : "i") + "&"
             + "date=" + this.datePicker.getYear() + "-" + this.datePicker.getMonth()+1 + "-" + this.datePicker.getDayOfMonth() + "&"
             + "max_height=" + this.pickers[0].getValue() + "&"
             + "plants=" + this.pickers[1].getValue() + "&"
             + "leaves=" + this.pickers[2].getValue();

        if(this.ghSwitch.isChecked()){
            params += "&temperature=" + this.pickers[3].getValue() + "&humidity=" + this.pickers[4].getValue();
        }

        Toast.makeText(this.getApplicationContext(), params, Toast.LENGTH_LONG).show();
        return params;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadComponents(){
        this.DB_state = findViewById(R.id.db_state_tv);

        this.datePicker = findViewById(R.id.date_picker);
        this.datePicker.setMaxDate(System.currentTimeMillis());

        this.tvs = new TextView[5];
        this.tvs[0] = findViewById(R.id.db_max_height_tv);
        this.tvs[1] = findViewById(R.id.db_plants_tv);
        this.tvs[2] = findViewById(R.id.db_leaves_tv);
        this.tvs[3] = new TextView(this.getApplicationContext());
        this.tvs[3].setText("Temperatura");
        this.tvs[3].setTextColor(Color.WHITE);
        this.tvs[3].setId(View.generateViewId());
        this.tvs[4] = new TextView(this.getApplicationContext());
        this.tvs[4].setText("Umidità");
        this.tvs[4].setTextColor(Color.WHITE);
        this.tvs[4].setId(View.generateViewId());

        this.pickers = new NumberPicker[5];
        this.pickers[0] = findViewById(R.id.db_max_height_picker);
        this.pickers[1] = findViewById(R.id.db_plants_picker);
        this.pickers[2] = findViewById(R.id.db_leaves_picker);

        this.pickers[3] = new NumberPicker(this.getApplicationContext());
        this.pickers[3].setId(View.generateViewId());
        this.pickers[4] = new NumberPicker(this.getApplicationContext());
        this.pickers[4].setId(View.generateViewId());

        for(int i = 0; i < 5; i++){
            this.pickers[i].setMaxValue( (i >= 3 ? 100 : 500) );
            this.pickers[i].setMinValue(0);
            this.pickers[i].setTextSize(20);
        }
        for(int i = 3; i < 5; i++){
            this.tvs[i].setTextSize(20);
            this.tvs[i].setTextColor(this.tvs[0].getTextColors());
            this.pickers[i].setMinimumWidth(ConstraintLayout.LayoutParams.MATCH_PARENT);
            this.pickers[i].setTextColor(this.pickers[i].getTextColor());
        }



        this.ghSwitch = findViewById(R.id.out_in_switch);
        this.ghSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConstraintLayout currentLayout = findViewById(R.id.db_pickers_layout);

            //If checked, means that we're gonna sending outside greenhouse's data
            if(isChecked){
                ConstraintSet set = new ConstraintSet();

                currentLayout.addView(this.tvs[3], 0);
                currentLayout.addView(this.tvs[4], 0);
                currentLayout.addView(this.pickers[3], 0);
                currentLayout.addView(this.pickers[4], 0);
                set.clone(currentLayout);

                set.connect(this.tvs[3].getId(), ConstraintSet.TOP, this.tvs[2].getId(), ConstraintSet.TOP);
                set.connect(this.tvs[3].getId(), ConstraintSet.LEFT,this.tvs[2].getId(), ConstraintSet.RIGHT, 20);
                set.connect(this.pickers[3].getId(), ConstraintSet.TOP, this.tvs[3].getId(), ConstraintSet.BOTTOM);
                set.connect(this.pickers[3].getId(), ConstraintSet.LEFT, this.tvs[3].getId(), ConstraintSet.LEFT);
                set.connect(this.pickers[3].getId(), ConstraintSet.RIGHT, this.tvs[3].getId(), ConstraintSet.RIGHT);

                set.connect(this.tvs[4].getId(), ConstraintSet.TOP, this.tvs[3].getId(), ConstraintSet.TOP);
                set.connect(this.tvs[4].getId(), ConstraintSet.LEFT, this.tvs[3].getId(), ConstraintSet.RIGHT, 20);
                set.connect(this.pickers[4].getId(), ConstraintSet.TOP, this.tvs[4].getId(), ConstraintSet.BOTTOM);
                set.connect(this.pickers[4].getId(), ConstraintSet.RIGHT, this.tvs[4].getId(), ConstraintSet.RIGHT);
                set.connect(this.pickers[4].getId(), ConstraintSet.LEFT, this.tvs[4].getId(), ConstraintSet.LEFT);

                set.connect(this.tvs[2].getId(), ConstraintSet.RIGHT, this.tvs[3].getId(), ConstraintSet.LEFT);

                set.applyTo(currentLayout);
            }else{
                currentLayout.removeView(this.tvs[3]);
                currentLayout.removeView(this.tvs[4]);
                currentLayout.removeView(this.pickers[3]);
                currentLayout.removeView(this.pickers[4]);
            }
        });
    }
}