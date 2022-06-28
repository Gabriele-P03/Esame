package com.greenhouse.cloud.collector.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.greenhouse.R;

public class UsersListAdapter extends BaseAdapter implements ListAdapter {

    private UserCollector userCollector;
    private Context context;

    public UsersListAdapter(UserCollector userCollector, Context context) {
        this.userCollector = userCollector;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.userCollector.getUSERS().size();
    }

    @Override
    public Object getItem(int position) {
        return this.userCollector.getUSERS().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null)
            v = LayoutInflater.from(this.context).inflate(R.layout.list_users, null);

        if(v != null){

            User user = this.userCollector.getUSERS().get(position);

            ((TextView)v.findViewById(R.id.list_users_id)).setText(String.valueOf(user.getID_employee()));
            ((TextView)v.findViewById(R.id.list_users_name)).setText(user.getFirst_name());
            ((TextView)v.findViewById(R.id.list_users_lastname)).setText(user.getLast_name());
            ((TextView)v.findViewById(R.id.list_users_username)).setText(user.getUsername());
            ((TextView)v.findViewById(R.id.list_users_cf)).setText(user.getCF());
            ((TextView)v.findViewById(R.id.list_users_birthday)).setText(user.getBirthday());
            ((TextView)v.findViewById(R.id.list_users_grade)).setText(user.getGrade().name());
        }

        return v;
    }
}
