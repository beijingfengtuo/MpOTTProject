package cn.cibnmp.ott.ui.detail.presenter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;

import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.UserStateEvent;
import cn.cibnmp.ott.ui.detail.DetailHolderHelper;
import cn.cibnmp.ott.ui.detail.bean.AddDetailReserveBeanEvent;
import cn.cibnmp.ott.ui.detail.bean.AddUserReserveEvent;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.ui.detail.bean.ReserveBeanEvent;
import cn.cibnmp.ott.ui.detail.content.DbQueryListener;
import cn.cibnmp.ott.ui.detail.content.UserReserveHelper;
import cn.cibnmp.ott.utils.Lg;
import de.greenrobot.event.EventBus;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by yanjing on 2017/6/7.
 */

public class LiveManager {
    private final static String TAG = "LiveManager";
    private DbQueryListener dbQueryListener;

    private ReserveBean currDetailBean = null;
    private boolean test = true;

    private static LiveManager liveManager;

    public static LiveManager getLiveManager() {
        if (liveManager == null) {
            liveManager = new LiveManager();
        }
        return liveManager;
    }

    public LiveManager() {
        Lg.i(TAG, "------homeActivity LiveManager onCreat----");
    }

    public void startLiveThread() {
        if (App.userReservedList.size() > 0 && stop) {
            stop = false;
//            ThreadExecutor.getInstance().excute(checkRunnable);  //若有预约，则重启计时线程
            new Thread(checkRunnable).start();
        }
    }

    public void onEvent(UserStateEvent event) {
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
                        Lg.i(TAG, "user reserved list is empty . current userId = BaseApplication.userId");
                    } else {
                        Lg.i(TAG, "user reserved list's size is " + list.size() + " , current userId = BaseApplication.userId");
                        App.addAllList(list);
                        stop = false;
//                        ThreadExecutor.getInstance().excute(checkRunnable);  //若有预约，则重启计时线程
                        new Thread(checkRunnable).start();
                    }
                }
            };
        }
        UserReserveHelper.queryLiveGQ(0, 100, 0, dbQueryListener);
    }


    public void onEvent(AddUserReserveEvent event) {
        if (event == null) {
            Lg.e(TAG, "AddUserReserveEvent: event is null.");
            return;
        }

        Lg.i(TAG, "user has added new reserve .");  //此次来判断是否启动一个线程来检测已预约直播的状态

        if (stop) {
            stop = false;
//            ThreadExecutor.getInstance().excute(checkRunnable);
            new Thread(checkRunnable).start();
        }

    }

    public  void onEvent(AddDetailReserveBeanEvent event) {
        if (event == null) {
            Lg.d(TAG, "AddDetailReserveBeanEvent: event is null.");
            return;
        }

        Lg.d(TAG, "AddDetailReserveBeanEvent received .");

        this.currDetailBean = event.getReserveBean();

        if (currDetailBean != null && stop) {
            stop = false;
//            ThreadExecutor.getInstance().excute(checkRunnable);
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
                Lg.i(TAG,"run  run run");
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
        if (bean.getLivestatus() < 2 && bean.getLiveEndTimeStamp() <= serverCurr) {
            bean.setLivestatus(2);
            bean.setLiveFlag(1);
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
        Lg.i(TAG,"预约哈哈哈"+System.currentTimeMillis()+"....."+ App.timeDvalue);
        Lg.i(TAG, "预约的直播开始时间戳 直播已经开始" + serverCurr+"........"+bean.getLiveStartTimeStamp());
        //直播未开始时，如果刚到达直播开始时间点，用户已预约时要弹出对话框通知用户。
        if (bean.getLivestatus() == 0 && bean.getLiveStartTimeStamp() <= serverCurr
                && bean.getLiveEndTimeStamp() > serverCurr) {
            Lg.i(TAG, "预约的直播开始时间戳 直播已经开始" + serverCurr);
            Lg.i("", "开始--------------------->>>>>111" + bean.getName());
            bean.setLivestatus(1);
            //添加预约
            UserReserveHelper.add(bean, false);
            Message msg = new Message();
            msg.what = 1001;
            msg.obj = bean;
            mHandler.sendMessage(msg);
        }
        //直播已经结束，如果刚到达直播结束时间点，用户已预约，将此条数据从数据库中删除
        else if (bean.getLivestatus() < 2 && bean.getLiveEndTimeStamp() <= serverCurr) {
            Lg.d(TAG, "--live has ended-已提醒--bean.getLiveEndTimeStamp-------" + bean.getLiveEndTimeStamp());
//            UserReserveHelper.del(String.valueOf(App.userId), bean);
            //更新预约
            bean.setLivestatus(2);
            bean.setLiveFlag(1);
            UserReserveHelper.add(bean, false);
            Message msg = new Message();
            msg.what = 1002;
            msg.obj = bean;
            mHandler.sendMessage(msg);
        }
    }

    //发送预约通知
    private void startMesseg(final ReserveBean reserveBean) {
        Lg.i("", "开始--------------------->>>>>222" + reserveBean.getName());
        //000000000000000000000000000
        String name = "";
        if (reserveBean.getName() != null) {
            name = reserveBean.getName();
        }
//        Toast.makeText(BaseApplication.getAppManager().currentActivity(), "直播通知了", Toast.LENGTH_SHORT).show();
        Activity activity = App.getAppManager().currentActivity();
        setYuYueMsg(reserveBean,activity);
        new LiveStartDialog.Builder(activity).setTitle("《" + name + "》")
                .create(activity, new LiveStartDialog.RemindLiveListener() {
                    @Override
                    public void start() {
                 //       ((BaseActivity) App.getAppManager().currentActivity()).LiveOpenDetail(reserveBean.getEpgId(), reserveBean.getLid(), reserveBean.getAction());

                        NavigationInfoItemBean infoItemBean = new NavigationInfoItemBean();
                        infoItemBean.setEpgId(Integer.valueOf(reserveBean.getEpgId()));
                        infoItemBean.setContentId(reserveBean.getLid());
                                infoItemBean.setAction("open_live_detail_page");
                        DetailHolderHelper.onClick(infoItemBean, App.getAppManager().currentActivity());
                    }
                }).show();

    }

    NotificationManager mNotificationManager;
    int timeY;
    private void setYuYueMsg(ReserveBean resultBean, Context context) {
        if (resultBean != null) {
            timeY = (int) System.currentTimeMillis();
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setTicker("预约《" + resultBean.getName() + "》节目，已经开始了！") //通知首次出现在通知栏，带上升动画效果的
                    .setContentTitle("预约《" + resultBean.getName() + "》节目 已经开始了")//设置通知栏标题
                    .setContentText("请收看！")//内容名称
                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。
                    .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果,使用当前的用户默认设置
                    .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
            Notification notification = mBuilder.getNotification();//将builder对象转换为普通的notification
            notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
            mNotificationManager.notify(timeY, notification);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mNotificationManager.cancel(timeY);
//                }
//            }, 2000);
        }
    }
}