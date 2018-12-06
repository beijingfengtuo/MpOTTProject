package cn.cibnmp.ott.bean;

/**
 * Created by cibn-lyc on 2018/1/18.
 */

public class ContentBean {
    private String displayName;
    private String slogan;
    private Object scrollMarquee;
    private String playTime;
    private String img;
    private String imgh;
    private Object viewtypecode;
    private String action;
    private Object actionUrl;
    private String actionParams;
    private String contentType;
    private String contentId;
    private int epgId;
    private int vipType;
    private String topLeftCorner;
    private String bottomLeftCorner;
    private String topRightCorner;
    private String bottomRightCorner;
    private String year;
    private String score;
    private Object content;
    private int packageType;//0是未购买 1是标题 2是空 3已购买

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Object getScrollMarquee() {
        return scrollMarquee;
    }

    public void setScrollMarquee(Object scrollMarquee) {
        this.scrollMarquee = scrollMarquee;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgh() {
        return imgh;
    }

    public void setImgh(String imgh) {
        this.imgh = imgh;
    }

    public Object getViewtypecode() {
        return viewtypecode;
    }

    public void setViewtypecode(Object viewtypecode) {
        this.viewtypecode = viewtypecode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(Object actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionParams() {
        return actionParams;
    }

    public void setActionParams(String actionParams) {
        this.actionParams = actionParams;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getEpgId() {
        return epgId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public int getVipType() {
        return vipType;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public String getTopLeftCorner() {
        return topLeftCorner;
    }

    public void setTopLeftCorner(String topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
    }

    public String getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public void setBottomLeftCorner(String bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public String getTopRightCorner() {
        return topRightCorner;
    }

    public void setTopRightCorner(String topRightCorner) {
        this.topRightCorner = topRightCorner;
    }

    public String getBottomRightCorner() {
        return bottomRightCorner;
    }

    public void setBottomRightCorner(String bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "displayName='" + displayName + '\'' +
                ", slogan='" + slogan + '\'' +
                ", scrollMarquee=" + scrollMarquee +
                ", playTime='" + playTime + '\'' +
                ", img='" + img + '\'' +
                ", imgh='" + imgh + '\'' +
                ", viewtypecode=" + viewtypecode +
                ", action='" + action + '\'' +
                ", actionUrl=" + actionUrl +
                ", actionParams='" + actionParams + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentId='" + contentId + '\'' +
                ", epgId=" + epgId +
                ", vipType=" + vipType +
                ", topLeftCorner='" + topLeftCorner + '\'' +
                ", bottomLeftCorner='" + bottomLeftCorner + '\'' +
                ", topRightCorner='" + topRightCorner + '\'' +
                ", bottomRightCorner='" + bottomRightCorner + '\'' +
                ", year='" + year + '\'' +
                ", score='" + score + '\'' +
                ", content=" + content +
                ", packageType=" + packageType +
                '}';
    }
}
