package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBName = "myDb.db";
    public static final int DBVersion = 1;


    public static final String Table_Name = "myDb";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "Email";
    public static final String Col_3 = "Username";
    public static final String Col_4 = "Password";



    public static final String listTableName = "mylist";
    public static final String listCol_1 = "ID";
    public static final String listCol_2 = "label";
    public static final String listCol_3 = "timestamp";
    public static final String listCol_4 = "speed";
    public static final String listCol_5 = "road";


    public DatabaseHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Email TEXT, Username TEXT, Password TEXT)" );
        db.execSQL("CREATE TABLE " + listTableName +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, label TEXT not null unique, timestamp TEXT, speed TEXT, road TEXT)" );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        db.execSQL("DROP TABLE IF EXISTS " + listTableName);
        onCreate(db);
    }




}
