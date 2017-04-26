package cn.i0358.bmob;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by fanyk on 2017/4/24.
 */
public class DB<T> {

    private BmobQuery query;
    private String table;
    private DB(String table)
    {
        this.table=table;
        query=BmobQuery.create();
    }
    public static DB table(String table)
    {
        return new DB(table);
    }
    public DB where(String fieldName,Object value)
    {
        query.where(fieldName,value);
        return this;
    }
    public DB where(String fieldName,String exp,Object value) {
        query.where(fieldName, exp, value);
        return this;
    }
    public DB orderBy(String fieldName,Boolean asc)
    {
        query.orderBy(fieldName,asc);
        return this;
    }
    public DB perpage(int perPageNum) {
        this.query.perpage(perPageNum);
        return this;
    }

    public DB page(int pageNum, int perPageNum) {
        this.query.page(pageNum,perPageNum);
        return this;
    }
    public DB page(int pageNum)
    {
        this.query.page(pageNum);
        return this;
    }

    public JSONObject get()
    {
        try {
            Retrofit api=Api.createRxApi();
            BmobService bmobservice=api.create(BmobService.class);
            Call<JSONObject> jsonarr=bmobservice.list(this.table,this.query.toQueryMap());
            return jsonarr.execute().body();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Object obj)
    {
        try{

            Retrofit api=Api.createRxApi();
            BmobService bmobservice=api.create(BmobService.class);
            Call<JSONObject> json=bmobservice.save(this.table, obj);
            Response<JSONObject> result=json.execute();
            if(result.code()==201)
            {
                return true;
            }else{
                System.out.println(result.errorBody().string());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void save(final Object obj, final OnSave save)
    {
        try {

            Retrofit api = Api.createRxApi();
            BmobService bmobservice = api.create(BmobService.class);
            final Call<JSONObject> json = bmobservice.save(this.table, obj);
            json.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if(response.code()==201)
                    {
                        System.out.println("保存icp成功");
                        if(save!=null)
                        {

                            save.success(obj);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable throwable) {
                    System.out.println("保存icp失败");
                    throwable.printStackTrace();
                    if(save!=null)
                    {

                        save.error(obj,throwable);
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    interface OnSave<T>{
        public void success(T t);
        public void error(T t,Throwable throwable);
    }

}
