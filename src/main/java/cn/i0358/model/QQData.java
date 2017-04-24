package cn.i0358.model;


import cn.i0358.util.BmobIntTodate;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.scienjus.smartqq.model.GroupMessage;

/**
 * Created by fanyk on 2017/4/22.
 */
public class QQData {

    private long groupId;
//    @JSONField(serializeUsing = BmobIntTodate.class)
    private long time;
    // @JSONField(nos)
    private String content;
    private long userId;

    public QQData()
    {

    }
    public QQData(long groupId, long time, String content, long userId) {
        this.groupId = groupId;
        this.time = time;
        this.content = content;
        this.userId = userId;
    }


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static QQData create(GroupMessage message)
    {
        QQData data=new QQData();
        data.setContent(message.getContent());
        data.setTime(message.getTime());
        data.setGroupId(message.getGroupId());
        data.setUserId(message.getUserId());
        return data;
    }

    public static QQData createFromJson(JSONObject obj)
    {
        return null;
    }
}
