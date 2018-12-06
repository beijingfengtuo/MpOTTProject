package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationInfoItemBean implements Serializable {

    private int id;

    private int blockId;

    private int position;

    private int viewtype;

    private String displayName;

    private String name;

    private String slogan;

    private String scrollMarquee;

    private String playTime;

    private String descInfo;

    private String img;

    private String imgh;

    private String imgBack;

    private String action;

    private String actionUrl;

    private String actionParams;

    private String contentType;

    private String contentId;

    private int epgId;

    private int state;

    private int vipType;

    private String topLeftCorner;

    private String bottomLeftCorner;

    private String topRightCorner;

    private String bottomRightCorner;

    private String navName;  //首页推荐位所在的导航名称,首页专用，不从后台接口获取值

    private int navPos;  //首页推荐位的位置,首页专用，不从后台接口获取值

    private List<NavigationInfoItemBean> contents;

    private List<NavigationInfoItemBean> tagContents;

    public List<NavigationInfoItemBean> getTagContents() { //特定4个标签数据
        return tagContents;
    }

    public void setTagContents(List<NavigationInfoItemBean> tagContents) {
        this.tagContents = tagContents;
    }

    private long sid;

    public NavigationInfoItemBean(String name, String img, String action) {
        setName(name);
        setImg(img);
        setAction(action);
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSlogan() {
        return this.slogan;
    }

    public void setScrollMarquee(String scrollMarquee) {
        this.scrollMarquee = scrollMarquee;
    }

    public String getScrollMarquee() {
        return this.scrollMarquee;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getPlayTime() {
        return this.playTime;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
    }

    public String getDescInfo() {
        return this.descInfo;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return this.img;
    }

    public void setImgh(String imgh) {
        this.imgh = imgh;
    }

    public String getImgh() {
        return this.imgh;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public void setActionParams(String actionParams) {
        this.actionParams = actionParams;
    }

    public String getActionParams() {
        return this.actionParams;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public int getEpgId() {
        return this.epgId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public int getVipType() {
        return this.vipType;
    }

    public void setTopLeftCorner(String topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
    }

    public String getTopLeftCorner() {
        return this.topLeftCorner;
    }

    public void setBottomLeftCorner(String bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public String getBottomLeftCorner() {
        return this.bottomLeftCorner;
    }

    public void setTopRightCorner(String topRightCorner) {
        this.topRightCorner = topRightCorner;
    }

    public String getTopRightCorner() {
        return this.topRightCorner;
    }

    public void setBottomRightCorner(String bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }

    public String getBottomRightCorner() {
        return this.bottomRightCorner;
    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }

    public String getImgBack() {
        return imgBack;
    }

    public void setImgBack(String imgBack) {
        this.imgBack = imgBack;
    }

    public NavigationInfoItemBean() {
    }

    public NavigationInfoItemBean(int viewtype) {
        this.viewtype = viewtype;
    }

    public List<NavigationInfoItemBean> getContents() {
        return contents;
    }

    public void setContents(List<NavigationInfoItemBean> contents) {
        this.contents = contents;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }

    public int getNavPos() {
        return navPos;
    }

    public void setNavPos(int navPos) {
        this.navPos = navPos;
    }

    @Override
    public String toString() {
        return "NavigationInfoItemBean{" +
                "id=" + id +
                ", blockId=" + blockId +
                ", position=" + position +
                ", viewtype=" + viewtype +
                ", displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                ", slogan='" + slogan + '\'' +
                ", scrollMarquee='" + scrollMarquee + '\'' +
                ", playTime='" + playTime + '\'' +
                ", descInfo='" + descInfo + '\'' +
                ", img='" + img + '\'' +
                ", imgh='" + imgh + '\'' +
                ", imgBack='" + imgBack + '\'' +
                ", action='" + action + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                ", actionParams='" + actionParams + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentId='" + contentId + '\'' +
                ", epgId=" + epgId +
                ", state=" + state +
                ", vipType=" + vipType +
                ", topLeftCorner='" + topLeftCorner + '\'' +
                ", bottomLeftCorner='" + bottomLeftCorner + '\'' +
                ", topRightCorner='" + topRightCorner + '\'' +
                ", bottomRightCorner='" + bottomRightCorner + '\'' +
                ", contents=" + contents +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationInfoItemBean that = (NavigationInfoItemBean) o;

        if (id != that.id) return false;
        if (blockId != that.blockId) return false;
        if (position != that.position) return false;
        if (viewtype != that.viewtype) return false;
        if (epgId != that.epgId) return false;
        if (state != that.state) return false;
        if (vipType != that.vipType) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (slogan != null ? !slogan.equals(that.slogan) : that.slogan != null) return false;
        if (scrollMarquee != null ? !scrollMarquee.equals(that.scrollMarquee) : that.scrollMarquee != null)
            return false;
        if (playTime != null ? !playTime.equals(that.playTime) : that.playTime != null)
            return false;
        if (descInfo != null ? !descInfo.equals(that.descInfo) : that.descInfo != null)
            return false;
        if (img != null ? !img.equals(that.img) : that.img != null) return false;
        if (imgh != null ? !imgh.equals(that.imgh) : that.imgh != null) return false;
        if (imgBack != null ? !imgBack.equals(that.imgBack) : that.imgBack != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (actionUrl != null ? !actionUrl.equals(that.actionUrl) : that.actionUrl != null)
            return false;
        if (actionParams != null ? !actionParams.equals(that.actionParams) : that.actionParams != null)
            return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (contentId != null ? !contentId.equals(that.contentId) : that.contentId != null)
            return false;
        if (topLeftCorner != null ? !topLeftCorner.equals(that.topLeftCorner) : that.topLeftCorner != null)
            return false;
        if (bottomLeftCorner != null ? !bottomLeftCorner.equals(that.bottomLeftCorner) : that.bottomLeftCorner != null)
            return false;
        if (topRightCorner != null ? !topRightCorner.equals(that.topRightCorner) : that.topRightCorner != null)
            return false;
        if (bottomRightCorner != null ? !bottomRightCorner.equals(that.bottomRightCorner) : that.bottomRightCorner != null)
            return false;
        return contents != null ? contents.equals(that.contents) : that.contents == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + blockId;
        result = 31 * result + position;
        result = 31 * result + viewtype;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (slogan != null ? slogan.hashCode() : 0);
        result = 31 * result + (scrollMarquee != null ? scrollMarquee.hashCode() : 0);
        result = 31 * result + (playTime != null ? playTime.hashCode() : 0);
        result = 31 * result + (descInfo != null ? descInfo.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (imgh != null ? imgh.hashCode() : 0);
        result = 31 * result + (imgBack != null ? imgBack.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (actionUrl != null ? actionUrl.hashCode() : 0);
        result = 31 * result + (actionParams != null ? actionParams.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (contentId != null ? contentId.hashCode() : 0);
        result = 31 * result + epgId;
        result = 31 * result + state;
        result = 31 * result + vipType;
        result = 31 * result + (topLeftCorner != null ? topLeftCorner.hashCode() : 0);
        result = 31 * result + (bottomLeftCorner != null ? bottomLeftCorner.hashCode() : 0);
        result = 31 * result + (topRightCorner != null ? topRightCorner.hashCode() : 0);
        result = 31 * result + (bottomRightCorner != null ? bottomRightCorner.hashCode() : 0);
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        return result;
    }
}
