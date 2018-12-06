package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;
import java.util.List;

import cn.cibnmp.ott.bean.BlockLayout;
import cn.cibnmp.ott.bean.NavigationBlock;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;

/**
 * Created by wanqi on 2017/3/9.
 */

public class DetailInfoBean implements Serializable {
    //直播的
    private long liveId;
    private String liveName;
    private DetailBean epglive; //直播详情页媒资内容

    //点播的
    private int subjectId;

    private String name;

    private int epgId;

    private int childType; //详情页子集类型

    private DetailBean epgvideo; //详情页媒资内容

    private List<DetailChildBean> child; // 子集

    private BlockLayout layout;

    private List<NavigationBlock> blocks;

    private List<NavigationInfoBlockBean> content;

    private String imgUrl;

    private String bgImgUrl;

    private String action;

    private String videoType;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSubjectId() {
        return this.subjectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEpgId(int epgId) {
        this.epgId = epgId;
    }

    public int getEpgId() {
        return this.epgId;
    }

    public void setLayout(BlockLayout layout) {
        this.layout = layout;
    }

    public BlockLayout getLayout() {
        return this.layout;
    }

    public void setBlocks(List<NavigationBlock> blocks) {
        this.blocks = blocks;
    }

    public List<NavigationBlock> getBlocks() {
        return this.blocks;
    }

    public void setContent(List<NavigationInfoBlockBean> conntent) {
        this.content = conntent;
    }

    public List<NavigationInfoBlockBean> getContent() {
        return this.content;
    }

    public DetailBean getEpgvideo() {
        return epgvideo;
    }

    public void setEpgvideo(DetailBean epgvideo) {
        this.epgvideo = epgvideo;
    }

    public int getChildType() {
        return childType;
    }

    public void setChildType(int childType) {
        this.childType = childType;
    }

    public List<DetailChildBean> getChild() {
        return child;
    }

    public void setChild(List<DetailChildBean> child) {
        this.child = child;
    }

    public DetailInfoBean() {
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public DetailBean getEpglive() {
        return epglive;
    }

    public void setEpglive(DetailBean epglive) {
        this.epglive = epglive;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    @Override
    public String toString() {
        return "DetailInfoBean{" +
                "liveId=" + liveId +
                ", liveName='" + liveName + '\'' +
                ", epglive=" + epglive +
                ", subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", epgId=" + epgId +
                ", childType=" + childType +
                ", epgvideo=" + epgvideo +
                ", layout=" + layout +
                ", blocks=" + blocks +
                ", conntent=" + content +
                ", imgUrl='" + imgUrl + '\'' +
                ", bgImgUrl='" + bgImgUrl + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
