package cn.i0358.bmob;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by fanyk on 2017/4/22.
 */
public class Api {

        public static String APPKEY="d906953dd3d938e25d240485f44c57bd";
        public static String RESTKRY="e9c5aaa92fd184361486049d4fef2a8c";
        public static Retrofit createApi()
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .baseUrl("https://api.bmob.cn/")
                    .build();
            return retrofit;
        }
        public static Retrofit createRxApi()
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://api.bmob.cn/")
                    .build();
            return retrofit;
        }
}
