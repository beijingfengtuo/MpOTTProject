package cn.cibnmp.ott.ui.base;

import android.os.Handler;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.cibnmp.ott.bean.HomeMenuBean;
import cn.cibnmp.ott.bean.HomeMenuItemBean;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.utils.Lg;

/**
 * Created by yanjing on 2016/12/15.
 */

public class HomeUpdateFragment extends BaseFragment {
    public List<HomeMenuItemBean> threeList = new ArrayList<>();
    public final int ERROR_MSG = 4004;
    public final int DATE_MSG = 4005;

    // 获取首页Live导航数据
//    public void loadHomeMenu(final boolean useCache, final Handler handler) {
//        HttpRequest.getInstance().excute("getHomeMenuList", new Object[]{App.epgUrl, useCache, new HttpResponseListener() {
//            @Override
//            public void onSuccess(String response) {
//                Lg.i("Live", App.epgUrl + "-->>Live刷新-----------yy>>>" + response);
//                handleLoadHomeMenuSuccessResponse(response, useCache, handler);
//            }
//
//            @Override
//            public void onError(String error) {
//                Lg.i("Live", App.epgUrl + "-->>Live刷新-----------cc>>>" + error);
//                handler.sendEmptyMessage(ERROR_MSG);
//            }
//        }});
//    }

    // 获取首页导航数据接口成功时的回调处理
//    private void handleLoadHomeMenuSuccessResponse(String response, boolean useCache, final Handler handler) {
//        if (response == null) {
//            Lg.i("Live", "-->>Live数据-----------失败>>>0");
//            handler.sendEmptyMessage(ERROR_MSG);
//            return;
//        }
//        HomeMenuBean menuBean = null;
//        try {
//            menuBean = JSON.parseObject(response, HomeMenuBean.class);
//        } catch (Exception e) {
//            Lg.i("Live", "-->>Live数据-----------失败>>>1");
//            handler.sendEmptyMessage(ERROR_MSG);
//            return;
//        }
//        if (menuBean == null) {
//            Lg.i("Live", "-->>Live数据-----------失败>>>2");
//            handler.sendEmptyMessage(ERROR_MSG);
//            return;
//        }
//        List<HomeMenuItemBean> beans = menuBean.getGetMenuList();
//        if (beans == null || beans.isEmpty()) {
//            Lg.i("Live", "-->>Live数据-----------失败>>>3");
//            handler.sendEmptyMessage(ERROR_MSG);
//            return;
//        }
//        threeList.clear();
//        for (int i = 0; i < beans.size(); i++) {
//            if (beans.get(i).getVipType() == 1) {
//                threeList.add(beans.get(i));
//                beans.remove(i);
//                --i;
//                continue;
//            }
//        }
//        if (threeList.size() > 0) {
//            handler.sendEmptyMessage(DATE_MSG);
//        } else {
//            handler.sendEmptyMessage(ERROR_MSG);
//        }
//    }
}
