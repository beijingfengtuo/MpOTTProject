package cn.cibnmp.ott.lib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.bean.ReserveBean;
import cn.cibnmp.ott.utils.Lg;

/**
 * Created by axl on 2018/1/30.
 */

public class AutoReceiver  extends BroadcastReceiver{
    private Intent mIntent;
    private Bundle bundle;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("VIDEO_TIMER")) {
            Bundle getBundle = intent.getBundleExtra(Constant.activityBundleExtra);
            if (getBundle != null && getBundle.getSerializable("ReserveBean") != null) {
                long time = System.currentTimeMillis();
                ReserveBean reserveBean = (ReserveBean) getBundle.getSerializable("ReserveBean");
                Lg.i("", "开始--------------------->>>>>00" + reserveBean.getName());
                if (mIntent == null) {
                    mIntent = new Intent(context, DetailActivity.class);
                    bundle = new Bundle();
                    bundle.putInt("detail_type", 4);
                    bundle.putInt("detail_ty", 4);
                }
                bundle.putLong(Constant.contentIdKey, Long.valueOf(reserveBean.getLid()));
                mIntent.putExtra(Constant.activityBundleExtra, bundle);
                PendingIntent pi = PendingIntent.getActivity(context, ((int) time), mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setTicker(reserveBean.getName() + "正在直播") //通知首次出现在通知栏，带上升动画效果的
                        .setContentTitle("CIBN当前节目正在直播")//设置通知栏标题
                        .setContentText(reserveBean.getName())//内容名称
                        .setContentIntent(pi) //设置通知栏点击意图
                        .setWhen(time)//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_VIBRATE//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
                Notification notification = mBuilder.getNotification();//将builder对象转换为普通的notification
                notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
                mNotificationManager.notify(((int) time), notification);
            }
        }
    }
}
