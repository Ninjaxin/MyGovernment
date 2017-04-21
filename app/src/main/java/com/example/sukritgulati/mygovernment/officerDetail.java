package com.example.sukritgulati.mygovernment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class officerDetail extends AppCompatActivity {

    private TextView locationText;
    private TextView titleText;
    private TextView nameText;
    private TextView party;
    private ImageView photoImage;
    private TextView addressLabel;
    private TextView addressText;
    private TextView phoneLabel;
    private TextView phoneText;
    private TextView emailLabel;
    private TextView emailText;
    private TextView websiteLabel;
    private TextView websiteText;
    private ImageView youtubeIcon;
    private ImageView facebookIcon;
    private ImageView googlePlusIcon;
    private ImageView twitterIcon;
    private ScrollView scrollView;

    private Officer officer;

    private String youtube = "";
    private String googlePlus = "";
    private String twitter = "";
    private String facebook = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_detail);

        locationText = (TextView) findViewById(R.id.locationTextView);
        titleText = (TextView) findViewById(R.id.titleTextView);
        nameText = (TextView) findViewById(R.id.nameTextView);
        party =(TextView) findViewById(R.id.partyTextView);
        photoImage = (ImageView) findViewById(R.id.officerImageView);
        addressLabel = (TextView) findViewById(R.id.AddLbltextView);
        addressText = (TextView) findViewById(R.id.AddtextView);
        phoneLabel = (TextView) findViewById(R.id.phoneLblTextView);
        phoneText = (TextView) findViewById(R.id.phoneTextView);
        emailLabel = (TextView) findViewById(R.id.emailLblTextView);
        emailText = (TextView) findViewById(R.id.emailTextView) ;
        websiteLabel = (TextView) findViewById(R.id.WebsiteLbltextView);
        websiteText = (TextView) findViewById(R.id.WebsiteTextView);
        youtubeIcon = (ImageView) findViewById(R.id.youtubeimageView);
        facebookIcon = (ImageView) findViewById(R.id.facebookimageView);
        googlePlusIcon = (ImageView) findViewById(R.id.googleimageView);
        twitterIcon = (ImageView) findViewById(R.id.twitterimageView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Intent intent = getIntent();

        officer = (Officer) intent.getSerializableExtra("OFFICE_DETAIL");


        locationText.setText( officer.getmZip() != null ? officer.getmCity() +", "
                + officer.getmState()+" "+ officer.getmZip() : "");

        titleText.setText(officer.getTitle());
        nameText.setText(officer.getName());
        if(!officer.getParty().equals("Unknown")) {
            party.setText(officer.getParty());
        }
        scrollView.setBackgroundColor(officer.getPartyColor());
        loadPicasoImg(officer.getPhotoUrl());
        addressText.setText(officer.getAddress());
        Linkify.addLinks(addressText,Linkify.MAP_ADDRESSES);
        addressText.setLinkTextColor(Color.WHITE);
        phoneText.setText(officer.getPhone());
        Linkify.addLinks(phoneText,Linkify.PHONE_NUMBERS);
        phoneText.setLinkTextColor(Color.WHITE);
        emailText.setText(officer.getEmail());
        Linkify.addLinks(emailText,Linkify.EMAIL_ADDRESSES);
        emailText.setLinkTextColor(Color.WHITE);
        websiteText.setText(officer.getUrl());
        Linkify.addLinks(websiteText,Linkify.WEB_URLS);
        websiteText.setLinkTextColor(Color.WHITE);
        ArrayList<Channel> myChannels = officer.getMyChannels();
        for (Channel myChan : myChannels) {
            showChannel(myChan);
        }

    }

    private void showChannel(Channel chan){
        switch (chan.getType()){
            case "GooglePlus":
                googlePlusIcon.setVisibility(View.VISIBLE);
                googlePlus = chan.getcID();
                break;
            case "Facebook":
                facebookIcon.setVisibility(View.VISIBLE);
                facebook = chan.getcID();
                break;
            case "Twitter":
                twitterIcon.setVisibility(View.VISIBLE);
                twitter = chan.getcID();
                break;
            case "YouTube":
                youtubeIcon.setVisibility(View.VISIBLE);
                youtube = chan.getcID();
                break;
            default:
        }

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
                            .resize(400,400)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photoImage);
                }
            }).build();
            picasso.load(url)
                    .resize(400,400)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        } else {
            Picasso.with(this).load(url)
                    .resize(400,400)
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.missingimage)
                    .into(photoImage);

        }
    }

    public void onBigImage(View v) {

        Intent intent = new Intent(officerDetail.this,Photo_Activity.class);
        intent.putExtra("PHOTO_DETAIL",officer);
        startActivity(intent);

    }


    public void onChannelPressed(View v) {

        switch (v.getTag().toString()){
            case "1":
                youTubeClicked(youtube);
                break;
            case "2":
                googlePlusClicked(googlePlus);
                break;
            case "3":
                twitterClicked(twitter);
                break;
            case "4":
                facebookClicked(facebook);
                break;
            default:
        }
    }

    public void youTubeClicked(String name) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
    public void googlePlusClicked(String name) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void twitterClicked(String  name) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(String name) {
        String FACEBOOK_URL = "https://www.facebook.com/" + name;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + name;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

}
