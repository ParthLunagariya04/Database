package com.parth.sqldatabas.model;

public class DetailsModel {
    private int id;
    private String fullName;
    private String userName;
    private String email;
    private String mobileNo;
    private byte[] byteBuffer;

    //constructor
    public DetailsModel( String fullName, String userName, String email, String mobileNo, byte[] byteBuffer) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.byteBuffer = byteBuffer;
    }

    public DetailsModel(int id){
        this.id = id;
    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }
}
