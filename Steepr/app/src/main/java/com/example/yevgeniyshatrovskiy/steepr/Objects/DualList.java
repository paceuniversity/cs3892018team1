package com.example.yevgeniyshatrovskiy.steepr.Objects;

import java.util.ArrayList;

/**
 * Created by yevgeniyshatrovskiy on 4/19/18.
 */

public class DualList {

    ArrayList<TeaDetails> details;
    ArrayList<TeaCategory> allRec;

    public DualList(ArrayList<TeaDetails> details, ArrayList<TeaCategory> allRec) {
        this.details = details;
        this.allRec = allRec;
    }

    public DualList(){

    }

    public ArrayList<TeaDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TeaDetails> details) {
        this.details = details;
    }

    public ArrayList<TeaCategory> getAllRec() {
        return allRec;
    }

    public void setAllRec(ArrayList<TeaCategory> allRec) {
        this.allRec = allRec;
    }
}
