package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by yangwenwu on 18/2/28.
 */

public class ActionParams implements Serializable{

    private int p1;
    private int p2;
    private int p3;

    public ActionParams() {
    }

    public ActionParams(int p1, int p2, int p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }


    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getP3() {
        return p3;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }
}
