package cn.cibnmp.ott.ui.detail.content;

import com.alibaba.fastjson.JSON;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.detail.bean.ReservationBean;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.bean.ReserveBeanFather;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/30.
 */

public class UserReserveHelper {


        private final static String TAG = "UserReserveHelper";

//    /**
//     * 添加预约
//     *
//     * @param userId      如用户未登录，则使用App.pulicTID或者传空
//     * @param reserveBean
//     */
//    public static void add(String userId, ReserveBean reserveBean) {
//        if (reserveBean == null) {
//            return;
//        }
//        if (TextUtils.isEmpty(userId) || userId.equals("0")) {  //如果用户未登录时，使用tid
//            userId = App.publicTID;
//        }
//
//        if (isReserved(userId, String.valueOf(reserveBean.getLiveId()))) {
//            Lg.e(TAG, " userId = " + userId + " , liveId = " + reserveBean.getLiveId()
//                    + " has exits , can't reserve again!");
//            return;
//        }
//
//        App.userReservedList.add(reserveBean);
//        EventBus.getDefault().post(new AddUserReserveEvent(true));
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("userId", userId);
//        map.put("livestartstamp", String.valueOf(reserveBean.getLiveStartTimeStamp()));
//        map.put("liveendstamp", String.valueOf(reserveBean.getLiveEndTimeStamp()));
//        map.put("currstamp", String.valueOf(reserveBean.getCurrTimeStamp()));
//        map.put("liveid", String.valueOf(reserveBean.getLiveId()));
//        map.put("name", String.valueOf(reserveBean.getName()));
//        map.put("posterfid", String.valueOf(reserveBean.getPosterFid()));
//        map.put("onlive", reserveBean.isOnLive() ? 1 : 0);  //1代表正在直播中，0代表未开始
//        try {
//            DBUtils.getInstance().insert(userReserveTableName, map);
//            Lg.d(TAG, "user add new reserve , liveid = " + reserveBean.getLiveId() + " , userId = " + userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 删除指定liveid的预约
//     *
//     * @param reserveBean
//     */
//    public static void del(String userId, ReserveBean reserveBean) {
//        try {
//            if (TextUtils.isEmpty(userId) || userId.equals("0")) {
//                userId = App.publicTID;
//            }
//            App.userReservedList.remove(reserveBean);
//            DBUtils.getInstance().delete(userReserveTableName, new String[]{"userId", "liveid"},
//                    new String[]{userId, reserveBean.getLiveId()});
//            Lg.d(TAG, "delete a reserve , liveid = " + reserveBean.getLiveId() + " , userId = " + userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 删除用户的所有的预约
//     */
//    public static void delAll(String userId) {
//        try {
//            if (TextUtils.isEmpty(userId) || userId.equals("0")) {
//                userId = App.publicTID;
//            }
//            DBUtils.getInstance().delete(userReserveTableName, new String[]{"userId"},
//                    new String[]{userId});
//            App.userReservedList.clear();
//            Lg.d(TAG, "delete all reserves  , userId = " + userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 查询用户是否预约某一个直播
//     *
//     * @param userId
//     * @param liveId
//     * @return false 代表未预约，true代表已预约
//     */
//    public static boolean isReserved(String userId, String liveId) {
//        if (TextUtils.isEmpty(userId) || userId.equals("0")) {  //如果用户未登录时，使用tid
//            userId = App.publicTID;
//        }
//        String sql = "select * from " + userReserveTableName + " where userId = '" + userId
//                + "' and liveid = '" + liveId + "'";
//
//        Lg.d(TAG, "query sql : " + sql);
//        try {
//            List<String[]> list = DBUtils.getInstance().query(sql);
//            if (list == null || list.size() <= 0) {
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    /**
//     * 将直播未开始状态更新为直播中状态
//     */
//    public static void updateReserveState(String userId, String liveId) {
//        if (TextUtils.isEmpty(userId) || userId.equals("0")) {  //如果用户未登录时，使用tid
//            userId = App.publicTID;
//        }
//
//        try {
//            DBUtils.getInstance().update(userReserveTableName, new String[]{"onlive"},
//                    new Object[]{1}, new String[]{"userId", "liveid"}, new String[]{userId, liveId});
//            Lg.d(TAG, "update updateReserveState success , userId = " + userId + " , liveId = " + liveId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Lg.e(TAG, "update updateReserveState failed , userId = " + userId + " , liveId = " + liveId);
//        }
//    }
//
//
//    /**
//     * 查询用户预约列表
//     *
//     * @return
//     */
//    public static List<ReserveBean> query(String userId) {
//        if (TextUtils.isEmpty(userId) || userId.equals("0")) {  //如果用户未登录时，使用tid
//            userId = App.publicTID;
//        }
//        String sql = "select * from " + userReserveTableName + " where userId = '" + userId + "'";
//        Lg.d(TAG, "query sql : " + sql);
//        try {
//            List<String[]> list = DBUtils.getInstance().query(sql);
//            if (list == null) {
//                return null;
//            }
//            List<ReserveBean> reservelist = new CopyOnWriteArrayList<>();
//            ReserveBean bean;
//            for (String[] strs : list) {
//                bean = new ReserveBean();
//                bean.setLiveStartTimeStamp(Long.valueOf(strs[1]));
//                bean.setLiveEndTimeStamp(Long.valueOf(strs[2]));
//                bean.setCurrTimeStamp(Long.valueOf(strs[3]));
//                bean.setLiveId(strs[4]);
//                bean.setName(strs[5]);
//                bean.setPosterFid(strs[6]);
//                bean.setOnLive(Integer.valueOf(strs[7]) == 1 ? true : false); //1代表已预约
//                reservelist.add(bean);
//            }
//            return reservelist;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


