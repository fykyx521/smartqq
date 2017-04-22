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

    private int starttime;
    private int peoplenum;
    private int unitprice;


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

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
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


}
