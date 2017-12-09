package com.kiit.viper.greasemonkey;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.kiit.viper.greasemonkey.LoginActivity.fUser;


public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_LOCATION = 1;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    TextView textView,dataView;
    //private LocationManager locationManager;
    private String provider;
    FirebaseDatabase database;
    DatabaseReference servicesRef;
    DatabaseReference consumer;
    ProgressBar progressBar;
    ProgressBar progressBar1;
    ImageView user;
    HashMap<String,String> servicesMap;
    ListView listView;
    //Button next;
    String address_final=null;
    List<ServicesModel> servicesModelList;
    LocationManager locationManager;
    //String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        servicesMap = new HashMap<>();
        textView = (TextView)findViewById(R.id.text_location);
        //next = (Button) findViewById(R.id.next);
        //dataView = (TextView)findViewById(R.id.data);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.services);
        progressBar.setVisibility(View.VISIBLE);
        //textView.setText("");
        servicesModelList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        user = (ImageView) findViewById(R.id.user);
       // user.setImageResource(R.drawable.common_google_signin_btn_icon_light);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)

                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        /**/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                getLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        progressBar1 = (ProgressBar) findViewById(R.id.userload);
        database = FirebaseDatabase.getInstance();
        consumer = database.getReference("consumers");
        consumer.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              final  ConsumerModel c = dataSnapshot.getValue(ConsumerModel.class);
                if(c.getUid().equals(fUser.getUid())){
                    ImageLoader.getInstance().displayImage(c.getPhoto(),user, new ImageLoadingListener(){
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar1.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progressBar1.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar1.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            progressBar1.setVisibility(View.GONE);
                        }
                    });
                    user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ConsumerDetails.class);
                            // Intent intent = new Intent(this, MyActivity.class);
                            Bundle extras = new Bundle();

                                extras.putString("EXTRA_NAME", c.getName());
                                extras.putString("EXTRA_EMAIL",c.getEmail());
                                extras.putString("EXTRA_PHOTO",c.getPhoto());
                                intent.putExtras(extras);
                                startActivity(intent);

                        }
                    });
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


        servicesRef=database.getReference("services");
        servicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ServicesModel services = new ServicesModel();
                ServicesModel servicesModel = dataSnapshot.getValue(ServicesModel.class);
                services.setImg(servicesModel.getImg().toString());
                services.setName(servicesModel.getName().toString());
                services.setCode(servicesModel.getCode().toString());
                servicesModelList.add(services);
                ServicesAdapter servAdapter = new ServicesAdapter(getApplicationContext(),servicesModelList);
                listView.setAdapter(servAdapter);



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
        /*next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ServicesUI.class);
               // Intent intent = new Intent(this, MyActivity.class);
                ServicesModel item ;
                item= (ServicesModel) listView.getItemAtPosition(position);
                Bundle extras = new Bundle();
                if(address_final.equals(null)){
                    extras.putString("EXTRA_SERVICECODE", String.valueOf(item.getCode()));
                    extras.putString("EXTRA_ADDRESS","Bhubaneswar");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
                else{
                    extras.putString("EXTRA_SERVICECODE", String.valueOf(item.getCode()));
                    extras.putString("EXTRA_ADDRESS",address_final);
                    intent.putExtras(extras);
                    startActivity(intent);
                }

            }
        });


    }

    private void getLocation() throws IOException {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                //latitude = String.valueOf(lati);
                //longitude = String.valueOf(longi);

                getAddress(lati,longi);


                 progressBar.setVisibility(View.GONE);

            } else  if (location1 != null) {
                double lati = location1.getLatitude();
                double longi = location1.getLongitude();

                getAddress(lati,longi);
               // textView.setText(address);


            } else  if (location2 != null) {
                double lati = location2.getLatitude();
                double longi = location2.getLongitude();

                getAddress(lati,longi);


            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }



        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    void getAddress(double lat,double longi) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1);

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        System.out.print("City"+city);
        if(city.equals(null)){
            address_final="Bhubaneswar";
        }
        address_final = city;
        textView.setText(city);

       // return /*address+","+*/city ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();


            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
           /* ClearCache.getInstance().clearApplicationData();
            AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, PendingIntent.getActivity(this.getBaseContext(), 0, new    Intent(getIntent()), getIntent().getFlags()));
            android.os.Process.killProcess(android.os.Process.myPid());*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
