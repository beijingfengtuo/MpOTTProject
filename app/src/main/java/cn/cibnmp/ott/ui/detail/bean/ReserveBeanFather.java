package cn.cibnmp.ott.ui.detail.bean;

import java.util.List;

/**
 * Created by axl on 2018/1/30.
 */

public class ReserveBeanFather  {

    private long lid;
    private int TotalNum;
    private int StartIndex;
    private int LocalFlag;
    private int CurNum;
    private List<ReserveBean> LiveAppointment;

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }

    public int getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }

    public int getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(int startIndex) {
        StartIndex = startIndex;
    }

    public int getLocalFlag() {
        return LocalFlag;
    }

    public void setLocalFlag(int localFlag) {
        LocalFlag = localFlag;
    }

    public int getCurNum() {
        return CurNum;
    }

    public void setCurNum(int curNum) {
        CurNum = curNum;
    }

    public List<ReserveBean> getLiveAppointment() {
        return LiveAppointment;
    }

    public void setLiveAppointment(List<ReserveBean> liveAppointment) {
        LiveAppointment = liveAppointment;
    }
}
