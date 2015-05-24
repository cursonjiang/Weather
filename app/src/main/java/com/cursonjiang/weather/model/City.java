package com.cursonjiang.weather.model;

/**
 * 市
 * Created by Curson on 15/5/23.
 */
public class City {

    private int id;

    private String cityName;

    private String cityCode;

    private int provinceId;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceIn) {
        this.provinceId = provinceIn;
    }
}
