package cn.cibnmp.ott.ui.detail.player.widgets;


import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;

/**
 * Created by sh on 16/12/29.
 */

public class BasePlayerFrag extends BaseFragment {

    protected static final String TYPE = "type";
    protected static final String VID = "vid";

    protected long vid;
    protected int sid = 1; // 当前播放的子集id

    protected boolean isLayPop = false;
    protected boolean isPrepare = false;
    protected boolean need2seek = true; // 第一次开始播放时候需要判断上次播放的位置

    protected boolean wificonn, mobleconn;
    protected boolean isStartPlay = false;

    protected boolean able4G = false;

    public boolean surface_show = false;

    public int screen_o = 0; // 横竖屏状态 0竖屏，1横屏

    protected boolean screen_lock = false;
    public  boolean payBtn=false;


    public void setPayStatus(boolean payStatus,int start_time,int end_time){}

    public void setWaitMsg(int t, String msg, int tag) {
    }

    public void popLay() {
    }

    public void pause() {
    }

    public void btnPause() {
    }

    public void removeUrltv() {
    }

    public void upProgressBar() {
    }

    public void changeScreenOrientation(int o) {
    }

    public void setNetState(boolean wifi, boolean moble) {
    }

    public void setScreenOrientation() {
    }

    public void setDetailData(DetailBean dBean) {
    }

    public void setPlayData(String url, int sid, VideoUrlInfoBean datas, DetailInfoBean detailInfoBean) {
    }

    public void addRecord() {
    }

    public void updateDetail() {
    }

    public void recLogCatch() {
    }

    public void logPost_min() {
    }

    public void seekTo(int l){}

    /**
     * TODO zxy
     * 添加直播节目的直播状态0未开始、1直播中、2已结束、回看
     *
     * @param status
     */
    public void setPlayStatus(String status) {
    }

    public void setPlaySource(String url,long seek){}


    //set
    //播放错误
    public void setErrorLog(String code,String msg){
    }
    //带试看
    public void initFreeTimeLay(boolean b, long startTime, long endTime) {

    }

    public void setPayBtn(boolean payBtn) {
        this.payBtn = payBtn;
    }

    //用于直播试看
    public void initFreeTimeLay(boolean b, long i) {

    }

    //

}
