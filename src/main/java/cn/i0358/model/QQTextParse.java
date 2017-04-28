package cn.i0358.model;

import cn.i0358.util.ChineseNumber;
import org.joda.time.*;

import java.sql.*;
import java.util.*;
import java.util.Date;
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

    private Long phone;
    private int pnum=1;//人数
    private SDate sdata;

    public QQTextParse(String content)
    {
        this.content=content;
        this.parse();
    }
    public static QQTextParse create(String content)
    {
        return new QQTextParse(content);
    }

    private void parse()
    {
        this.parsecptype();
        this.parsefromto();
        this.parsemobile();
        this.parsepeonum();
        this.parsedate();
    }

    public ICP toIcp()
    {
        ICP icp=new ICP();
        Date startDate=this.sdata.getDate();
        icp.setStartdate(startDate);
        icp.setStarttime(startDate.getTime());
        icp.setPeoplenum(this.pnum);
        icp.setCptype(this.cptype);
        icp.setUnitprice(0);
        icp.setFrom(this.from.getAddr());
        icp.setTo(this.to.getAddr());
        icp.setPhone(this.phone);
        return icp;
    }



    /**
     *   解析时间
      */
    private void parsedate()
    {
        this.sdata=SDate.create(this.content);
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
            this.phone=Long.parseLong(mt.group());
        }else{
            throw new RuntimeException("电话号码不符合");
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
        if(this.content.contains("私家车"))
        {
            this.cptype=1;
            return;
        }
        if(this.content.contains("有车没"))
        {
            this.cptype=0;
            return;
        }

        throw new RuntimeException("当前信息不符合"+this.content);
    }

    @Override
    public String toString() {
        String cstr=this.cptype==1?"车找人":"人找车";
        return cstr+ "人数:"+this.pnum+"---"+this.from+"到"+this.to+ "电话"+this.phone+"人数 :"+this.pnum+" 时间"+this.sdata;

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
        public int getAddr()
        {
//            <option value="141124">临县</option>
//            <option value="140100">太原</option>
//            <option value="141102">离石</option>
            int result=0;
            switch (this.adds)
            {
                case "临县": result=141124;break;
                case "太原": result=140100;break;
                case "离石":result=141102;break;
            }
            return result;
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
        int  mda=0;// 上午 1  中午2  下午3
        String content;
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        public SDate(String content)
        {
            this.content=content;
            this.parse();
        }
        public Date getDate(){
//            LocalDateTime
            DateTime time=new DateTime();
            return time.withDate(this.date).withHourOfDay(this.time.getHourOfDay()).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
        }
        public int getTime()
        {
            return this.time.getHourOfDay();
        }

        public static SDate create(String content)
        {
            return new SDate(content);
        }
        private void parse()
        {
            this.parseDay();
            this.parseTime();
        }
        private void parseDay()
        {
            if(this.content.contains("明")) {
                this.date=this.date.plusDays(1);
                return;
            }
            if(this.content.contains("后天")) {
                this.date=this.date.plusDays(2);
                return;
            }
            if(this.content.contains("号"))
            {
                Matcher matcher1=Pattern.compile("\\d{1,2}号").matcher(content);
                int day=0;
                if(matcher1.find())
                {
                    String hao=matcher1.group();
                    Matcher matcher2=Pattern.compile("\\d{1,2}").matcher(hao);
                    if(matcher2.find())
                    {
                        String group=matcher2.group();
                        day=Integer.parseInt(group);
                    }else{
                        day= ChineseNumber.chineseNumber2Int(hao);
                    }
                    if(day<this.date.dayOfMonth().get())
                    {
                        this.date=this.date.plusMonths(1).withField(DateTimeFieldType.dayOfMonth(),day);
                    }else{
                        this.date=this.date.withField(DateTimeFieldType.dayOfMonth(),day);
                    }
                }
            }
        }

        private void parseTime()
        {
            if(this.content.contains("早上"))
            {
                this.mda=1;
            }
            if(this.content.contains("早"))
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
            Pattern pattern=Pattern.compile("(\\S\\S)(点)");
            Matcher matcher=pattern.matcher(this.content);
            int stime=0;
            boolean match=matcher.find();
            if(match)
            {
                String dian=matcher.group();
                Matcher matcher1=Pattern.compile("\\d{1,2}").matcher(dian);
                if(matcher1.find())
                {
                    String group=matcher1.group();
                    stime=Integer.parseInt(group);
                }else{
                    stime= ChineseNumber.chineseNumber2Int(dian);
                }
            }else{
                if(mda==2)
                {
                    stime=13;
//                    this.time=this.time.withField(DateTimeFieldType.hourOfDay(),13);
                }
                if(this.content.contains("现在")||this.content.contains("随时走"))
                {
                    int minuteOfHour=this.time.getMinuteOfHour();
                    stime=this.time.getHourOfDay()+1;
                }
            }
            if(stime<10&&this.mda>1)// 中午或者下午
            {
                stime=12+stime;
            }
            if(stime<=4&&this.mda==0)
            {
                stime=12+stime;
            }
            if(stime==0&&this.mda==1)
            {
                stime=8;
            }

            if(this.date.getDayOfMonth()==LocalDate.now().getDayOfMonth())//如果是今天
            {
                int hour=LocalTime.now().getHourOfDay();
                if(hour>10&&stime<10) // 5 6点出发 但是没说今天
                {
                    stime+=12;
                }
            }
            System.out.println(stime);
            this.time=this.time.withField(DateTimeFieldType.hourOfDay(),stime);
        }
        private String replace(String dian)
        {
//            String[] hanzi=new String[]{"十二","十一","十","四",}
            return "";
        }
        @Override
        public String toString() {
            return this.date.toString()+"-------"+this.time.toString();
        }
    }
}
