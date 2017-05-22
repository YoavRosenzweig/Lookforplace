package com.project.yoavr.lookforplace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlResults extends SQLiteOpenHelper {


    public MySqlResults(Context context) {
        super(context, "result.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableSQL="CREATE TABLE myresult (_id  INTEGER  PRIMARY KEY AUTOINCREMENT  ,name TEXT,image TEXT,adders TEXT,distance TEXT)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

