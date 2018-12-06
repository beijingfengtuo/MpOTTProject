package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by cibn-lyc on 2017/12/22.
 */

public class HomeMenuItemBean implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 5599L;
    public int index;
    private int ID;
    private String Name;
    private String Slogan;
    private String description;
    private String PicFID;
    private long LayoutID;
    private int VipType;
    private int sqe;
    private int topicType;
    private String Action;
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getSlogan() {
        return Slogan;
    }
    public void setSlogan(String slogan) {
        Slogan = slogan;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPicFID() {
        return PicFID;
    }
    public void setPicFID(String picFID) {
        PicFID = picFID;
    }
    public long getLayoutID() {
        return LayoutID;
    }
    public void setLayoutID(long layoutID) {
        LayoutID = layoutID;
    }
    public int getVipType() {
        return VipType;
    }
    public void setVipType(int vipType) {
        VipType = vipType;
    }
    public int getSqe() {
        return sqe;
    }
    public void setSqe(int sqe) {
        this.sqe = sqe;
    }
    public int getTopicType() {
        return topicType;
    }
    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }
    public String getAction() {
        return Action;
    }
    public void setAction(String action) {
        Action = action;
    }
    @Override
    public String toString() {
        return "HomeMenuItemBean [index=" + index + ", ID=" + ID + ", Name=" + Name + ", Slogan=" + Slogan + ", description=" + description + ", PicFID=" + PicFID + ", LayoutID=" + LayoutID + ", VipType=" + VipType + ", sqe=" + sqe + ", topicType="
                + topicType + "]";
    }
}
