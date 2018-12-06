package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationItemBean implements Serializable {

    private int subjectId;

    private String name;

    private String slogan;

    private int subjectType;

    private String action;

    private String actionUrl;

    private String actionParams;

    private String description;

    private int state;

    private String imgUrl;

    private String bgImgUrl;

    private String pysx;

    private String pyqp;

    private int nameType;

    private int priceType;

    private int vipType;

    private int isSearchShow;

    private int isScreenShow;

    private int seq;

    private int epgId;

    private String psubjectId;

    private String pname;

    private int isSearchSubject;

    private String screenKey;

    private int isDefaultShow; //导航专用：0不用默认展示 1用默认展示 ,默认展示哪些导航

    private int isDefaultFocus;  //导航专用：1标示默认获取焦点的导航

    private int isMustShow; //导航专用：0不用强制展示 1用强制展示

    private int isSolidShow; //导航专用：0不用固定展示 1用固定展示

    private int imgDirection;  //栏目专用：0-竖图展示列表页,1-横图展示列表页

    private List<NavContentBean> content;

    private String imageParams;

    public NavigationItemBean() {
    }

    public NavigationItemBean(String name) {
        this.name = name;
    }

    public NavigationItemBean(String name, int isDefaultFocus) {
        this.name = name;
        this.isDefaultFocus = isDefaultFocus;
    }

    public NavigationItemBean(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionParams() {
        return actionParams;
    }

    public void setActionParams(String actionParams) {
        this.actionParams = actionParams;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }

    public String getPysx() {
        return pysx;
    }

    public void setPysx(String pysx) {
        this.pysx = pysx;
    }

    public String getPyqp() {
        return pyqp;
    }

    public void setPyqp(String pyqp) {
        this.pyqp = pyqp;
    }

    public int getNameType() {
        return nameType;
    }

    public void setNameType(int nameType) {
        this.nameType = nameType;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getVipType() {
        return vipType;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public int getIsSearchShow() {
        return isSearchShow;
    }

    public void setIsSearchShow(int isSearchShow) {
        this.isSearchShow = isSearchShow;
    }

    public int getIsScreenShow() {
        return isScreenShow;
    }

    public void setIsScreenShow(int isScreenShow) {
        this.isScreenShow = isScreenShow;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getEpgId() {
        return epgId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public String getPsubjectId() {
        return psubjectId;
    }

    public void setPsubjectId(String psubjectId) {
        this.psubjectId = psubjectId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getIsSearchSubject() {
        return isSearchSubject;
    }

    public void setIsSearchSubject(int isSearchSubject) {
        this.isSearchSubject = isSearchSubject;
    }

    public String getScreenKey() {
        return screenKey;
    }

    public void setScreenKey(String screenKey) {
        this.screenKey = screenKey;
    }

    public int getIsDefaultShow() {
        return isDefaultShow;
    }

    public void setIsDefaultShow(int isDefaultShow) {
        this.isDefaultShow = isDefaultShow;
    }

    public int getIsDefaultFocus() {
        return isDefaultFocus;
    }

    public void setIsDefaultFocus(int isDefaultFocus) {
        this.isDefaultFocus = isDefaultFocus;
    }

    public int getIsMustShow() {
        return isMustShow;
    }

    public void setIsMustShow(int isMustShow) {
        this.isMustShow = isMustShow;
    }

    public int getIsSolidShow() {
        return isSolidShow;
    }

    public void setIsSolidShow(int isSolidShow) {
        this.isSolidShow = isSolidShow;
    }

    public int getImgDirection() {
        return imgDirection;
    }

    public void setImgDirection(int imgDirection) {
        this.imgDirection = imgDirection;
    }

    public List<NavContentBean> getContent() {
        return content;
    }

    public void setContent(List<NavContentBean> content) {
        this.content = content;
    }

    public String getImageParams() {
        return imageParams;
    }

    public void setImageParams(String imageParams) {
        this.imageParams = imageParams;
    }

    @Override
    public String toString() {
        return "NavigationItemBean{" +
                "subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", slogan='" + slogan + '\'' +
                ", subjectType=" + subjectType +
                ", action='" + action + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                ", actionParams='" + actionParams + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", imgUrl='" + imgUrl + '\'' +
                ", bgImgUrl='" + bgImgUrl + '\'' +
                ", pysx='" + pysx + '\'' +
                ", pyqp='" + pyqp + '\'' +
                ", nameType=" + nameType +
                ", priceType=" + priceType +
                ", vipType=" + vipType +
                ", isSearchShow=" + isSearchShow +
                ", isScreenShow=" + isScreenShow +
                ", seq=" + seq +
                ", epgId=" + epgId +
                ", psubjectId='" + psubjectId + '\'' +
                ", pname='" + pname + '\'' +
                ", isSearchSubject=" + isSearchSubject +
                ", screenKey='" + screenKey + '\'' +
                ", isDefaultShow=" + isDefaultShow +
                ", isDefaultFocus=" + isDefaultFocus +
                ", isMustShow=" + isMustShow +
                ", isSolidShow=" + isSolidShow +
                ", imgDirection=" + imgDirection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationItemBean bean = (NavigationItemBean) o;

        if (subjectId != bean.subjectId) return false;
        if (subjectType != bean.subjectType) return false;
        if (state != bean.state) return false;
        if (nameType != bean.nameType) return false;
        if (priceType != bean.priceType) return false;
        if (vipType != bean.vipType) return false;
        if (isSearchShow != bean.isSearchShow) return false;
        if (isScreenShow != bean.isScreenShow) return false;
        if (seq != bean.seq) return false;
        if (epgId != bean.epgId) return false;
        if (isSearchSubject != bean.isSearchSubject) return false;
        if (isDefaultShow != bean.isDefaultShow) return false;
        if (isDefaultFocus != bean.isDefaultFocus) return false;
        if (isMustShow != bean.isMustShow) return false;
        if (isSolidShow != bean.isSolidShow) return false;
        if (imgDirection != bean.imgDirection) return false;
        if (name != null ? !name.equals(bean.name) : bean.name != null) return false;
        if (slogan != null ? !slogan.equals(bean.slogan) : bean.slogan != null) return false;
        if (action != null ? !action.equals(bean.action) : bean.action != null) return false;
        if (actionUrl != null ? !actionUrl.equals(bean.actionUrl) : bean.actionUrl != null)
            return false;
        if (actionParams != null ? !actionParams.equals(bean.actionParams) : bean.actionParams != null)
            return false;
        if (description != null ? !description.equals(bean.description) : bean.description != null)
            return false;
        if (imgUrl != null ? !imgUrl.equals(bean.imgUrl) : bean.imgUrl != null) return false;
        if (bgImgUrl != null ? !bgImgUrl.equals(bean.bgImgUrl) : bean.bgImgUrl != null)
            return false;
        if (pysx != null ? !pysx.equals(bean.pysx) : bean.pysx != null) return false;
        if (pyqp != null ? !pyqp.equals(bean.pyqp) : bean.pyqp != null) return false;
        if (psubjectId != null ? !psubjectId.equals(bean.psubjectId) : bean.psubjectId != null)
            return false;
        if (pname != null ? !pname.equals(bean.pname) : bean.pname != null) return false;
        return screenKey != null ? screenKey.equals(bean.screenKey) : bean.screenKey == null;

    }

    @Override
    public int hashCode() {
        int result = subjectId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (slogan != null ? slogan.hashCode() : 0);
        result = 31 * result + subjectType;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (actionUrl != null ? actionUrl.hashCode() : 0);
        result = 31 * result + (actionParams != null ? actionParams.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (bgImgUrl != null ? bgImgUrl.hashCode() : 0);
        result = 31 * result + (pysx != null ? pysx.hashCode() : 0);
        result = 31 * result + (pyqp != null ? pyqp.hashCode() : 0);
        result = 31 * result + nameType;
        result = 31 * result + priceType;
        result = 31 * result + vipType;
        result = 31 * result + isSearchShow;
        result = 31 * result + isScreenShow;
        result = 31 * result + seq;
        result = 31 * result + epgId;
        result = 31 * result + (psubjectId != null ? psubjectId.hashCode() : 0);
        result = 31 * result + (pname != null ? pname.hashCode() : 0);
        result = 31 * result + isSearchSubject;
        result = 31 * result + (screenKey != null ? screenKey.hashCode() : 0);
        result = 31 * result + isDefaultShow;
        result = 31 * result + isDefaultFocus;
        result = 31 * result + isMustShow;
        result = 31 * result + isSolidShow;
        result = 31 * result + imgDirection;
        return result;
    }
}
