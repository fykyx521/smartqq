package cn.i0358.model;

import cn.i0358.util.ChineseNumber;
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
        icp.setStartdate(this.sdata.getDate());
        icp.setStarttime(this.sdata.getTime());
        icp.setPeoplenum(this.pnum);
        icp.setCptype(this.cptype);
        icp.setUnitprice(0);
        icp.setFrom(this.from.getAddr());
        icp.setTo(this.to.getAddr());
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
            return this.date.toDate();
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
            }
            if(this.content.contains("后天")) {
                this.date=this.date.plusDays(2);
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
            if(matcher.find())
            {
                String dian=matcher.group();
                System.out.println("点:"+dian);
                Matcher matcher1=Pattern.compile("\\d{1,2}").matcher(dian);
                if(matcher1.find())
                {
                    String group=matcher1.group();
                    int stime=Integer.parseInt(group);
                    System.out.println("s1---"+stime);
                    this.time=this.time.withField(DateTimeFieldType.hourOfDay(),stime);
                }else{
                    int stime= ChineseNumber.chineseNumber2Int(dian);
                    System.out.println("s2-----"+stime);
                    System.out.println(this.time.toString());
                    this.time=this.time.withField(DateTimeFieldType.hourOfDay(),stime);

                    System.out.println(this.time.toString());
                }


            }

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
