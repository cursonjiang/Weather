package com.cursonjiang.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cursonjiang.weather.model.City;
import com.cursonjiang.weather.model.County;
import com.cursonjiang.weather.model.Province;
import com.cursonjiang.weather.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理器
 * Created by Curson on 15/5/23.
 */
public class DBManager {

    /**
     * 数据库名字
     */
    private static final String DB_NAME = "weather";

    /**
     * 数据库版本号
     */
    private static final int VERSION = 1;

    private static DBManager mDBManager;

    private SQLiteDatabase db;

    private DBManager(Context context) {
        DBHelper dbHelper = new DBHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取DBManager的实例
     *
     * @param context 上下文
     * @return mDBManager
     */
    public synchronized static DBManager getInstance(Context context) {
        if (mDBManager == null) {
            mDBManager = new DBManager(context);
        }
        return mDBManager;
    }

    /**
     * 将Province实例保存到数据库
     *
     * @param province 省
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        } else {
            Logger.d("province为空");
        }
    }

    /**
     * 从数据库中读取全国所有的省份信息
     *
     * @return provinceList
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return provinceList;
    }

    /**
     * 将City实例保存到数据库
     *
     * @param city 城市
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            db.insert("City", null, values);
        } else {
            Logger.d("city为空");
        }
    }

    /**
     * 从数据库中读取某省下所有的城市信息
     *
     * @param provinceId 省Id
     * @return cityList
     */
    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = db.query(
                "City",
                null,
                "province_id = ?",
                new String[]{String.valueOf(provinceId)},
                null,
                null,
                null
        );
        if (cursor != null) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setprovinceId(provinceId);
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cityList;
    }

    /**
     * 将County实例保存到数据库
     *
     * @param county 县
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("city_id", county.getCityId());
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            db.insert("County", null, values);
        } else {
            Logger.d("county为空");
        }
    }

    /**
     * 读取某城市下所有县的信息
     *
     * @param CityId 城市Id
     * @return countyList
     */
    public List<County> loadCounties(int CityId) {
        List<County> countyList = new ArrayList<County>();
        Cursor cursor = db.query(
                "County",
                null,
                "city_id = ?",
                new String[]{String.valueOf(CityId)},
                null,
                null,
                null
        );
        if (cursor != null) {
            do {
                County county = new County();
                county.setCityId(CityId);
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                countyList.add(county);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return countyList;
    }

}
