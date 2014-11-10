package com.com.bean;

public class Person {

	String id;
	String fName;
	String mName;
	String lname;
	String coName;

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
	@Override
	public String toString() {
		return "MyBean [id=" + id + ", fName=" + fName + ", mName=" + mName
				+ ", lname=" + lname + ", coName=" + coName + "]";
	}
	
}
