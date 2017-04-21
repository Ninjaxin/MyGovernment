package com.example.sukritgulati.mygovernment;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sukritgulati on 4/9/17.
 */

public class Officer implements Serializable {

    private String mCity;
    private String mState;
    private String mZip;
    private String title;
    private String name;
    private String address;
    private String party;
    private String phone;
    private String url;
    private String email;
    private String photoUrl;
    private ArrayList<Channel> myChannels;

    public Officer(String mCity, String mState, String mZip, String title, String name,
                   String address, String party, String phone, String url, String email,
                   String photoUrl, ArrayList<Channel> myChannels) {
        this.mCity = mCity;
        this.mState = mState;
        this.mZip = mZip;
        this.title = title;
        this.name = name;
        this.address = address;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.myChannels = myChannels;
    }


    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmZip() {
        return mZip;
    }

    public void setmZip(String mZip) {
        this.mZip = mZip;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<Channel> getMyChannels() {
        return myChannels;
    }

    public void setMyChannels(ArrayList<Channel> myChannels) {
        this.myChannels = myChannels;
    }

    public int getPartyColor() {
        if (party.toLowerCase().contains("republican")){
            return Color.RED;
        } else if (party.toLowerCase().contains("democratic")){
            return  Color.BLUE;
        } else {
            return Color.BLACK;
        }
    }

}
