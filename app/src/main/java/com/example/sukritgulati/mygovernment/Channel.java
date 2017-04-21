package com.example.sukritgulati.mygovernment;

import java.io.Serializable;

/**
 * Created by sukritgulati on 4/13/17.
 */

public class Channel implements Serializable {


    private String type;
    private String cID;

    public Channel(String type, String cID) {
        this.type = type;
        this.cID = cID;
    }

    public String getType() {
        return type;
    }

    public String getcID() {
        return cID;
    }


}
