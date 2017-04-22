package cn.i0358;

import cn.i0358.model.QQData;
import cn.i0358.service.CarPeopleServcie;
import com.alibaba.fastjson.JSON;
import com.scienjus.smartqq.model.GroupMessage;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Date;

/**
 * Created by fanyk on 2017/4/21.
 */
public class Main {


     public void handlerMessage(GroupMessage message)
     {
//          if(message.getContent().indexOf("车找人"))
          new Service().handle(message);
     }

     public void handlerMessage(QQData data)
     {
         new Service().handle(data);
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
