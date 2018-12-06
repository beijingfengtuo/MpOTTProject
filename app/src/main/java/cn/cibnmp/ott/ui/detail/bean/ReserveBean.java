package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by axl on 2018/1/30.
 */

public class ReserveBean implements Serializable {

    //还需要扩展，预约直播的一些信息，要在我的预约那展示
    private long liveStartTimeStamp;  //预约的直播开始时间戳
    private long currTimeStamp;  //当前的时间戳
    private String Lid;  //直播id
    //直播状态 0未开始 ， 1已开始 ， 2已结束 , 3回看
    private int livestatus;
    private long liveEndTimeStamp;  //直播结束的时间戳
    private String name;    //直播的名称
    private String posterFid;  //直播的海报
    private String liveStartDate;  //直播开始日期
    //直播状态 0未结束 ， 1已结束
    private int liveFlag;
    private String epgId;//应用的epgId
    private String action;
    private long sid;
    private String storyPlot;

    public String getLiveStartDate() {
        return liveStartDate;
    }

    public void setLiveStartDate(String liveStartDate) {
        this.liveStartDate = liveStartDate;
    }

    public int getLiveFlag() {
        return liveFlag;
    }

    public void setLiveFlag(int liveFlag) {
        this.liveFlag = liveFlag;
    }

    public String getEpgId() {
        return epgId;
    }

    public void setEpgId(String epgId) {
        this.epgId = epgId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getStoryPlot() {
        return storyPlot;
    }

    public void setStoryPlot(String storyPlot) {
        this.storyPlot = storyPlot;
    }

    public long getLiveStartTimeStamp() {
        return liveStartTimeStamp;
    }

    public void setLiveStartTimeStamp(long liveStartTimeStamp) {
        this.liveStartTimeStamp = liveStartTimeStamp;
    }

    public long getCurrTimeStamp() {
        return currTimeStamp;
    }

    public void setCurrTimeStamp(long currTimeStamp) {
        this.currTimeStamp = currTimeStamp;
    }

    public String getLid() {
        return Lid;
    }

    public void setLid(String lid) {
        Lid = lid;
    }

    public int getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(int livestatus) {
        this.livestatus = livestatus;
    }

    public long getLiveEndTimeStamp() {
        return liveEndTimeStamp;
    }

    public void setLiveEndTimeStamp(long liveEndTimeStamp) {
        this.liveEndTimeStamp = liveEndTimeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterFid() {
        return posterFid;
    }

    public void setPosterFid(String posterFid) {
        this.posterFid = posterFid;
    }
}
