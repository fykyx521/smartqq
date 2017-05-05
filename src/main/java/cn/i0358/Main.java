package cn.i0358;

import cn.i0358.bmob.DB;
import cn.i0358.model.ICP;
import cn.i0358.model.QQData;
import cn.i0358.model.QQTextParse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import retrofit2.adapter.rxjava2.Result;

import java.time.Period;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by fanyk on 2017/4/21.
 */
public class Main {

    private static Map<Long,GroupMessage> map= Collections.synchronizedMap(new WeakHashMap<Long, GroupMessage>());

    private boolean filterMessage(GroupMessage message)
    {
        boolean cansave=false;
        try {
            GroupMessage mapmes=map.get(message.getUserId());
            if(mapmes!=null)
            {
                System.out.println("nulll");
                Long now=System.currentTimeMillis();
                Interval it=new Interval(mapmes.getDt(),now);
                if(it.toPeriod().getMinutes()>=15)
                {
                    System.out.println("true");
                    cansave=true;
                }
            }else{
                message.setDt(System.currentTimeMillis());
                map.put(message.getUserId(),message);
                System.out.println("true");
                cansave=true;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cansave;
    }

    public void handlerMessage(GroupMessage message)
     {

         boolean cansave=this.filterMessage(message);
         if(!cansave)
         {
             System.out.println("cannot save");
             return;
         }
//         System.out.println(Thread.currentThread().getName());
         Observable<Result<JSONObject>> result=DB.table("icp").where("qq",message.getUserId()+"").first();
         result.observeOn(Schedulers.newThread())
         .filter(new Predicate<Result<JSONObject>>() {
             @Override
             public boolean test(Result<JSONObject> jsonObjectResult) throws Exception {
                 System.out.println("filter");

                 if(jsonObjectResult.isError())
                 {
                     return true;
                 }else{
                     JSONObject obj=jsonObjectResult.response().body();
                     JSONArray arr=obj.getJSONArray("results");
                     if(arr.size()==0)
                     {
                         return true;
                     }
                     JSONObject first=arr.getJSONObject(0);
                     String updatedAt=first.getString("updatedAt");
                     DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                     LocalDateTime time= LocalDateTime.parse(updatedAt,format);
                     LocalDateTime now= LocalDateTime.now();
                     Interval it=new Interval(time.toDate().getTime(),now.toDate().getTime());
                     int minutes = it.toPeriod().getMinutes();
                     if(minutes>=15) //15 分钟 以内不重复发
                     {
                         return true;
                     }
                     return false;
                 }
             }
         }).flatMap(new Function<Result<JSONObject>, Observable<GroupMessage>>() {
             @Override
             public Observable<GroupMessage> apply(Result<JSONObject> jsonObjectResult) throws Exception {
                 System.out.println("flag map");
                 return Observable.just(message);
             }
         }).map((GroupMessage s)->{ System.out.println("map thread   "+Thread.currentThread().getName()); return QQTextParse.create(s.getContent());})
                .map((QQTextParse qtp)->{ICP icp=qtp.toIcp();  icp.qqgrouptext(message.getUserId()+"",message.getGroupId()+"",message.getContent()); return icp;})
                .flatMap((ICP icp2)->{System.out.println("flatmap thread  "+Thread.currentThread().getName()); return DB.table("icp").saveRx(icp2); })
                .map((Result<JSONObject> obj)->{
                    System.out.println("map1 thread   "+Thread.currentThread().getName());
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
     }

    private static void ct(String name)
    {
        System.out.println(name+":"+Thread.currentThread().getName());
    }
     public static void main(String args[])
     {

//         Observable.just(123)
//                 .map(new Function<Integer, Object>() {
//
//                     @Override
//                     public Object apply(Integer integer) throws Exception {
//                         ct("map");
//                         return integer+1;
//                     }
//                 })
//                 .filter(new Predicate<Object>() {
//             @Override
//             public boolean test(Object o) throws Exception {
//                 ct("filter");
//                 return true;
//             }
//         }).subscribeOn(Schedulers.io())
//                 .subscribeOn(Schedulers.computation())
//                 .flatMap(new Function<Object, ObservableSource<?>>() {
//
//                     @Override
//                     public ObservableSource<?> apply(Object o) throws Exception {
//                         ct("flatmap");
//                         return Observable.just(o);
//                     }
//                 })
//                 .map(new Function<Object, Object>() {
//
//                     @Override
//                     public Object apply(Object o) throws Exception {
//                         ct("map2");
//                         return o;
//                     }
//                 })
//                 .observeOn(Schedulers.newThread())
//                 .subscribe(new Consumer<Object>() {
//             @Override
//             public void accept(Object o) throws Exception {
//                 ct("accept");
//             }
//         });
//
//         try {
//             Thread.sleep(10000);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
            GroupMessage message=new GroupMessage(2,System.currentTimeMillis(),112233,"1人找车，太原回临县，下午五六点走13663580433");
            new Main().handlerMessage(message);
            new Main().handlerMessage(message);


         try {
             Thread.sleep(10000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }

//          Main main=new Main();
//          QQData data=new QQData(12522111,System.currentTimeMillis(),"12345645",123444);
//          main.handlerMessage(data);
//         try{
////             System.in.read();
//         }catch (Exception e)
//         {
//
//         }
//         System.out.println("end"+Thread.currentThread().getName());

//         String message="111人找车，太原回临县，下午五六点走13663580433";
//
//         int pc=message.indexOf("人找车");
//         System.out.println(System.currentTimeMillis());

     }
}
