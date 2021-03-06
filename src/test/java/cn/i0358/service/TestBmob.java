package cn.i0358.service;

import cn.i0358.bmob.Api;
import cn.i0358.bmob.BmobQuery;
import cn.i0358.bmob.DB;
import cn.i0358.model.ICP;
import cn.i0358.model.QQData;
import cn.i0358.model.QQTextParse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import com.scienjus.smartqq.model.GroupMessage;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
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

    public void testTime()
    {
        DateTime time= LocalDate.now().toDateTime(LocalTime.now());
        System.out.println(time);
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
        boolean insert=DB.table("icp").save(icp);
        System.out.println(insert);
    }

    public void testInsert2() throws Exception {
//        太原回离石。三人找车。4点走13663684042
        //明天中午太原到离石~，车找人，联系方式18534787592*18534787593"

//        String content="太原回离石。三人找车。4点走13663684042";
//                //明天中午太原到离石~，车找人，联系方式18534787592*18534787593"
//        QQTextParse parse=QQTextParse.create(content);
//        ICP icp1=parse.toIcp();
//        System.out.println(icp1.getPhone());
//        DB.table("icp").save(icp1);
//        if(true)
//        {
//            return;
//        }
//        throw new Exception("dsad");
//        icp.setQq("qq");
//        icp.setQqgroup("qq2");
//        icp.setQqtext(content);
//        icp.setTest()
        System.out.println(Thread.currentThread().getName());
        GroupMessage message=new GroupMessage(1,2,3,"太原回离石。三人找车。4点走13663684042");
        Observable.just(message)
                .observeOn(Schedulers.newThread())
                .map((GroupMessage s)->{ System.out.println("map thread:"+Thread.currentThread().getName()); return QQTextParse.create(s.getContent());})
                .map((QQTextParse qtp)->{ICP icp=qtp.toIcp();  icp.qqgrouptext(message.getUserId()+"",message.getGroupId()+"",message.getContent()); return icp;})
                .flatMap((ICP icp2)->{System.out.println("f1"+Thread.currentThread().getName()); return DB.table("icp").saveRx(icp2); })
                .map((Result<JSONObject> obj)->{
                    System.out.println("map1"+Thread.currentThread().getName());
                    if(!obj.isError()) {
                        return QQData.create(message);
                    }
                    return null;

                })
                .flatMap((QQData data)->
                 {if(data!=null){
                     return DB.table("QQdata").saveRx(data);
                  } return Observable.empty();
                 })
//                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<JSONObject>>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("sub thread"+Thread.currentThread().getName());
                System.out.println("sub");
            }

            @Override
            public void onNext(Result<JSONObject> value) {
                System.out.println("next thread"+Thread.currentThread().getName());
                System.out.println("next"+value.response().code());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error thread"+Thread.currentThread().getName());
                System.out.println("error"+e.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("complete thread"+Thread.currentThread().getName());
                System.out.println("complete");
            }
        });

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
    public void testDate()
    {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime.parse("2017-05-03 08:28:11",format);
    }
    public void testFirst()
    {
//        Observable<Result<JSONObject>> obj=DB.table("icp").where("qq" ,"2049474019").orderBy("createdAt",false).first();
//        obj.subscribe(new Consumer<Result<JSONObject>>() {
//            @Override
//            public void accept(Result<JSONObject> jsonObjectResult) throws Exception {
//                if(!jsonObjectResult.isError())
//                {
//                }
//                System.out.println(jsonObjectResult.response().code());
//                System.out.println(jsonObjectResult.response().body());
//            }
//        });

        Observable<Result<JSONObject>> result=DB.table("icp").where("qq","2049474019").first();
        result.filter(new Predicate<Result<JSONObject>>() {
            @Override
            public boolean test(Result<JSONObject> jsonObjectResult) throws Exception {
                if(jsonObjectResult.isError())
                {
                    return false;
                }else{
                    JSONObject obj=jsonObjectResult.response().body();
                    JSONArray arr=obj.getJSONArray("results");
                    if(arr.size()==0)
                    {
                        return false;
                    }
                    JSONObject first=arr.getJSONObject(0);
                    String updatedAt=first.getString("updatedAt");
                    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    org.joda.time.LocalDateTime time= org.joda.time.LocalDateTime.parse(updatedAt,format);
                    org.joda.time.LocalDateTime now= org.joda.time.LocalDateTime.now();
                    Interval it=new Interval(time.toDate().getTime(),now.toDate().getTime());
                    int minutes = it.toPeriod().getMinutes();
                    System.out.println("分种:"+minutes);
                    if(minutes>=3)
                    {
                        return true;
                    }
                    return false;
                }
            }
        }).compose(new ObservableTransformer<Result<JSONObject>, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<Result<JSONObject>> upstream) {
                return null;
            }
        });
    }

}
