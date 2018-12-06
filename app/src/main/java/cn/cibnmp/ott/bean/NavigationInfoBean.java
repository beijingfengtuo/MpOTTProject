package cn.cibnmp.ott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationInfoBean implements Serializable {

    private int subjectId;

    private String name;

    private int epgId;

    private String slogan;

    private String description;

    private BlockLayout layout;

    private List<NavigationBlock> blocks;

    private List<NavigationInfoBlockBean> content;

    private String imgUrl;

    private String bgImgUrl;

    private String action;

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

    public NavigationInfoBean() {
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NavigationInfoBean{" +
                "subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", epgId=" + epgId +
                ", slogan='" + slogan + '\'' +
                ", description='" + description + '\'' +
                ", layout=" + layout +
                ", blocks=" + blocks +
                ", content=" + content +
                ", imgUrl='" + imgUrl + '\'' +
                ", bgImgUrl='" + bgImgUrl + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
