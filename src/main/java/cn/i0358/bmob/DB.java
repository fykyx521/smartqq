package cn.i0358.bmob;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
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

    public boolean insert(Object obj)
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

}
