package cn.i0358.bmob;

import com.alibaba.fastjson.JSONObject;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.*;

import java.util.Map;


/**
 * Created by fanyk on 2017/4/24.
 */
public interface BmobService<T> {

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @POST("1/classes/{tableName}")
    public Call<JSONObject> save(@Path("tableName") String tableName, @Body Object obj);

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @POST("1/classes/{tableName}")
    public Observable<Result<JSONObject>> saveRx(@Path("tableName") String tableName, @Body Object obj);

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @DELETE("1/classes/{tableName}")
    public Observable<Result<JSONObject>> deleteRx(@Path("tableName") String tableName,@QueryMap Map<String,String> map);

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @GET("1/classes/{tableName}")
    public Call<JSONObject> list(@Path("tableName") String tableName, @QueryMap Map<String,String> map);

    @Headers({
            "X-Bmob-Application-Id: d906953dd3d938e25d240485f44c57bd",
            "X-Bmob-REST-API-Key: e9c5aaa92fd184361486049d4fef2a8c",
            "Content-Type: application/json"
    })
    @GET("1/classes/{tableName}")
    public Observable<Result<JSONObject>> rxlist(@Path("tableName") String tableName,@QueryMap Map<String,String> map);



}
