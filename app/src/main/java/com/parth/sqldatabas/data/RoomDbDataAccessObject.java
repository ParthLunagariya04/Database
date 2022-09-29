package com.parth.sqldatabas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.parth.sqldatabas.model.RoomDBModel;

import java.util.List;

@Dao
public interface RoomDbDataAccessObject {

    @Query("select * from _user_details")
    List<RoomDBModel> getAllData();

    @Query("select * from _user_details where _id = :id and _fullName = :fullName and _email =:email and _image = :image limit 1")
    LiveData<RoomDBModel> getDataById(String id, String fullName, String email, byte[] image);

    @Insert
    void addData(RoomDBModel roomDBModel);

    @Query("SELECT * FROM _user_details WHERE _userName = :userName")
    RoomDBModel isTaken(String userName);

    @Query("SELECT * FROM _user_details WHERE _fullName = :fullName")
    RoomDBModel isAlreadyTaken(String fullName);

    @Query("select * from _user_details where _userName = :userName and _password = :password")
    RoomDBModel logIn(String userName, String password);

    @Update
    void updateData(RoomDBModel roomDBModel);

    @Query("UPDATE _user_details SET _fullName =:fName, _userName =:mobileNo2, _email =:email, _mobileNo =:mobileNO1, _image =:image where _id=:id")
    void update(String fName, String mobileNo2, String email, String mobileNO1, byte[] image, int id);

    @Delete
    void deleteData(RoomDBModel roomDBModel);

    @Query("DELETE FROM _user_details")
    void deleteAllData();
}