        /**
         * 添加预约
         *
         * @param reserveBean
         */
        public static void add(final ReserveBean reserveBean, final boolean isUpdate) {
            if (reserveBean == null) {
                return;
            }
            HttpRequest.getInstance().excute("getRequestComcaLiveAppointmentAdd", new Object[]{JSON.toJSONString(reserveBean), new HttpResponseListener() {
                @Override
                public void onSuccess(String response) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>Y" + response);
                    ReservationBean r= new ReservationBean(true);
                    r.setLid(Long.parseLong(reserveBean.getLid()));
                    r.setSid(reserveBean.getSid());
                    EventBus.getDefault().post(r);

                    if (isUpdate) {
                        App.userReservedList.add(reserveBean);

                    } else {
                        App.userReservedList.remove(reserveBean);
                    }
//                Lg.i("kjy", "yyyyyyyyyyyY" + App.userReservedList.size());
                }

                @Override
                public void onError(String error) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>N" + error);
                }
            }});
        }

        /**
         * 删除指定liveid的预约
         */
        public static void del(int allflag, final long liveId, final long sid) {
            HttpRequest.getInstance().excute("getRequestComcaLiveAppointmentDel", new Object[]{allflag, liveId,sid, new HttpResponseListener() {
                @Override
                public void onSuccess(String response) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>Y" + response);
                    ReservationBean r= new ReservationBean(false);
                    r.setLid(liveId);
                    r.setSid(sid);
                    EventBus.getDefault().post(r);
                }

                @Override
                public void onError(String error) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentAdd--->>N" + error);

                }
            }});
        }

        /**
         * 查询用户是否预约某一个直播
         *
         *
         * @param sid
         * @param liveId
         * @return false 代表未预约，true代表已预约
         */
        public static void isReserved( long liveId,long sid, final DbQueryLiveidListener dbQueryLiveidListener) {
            Lg.i("USER","getRequestComcaLiveAppointmentGet TT");
            HttpRequest.getInstance().excute("getRequestComcaLiveAppointmentQueryAdd", new Object[]{liveId,sid, new HttpResponseListener() {
                @Override
                public void onSuccess(String response) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentGet--->>Y" + response);
                    dbQueryLiveidListener.query(true);
                }

                @Override
                public void onError(String error) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentGet--->>N" + error);
                    dbQueryLiveidListener.query(false);
                }
            }});
        }

        /**
         * 查询用户预约列表
         *
         * @return
         */
        public static void query(Integer startindex, Integer getnum, final DbQueryListener dbQueryListener) {
            HttpRequest.getInstance().excute("getRequestComcaLiveAppointmentList", new Object[]{startindex, getnum, new HttpResponseListener() {
                @Override
                public void onSuccess(String response) {
                    Lg.i("kjy", "getRequestComcaLiveAppointmentList--->>" + response);
                    ReserveBeanFather reservelist = null;
                    try {
                        reservelist = JSON.parseObject(response, ReserveBeanFather.class);
                    } catch (Exception e) {
                        dbQueryListener.query(null);
                    }
                    if (reservelist != null && reservelist.getLiveAppointment() != null && reservelist.getLiveAppointment().size() > 0) {
                        dbQueryListener.query(reservelist.getLiveAppointment());
                    } else {
                        dbQueryListener.query(null);
                    }
                }

                @Override
                public void onError(String error) {
                    dbQueryListener.query(null);
                }
            }});
        }
    /**
     * 查询用户预约列表(高清影视)
     *
     * @return
     */


    /**
     * 查询用户预约数据(高清影视)
     *
     * @return
     */
    public static void queryDateGQ(Integer startindex, Integer getnum, Integer condition, final DbQueryListener dbQueryListener) {
        HttpRequest.getInstance().excute("getRequestComcaLiveAppointmentConditionList", new Object[]{startindex, getnum, condition, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i("kjy", "getRequestComcaLiveAppointmentConditionList--->>" + response);
                ReserveBeanFather reservelist = null;
                try {
                    reservelist = JSON.parseObject(response, ReserveBeanFather.class);
                } catch (Exception e) {
                    dbQueryListener.query(null);
                }
                if (reservelist != null && reservelist.getLiveAppointment() != null && reservelist.getLiveAppointment().size() > 0) {
                    dbQueryListener.query(reservelist.getLiveAppointment());
                } else {
                    dbQueryListener.query(null);
                }
            }

            @Override
            public void onError(String error) {
                dbQueryListener.query(null);
            }
        }});
    }


    /**
     * 查询所有未开始的预约数据(高清影视)
     *
     * @return
     */
    public static void queryLiveGQ(Integer startindex, Integer getnum, Integer stat, final DbQueryListener dbQueryListener) {
        HttpRequest.getInstance().excute("getRequestComcaLiveAppointLivestatList", new Object[]{startindex, getnum, stat, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i("kjy", "getRequestComcaLiveAppointLivestatList--->>" + response);
                ReserveBeanFather reservelist = null;
                try {
                    reservelist = JSON.parseObject(response, ReserveBeanFather.class);
                } catch (Exception e) {
                    dbQueryListener.query(null);
                }
                if (reservelist != null && reservelist.getLiveAppointment() != null && reservelist.getLiveAppointment().size() > 0) {
                    dbQueryListener.query(reservelist.getLiveAppointment());
                } else {
                    dbQueryListener.query(null);
                }
            }

            @Override
            public void onError(String error) {
                dbQueryListener.query(null);
            }
        }});
    }
    }
