package cn.cibnmp.ott.ui.detail.bean;

import java.util.List;

/**
 * Created by sh on 17/3/30.
 */

public class DetailChildBean {
    private long sid;
    private String sname;
    private int priceType;
    private int isLimitTime;
    private long startTime;
    private long endTime;
    private String period;

    private String storyPlot;
    private String img;
    private long startDate;//直播开始时间
    private long endDate;//直播结束时间
    //直播状态 0未开始 ， 1已开始 ， 2已结束, 3 回看
    private int status;
    private List<LiveMedia> media;

    //-------------->>资讯
    private String contentId;
    private String displayName;
    private String year;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<LiveMedia> getMedia() {
        return media;
    }

    public void setMedia(List<LiveMedia> media) {
        this.media = media;
    }

    public int getIsLimitTime() {
        return isLimitTime;
    }

    public int getPriceType() {
        return priceType;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getSid() {
        return sid;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getSname() {
        return sname;
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

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getStartDate() {
        return startDate;
    }

    public String getImg() {
        return img;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
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

    @Override
    public String toString() {
        return "DetailChildBean{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", priceType=" + priceType +
                ", isLimitTime=" + isLimitTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", storyPlot='" + storyPlot + '\'' +
                ", img='" + img + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", media='" + media + '\'' +
                '}';
    }
}
