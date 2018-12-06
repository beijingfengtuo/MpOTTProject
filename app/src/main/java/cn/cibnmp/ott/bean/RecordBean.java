package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * 观看历史 界面收藏等公用bean
 *
 * Created by cibn-lyc on 2018/1/16.
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStoryPlot() {
        return storyPlot;
    }

    public void setStoryPlot(String storyPlot) {
        this.storyPlot = storyPlot;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getChildType() {
        return childType;
    }

    public void setChildType(int childType) {
        this.childType = childType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "{" +
                "vid=" + vid +
                ", epgId=" + epgId +
                ", videoType='" + videoType + '\'' +
                ", vname='" + vname + '\'' +
                ", sid=" + sid +
                ", sname='" + sname + '\'' +
                ", duration=" + duration +
                ", currentPos=" + currentPos +
                ", posterFid='" + posterFid + '\'' +
                ", videopoint='" + videopoint + '\'' +
                ", storyPlot='" + storyPlot + '\'' +
                ", action='" + action + '\'' +
                ", childType=" + childType +
                ", period='" + period + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordBean that = (RecordBean) o;

        if (vid != that.vid) return false;
        if (epgId != that.epgId) return false;
        if (sid != that.sid) return false;
        if (duration != that.duration) return false;
        if (currentPos != that.currentPos) return false;
        if (childType != that.childType) return false;
        if (videoType != null ? !videoType.equals(that.videoType) : that.videoType != null)
            return false;
        if (vname != null ? !vname.equals(that.vname) : that.vname != null) return false;
        if (sname != null ? !sname.equals(that.sname) : that.sname != null) return false;
        if (posterFid != null ? !posterFid.equals(that.posterFid) : that.posterFid != null)
            return false;
        if (videopoint != null ? !videopoint.equals(that.videopoint) : that.videopoint != null)
            return false;
        if (storyPlot != null ? !storyPlot.equals(that.storyPlot) : that.storyPlot != null)
            return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        return period != null ? period.equals(that.period) : that.period == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (vid ^ (vid >>> 32));
        result = 31 * result + epgId;
        result = 31 * result + (videoType != null ? videoType.hashCode() : 0);
        result = 31 * result + (vname != null ? vname.hashCode() : 0);
        result = 31 * result + (int) (sid ^ (sid >>> 32));
        result = 31 * result + (sname != null ? sname.hashCode() : 0);
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (int) (currentPos ^ (currentPos >>> 32));
        result = 31 * result + (posterFid != null ? posterFid.hashCode() : 0);
        result = 31 * result + (videopoint != null ? videopoint.hashCode() : 0);
        result = 31 * result + (storyPlot != null ? storyPlot.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + childType;
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }
}
