package cn.cibnmp.ott.bean;

/**
 * Created by wanqi on 2016/8/22.
 */
public class MarqueeMsgEvent {

    private boolean hasNewMarqueeMsg = false;
    private String msg;

    public MarqueeMsgEvent(boolean hasNewMarqueeMsg) {
        this.hasNewMarqueeMsg = hasNewMarqueeMsg;
    }

    public MarqueeMsgEvent(boolean hasNewMarqueeMsg, String msg) {
        this.hasNewMarqueeMsg = hasNewMarqueeMsg;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isHasNewMarqueeMsg(){
        return hasNewMarqueeMsg;
    }
}
