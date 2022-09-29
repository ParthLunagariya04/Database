package com.parth.sqldatabas.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "_user_details")
public class RoomDBModel {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "_fullName")
    private String _fullName;

    @ColumnInfo(name = "_userName")
    private String _userName;

    @ColumnInfo(name = "_email")
    private String _email;

    @ColumnInfo(name = "_mobileNo")
    private String _mobileNo;

    @ColumnInfo(name = "_password")
    private String _password;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] _image;

    public RoomDBModel(int _id, String _fullName, String _userName, String _email, String _mobileNo, String _password, byte[] _image) {
        this._id = _id;
        this._fullName = _fullName;
        this._userName = _userName;
        this._email = _email;
        this._mobileNo = _mobileNo;
        this._password = _password;
        this._image = _image;
    }

    @Ignore
    public RoomDBModel(String _fullName, String _userName, String _email, String _mobileNo, byte[] _image) {
        this._fullName = _fullName;
        this._userName = _userName;
        this._email = _email;
        this._mobileNo = _mobileNo;
        this._image = _image;
    }

    @Ignore
    public RoomDBModel(String _fullName, String _userName, String _email, String _mobileNo, String _password, byte[] _image) {
        this._fullName = _fullName;
        this._userName = _userName;
        this._email = _email;
        this._mobileNo = _mobileNo;
        this._password = _password;
        this._image = _image;
    }

    @Ignore
    public RoomDBModel(String _fullName, String _userName, String _email, String _mobileNo, String _password) {
        this._fullName = _fullName;
        this._userName = _userName;
        this._email = _email;
        this._mobileNo = _mobileNo;
        this._password = _password;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_fullName() {
        return _fullName;
    }

    public void set_fullName(String _fullName) {
        this._fullName = _fullName;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_mobileNo() {
        return _mobileNo;
    }

    public void set_mobileNo(String _mobileNo) {
        this._mobileNo = _mobileNo;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] _image) {
        this._image = _image;
    }
}


