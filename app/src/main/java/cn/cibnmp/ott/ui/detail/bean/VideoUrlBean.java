package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by sh on 17/3/31.
 */

public class VideoUrlBean {

    private String duration;
    private String fileName;
    private String bitrate;
    private String source;
    private String fid;
    private String url;
    private String code;
    private int srcfrom;
    private String playinfo;
    private int playseq;
    private int playertype;
    private String videoratio;
    private String videorate;

    public int getPlayertype() {
        return playertype;
    }

    public int getPlayseq() {
        return playseq;
    }

    public int getSrcfrom() {
        return srcfrom;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getCode() {
        return code;
    }

    public String getDuration() {
        return duration;
    }

    public String getFid() {
        return fid;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPlayinfo() {
        return playinfo;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getVideorate() {
        return videorate;
    }

    public String getVideoratio() {
        return videoratio;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPlayertype(int playertype) {
        this.playertype = playertype;
    }

    public void setPlayinfo(String playinfo) {
        this.playinfo = playinfo;
    }

    public void setPlayseq(int playseq) {
        this.playseq = playseq;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSrcfrom(int srcfrom) {
        this.srcfrom = srcfrom;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVideorate(String videorate) {
        this.videorate = videorate;
    }

    public void setVideoratio(String videoratio) {
        this.videoratio = videoratio;
    }
}
