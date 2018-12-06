package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/8/14.
 */

public class UserMsg implements Serializable {

    public static final int common_msg = 1;  //普通消息 ,content是消息体
    public static final int text_img_msg = 2;  //图文消息,content是下载地址

    public static final int common_showType = 1;  //普通展现类型
    public static final int toast_showType = 2;   //弹窗展现类型
    public static final int marquee_showType = 3;  //跑马灯展现类型


    public static final int speed_slow = 1;
    public static final int speed_normal = 2;
    public static final int speed_fast = 3;


    private String title; // 消息标题
    private int msgtype;
    private int showtype;
    private int times;    //显示次数
    private int interval; //每次显示间隔，分钟
    private int speed;
    private String actkey;
    private String actval;
    private String abstracts;
    private String content;
    private String thumnail;
    private long starttime;  //跑马灯开始时间,后台传值精确到秒
    private long endtime;    //跑马灯结束时间,后台传值精确到秒


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getActkey() {
        return actkey;
    }

    public void setActkey(String actkey) {
        this.actkey = actkey;
    }

    public String getActval() {
        return actval;
    }

    public void setActval(String actval) {
        this.actval = actval;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }
}
