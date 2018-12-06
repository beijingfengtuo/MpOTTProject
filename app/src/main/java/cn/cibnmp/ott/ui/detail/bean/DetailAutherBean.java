package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by sh on 17/4/5.
 */

public class DetailAutherBean {
    private long seriesId;
    private int seriesType;

    public int getSeriesType() {
        return seriesType;
    }

    public long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(long seriesId) {
        this.seriesId = seriesId;
    }

    public void setSeriesType(int seriesType) {
        this.seriesType = seriesType;
    }
}
