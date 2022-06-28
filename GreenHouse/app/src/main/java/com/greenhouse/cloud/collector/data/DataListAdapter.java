package com.greenhouse.cloud.collector.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.greenhouse.R;
import com.greenhouse.cloud.jobs.Task;
import com.greenhouse.json.JSONArray;
import com.greenhouse.json.JSONMap;
import com.greenhouse.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DataListAdapter<T extends InsideData> extends BaseAdapter implements ListAdapter {

    //Store if the user has sent the request to the inside table
    private Context context;
    private ArrayList<T> arrayList;
    private int grade;
    Class<? extends InsideData> parameterTypeClass;

    public DataListAdapter(Context context, JSONArray array, Class<? extends InsideData> parameterTypeClass, int grade) {
        this.context = context;
        this.grade = grade;

        this.parameterTypeClass = parameterTypeClass;
        this.arrayList = new ArrayList<>();

        try {
            for (JSONObject object : array.getObjects()) {
                if (object != null) {
                    //Retrieve class of T parameter
                    if (parameterTypeClass == InsideData.class) {
                        this.arrayList.add((T) new InsideData(object.getMaps()));
                    } else if (parameterTypeClass == OutsideData.class) {
                        this.arrayList.add((T) new OutsideData(object.getMaps()));
                    }
                }
            }

        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null)
            v = LayoutInflater.from(this.context).inflate(R.layout.data_list_view, null);

        if(v != null){
            ((TextView)v.findViewById(R.id.data_list_view_id)).setText(String.valueOf(this.arrayList.get(position).getID()));
            ((TextView)v.findViewById(R.id.data_list_view_id_harvester)).setText(this.arrayList.get(position).getUsername_fk_employee());
            ((TextView)v.findViewById(R.id.data_list_view_plants)).setText(String.valueOf(this.arrayList.get(position).getPlants()));
            ((TextView)v.findViewById(R.id.data_list_view_leaves)).setText(String.valueOf(this.arrayList.get(position).getLeaves()));
            ((TextView)v.findViewById(R.id.data_list_view_max_height)).setText(String.valueOf(this.arrayList.get(position).getMax_height()));
            ((TextView)v.findViewById(R.id.data_list_view_date)).setText(this.arrayList.get(position).getDate().toString());

            //The query has been done on Outside table, let's add T, H and L text view
            if(this.parameterTypeClass == OutsideData.class){
                this.loadOutsideTextView(v, position);
            }
            //Now let's check if either Delete button or Edit one must be implemented... it means to be CEO
            if(this.grade == 2){
                this.loadCEOFeatures(v, position);
            }
        }

        return v;
    }

    /**
     * Since Outside's data contain also temperature, humidity and light information, this function
     * create new text views and constraints them to the layout
     *
     * @param v
     * @param position
     */
    private void loadOutsideTextView(View v, int position){
        ((TextView)v.findViewById(R.id.data_list_view_temperature)).setText(((OutsideData)this.arrayList.get(position)).getTemperature() + "°C");
        ((TextView)v.findViewById(R.id.data_list_view_humidity)).setText(((OutsideData)this.arrayList.get(position)).getHumidity() + " %");
        ((TextView)v.findViewById(R.id.data_list_view_light)).setText(((OutsideData)this.arrayList.get(position)).getLight() + " %");
    }

    /**
     * Called by getView() in order to load CEO's functions (i.e. Deleting and Editing data)
     * @param position of the data selected relative to the array list
     * @param v
     */
    private void loadCEOFeatures(View v, int position){
        ImageButton deleteButton = new ImageButton(this.context);
        deleteButton.setBackgroundResource(R.mipmap.trash);
        deleteButton.setId(View.generateViewId());
        deleteButton.setOnClickListener( l -> this.deleteData(l, position));

        ImageButton editButton = new ImageButton(this.context);
        editButton.setBackgroundResource(R.mipmap.pencil);
        editButton.setId(View.generateViewId());

        ConstraintLayout layout = v.findViewById(R.id.data_list_view_layout);
        layout.addView(deleteButton);
        layout.addView(editButton);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(deleteButton.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        set.connect(deleteButton.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 5);
        set.connect(deleteButton.getId(), ConstraintSet.RIGHT, editButton.getId(), ConstraintSet.LEFT);

        set.connect(editButton.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        set.connect(editButton.getId(), ConstraintSet.LEFT, deleteButton.getId(), ConstraintSet.RIGHT, 5);
        set.connect(editButton.getId(), ConstraintSet.RIGHT, R.id.data_list_view_id, ConstraintSet.LEFT);

        set.connect(R.id.data_list_view_id, ConstraintSet.LEFT, editButton.getId(), ConstraintSet.RIGHT, 5);
        set.connect(R.id.data_list_view_id, ConstraintSet.RIGHT, R.id.data_list_view_id_harvester, ConstraintSet.LEFT, 5);
        set.applyTo(layout);
    }


    /**
     * Called when CEO clicks on Delete button.
     * It sends the sql query for deleting the data selected
     * @param l
     * @param position
     */
    private void deleteData(View l, int position) {

        //Popup for make sure that CEO wanna delete it
        LayoutInflater li = (LayoutInflater)this.context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = li.inflate(R.layout.data_delete_popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.showAsDropDown(l);

        popupView.findViewById(R.id.data_delete_popup_button_dismiss).setOnClickListener(l1 -> popupWindow.dismiss());
        popupView.findViewById(R.id.data_delete_popup_button_ok).setOnClickListener(l1 -> new DeleteTask(position).execute(
                "table=" + (parameterTypeClass == InsideData.class ? "Inside" : "Outside") + "&id_data=" + this.arrayList.get(position).getID()
        ));
    }

    private class DeleteTask extends Task{

        private int position;

        public DeleteTask(int position) {
            super(DataListAdapter.this.context);
            this.position = position;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            //Let's add single quote in order to improve SQLInjection avoiding
            //In the PHP script, the value of id_data will be checked if it really an integer (' OR 1=1#)
            return super.doInBackground("employer/delete.php?" + strings[0]);
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            //it means that data has been deleted successfully, then it will be deleted from list
            if(s == 200){
                DataListAdapter.this.arrayList.remove(this.position);
                DataListAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
