package cn.cibnmp.ott.bean;

import java.io.Serializable;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.utils.DeviceUtils;
import cn.cibnmp.ott.utils.Util;


public class DeviceAuthenBean implements Serializable {

    private static final long serialVersionUID = -5172227272571745659L;

    private int screensize;  //屏幕dpi
    private int Screenwidth;  //屏幕宽度
    private int screenheight; //屏幕高度
    private float screendensity;   //像素密度
    private long ramSize;  //可用的运行时内存大小
    private long totalRamSize;  //全部的运行时内存大小
    private long romSize;   //可用的内存存储大小
    private long totalRomSize;  //全部的内存存储大小
    private long sdSize;   //可用的sd卡大小
    private long totalSdSize;   //全部的sd卡大小
    private String lanMac;  //有线mac地址
    private String wlanMac;  //无线mac地址

    public DeviceAuthenBean() {
        setScreensize(App.ScreenDpi);
        setScreendensity(App.ScreenDesity);
        setScreenwidth(App.ScreenWidth);
        setScreenheight(App.ScreenHeight);
        setRamSize(Util.getAvailMemory(App.getInstance()));
        setTotalRamSize(Util.getTotalMemory(App.getInstance()));
        setRomSize(Util.getRomavAilableSize());
        setTotalRomSize(Util.getRomTotalSize());
        setSdSize(Util.getAvailableSDCardSize());
        setTotalSdSize(Util.getSDCardSize());
        setLanMac(DeviceUtils.getLanMac());
        setWlanMac(DeviceUtils.getWlanMac(App.getInstance()));
    }


    public float getScreendensity() {
        return screendensity;
    }

    public void setScreendensity(float screendensity) {
        this.screendensity = screendensity;
    }

    public int getScreensize() {
        return screensize;
    }

    public void setScreensize(int screensize) {
        this.screensize = screensize;
    }

    public int getScreenwidth() {
        return Screenwidth;
    }

    public void setScreenwidth(int screenwidth) {
        Screenwidth = screenwidth;
    }

    public int getScreenheight() {
        return screenheight;
    }

    public void setScreenheight(int screenheight) {
        this.screenheight = screenheight;
    }

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public long getTotalRamSize() {
        return totalRamSize;
    }

    public void setTotalRamSize(long totalRamSize) {
        this.totalRamSize = totalRamSize;
    }

    public long getRomSize() {
        return romSize;
    }

    public void setRomSize(long romSize) {
        this.romSize = romSize;
    }

    public long getTotalRomSize() {
        return totalRomSize;
    }

    public void setTotalRomSize(long totalRomSize) {
        this.totalRomSize = totalRomSize;
    }

    public long getSdSize() {
        return sdSize;
    }

    public void setSdSize(long sdSize) {
        this.sdSize = sdSize;
    }

    public long getTotalSdSize() {
        return totalSdSize;
    }

    public void setTotalSdSize(long totalSdSize) {
        this.totalSdSize = totalSdSize;
    }

    public String getLanMac() {
        return lanMac;
    }

    public void setLanMac(String lanMac) {
        this.lanMac = lanMac;
    }

    public String getWlanMac() {
        return wlanMac;
    }

    public void setWlanMac(String wlanMac) {
        this.wlanMac = wlanMac;
    }
}
