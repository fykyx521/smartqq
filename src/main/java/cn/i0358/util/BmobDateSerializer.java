package cn.i0358.util;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanyk on 2017/4/22.
 */
public class BmobDateSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        Date value = (Date) object;
        Map<String,String> map=new HashMap<String, String>();
        map.put("__type","Date");
        map.put("iso",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(value));
        serializer.write(map);
//        throw new IOException("error");
//        serializer.write(map);
    }


}
