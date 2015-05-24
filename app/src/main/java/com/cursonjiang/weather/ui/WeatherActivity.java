package com.cursonjiang.weather.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cursonjiang.weather.App;
import com.cursonjiang.weather.R;
import com.cursonjiang.weather.util.HttpCallbackListener;
import com.cursonjiang.weather.util.HttpUtil;
import com.cursonjiang.weather.util.Utility;
import com.cursonjiang.weather.util.logger.Logger;

/**
 * Created by Curson on 15/5/24.
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;

    /**
     * 切换城市按钮
     */
    private Button switchCity;

    /**
     * 更新天气按钮
     */
    private Button refreshWeather;

    /**
     * 城市名称
     */
    private TextView cityNameText;

    /**
     * 发布时间
     */
    private TextView publishText;

    /**
     * 天气描述信息
     */
    private TextView weatherDespText;

    /**
     * 最高气温
     */
    private TextView temp1Text;

    /**
     * 最低气温
     */
    private TextView temp2Text;

    /**
     * 用于显示当前日期
     */
    private TextView currentDataText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDataText = (TextView) findViewById(R.id.current_date);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            //有县级代号时就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }
    }

    /**
     * 查询县级代号所对应的天气代号
     *
     * @param countyCode 县级代号
     */
    private void queryWeatherCode(String countyCode) {
        String url = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(url, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气
     *
     * @param weatherCode 天气代号
     */
    private void queryWeatherInfo(String weatherCode) {
        String url = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(url, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     *
     * @param url  地址
     * @param type 类型
     */
    private void queryFromServer(String url, final String type) {
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("county".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        Logger.json(response);
                        //从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    //处理服务器返回的天气信息
                    Utility.handleWeatherResponse(App.getContext(), response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        temp1Text.setText(sp.getString("temp1", ""));
        temp2Text.setText(sp.getString("temp2", ""));
        cityNameText.setText(sp.getString("city_name", ""));
        currentDataText.setText(sp.getString("current_data", ""));
        weatherDespText.setText(sp.getString("weather_desp", ""));
        publishText.setText("今天" + sp.getString("publish_time", "") + "发布");
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getContext());
                String weatherCode = sp.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
