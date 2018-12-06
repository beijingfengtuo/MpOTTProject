package cn.cibnmp.ott.ui.detail.bean;

/**
 * Created by axl on 2018/1/30.
 */

public class AddDetailReserveBeanEvent {
    private ReserveBean bean = null;

    public AddDetailReserveBeanEvent(ReserveBean bean) {
        this.bean = bean;
    }

    public ReserveBean getReserveBean() {
        return bean;
    }
}
