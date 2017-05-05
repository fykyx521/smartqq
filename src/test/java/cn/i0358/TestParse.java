package cn.i0358;

import cn.i0358.model.ICP;
import cn.i0358.model.QQTextParse;
import junit.framework.TestCase;

/**
 * Created by fanyk on 2017/5/4.
 */
public class TestParse extends TestCase{


    public void test0()
    {
        String content="人找车，明天早上8点多9点也行临县去太原13152981398";
        QQTextParse txt=QQTextParse.create(content);
        System.out.println(txt.toString());
    }
}
