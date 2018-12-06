package cn.cibnmp.ott.ui.detail.bean;

import java.util.List;

/**
 * Created by wanqi on 2017/3/9.
 */

public class VideoUrlInfoBean {
    private long vid;
    private long sid;
    private String authCode;
    private int priceType;
    private int isLimitTime;
    private long startTime;
    private long endTime;

    private String vname;
    private String videoType;

    private List<VideoUrlBean> media;

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getIsLimitTime() {
        return isLimitTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setIsLimitTime(int isLimitTime) {
        this.isLimitTime = isLimitTime;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSid() {
        return sid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getVid() {
        return vid;
    }

    public String getAuthCode() {
        return authCode;
    }

    public List<VideoUrlBean> getMedia() {
        return media;
    }

    public void setMedia(List<VideoUrlBean> media) {
        this.media = media;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
