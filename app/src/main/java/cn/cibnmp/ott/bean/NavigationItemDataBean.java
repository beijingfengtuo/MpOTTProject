package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/3/18.
 */

public class NavigationItemDataBean implements Serializable{


    private int epgId;
    private String name;
    private String logoimg;
    private String backimg;
    private int isTitileHidden;  //是否隐藏导航：0不隐藏1隐藏
    private int isTitileUserDefined;  //是否自定义导航：0不允许1允许

    private List<NavigationItemBean> content;

    public NavigationItemDataBean() {
    }


    public int getEpgId() {
        return epgId;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoimg() {
        return logoimg;
    }

    public void setLogoimg(String logoimg) {
        this.logoimg = logoimg;
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public int getIsTitileHidden() {
        return isTitileHidden;
    }

    public void setIsTitileHidden(int isTitileHidden) {
        this.isTitileHidden = isTitileHidden;
    }

    public int getIsTitileUserDefined() {
        return isTitileUserDefined;
    }

    public void setIsTitileUserDefined(int isTitileUserDefined) {
        this.isTitileUserDefined = isTitileUserDefined;
    }

    public List<NavigationItemBean> getContent() {
        return content;
    }

    public void setContent(List<NavigationItemBean> content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "NavigationItemDataBean{" +
                "epgId=" + epgId +
                ", name='" + name + '\'' +
                ", logoimg='" + logoimg + '\'' +
                ", backimg='" + backimg + '\'' +
                ", isTitileHidden=" + isTitileHidden +
                ", isTitileUserDefined=" + isTitileUserDefined +
                ", contents=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationItemDataBean that = (NavigationItemDataBean) o;

        if (epgId != that.epgId) return false;
        if (isTitileHidden != that.isTitileHidden) return false;
        if (isTitileUserDefined != that.isTitileUserDefined) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (logoimg != null ? !logoimg.equals(that.logoimg) : that.logoimg != null) return false;
        if (backimg != null ? !backimg.equals(that.backimg) : that.backimg != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;

    }

    @Override
    public int hashCode() {
        int result = epgId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (logoimg != null ? logoimg.hashCode() : 0);
        result = 31 * result + (backimg != null ? backimg.hashCode() : 0);
        result = 31 * result + isTitileHidden;
        result = 31 * result + isTitileUserDefined;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
