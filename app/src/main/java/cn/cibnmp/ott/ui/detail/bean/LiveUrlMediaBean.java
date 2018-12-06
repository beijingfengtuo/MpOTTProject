package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by yanjing on 2017/3/30.
 */

public class LiveUrlMediaBean implements Serializable {
    private String fileName;
    private String fid;
    //播放地址
    private String url;
    private String definition;
    private int mediaType;
    private int contentType;
    private int level;
    //介绍
    private String source;
    private String img;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "LiveUrlMediaBean{" +
                "fileName='" + fileName + '\'' +
                ", fid='" + fid + '\'' +
                ", url='" + url + '\'' +
                ", definition='" + definition + '\'' +
                ", mediaType=" + mediaType +
                ", contentType=" + contentType +
                ", level=" + level +
                ", source='" + source + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
