package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by geshuaipeng on 2018/2/7.
 */

public class SearchOneUtils {

    /**
     * 跳转
     *
     */
    public static void actionTo(SearchResultEntity itemBean, Context context , int position){
        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(itemBean.getData().getListcontent().getContent().get(position).getAction())) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject("{'p1':'','p2':'','p3':''}");
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = itemBean.getData().getListcontent().getContent().get(position).getAction();
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
                itemBean.getData().getListcontent().getContent().get(position).getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(itemBean.getData().getListcontent().getContent().get(position).getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(itemBean.getData().getListcontent().getContent().get(position).getContentId())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(itemBean.getData().getListcontent().getContent().get(position).getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl));
    }
}
