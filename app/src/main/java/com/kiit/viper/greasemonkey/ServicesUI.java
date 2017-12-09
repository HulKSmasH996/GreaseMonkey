package com.kiit.viper.greasemonkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ServicesUI extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference servicesRef;
    List<ServicesDetailsModel> servicesDetailsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_ui);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        final String servcode = extras.getString("EXTRA_SERVICECODE");
        final String area = extras.getString("EXTRA_ADDRESS");
        listView = (ListView) findViewById(R.id.servicesdetails);
        servicesDetailsModelList = new ArrayList<>();
        /*TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(servcode+","+area);*/
        database = FirebaseDatabase.getInstance();
        servicesRef=database.getReference("users");
        servicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ServicesDetailsModel servicesdetails = new ServicesDetailsModel();
                ServicesDetailsModel servicesDetailsModel = dataSnapshot.getValue(ServicesDetailsModel.class);
                if(servicesDetailsModel.getService().equals(servcode)&&servicesDetailsModel.getCity().toLowerCase().equals(area.toLowerCase())){
                    servicesdetails.setName(servicesDetailsModel.getName());
                    servicesdetails.setPname(servicesDetailsModel.getPname());
                    servicesdetails.setService(servicesDetailsModel.getService());
                    servicesdetails.setAddress(servicesDetailsModel.getAddress());
                    servicesdetails.setCity(servicesDetailsModel.getCity());
                    servicesdetails.setPin(servicesDetailsModel.getPin());
                    servicesdetails.setState(servicesDetailsModel.getState());
                    servicesdetails.setPhone(servicesDetailsModel.getPhone());
                    servicesDetailsModelList.add(servicesdetails);
                    ServiceDetailsAdapter serviceDetailsAdapter = new ServiceDetailsAdapter(getApplicationContext(),servicesDetailsModelList);
                    listView.setAdapter(serviceDetailsAdapter);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
