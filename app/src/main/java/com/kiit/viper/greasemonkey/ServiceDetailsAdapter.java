package com.kiit.viper.greasemonkey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by VIPER on 29-Nov-17.
 */

public class ServiceDetailsAdapter extends BaseAdapter{

    Context context;
    List<ServicesDetailsModel> data;
    private static LayoutInflater inflater = null;

    public ServiceDetailsAdapter(Context context, List<ServicesDetailsModel> data) {
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
            vi = inflater.inflate(R.layout.services_details_row, null);
        final TextView text = (TextView) vi.findViewById(R.id.providername);
        text.setText(data.get(position).getPname());
        final TextView text1 = (TextView) vi.findViewById(R.id.name);
        text1.setText(data.get(position).getName());
        final TextView text2= (TextView) vi.findViewById(R.id.address);
        text2.setText(data.get(position).getAddress()+","+data.get(position).getCity()+","+data.get(position).getState());
        final TextView text3 = (TextView) vi.findViewById(R.id.phone);
        text3.setText(data.get(position).getPhone());

        return  vi;
    }
}