package com.bean;

import com.google.inject.Singleton;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class Person {

	String id;
	String fName;
	String mName;
	String lname;
	String coName;
    String coId;
    String date;
    String time;
    String desc;

    public Person() {
        id = "1";
        fName = "1";
        mName = "1";
        lname = "1";
        coName = "1";
        coId = "1";
        date = "1";
        time = "1";
        desc = "1";
    }


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
