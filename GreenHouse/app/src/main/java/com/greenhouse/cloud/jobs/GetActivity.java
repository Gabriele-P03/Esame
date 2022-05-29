/**
 * GetActivity is the one which provides GET CRUD method for data from MySQL-DB
 * It is used by Analyst, CEO and COO; they all are able to get data inserted by all employees.
 * These last ones are able to get only the own ones
 *
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud.jobs;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.greenhouse.R;
import com.greenhouse.cloud.collector.UserCollector;
import com.greenhouse.cloud.collector.UsersListAdapter;

public class GetActivity extends AppCompatActivity {

    private Button getButton;
    private CheckBox insideGH, outsideGH;
    private ListView listViewUsers;

    //Is is used only to set constraint automatically
    private TextView max_leaves_tv;

    /*
        Min Height, Max Height, Min Plants, Max Plants, Min Leaves, Max Leaves

        Shown on outsideGH om: Min Temperature, Max Temperature, Min Humidity, Max Humidity, Min Light, Max Light
     */
    private NumberPicker[] pickers = new NumberPicker[12];

    private TextView[] textViews = new TextView[6]; //Only for outside's pickers
    private static final String[] textViewsText = new String[]{
            "Min Temperature", "Max Temperature", "Min Humidity", "Max Humidity", "Min Light", "Max Light"
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        this.loadComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadComponents() {

        this.max_leaves_tv = findViewById(R.id.get_max_leaves_tv);
        this.listViewUsers = findViewById(R.id.users_list_view_get_activity);
        this.getButton = findViewById(R.id.get_data_button);
        this.getButton.setOnClickListener(l -> performGetData());

        this.pickers[0] = findViewById(R.id.get_min_height_picker);
        this.pickers[1] = findViewById(R.id.get_max_height_picker);
        this.pickers[2] = findViewById(R.id.get_min_plants_picker);
        this.pickers[3] = findViewById(R.id.get_max_plants_picker);
        this.pickers[4] = findViewById(R.id.get_min_leaves_picker);
        this.pickers[5] = findViewById(R.id.get_max_leaves_picker);

        for(int i = 0; i < 6; i++){
            this.pickers[6+i] = new NumberPicker(this.getApplicationContext());
            this.pickers[6+i].setId(View.generateViewId());
            this.pickers[6+i].setTextSize(20);
            this.pickers[6+i].setTextColor(Color.WHITE);

            this.textViews[i] = new TextView(this.getApplicationContext());
            this.textViews[i].setId(View.generateViewId());
            this.textViews[i].setText(GetActivity.textViewsText[i]);
            this.textViews[i].setTextSize(20);
            this.textViews[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            this.textViews[i].setTextColor(Color.WHITE);
        }

        for (int i = 0; i < this.pickers.length; i++){
            this.pickers[i].setMinValue(0);
            this.pickers[i].setMaxValue(100);
        }

        this.insideGH = findViewById(R.id.get_inside_gh_cb);
        this.outsideGH = findViewById(R.id.get_outside_gh_cb);

        ConstraintLayout layout = findViewById(R.id.scroll_view_get_activity).findViewById(R.id.scroll_view_get_activity_const_layout);
        ConstraintSet set = new ConstraintSet();

        this.outsideGH.setOnCheckedChangeListener( (cView, isChecked) -> {


            if(layout != null) {

                if(isChecked){

                    //Adding views
                    for(int i = 0; i < 6; i++){
                        layout.addView(this.pickers[6+i], 0);
                        layout.addView(this.textViews[i], 0);
                    }
                    set.clone(layout);

                    for(int i = 0; i < 6; i++){
                        //Set top constraint to max_leaves_tv's top-side
                        set.connect(this.textViews[i].getId(), ConstraintSet.TOP, this.max_leaves_tv.getId(), ConstraintSet.TOP);
                        //Set left constraint to max_leaves_tv's right side if it is the temperature one, otherwise the previous one
                        set.connect(this.textViews[i].getId(), ConstraintSet.LEFT, (i == 0 ? this.max_leaves_tv.getId() : this.textViews[i-1].getId()), ConstraintSet.RIGHT, (i % 2 == 0 ? 20 : 10));
                        //Set right constraint to layout's right-side if it is the last one, otherwise the next one
                        set.connect(this.textViews[i].getId(), ConstraintSet.RIGHT, (i == 5 ? layout.getId() : this.textViews[i+1].getId()), (i == 5 ? ConstraintSet.RIGHT : ConstraintSet.LEFT));

                        set.connect(this.pickers[6+i].getId(), ConstraintSet.TOP, this.textViews[i].getId(), ConstraintSet.BOTTOM);
                        set.connect(this.pickers[6+i].getId(), ConstraintSet.LEFT, this.textViews[i].getId(), ConstraintSet.LEFT);
                        set.connect(this.pickers[6+i].getId(), ConstraintSet.RIGHT, this.textViews[i].getId(), ConstraintSet.RIGHT);
                    }

                    set.applyTo(layout);
                }else{
                    for(int i = 0; i < 6; i++){
                        layout.removeView(this.pickers[6+i]);
                        layout.removeView(this.textViews[i]);
                    }
                }

            }else{
                throw new RuntimeException("Could not retrieve the constraint layout...");
            }

        });

        //If the user is not an employee, it provides getting method to retrieve data of every employee
        if(this.getIntent().getIntExtra("grade", 0) > 0){
            UserCollector userCollector = new UserCollector(this.getApplicationContext());
            listViewUsers.setAdapter(new UsersListAdapter(userCollector, this.getApplicationContext()));
        }
    }

    /**
     * Called when user clicks on Get Data Button
     * It
     */
    private void performGetData() {
    }

}