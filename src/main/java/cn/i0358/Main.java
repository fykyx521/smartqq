package cn.i0358;

import cn.i0358.model.QQData;
import cn.i0358.service.CarPeopleServcie;
import com.scienjus.smartqq.model.GroupMessage;

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

          Main main=new Main();
          QQData data=new QQData(12522111,System.currentTimeMillis(),"12345645",123444);
          main.handlerMessage(data);
         try{
//             System.in.read();
         }catch (Exception e)
         {

         }
         System.out.println("end"+Thread.currentThread().getName());

//         String message="111人找车，太原回临县，下午五六点走13663580433";
//
//         int pc=message.indexOf("人找车");
//         System.out.println(System.currentTimeMillis());

     }
}
