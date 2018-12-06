package cn.cibnmp.ott.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.util.List;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.ReserveBeanEvent;
import cn.cibnmp.ott.bean.UserStateEvent;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.detail.bean.AddDetailReserveBeanEvent;
import cn.cibnmp.ott.ui.detail.bean.AddUserReserveEvent;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.content.DbQueryListener;
import cn.cibnmp.ott.ui.detail.content.UserReserveHelper;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;

/**
 * Created by axl on 2018/1/30.
 */

public class TimerService extends Service {
    private final static String TAG = "ReserveService";
    private DbQueryListener dbQueryListener;

    private ReserveBean currDetailBean = null;
    private boolean test = true;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop = true;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Lg.d(TAG, "ReserveService onStartCommand...");
        if (App.userReservedList.size() > 0 && stop) {
            stop = false;
            new Thread(checkRunnable).start();  //若有预约，则重启计时线程
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public TimerService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void onEventMainThread(UserStateEvent event) {
        if (event == null) {
            Lg.e(TAG, "UserStateEvent : event is null.");
            return;
        }

        Lg.d(TAG, "user login state changed , update userReservedlist . ");

        //先清空用户的预约列表
        App.userReservedList.clear();
        stop = true;   //停止当前的计时
        if (dbQueryListener == null) {
            dbQueryListener = new DbQueryListener() {
                @Override
                public void query(List<ReserveBean> list) {
                    //若为空，则不启动计时线程
                    if (list == null || list.size() <= 0) {
                        Lg.i(TAG, "user reserved list is empty . current userId = " + App.userId);
                    } else {
                        Lg.i(TAG, "user reserved list's size is " + list.size() + " , current userId = " + App.userId);
                        App.addAllList(list);
                        stop = false;
                        new Thread(checkRunnable).start();  //若有预约，则重启计时线程
                    }
                }
            };
        }
        UserReserveHelper.query(0, 50, dbQueryListener);

    }


    protected void onEventMainThread(AddUserReserveEvent event) {
        if (event == null) {
            Lg.e(TAG, "AddUserReserveEvent: event is null.");
            return;
        }

        Lg.i(TAG, "user has added new reserve .");  //此次来判断是否启动一个线程来检测已预约直播的状态

        if (stop) {
            stop = false;
            new Thread(checkRunnable).start();
        }

    }

    protected void onEventMainThread(AddDetailReserveBeanEvent event) {
        if (event == null) {
            Lg.d(TAG, "AddDetailReserveBeanEvent: event is null.");
            return;
        }

        Lg.d(TAG, "AddDetailReserveBeanEvent received .");

        this.currDetailBean = event.getReserveBean();

        if (currDetailBean != null && stop) {
            stop = false;
            new Thread(checkRunnable).start();
        }
    }

    private boolean stop = true;  //线程的状态,是否是停止状态
    private long serverCurr = System.currentTimeMillis() + App.timeDvalue;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1001:
                    startMesseg((ReserveBean) msg.obj);
                    break;
                case 1002:
                    ReserveBean bean = (ReserveBean) msg.obj;
                    ReserveBeanEvent event = new ReserveBeanEvent();
                    event.setLiveId(bean.getLid());
                    EventBus.getDefault().post(event);
                    break;
            }

            return false;
        }
    });

    private Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            while (!stop) {
                try {
                    Thread.sleep(1000l);
                    serverCurr = System.currentTimeMillis() + App.timeDvalue;
                    for (ReserveBean bean : App.userReservedList) {
                        handleReserveBean(bean);
                    }
                    if (currDetailBean != null) {
                        handleDetailReserveBean(currDetailBean);
                    }
                    if (App.userReservedList.size() <= 0 && currDetailBean == null) {
                        stop = true;
                    }
//                    Lg.i(TAG, "serverCurr = " + serverCurr + " , userReservedList.size = "
//                            + App.userReservedList.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void handleDetailReserveBean(ReserveBean bean) {
        if (bean.getLivestatus() != 2 && bean.getLiveEndTimeStamp() <= serverCurr) {
            bean.setLivestatus(2);
            Lg.i(TAG, "--live has ended----bean.getLiveEndTimeStamp-------" + bean.getLivestatus());
            Message msg = new Message();
            msg.what = 1002;
            msg.obj = bean;
            mHandler.sendMessage(msg);
            currDetailBean = null;
        }
    }

    private void handleReserveBean(ReserveBean bean) {
//        //000000000000000测试的的的
//        if (test) {
//            test = false;
//            Message msg = new Message();
//            msg.what = 1001;
//            msg.obj = bean;
//            mHandler.sendMessage(msg);
//        }
//        //000000000000000测试的的的

        //直播未开始时，如果刚到达直播开始时间点，用户已预约时要弹出对话框通知用户。
        if (bean.getLivestatus() == 0 && bean.getLiveStartTimeStamp() <= serverCurr
                && bean.getLiveEndTimeStamp() > serverCurr) {
            Lg.i(TAG, "预约的直播开始时间戳 直播已经开始" + serverCurr);
//            Lg.i("", "开始--------------------->>>>>111" + bean.getName());
            bean.setLivestatus(1);
            //添加预约
            UserReserveHelper.add(bean, false);
            Message msg = new Message();
            msg.what = 1001;
            msg.obj = bean;
            mHandler.sendMessage(msg);
        }
        //直播已经结束，如果刚到达直播结束时间点，用户已预约，将此条数据从数据库中删除
        else if (bean.getLivestatus() != 2 && bean.getLiveEndTimeStamp() <= serverCurr) {
            Lg.d(TAG, "--live has ended-已提醒--bean.getLiveEndTimeStamp-------" + bean.getLiveEndTimeStamp());
//            UserReserveHelper.del(String.valueOf(App.userId), bean);
            //更新预约
            bean.setLivestatus(2);
            UserReserveHelper.add(bean, false);
            Message msg = new Message();
            msg.what = 1002;
            msg.obj = bean;
            mHandler.sendMessage(msg);
        }
    }

    private int count = 0;

    //发送预约通知
    private void startMesseg(ReserveBean reserveBean) {
        Lg.i("", "开始--------------------->>>>>222" + reserveBean.getName());
        Intent videoIntent = new Intent(this, AutoReceiver.class);
        videoIntent.setAction("VIDEO_TIMER");
        Bundle bundle = new Bundle();
        bundle.putSerializable("ReserveBean", reserveBean);
        videoIntent.putExtra(Constant.activityBundleExtra, bundle);
        // PendingIntent这个类用于处理即将发生的事情
        PendingIntent sender = PendingIntent.getBroadcast(this, count++, videoIntent, PendingIntent.FLAG_UPDATE_CURRENT); //
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用相对时间
        // SystemClock.elapsedRealtime()表示手机开始到现在经过的时间
//        Lg.i("kjy", "SystemClock.elapsedRealtime()" + SystemClock.elapsedRealtime()+ 10 * 1000);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), sender);
    }
}
