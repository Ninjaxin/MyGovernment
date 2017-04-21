package com.example.sukritgulati.mygovernment;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sukritgulati on 4/12/17.
 */

public class AsyncOfficerInfoDownloader extends AsyncTask<String,Integer,String> {
    private MainActivity mainActivity;
    private final String API_KEY = "AIzaSyDWZqLNgKTZmQCUdJv9NQUiB_Cw4Y4UgY8";
    private final String API_String = "key";
    private final String Search_String = "address";
    private final String dataURL = " https://www.googleapis.com/civicinfo/v2/representatives?";
    private static final String TAG = "AsyncOfficerInfoDownloader";

    public AsyncOfficerInfoDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();
        try {

            Uri.Builder builder = Uri.parse(dataURL).buildUpon();

            builder.appendQueryParameter(API_String,API_KEY);
            builder.appendQueryParameter(Search_String,params[0]);
            URL url =  new URL(builder.build().toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {

            return null;
        }


        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
       // super.onPostExecute(s);
        ArrayList<Officer> officersList = parseJSON(s);
        mainActivity.updateData(officersList);
    }

    private ArrayList<Officer> parseJSON(String s) {
        ArrayList<Officer> officers = new ArrayList<>();
        try {

            String mcity, mstate, mzip = null;
            JSONObject jObj = new JSONObject(s);
            //normalized object
            JSONObject normalizedjObj = (JSONObject) jObj.get("normalizedInput");
            mcity = normalizedjObj.getString("city") != null ? normalizedjObj.getString("city") : "";
            mstate = normalizedjObj.getString("state") != null ? normalizedjObj.getString("state") : "";
            mzip = normalizedjObj.getString("zip") != null ? normalizedjObj.getString("zip") : "";

            //offices Array
            JSONArray offjArray = jObj.getJSONArray("offices");
            for(int i = 0; i< offjArray.length(); i++){
                JSONObject joffObj = (JSONObject) offjArray.get(i);
                String title  = joffObj.getString("name");

                ///get indices and iterate through the indices for info
                JSONArray indexArray = joffObj.getJSONArray("officialIndices");
                for(int j = 0; j< indexArray.length(); j++){
                    int oIndex = indexArray.getInt(j);
                    JSONArray offInfoArray = jObj.getJSONArray("officials");
                    JSONObject offInfoObj = (JSONObject) offInfoArray.get(oIndex);
                    String offname = offInfoObj.getString("name");

                    JSONObject addressObj = offInfoObj.has("address")? offInfoObj.getJSONArray("address").getJSONObject(0): null;

                    //check if lin1 and lin2 and line 3 exist, then concatenate of not null
                    StringBuffer streetAddress = new StringBuffer();
                    if (addressObj!=null) {
                        if (addressObj.has("line1")) {
                            streetAddress.append(addressObj.getString("line1") + "\n");
                        }
                        if (addressObj.has("line2")) {
                            streetAddress.append(addressObj.getString("line2") + "\n");
                        }
                        if (addressObj.has("line3")) {
                            streetAddress.append(addressObj.getString("line3") + "\n");
                        }
                    }
                    String tempStreet = streetAddress.toString();
                    String street = tempStreet != "" ? tempStreet : "No Data Provided";
                    String mainAdd = addressObj!=null ? addressObj.getString("city") +", " + addressObj.getString("state")+ " " + addressObj.getString("zip") : "";
                    String party = offInfoObj.has("party")? offInfoObj.getString("party") : "";
                    String phone = offInfoObj.has("phones")? offInfoObj.getJSONArray("phones").getString(0) : "No Data Provided";
                    String url = offInfoObj.has("urls")? offInfoObj.getJSONArray("urls").getString(0) : "No Data Provided";
                    String email = offInfoObj.has("emails")? offInfoObj.getJSONArray("emails").getString(0) : "No Data Provided";
                    String photo = offInfoObj.has("photoUrl")? offInfoObj.getString("photoUrl") : "placeholder.png";
                    ArrayList<Channel> mChannel = new ArrayList<>();
                    if(offInfoObj.has("channels")){
                        JSONArray cArray = offInfoObj.getJSONArray("channels");

                        for(int k = 0; k< cArray.length(); k++){
                            String type = cArray.getJSONObject(k).getString("type");
                            String ids = cArray.getJSONObject(k).getString("id");
                            Channel chObj = new Channel(type,ids);
                            mChannel.add(chObj);
                        }

                    }

                    Officer newOfficer = new Officer(mcity,mstate,mzip,title,offname,street + mainAdd, party, phone, url, email,
                            photo,mChannel);
                    officers.add(newOfficer);
                    //
                    // public Officer(String mCity, String mState, String mZip, String title, String name,
//                            String address, String party, String phone, String url, String email,
//                            String photoUrl, ArrayList<Channel> myChannels) {

                }
            }

//            count = jObjMain.length();
//
//            for (int i = 0; i < jObjMain.length(); i++) {
//                JSONObject jStock = (JSONObject) jObjMain.get(i);
//                String stockName = jStock.getString("t");
//                Double stockPrice = jStock.getDouble("l");
//                Double stockChangeAmount = jStock.getDouble("c");
//                Double stockChangePercentage = jStock.getDouble("cp");
//                stockList.add(new Stock(stockName,symbolMap.get(stockName),stockPrice,stockChangeAmount,stockChangePercentage));
//            }
            return officers;
        } catch (Exception e) {
            e.printStackTrace();
            mainActivity.addressText.setText("No Data found");

        }
        return officers;

    }
}
