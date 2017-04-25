package cn.i0358.model;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanyk on 2017/4/25.
 */
public class QQTextParse {

    private String content;

    private int cptype; //0 人找车 1 车找人

    private FromTo from;
    private FromTo to;

    private String phone;
    private int pnum=1;//人数
    private SDate sdata;

    public QQTextParse(String content)
    {
        this.content=content;
        this.parse();
    }

    private void parse()
    {
        this.parsecptype();
        this.parsefromto();
        this.parsemobile();
        this.parsepeonum();
        this.parsedate();
    }

    /**
     *   解析时间
      */
    private void parsedate()
    {

    }

    private void parsepeonum()
    {
//        this.content.contains("一位");
        String content=this.content;
        Pattern pattern=Pattern.compile("(\\S)(位|个|人)");
        Matcher matcher=pattern.matcher(content);
        if(matcher.find())
        {
            String group=matcher.group();
            group=group.replace("一","1");
            group=group.replace("两","2");
            group=group.replace("二","2");
            group=group.replace("三","3");
            group=group.replace("四","4");

            Matcher matcher1=Pattern.compile("\\d+").matcher(group);
            if(matcher1.find())
            {
                try{
                    this.pnum=Integer.parseInt(matcher1.group());
                    if(this.pnum>4)
                    {
                        this.pnum=4;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


    }
    private void parsemobile()
    {
        Pattern datePattern = Pattern.compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
        Matcher mt=datePattern.matcher(this.content);
        if(mt.find())
        {
            this.phone=mt.group();
        }
    }

    /**
     *   解析 从哪到哪
     */
    private void parsefromto()
    {
         String[] addrs=new String[]{"临县","太原","离石"};
         List<FromTo> list=new ArrayList<FromTo>();
         for(String addr:addrs)
         {
             int index=this.content.indexOf(addr);
             if(index!=-1)
             {
                 list.add(new FromTo(index,addr));
             }
         }
         Collections.sort(list, new Comparator<FromTo>() {
             @Override
             public int compare(FromTo o1, FromTo o2) {
                 return (o1.index < o2.index) ? -1 : ((o1.index == o2.index) ? 0 : 1);
             }
         });
         if(list.size()==2)
         {
             this.from=list.get(0);
             this.to= list.get(1);
         }
    }
    private void parsecptype()
    {
        if(this.content.contains("找车"))
        {
            this.cptype=0;
            return;
        }
        if(this.content.contains("车找"))
        {
            this.cptype=1;
            return;
        }
        throw new RuntimeException("当前信息不符合"+this.content);
    }

    @Override
    public String toString() {
        String cstr=this.cptype==1?"车找人":"人找车";
        return cstr+ "人数:"+this.pnum+"---"+this.from+"到"+this.to+ "电话"+this.phone+" :"+this.pnum;

    }

    class FromTo implements Comparable<FromTo>{
        public Integer index;
        public String adds;
//        public int addr;
        public FromTo(int index,String adds)
        {
            this.index=index;
            this.adds=adds;
        }

        @Override
        public int compareTo(FromTo to) {
           return this.index.compareTo(to.index);
        }

        @Override
        public String toString() {
            return this.adds;
        }
    }
    public static class SDate{
        Date sdate;
        int  dtime;//出发时间
        int  mda;// 上午 1  中午2  下午3
        String content;
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        public SDate(String content)
        {
            this.content=content;
            this.parse();
        }
        public static SDate create(String content)
        {
            return new SDate(content);
        }
        public void parse()
        {
            this.parseDay();
            this.parseTime();
        }
        private void parseDay()
        {
            if(this.content.contains("明天")) {
                this.date.plusDays(1);
            }
            if(this.content.contains("后天")) {
                this.date.plusDays(2);
            }
        }
        private void parseTime()
        {
            if(this.content.contains("早上"))
            {
                this.mda=1;
            }
            if(this.content.contains("午饭")||this.content.contains("中午"))
            {
                this.mda=2;
            }
            if(this.content.contains("下午"))
            {
                this.mda=3;
            }
            Pattern pattern=Pattern.compile("(\\S)(点)");
            Matcher matcher=pattern.matcher(this.content);
            if(matcher.find())
            {
                Matcher matcher1=Pattern.compile("(\\d)$").matcher(matcher.group());
                if(matcher1.find())
                {
                    String group=matcher.group();
                    int stime=Integer.parseInt(group);
                    System.out.println(stime);
                    this.time.withField(DateTimeFieldType.hourOfDay(),stime);
                }


            }

        }

        @Override
        public String toString() {
            return this.date.toString()+"-------"+this.time.toString();
        }
    }
}
