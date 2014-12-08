package com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kanaiyathacker on 28/11/2014.
 */
public class AttandanceTransaction implements Serializable{
    private List<String> cardId;
    private String adminId;
    private String date;
    private String time;
    private String type;
    private String trasTable;
    private double latitude;
    private double longitude;

    public String getTrasTable() {
        return trasTable;
    }

    public void setTrasTable(String trasTable) {
        this.trasTable = trasTable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public AttandanceTransaction() {

    }

    public List<String> getCardId() {
        return cardId;
    }

    public void setCardId(List<String> cardId) {
        this.cardId = cardId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
