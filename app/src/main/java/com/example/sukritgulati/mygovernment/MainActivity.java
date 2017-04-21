package com.example.sukritgulati.mygovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private Locator locator;
    private static ArrayList<Officer> officerList = new ArrayList<>();
    public TextView addressText;
    private RecyclerView recyclerView;
    private OfficerAdapter officerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressText =  (TextView)findViewById(R.id.locationTextView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        officerAdapter = new OfficerAdapter(officerList,this);
        recyclerView.setAdapter(officerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(officerList.size() == 0) {
            locator = new Locator(this);
        } else {
            addressText.setText( officerList.get(0).getmZip() != null ? officerList.get(0).getmCity() +", "
                    + officerList.get(0).getmState()+" "+ officerList.get(0).getmZip() : "");
        }

//        for(int i = 0; i<5;i++){
//            Officer of = new Officer();
//            of.setTitle("sa");
//            of.setName("hello"+i);
//            officerList.add(of);
//        }

        officerAdapter.notifyDataSetChanged();
    }

    public void updateData(ArrayList<Officer> mOfficersList) {
        if(mOfficersList.size()!= 0) {
            addressText.setText(mOfficersList.get(0).getmZip() != null ? mOfficersList.get(0).getmCity() + ", "
                    + mOfficersList.get(0).getmState() + " " + mOfficersList.get(0).getmZip() : "");
        }
        officerList.removeAll(officerList);
        officerList.addAll(mOfficersList);
        officerAdapter.notifyDataSetChanged();
    }

    public void displayAddress(double latitude, double longitude){
        String address = doAddress(latitude,longitude);
        if( address != null ){
            addressText.setText(address);
            if (networkCheck()) {
                new AsyncOfficerInfoDownloader(this).execute(address);
            } else {
                addressText.setText("No Data For Location");
                showDialogAlert("No Network Connection", "Data Cannot br Accessed/loaded without an internet connection");

            }
            officerAdapter.notifyDataSetChanged();
        }
    }

    public void showDialogAlert(String title,String message){
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        //builder.setIcon(drawable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                       // locator.setUpLocationManager();
                       // locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean networkCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchMenu:
                showDialog();
                return true;
            case R.id.infoMenu:
                Intent in = new Intent(MainActivity.this, InfoActivity.class);
                in.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(in);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(et);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doLocationName(et.getText().toString());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        //builder.setMessage("Enter City, State or a ZipCode:");
        builder.setTitle("Enter City, State or a ZipCode:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);


        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

//                    sb.append("\nAddress\n\n");
//                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
//                        sb.append("\t" + ad.getAddressLine(i) + "\n");


                    sb.append("" + ad.getLocality() + ", " + ad.getAdminArea()+" " + ad.getPostalCode() + "\n");

                }

                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }
    public void doLocationName(String  loc) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            if (networkCheck()) {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(loc, 1);
            StringBuilder sb = new StringBuilder();

            for (Address ad : addresses) {
                Log.d(TAG, "doLocation: " + ad);
               String code =  ad.getPostalCode() != null? ad.getPostalCode() : "";
                sb.append("" + ad.getLocality() + ", " + ad.getAdminArea() + " " + code  + "\n");

            }
           // addressText.setText(sb.toString());


                new AsyncOfficerInfoDownloader(this).execute(sb.toString());
            } else {
                addressText.setText("No Data For Location");
                showDialogAlert("No Network Connection", "Data Cannot br Accessed/loaded without an internet connection");

            }
            officerAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        Officer off = officerList.get(pos);
        Intent intent = new Intent(MainActivity.this,officerDetail.class);
        intent.putExtra("OFFICE_DETAIL",off);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }
    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
