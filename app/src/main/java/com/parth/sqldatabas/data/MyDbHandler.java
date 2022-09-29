package com.parth.sqldatabas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parth.sqldatabas.parameters.Parameters;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Parameters.DB_NAME, null, Parameters.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = " CREATE TABLE " + Parameters.TABLE_NAME + "("
                + Parameters.KEY_ID + " INTEGER PRIMARY KEY, "
                + Parameters.KEY_FULL_NAME + " TEXT, "
                + Parameters.KEY_USER_NAME + " TEXT, "
                + Parameters.KEY_EMAIL + " TEXT, "
                + Parameters.KEY_MOBILE_NO + " TEXT, "
                + Parameters.KEY_PASSWORD + " TEXT, "
                + Parameters.KEY_AVATAR + " BLOB " + ")";

        Log.d("parth", "it's running " + create);
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Parameters.TABLE_NAME);
        onCreate(sqLiteDatabase);
        //sqLiteDatabase.execSQL();
    }

    public Boolean insertData(String fullName, String userName, String email, String mobileNo, String password, byte[] byteBuffer) {
        /*String fullName, String userName, String email, String mobileNo, String password, byte[] byteBuffer*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Parameters.KEY_FULL_NAME, fullName);
        contentValues.put(Parameters.KEY_USER_NAME, userName);
        contentValues.put(Parameters.KEY_EMAIL, email);
        contentValues.put(Parameters.KEY_MOBILE_NO, mobileNo);
        contentValues.put(Parameters.KEY_PASSWORD, password);
        contentValues.put(Parameters.KEY_AVATAR, byteBuffer);

        long result = db.insert(Parameters.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUserName(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Parameters.TABLE_NAME + " WHERE " + Parameters.KEY_USER_NAME + " = ? ", new String[]{
                userName
        });
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserNamePassword(String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Parameters.TABLE_NAME + " WHERE " + Parameters.KEY_USER_NAME + " = ? "
                + " AND " + Parameters.KEY_PASSWORD + " =? ", new String[]{
                userName, password
        });

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //upgrade data
    public void upgradeData(String fullName, String userName, String email, String mobileNo, String password, byte[] byteBuffer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Parameters.KEY_FULL_NAME, fullName);
        contentValues.put(Parameters.KEY_USER_NAME, userName);
        contentValues.put(Parameters.KEY_EMAIL, email);
        contentValues.put(Parameters.KEY_MOBILE_NO, mobileNo);
        contentValues.put(Parameters.KEY_PASSWORD, password);
        contentValues.put(Parameters.KEY_AVATAR, byteBuffer);

        db.update(Parameters.TABLE_NAME, contentValues, "user_name=?", new String[]{
                userName
        });
        db.close();
    }
}
