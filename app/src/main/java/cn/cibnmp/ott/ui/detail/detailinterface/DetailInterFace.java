package cn.cibnmp.ott.ui.detail.detailinterface;

import java.util.List;

import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoResultBean;

/**
 * Created by axl on 2018/3/12.
 */

public interface DetailInterFace {

    //详情数据错误了
    void onDetailError();

    void onDetailResult(DetailInfoResultBean resultBean);

    void onMergeDataLayout(List<LayoutItem> newLaytItemList, List<NavigationInfoItemBean> newInfoItemBeanList);

}
