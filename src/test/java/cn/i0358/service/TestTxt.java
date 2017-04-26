package cn.i0358.service;

import cn.i0358.model.QQTextParse;
import cn.i0358.util.ChineseNumber;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import junit.framework.TestCase;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalTime;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanyk on 2017/4/25.
 */
public class TestTxt extends TestCase {

    public void testReadLine()
    {
        try {
            File file=new File("a.txt");
            List<String> lines=Files.readLines(file,Charsets.UTF_8);
            for (String line:lines) {

                try {
                    QQTextParse parse=new QQTextParse(line);
                    System.out.println(line+"----"+parse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void testParse()
    {
        String content="明天 离石去太原车找3人18634778899";
        QQTextParse parse=new QQTextParse(content);
        System.out.println(parse.toString());

    }
    public void testDate()
    {
        String content="下午3点出发";
//        LocalTime.now().withField(DateTimeFieldType.hourOfDay(),13)
        System.out.println(QQTextParse.SDate.create(content));
    }


    public void testPnum()
    {
        //String content="一位 2位 3位 2人4人 三人 四人 二人";
        //String content="8点临县到太原 人找车。一位15340661122";
        String content="明天 离石去太原车找2人18634778899";
        Pattern pattern=Pattern.compile("(\\S)(位|个|人)");
        Matcher matcher=pattern.matcher(content);
        while (matcher.find())
        {
            System.out.println(matcher.group());
            Matcher matcher1=Pattern.compile("\\d+").matcher("1人");
//            System.out.println();
            if(matcher1.find())
            {
                System.out.println(matcher1.group());
            }
        }
//        System.out.println(Integer.parseInt("。1位"));

    }
    public void testDian()
    {
        Matcher matcher3=Pattern.compile("\\d{1,2}").matcher("12点");
        if(matcher3.find())
        {
            System.out.println(matcher3.group());
            return;
        }
        String hanzi="十三点";
        int num=ChineseNumber.chineseNumber2Int("12点");
        System.out.println(num);

        Pattern pattern=Pattern.compile("(\\S\\S)(点)");
        Matcher matcher=pattern.matcher("13点");
        if(matcher.find())
        {
            System.out.println(matcher.group());
        }
    }
    public void testDian2()
    {
        QQTextParse.SDate date=QQTextParse.SDate.create("明天12点走");
        System.out.println(date.toString());
//        System.out.println(LocalTime.now().withField(DateTimeFieldType.hourOfDay(),12).toString());
    }

}
