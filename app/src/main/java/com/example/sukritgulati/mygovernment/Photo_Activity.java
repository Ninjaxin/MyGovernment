package com.example.sukritgulati.mygovernment;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Photo_Activity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private TextView locationText;
    private TextView titleText;
    private TextView nameText;
    private ImageView imageView;
    private Officer officer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        constraintLayout = (ConstraintLayout) findViewById(R.id.comstraintLayout);
        locationText = (TextView) findViewById(R.id.loctextView);
        titleText = (TextView) findViewById(R.id.titletextView);
        nameText = (TextView) findViewById(R.id.nametextView);
        imageView = (ImageView) findViewById(R.id.photoimageView);

        Intent intent = getIntent();

        officer = (Officer) intent.getSerializableExtra("PHOTO_DETAIL");


        locationText.setText( officer.getmZip() != null ? officer.getmCity() +", "
                + officer.getmState()+" "+ officer.getmZip() : "");

        titleText.setText(officer.getTitle());
        nameText.setText(officer.getName());

        constraintLayout.setBackgroundColor(officer.getPartyColor());
        loadPicasoImg(officer.getPhotoUrl());
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent startMain = new Intent(Photo_Activity.this,MainActivity.class);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }

    private void loadPicasoImg(String url) {
        System.out.println("url");
        if (url.contains("http")) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed
                    final String changedUrl = uri.toString().replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                }
            }).build();
            picasso.load(url)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        } else {
            Picasso.with(this).load(url)
                    .fit()
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageView);

        }
    }
}
