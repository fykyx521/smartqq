package cn.i0358.util;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Created by fanyk on 2017/4/22.
 */
public class BmobIntTodate implements ObjectSerializer{
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {

        Long value = (Long) object;
        Date dt=new Date();
        dt.setTime(value);
        Map<String,String> map=new HashMap<String, String>();
        map.put("__type","Date");
        map.put("iso",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(dt));
        serializer.writeWithFieldName(map,"senddate");
        serializer.writeWithFieldName(value,"time");



    }
}
