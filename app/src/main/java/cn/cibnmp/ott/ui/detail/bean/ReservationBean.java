package cn.cibnmp.ott.ui.detail.bean;

import java.io.Serializable;

/**
 * Created by axl on 2018/3/7.
 */

public class ReservationBean implements Serializable{

    private boolean addReserve = false;

    public ReservationBean(boolean addReserve) {
        this.addReserve = addReserve;
    }

    public boolean getAddReserve() {
        return addReserve;
    }


    private long lid;

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    private long sid;

}
