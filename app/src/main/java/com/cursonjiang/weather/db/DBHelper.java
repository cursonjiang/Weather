package com.cursonjiang.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by Curson on 15/5/23.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * 省表
     */
    private static final String CREATE_PROVINCE =
            "create table Province (" +
                    "id integer primary key autoincrement, " +
                    "province_name text, " +
                    "province_code text)";

    /**
     * 市表
     */
    private static final String CREATE_CITY =
            "create table City (" +
                    "id integer primary key autoincrement," +
                    "city_name text," +
                    "city_code text," +
                    "province_id integer )";

    /**
     * 县表
     */
    private static final String CREATE_COUNTY =
            "create table County (" +
                    "id integer primary key autoincrement," +
                    "county_name text," +
                    "county_code text," +
                    "city_id integer)";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建表
     *
     * @param db 数据库
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
