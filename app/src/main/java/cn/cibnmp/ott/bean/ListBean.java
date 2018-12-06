package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * 所有集合Bean的基类
 *
 * Created by cibn-lyc on 2018/1/16.
 */

public class ListBean implements Serializable {
    private int startindex;
    private int totalnum;
    private int curnum;

    public int getStartindex() {
        return startindex;
    }
    public void setStartindex(int startindex) {
        this.startindex = startindex;
    }
    public int getTotalnum() {
        return totalnum;
    }
    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }
    public int getCurnum() {
        return curnum;
    }
    public void setCurnum(int curnum) {
        this.curnum = curnum;
    }

}
