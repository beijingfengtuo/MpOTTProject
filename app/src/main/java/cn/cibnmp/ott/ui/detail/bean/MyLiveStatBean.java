package cn.cibnmp.ott.ui.detail.bean;

import java.util.List;

/**
 * Created by yanjing on 2017/3/18.
 */

public class MyLiveStatBean {

    private int StartIndex;
    private int LocalFlag;
    private int CurNum;
    private List<cn.cibnmp.ott.ui.detail.bean.LiveAppointment> LiveAppointment;

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

    public List<cn.cibnmp.ott.ui.detail.bean.LiveAppointment> getLiveAppointment() {
        return LiveAppointment;
    }

    public void setLiveAppointment(List<cn.cibnmp.ott.ui.detail.bean.LiveAppointment> liveAppointment) {
        LiveAppointment = liveAppointment;
    }

    @Override
    public String toString() {
        return "MyLiveStatBean{" +
                "StartIndex=" + StartIndex +
                ", LocalFlag=" + LocalFlag +
                ", CurNum=" + CurNum +
                ", LiveAppointment=" + LiveAppointment +
                '}';
    }
}
