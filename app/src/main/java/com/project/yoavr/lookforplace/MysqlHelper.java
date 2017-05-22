package com.project.yoavr.lookforplace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yoavr on 13/04/2017.
 */

public class MysqlHelper extends SQLiteOpenHelper {


    public MysqlHelper(Context context) {
        super(context, "places.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableSQL="CREATE TABLE myplace (_id  INTEGER  PRIMARY KEY AUTOINCREMENT  , name TEXT)";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
