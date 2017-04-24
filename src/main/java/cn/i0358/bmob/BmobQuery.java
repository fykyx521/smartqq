package cn.i0358.bmob;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import java.util.*;

/**a
 *
 Key	Operation
 $lt	小于
 $lte	小于等于
 $gt	大于
 $gte	大于等于
 $ne	不等于
 $in	包含在数组中
 $nin	不包含在数组中
 $exists	这个 Key 有值
 $select	匹配另一个查询的返回值
 $dontSelect	排除另一个查询的返回
 $all	包括所有给定的值
 $regex	匹配PCRE表达式
 * Created by fanyk on 2017/4/24.
 */
public class BmobQuery
{
    private Map<String,Object> query;//查询条件
    private List<String> orderquery;//排序排行
    private Map<String,String> expQuery;//表达式Map

    private BmobQuery() {
        query=new HashMap<String, Object>();
        orderquery=new ArrayList<String>();
    }

    public static BmobQuery create()
    {
        return new BmobQuery();
    }

    private String tobmobExp(String option)
    {
        String value="";
        switch (option)
        {
            case ">": value="gt";break;
            case ">=":value="gte";break;
            case "<":value="lt";break;
            case "<=": value="lte";break;
            case "!=":value="ne";break;
        }
        return "$"+value;
    }

    public BmobQuery where(String key,Object value)
    {
        this.query.put(key,value);
        return this;
    }

    public BmobQuery where(String key,String option,Object value)
    {
        String bmobexp=this.tobmobExp(option);
        this.putExp(key,bmobexp,value);
        return this;
    }
    public BmobQuery orderBy(String key,Boolean isasc)
    {
        this.orderquery.add(isasc?key:"-"+key);
        return this;
    }

    private void putExp(String key,String bmobexp,Object value)
    {
        if(this.query.containsKey(key))
        {
            Map<String,Object> param=(Map<String,Object>)(this.query.get(key));
            param.put(bmobexp,value);
        }else{
            Map<String,Object> exp=new HashMap<String,Object>();
            exp.put(bmobexp,value);
            this.query.put(key,exp);
        }
    }



    /**
     *   转为map
      * @return
     */
    public Map<String,String> toQueryMap()
    {
        Map<String,String> mapquery=new HashMap<String, String>();
        mapquery.put("where",JSON.toJSONString(this.query));
        if(this.orderquery.size()>0)
        {
            mapquery.put("order",Joiner.on(",").join(this.orderquery));
        }
        return mapquery;
    }

    private class Where{
        private String exp;
        private Object value;

        public Where(String exp,Object value) {
            this.exp = exp;
            this.value=value;
        }
    }




}
