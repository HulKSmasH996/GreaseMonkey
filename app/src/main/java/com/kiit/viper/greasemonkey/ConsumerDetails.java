package com.kiit.viper.greasemonkey;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ConsumerDetails extends AppCompatActivity {
    ImageView img;
    TextView nameview;
    TextView emailview;
    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_details);
        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("EXTRA_NAME");
        final String email = extras.getString("EXTRA_EMAIL");
        final String image = extras.getString("EXTRA_PHOTO");
        nameview = (TextView) findViewById(R.id.name);
        nameview.setText(name);
        emailview = (TextView) findViewById(R.id.consumeremail);
        emailview.setText(email);


        img = (ImageView) findViewById(R.id.profile_image);
        progressBar1 = (ProgressBar) findViewById(R.id.progress);
        ImageLoader.getInstance().displayImage(image,img, new ImageLoadingListener(){
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
    }
}
