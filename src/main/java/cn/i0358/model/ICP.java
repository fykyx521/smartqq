package cn.i0358.model;

import cn.i0358.util.BmobDateSerializer;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by fanyk on 2017/4/22.
 */
public class ICP {


    //    cp.set("cptype",Number(this.cptype));
//    cp.set('from',Number(this.from));
//    cp.set('to',Number(this.to));
//    cp.set('startdate',new Date());
//    cp.set('starttime',Number(this.starttime));
//    cp.set('peoplenum',Number(this.peoplenum));
//    cp.set('unitprice',Number(this.unitprice));
    private int cptype;
    private int from;
    private int to;
    @JSONField(serializeUsing = BmobDateSerializer.class)
    private Date startdate;



    private Long phone;
    private long starttime;
    private int peoplenum;
    private int unitprice;
    private int datafrom=1;//数据来源 1 QQ

    public int getDatafrom() {
        return datafrom;
    }

    public void setDatafrom(int datafrom) {
        this.datafrom = datafrom;
    }


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getQqgroup() {
        return qqgroup;
    }

    public void setQqgroup(String qqgroup) {
        this.qqgroup = qqgroup;
    }

    public String getQqtext() {
        return qqtext;
    }

    public void setQqtext(String qqtext) {
        this.qqtext = qqtext;
    }

    private String qq;
    private String qqgroup;
    private String qqtext;



    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }


    public Date getStartdate() {
        return startdate;
    }


    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public int getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(int peoplenum) {
        this.peoplenum = peoplenum;
    }

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public int getCptype() {
        return cptype;
    }

    public void setCptype(int cptype) {
        this.cptype = cptype;
    }
    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public void qqgrouptext(String qq,String qqgroup,String content)
    {
        this.qq=qq;
        this.qqgroup=qqgroup;
        this.qqtext=content;
    }


}
