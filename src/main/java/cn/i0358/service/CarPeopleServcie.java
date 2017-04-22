package cn.i0358.service;

import cn.i0358.model.ICP;
import cn.i0358.model.QQData;
import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by fanyk on 2017/4/22.
 * d906953dd3d938e25d240485f44c57bd e9c5aaa92fd184361486049d4fef2a8c
 */
public interface CarPeopleServcie {
    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @POST("/1/classes/icp")
    public Call<JSONObject> save(@Body ICP data);

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @POST("/1/classes/QQData")
    public Call<JSONObject> save(@Body QQData data);

    @GET("/")
    public void get();

}
