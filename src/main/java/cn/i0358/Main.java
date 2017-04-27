package cn.i0358;

import cn.i0358.bmob.DB;
import cn.i0358.model.ICP;
import cn.i0358.model.QQData;
import cn.i0358.model.QQTextParse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.GroupMessage;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

import java.util.Date;

/**
 * Created by fanyk on 2017/4/21.
 */
public class Main {



    public void handlerMessage(GroupMessage message)
     {
         Observable.just(message)
                .observeOn(Schedulers.newThread())
                .map((GroupMessage s)->{ System.out.println("map thread   "+Thread.currentThread().getName()); return QQTextParse.create(s.getContent());})
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
     }


     public static void main(String args[])
     {

          Date date=new Date();
          date.setTime(1492847075);
          System.out.println(date);

         QQData data=new QQData(1,System.currentTimeMillis(),"",1);
         String json=JSON.toJSONString(data);
         System.out.println(json);
         return;

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
