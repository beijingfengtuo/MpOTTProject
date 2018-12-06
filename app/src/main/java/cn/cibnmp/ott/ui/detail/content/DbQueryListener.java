package cn.cibnmp.ott.ui.detail.content;

import java.util.List;

import cn.cibnmp.ott.ui.detail.bean.ReserveBean;

/**
 * Created by axl on 2018/1/30.
 */

public interface DbQueryListener {
    public void query(List<ReserveBean> list);
}
