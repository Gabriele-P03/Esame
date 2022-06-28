package com.greenhouse.cloud.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.greenhouse.R;
import com.greenhouse.cloud.jobs.employee.PutActivity;
import com.greenhouse.cloud.jobs.get_data.GetActivity;

/**
 * This is the layout which is rendered when user clicks Task button from the menu popup.
 *
 * It provides some tasks depending on the {@link GRADE}, which is passed as IntegerExtra, of the user:
 *
 * 0: harvester
 *      - Insert Data
 *      - Read only own data
 *
 * 1: analyst
 *      - Read all data
 *
 * 2: CEO
 *      - Read all data
 *      - Edit all data
 *      - Remove all data
 *
 * @author Gabriele-P03
 */

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        this.loadComponents();
    }

    private void loadComponents() {
        switch (this.getIntent().getIntExtra("grade", 0)){

            case 0:
                this.loadEmployeeTaskActivity();
            break;

            case 1:
                this.loadAnalystTaskActivity();
            break;

            case 2:
                this.loadAnalystTaskActivity();
            break;

            default:

        }
    }

    /**
     * Analyst may only read data
     */
    private void loadAnalystTaskActivity() {
        startActivity(new Intent(this.getApplicationContext(), GetActivity.class).putExtra("grade", this.getIntent().getIntExtra("grade", 0)));
    }

    /**
     * Employee may only read own data previously sent and send another ones
     */
    private void loadEmployeeTaskActivity() {
        Button newData = new Button(this.getApplicationContext());
        newData.setText("Send Data");
        newData.setId(View.generateViewId());
        newData.setOnClickListener( l -> startActivity(new Intent(this.getApplicationContext(), PutActivity.class)));

        Button readData = new Button(this.getApplicationContext());
        readData.setText("Read Data");
        readData.setId(View.generateViewId());
        readData.setOnClickListener(l -> startActivity(new Intent(this.getApplicationContext(), GetActivity.class).putExtra("grade", this.getIntent().getIntExtra("grade", 0))));

        ConstraintLayout layout = findViewById(R.id.task_activity_layout);
        layout.addView(newData);
        layout.addView(readData);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(newData.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        set.connect(newData.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 20);
        set.connect(newData.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 20);
        set.connect(newData.getId(), ConstraintSet.BOTTOM, readData.getId(), ConstraintSet.TOP);

        set.connect(readData.getId(), ConstraintSet.TOP, newData.getId(), ConstraintSet.BOTTOM, 20);
        set.connect(readData.getId(), ConstraintSet.LEFT, newData.getId(), ConstraintSet.LEFT);
        set.connect(readData.getId(), ConstraintSet.RIGHT, newData.getId(), ConstraintSet.RIGHT);
        set.connect(newData.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);

        set.applyTo(layout);
    }
}
