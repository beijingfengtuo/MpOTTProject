package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationInfoBlockBean implements Serializable {

    public static final int SHOWNAV = 0; //1隐藏0显示

    private int blockId;

    private String name;

    private int nameType;   /**是否显示名称 0显示名称 1不显示名称*/

    private int position;

    private List<NavigationInfoItemBean> indexContents;

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNameType(int nameType) {
        this.nameType = nameType;
    }

    public int getNameType() {
        return this.nameType;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setIndexContents(List<NavigationInfoItemBean> indexContents) {
        this.indexContents = indexContents;
    }

    public List<NavigationInfoItemBean> getIndexContents() {
        return this.indexContents;
    }

    public NavigationInfoBlockBean() {
    }

    @Override
    public String toString() {
        return "NavigationInfoBlockBean{" +
                "blockId=" + blockId +
                ", name='" + name + '\'' +
                ", nameType=" + nameType +
                ", position=" + position +
                ", indexContents=" + indexContents +
                '}';
    }
}
