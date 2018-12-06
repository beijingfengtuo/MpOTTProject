package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by axl on 2018/1/12.
 */

public class RecordBean implements Serializable {
    private long vid;
    private int epgId;
    private String videoType;
    private String vname;
    private long sid;
    private String sname;
    private long duration;
    private long currentPos;
    private String posterFid;
    private String videopoint;
    private String storyPlot;
    private String action;
    private long times;//传送时间戳

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    private int childType; //0电影，1电视剧，2综艺
    private String period; //期号
    private String no; // 轮播频道编号

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public int getEpgId() {
        return epgId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(long currentPos) {
        this.currentPos = currentPos;
    }

    public String getPosterFid() {
        return posterFid;
    }

    public void setPosterFid(String posterFid) {
        this.posterFid = posterFid;
    }

    public String getVideopoint() {
        return videopoint;
    }

    public void setVideopoint(String videopoint) {
        this.videopoint = videopoint;
    }

    public String getStoryPlot() {
        return storyPlot;
    }

    public void setStoryPlot(String storyPlot) {
        this.storyPlot = storyPlot;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getChildType() {
        return childType;
    }

    public void setChildType(int childType) {
        this.childType = childType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
