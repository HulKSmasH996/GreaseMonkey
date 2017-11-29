package com.kiit.viper.greasemonkey;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VIPER on 26-Nov-17.
 */

public class ServicesAdapter extends BaseAdapter {

    Context context;
    List<ServicesModel> data;
    private static LayoutInflater inflater = null;

    public ServicesAdapter(Context context, List<ServicesModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.services_row, null);
        final TextView text = (TextView) vi.findViewById(R.id.servicename);
        text.setText(data.get(position).getName());
        return  vi;
    }
}
