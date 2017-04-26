package cn.i0358.service;

import cn.i0358.bmob.DB;
import cn.i0358.model.ICP;
import cn.i0358.bmob.Api;
import cn.i0358.bmob.BmobQuery;
import cn.i0358.model.QQTextParse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import junit.framework.TestCase;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by fanyk on 2017/4/24.
 */
public class TestBmob extends TestCase {

    public void testDB()
    {
        JSONObject obj=DB.table("QQData").orderBy("createdAt",false).get();
        JSONArray arr=obj.getJSONArray("results");
        System.out.println(arr.size());
        System.out.println(arr.toJSONString());
        if(true)
        {
            return;
        }


        File file=new File("a.txt");
        for (Object item: arr) {
            try{
                JSONObject item2=(JSONObject) item;
                String content=item2.getString("content");
                if(!content.contains("车"))
                {
                    continue;
                }
                content=content.replaceAll("\\n","");
                content=content+'\n';
                Files.append(content,file, Charset.forName("UTF-8"));
            }catch (Exception e)
            {
                e.printStackTrace();
                return;
            }


        }
        System.out.println(obj.toJSONString());
    }




    public void testInsert()
    {
        ICP icp=new ICP();
        icp.setCptype(1);
        icp.setFrom(1);
        icp.setTo(1);
        icp.setStartdate(new Date());
        icp.setStarttime(10);
        icp.setUnitprice(100);
        boolean insert=DB.table("icp").insert(icp);
        System.out.println(insert);
    }

    public void testInsert2() throws InterruptedException {
        String content="太原回临县短4个，7-8点，车找人18035813587";
        QQTextParse parse=QQTextParse.create(content);
        ICP icp=parse.toIcp();
        icp.setQq("qq");
        icp.setQqgroup("qq2");
        icp.setQqtext(content);
        DB.table("icp").save(icp,null);
        Thread.sleep(3000);
//        System.out.println(insert);
    }

    public void testList()
    {

        Retrofit api=Api.createRxApi();
        CarPeopleServcie service = api.create(CarPeopleServcie.class);
        BmobQuery query=BmobQuery.create();
        query.where("groupId","!=",2192598935L);
        query.orderBy("createdAt",false);
        Observable<JSONObject> result=service.list(query.toQueryMap());

    //    result.subscribe()
        result.subscribe(new Consumer<JSONObject>() {
            @Override
            public void accept(JSONObject jsonObject) throws Exception {
                System.out.println("exist");
                System.out.println(jsonObject.toJSONString());
            }
        });
    }
    public void testQueryMap()
    {

        BmobQuery query=BmobQuery.create();
        query.where("groupId","2192598935").orderBy("createdAt",false).page(2,5);
        System.out.println(query.toQueryMap());
    }

    public void testList2()
    {

        Retrofit api=Api.createApi();
        CarPeopleServcie service = api.create(CarPeopleServcie.class);

        BmobQuery query=BmobQuery.create();
        query.where("groupId","!=",2192598935L);
        query.orderBy("createdAt",false);
        Call<JSONObject> result=service.list2(query.toQueryMap());
        try{
            String error=result.execute().body().toJSONString();
            System.out.println(error);
        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }

}
