package cn.cibnmp.ott.ui.detail.detailinterface;

import cn.cibnmp.ott.ui.detail.bean.DetailInfoResultBean;

/**
 * Created by axl on 2018/3/12.
 */

public interface DetailUrlInterFace {

    //详情数据错误了
    void onDetailError();

    void onDetailResult(DetailInfoResultBean resultBean);

    void onDetailProthcen();

}
