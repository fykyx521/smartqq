package cn.i0358.bmob;

import cn.i0358.model.ICP;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by fanyk on 2017/4/24.
 */
public class DB {

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

    /**
     *  获取一行记录
     * @return
     */
    public Observable<Result<JSONObject>> rxfirst()
    {
        this.page(1,1);
        Observable<Result<JSONObject>> json= this.rxget();
        return json;
    }
    /**
     *  获取一行记录
     * @return
     */
    public JSONObject first()
    {
        this.page(1,1);
        JSONObject json= this.get();
        return json;
    }


    public Observable<Result<JSONObject>> rxget()
    {
        try {
            Retrofit api=Api.createRxApi();
            BmobService bmobservice=api.create(BmobService.class);
            Observable<Result<JSONObject>> jsonarr=bmobservice.rxlist(this.table,this.query.toQueryMap());
            return jsonarr;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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

            Retrofit api=Api.createApi();
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
    public Observable<Result<JSONObject>> saveRx(final Object obj)
    {
            Retrofit api = Api.createRxApi();
            BmobService bmobservice = api.create(BmobService.class);
            final Observable<Result<JSONObject>> json = bmobservice.saveRx(this.table, obj);
            return json;
    }

    interface OnSave<T>{
        public void success(T t);
        public void error(T t,Throwable throwable);
    }

}
