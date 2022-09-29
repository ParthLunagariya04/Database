package com.parth.sqldatabas.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.parth.sqldatabas.model.RoomDBModel;

@Database(entities = RoomDBModel.class, exportSchema = false, version = 1)
public abstract class RoomDBHandler extends RoomDatabase {

    private static final String ROOM_DB_NAME = "userDetailsRoomDb";
    private static RoomDBHandler instance;

    public static synchronized RoomDBHandler getDB(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, RoomDBHandler.class, ROOM_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract RoomDbDataAccessObject roomDbDataAccessObject();
}
