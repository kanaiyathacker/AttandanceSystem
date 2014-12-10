package com.bean;

import java.util.List;

/**
 * Created by kanaiyalalt on 10/12/2014.
 */
public class ViewReport {

    private String totalStrength;
    private String totalAbsent;
    private List<User> absentUserList;

    public ViewReport(String totalStrength , String totalAbsent) {
        this.totalStrength = totalStrength;
        this.totalAbsent = totalAbsent;
    }

    public ViewReport(String totalStrength , String totalAbsent , List<User> absentUserList) {
        this.totalStrength = totalStrength;
        this.totalAbsent = totalAbsent;
        this.absentUserList = absentUserList;
    }

    public String getTotalStrength() {
        return totalStrength;
    }

    public void setTotalStrength(String totalStrength) {
        this.totalStrength = totalStrength;
    }

    public String getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(String totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public List<User> getAbsentUserList() {
        return absentUserList;
    }

    public void setAbsentUserList(List<User> absentUserList) {
        this.absentUserList = absentUserList;
    }
}
