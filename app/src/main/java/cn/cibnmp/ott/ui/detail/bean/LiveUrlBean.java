package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanjing on 2017/3/30.
 */

public class LiveUrlBean implements Serializable {
    private int liveId;
    private int childId;
    private int liveStatus;
    private String authCode;
    private int priceType;
    private List<LiveUrlMediaBean> media;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public List<LiveUrlMediaBean> getMedia() {
        return media;
    }

    public void setMedia(List<LiveUrlMediaBean> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "LiveUrlBean{" +
                "liveId=" + liveId +
                ", childId=" + childId +
                ", liveStatus=" + liveStatus +
                ", authCode='" + authCode + '\'' +
                ", priceType=" + priceType +
                ", media=" + media +
                '}';
    }
}
