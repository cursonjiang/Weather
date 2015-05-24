package com.cursonjiang.weather.util;

/**
 * 网络监听接口
 * Created by Curson on 15/5/23.
 */
public interface HttpCallbackListener {

    /**
     * 请求成功
     *
     * @param response 返回结果
     */
    void onFinish(String response);

    /**
     * 请求失败
     *
     * @param e 错误信息
     */
    void onError(Exception e);
}
