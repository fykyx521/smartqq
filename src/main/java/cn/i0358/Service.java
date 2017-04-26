package cn.i0358;

import cn.i0358.bmob.DB;
import cn.i0358.model.ICP;
import cn.i0358.model.QQData;
import cn.i0358.model.QQTextParse;
import cn.i0358.service.CarPeopleServcie;
import cn.i0358.bmob.Api;
import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.GroupMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by fanyk on 2017/4/21.
 */
public class Service {


        public void handle(GroupMessage message){
                QQData data=QQData.create(message);
                this.handle(data);

        }

        public void handle(QQData data){
                data=this.filter(data);
                this.save(data);
                try{
                        QQTextParse parse=QQTextParse.create(data.getContent());
                        ICP icp=parse.toIcp();
                        icp.setQq(data.getUserId()+"");
                        icp.setQqgroup(data.getGroupId()+"");
                        icp.setQqtext(data.getContent());
                        icp.setDatafrom(1);
                        DB.table("icp").save(icp,null);
                }catch (Exception e)
                {
                        e.printStackTrace();
                }
        }


        private QQData filter(QQData data)
        {
                return data;
        }

        private void save(QQData data)
        {
                try{
                        Retrofit retrofit = Api.createApi();
                        CarPeopleServcie service = retrofit.create(CarPeopleServcie.class);
                        Call<JSONObject> result=service.save(data);
                        result.enqueue(new Callback<JSONObject>() {
                                @Override
                                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                        if(response.code()==201)
                                        {
                                                System.out.println("保存成功"+Thread.currentThread().getName());
                                        }else {
                                                try{
                                                        System.out.println(response.errorBody().string());
                                                }catch (Exception e){
                                                        e.printStackTrace();
                                                }
                                        }




                                }

                                @Override
                                public void onFailure(Call<JSONObject> call, Throwable throwable) {
                                        System.out.println("保11111111111111");
                                }
                        });
                        System.out.println("保存");
                }catch (Exception e)
                {
                        e.printStackTrace();
                }
        }




}
