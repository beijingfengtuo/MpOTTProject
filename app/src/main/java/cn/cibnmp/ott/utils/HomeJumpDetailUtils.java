package cn.cibnmp.ott.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.ScreeningBean;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;

/**
 * Created by yangwenwu on 18/2/1.
 * 首页以及玩票页跳转详情页
 */

public class HomeJumpDetailUtils {
    /**
     * 跳转
     */
    public static void actionTo(NavigationInfoItemBean itemBean, Context context) {

        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(itemBean.getActionParams())) {
            try {
                JSONObject jsonObject = new JSONObject(itemBean.getActionParams());
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = itemBean.getActionUrl();
            //actionUrl为http://时，参数传递失败
            if (!TextUtils.isEmpty(ationUrl))
                ationUrl = URLEncoder.encode(ationUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p1为http://时，参数传递失败
            if (!TextUtils.isEmpty(p1))
                p1 = URLEncoder.encode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p2为http://时，参数传递失败
            if (!TextUtils.isEmpty(p2))
                p2 = URLEncoder.encode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p3为http://时，参数传递失败
            if (!TextUtils.isEmpty(p3))
                p3 = URLEncoder.encode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((BaseActivity) context).startActivity(
                itemBean.getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(itemBean.getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(itemBean.getContentId())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(itemBean.getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl),
                Utils.getUrlParameter(Constant.sidKey, String.valueOf(itemBean.getSid())));
    }


    /**
     * 筛选页面跳转详情页面参数
     *
     * @param itemBean
     * @param context
     */
    public static void actionToDetailByScreening(ScreeningDataBean.DataBean.ListcontentBean.ContentBean itemBean, Context context) {

        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(itemBean.getActionParams())) {
            try {
                JSONObject jsonObject = new JSONObject(itemBean.getActionParams());
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = itemBean.getActionUrl();
            //actionUrl为http://时，参数传递失败
            if (!TextUtils.isEmpty(ationUrl))
                ationUrl = URLEncoder.encode(ationUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p1为http://时，参数传递失败
            if (!TextUtils.isEmpty(p1))
                p1 = URLEncoder.encode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p2为http://时，参数传递失败
            if (!TextUtils.isEmpty(p2))
                p2 = URLEncoder.encode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p3为http://时，参数传递失败
            if (!TextUtils.isEmpty(p3))
                p3 = URLEncoder.encode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((BaseActivity) context).startActivity(
                itemBean.getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(itemBean.getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(itemBean.getContentId())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(itemBean.getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl),
                Utils.getUrlParameter(Constant.sidKey, String.valueOf(itemBean.getSid())));
    }

    /**
     *  观看记录、收藏跳转详情页
     *
     * @param recordBean
     * @param context
     */
    public static void actionToDetailByPlayHistory(RecordBean recordBean, Context context){
        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(recordBean.getAction())) {
            try {
                JSONObject jsonObject = new JSONObject(recordBean.getAction());
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = recordBean.getAction();
            //actionUrl为http://时，参数传递失败
            if (!TextUtils.isEmpty(ationUrl))
                ationUrl = URLEncoder.encode(ationUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p1为http://时，参数传递失败
            if (!TextUtils.isEmpty(p1))
                p1 = URLEncoder.encode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p2为http://时，参数传递失败
            if (!TextUtils.isEmpty(p2))
                p2 = URLEncoder.encode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p3为http://时，参数传递失败
            if (!TextUtils.isEmpty(p3))
                p3 = URLEncoder.encode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((BaseActivity) context).startActivity(
                recordBean.getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(recordBean.getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(recordBean.getVid())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(recordBean.getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl),
                Utils.getUrlParameter(Constant.sidKey, String.valueOf(recordBean.getSid())));
    }

    /**
     *  观看记录、收藏跳转详情页
     *
     * @param reserveBean
     * @param context
     */
    public static void actionToDetailByPlayHistory(ReserveBean reserveBean, Context context){
        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(reserveBean.getAction())) {
            try {
                JSONObject jsonObject = new JSONObject(reserveBean.getAction());
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = reserveBean.getAction();
            //actionUrl为http://时，参数传递失败
            if (!TextUtils.isEmpty(ationUrl))
                ationUrl = URLEncoder.encode(ationUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p1为http://时，参数传递失败
            if (!TextUtils.isEmpty(p1))
                p1 = URLEncoder.encode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p2为http://时，参数传递失败
            if (!TextUtils.isEmpty(p2))
                p2 = URLEncoder.encode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p3为http://时，参数传递失败
            if (!TextUtils.isEmpty(p3))
                p3 = URLEncoder.encode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((BaseActivity) context).startActivity(
                reserveBean.getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(reserveBean.getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(reserveBean.getLid())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(reserveBean.getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl),
                Utils.getUrlParameter(Constant.sidKey, String.valueOf(reserveBean.getSid())));
    }
}
