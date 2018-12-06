package cn.cibnmp.ott.bean;

/**
 * Created by wanqi on 2017/3/13.
 */

public class LayoutItem {

    private double c;

    private double r;

    public LayoutItem(double c, double r) {
        this.c = c;
        this.r = r;
    }

    public LayoutItem() {
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "LayoutItem{" +
                "c=" + c +
                ", r=" + r +
                '}';
    }
}
