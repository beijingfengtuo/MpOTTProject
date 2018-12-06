package cn.cibnmp.ott.ui.base;

import android.text.TextUtils;

import cn.cibnmp.ott.utils.Lg;

public enum Action {
    OPEN_STTING, // 打开设置页面action
    OPEN_COLLECTION, // 打开我的收藏action
    OPEN_VIP, // 打开开通会员action
    OPEN_FEEDBACK, // 打开开通会员action
    OPEN_SEARCH, // 打开搜索界面action
    OPEN_USER_VOUCHERS, // 打开代金券页面的action
    OPEN_USER_SIGN_IN, // 打开登录页面的action
    OPEN_BLE_GUIDE, // 打开的action
    USER_REGISTER, // 打开用户注册action
    USER_MODIFY, // 打开修改密码注册action
    USER_AGREEMENT, // 打开用户协议
    OPEN_MOBILE_MORE_VERTICAL,//电视剧和电影类的action
    OPEN_MOBILE_MORE_HORIZONTAL,//综艺类的action

    OPEN_DETAIL, //详情页
    OPEN_PERSON_DETAIL_PAGE, //人物详情页
    OPEN_NORMAL_DETAIL_PAGE, // 打开通用详情页
    OPEN_MOVIE_DETAIL_PAGE, // 打开电影详情页
    OPEN_TV_DETAIL_PAGE, // 打开电视剧详情页
    OPEN_VARIETY_DETAIL_PAGE, // 打开综艺详情页

    OPEN_NORMAL_CAROUSEL_PLAYER, // 打开轮播播放器
    OPEN_LIVE_DETAIL_PAGE,// 打开直播播放器

    OPEN_MY_BEADS,// 打开我的佛珠

    INVALID,
    OPEN_NORMAL_LIST_PAGE, //TODO zxy 筛选页面
    OPEN_PRODUCT_PACKAGE_PAGE,
    OPEN_NORMAL_H5_PAGE; // 详情页进入产品包页面

    public static Action createAction(String actionName) {
        if (TextUtils.isEmpty(actionName)) {
            Lg.e(TAG, "action name is invalid.");
            return INVALID;
        }
        for (Action a : Action.values()) {
            if (getActionName(a).equalsIgnoreCase(actionName)) {
                return a;
            }
        }

        return INVALID;

    }

    /**
     * 返回结果不区分大小写
     *
     * @param action
     * @return
     */
    public static String getActionName(Action action) {
        if (action == null || action == INVALID) {
            return "";
        } else if (action == OPEN_STTING) {
            return "open_stting";
        } else if (action == OPEN_COLLECTION) {
            return "open_collection";
        } else if (action == OPEN_VIP) {
            return "open_vip";
        } else if (action == OPEN_SEARCH) {
            return "open_search";
        } else if (action == OPEN_USER_VOUCHERS) {
            return "open_user_vouchers";
        } else if (action == OPEN_USER_SIGN_IN) {
            return "open_user_sign_in";
        } else if (action == USER_REGISTER) {
            return "user_register";
        } else if (action == USER_MODIFY) {
            return "user_modify";
        } else if (action == OPEN_MOBILE_MORE_VERTICAL) {
            return "open_mobile_more_vertical";
        } else if (action == OPEN_MOBILE_MORE_HORIZONTAL) {
            return "open_mobile_more_horizontal";
        } else if (action == USER_AGREEMENT) {
            return "user_agreement";
        } else if (action == OPEN_DETAIL) {
            return "open_detail";
        } else if (action == OPEN_PERSON_DETAIL_PAGE) {
            return "open_person_detail_page";
        } else if (action == OPEN_NORMAL_DETAIL_PAGE) {
            return "open_normal_detail_page";
        } else if (action == OPEN_MOVIE_DETAIL_PAGE) {
            return "open_movie_detail_page";
        } else if (action == OPEN_TV_DETAIL_PAGE) {
            return "open_tv_detail_page";
        } else if (action == OPEN_VARIETY_DETAIL_PAGE) {
            return "open_variety_detail_page";
        } else if (action == OPEN_FEEDBACK) {
            return "open_feedback";
        } else if (action == OPEN_NORMAL_CAROUSEL_PLAYER) {
            return "open_normal_carousel_player";
        } else if (action == OPEN_LIVE_DETAIL_PAGE) {
            return "open_live_detail_page";
        } else if (action == OPEN_BLE_GUIDE) {
            return "open_ble_guide";
        } else if (action == OPEN_MY_BEADS) {
            return "open_my_beads";
        } else if (action == OPEN_NORMAL_LIST_PAGE) { //筛选页面
            return "open_normal_list_page";
        } else if (action == OPEN_PRODUCT_PACKAGE_PAGE) { //详情页进入产品包页面
            return "open_product_package_page";
        } else if (action == OPEN_NORMAL_H5_PAGE) { //打开H5页面
            return "open_normal_h5_page";
        } else {
            return "";
        }
    }

    private static final String TAG = "Action";
}
