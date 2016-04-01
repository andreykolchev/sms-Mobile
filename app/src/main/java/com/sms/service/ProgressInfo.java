package com.sms.service;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

public class ProgressInfo {

    public int max;
    public int progress;
    public boolean indeterminate;

    public ProgressInfo(int max, int progress, boolean indeterminate) {
        this.max = max;
        this.progress = progress;
        this.indeterminate = indeterminate;
    }

    public void incMax(int add){
        max+=add;
    }

    public ProgressInfo incProgress(int progress){
        this.progress+=progress;
        return this;
    }


}
