package cn.i0358;

import cn.i0358.service.CarPeopleServcie;
import cn.i0358.model.ICP;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import java.util.Date;

/**
 * Created by fanyk on 2017/4/21.
 */
public class TestMessage extends TestCase {

        @Override
        public void runBare() throws Throwable {
                super.runBare();
        }


        public void testSer()
        {
                ICP icp=new ICP();
                icp.setCptype(1);
                icp.setFrom(1);
                icp.setTo(1);
                icp.setStartdate(new Date());
                icp.setStarttime(10);
                icp.setUnitprice(100);
                String json=JSON.toJSONString(icp);
                System.out.println(json);
        }
        public void testInsert()
        {
                try{
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(FastJsonConverterFactory.create())
                                .baseUrl("https://api.bmob.cn/")
                                .build();
                        CarPeopleServcie service = retrofit.create(CarPeopleServcie.class);
                        ICP icp=new ICP();
                        icp.setCptype(1);
                        icp.setFrom(1);
                        icp.setTo(1);
                        icp.setStartdate(new Date());
                        icp.setStarttime(10);
                        icp.setUnitprice(100);
                        Call<JSONObject> json=service.save(icp);
//                        if(json.execute().code())
                        System.out.println(json.execute().body().get("createdAt"));
                }catch (Exception e)
                {
                       e.printStackTrace();
                }

        }
}
