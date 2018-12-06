package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by axl on 2018/1/30.
 */

public class AddUserReserveEvent {
    private boolean addReserve = false;

    public AddUserReserveEvent(boolean addReserve) {
        this.addReserve = addReserve;
    }

    public boolean getAddReserve() {
        return addReserve;
    }
}
