package cn.cibnmp.ott.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import cn.cibnmp.ott.App;

/**
 * Created by cao on 2016/7/14.
 */
public class BrightUtils {


    /**
     * 判断是否开启了自动亮度调节
     */
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {

        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return automicBrightness;
    }

    /**
     * 停止自动亮度调节
     */

    public static void stopAutoBrightness(Activity activity) {

        //TODO: 18/3/2 yangwenwu 安卓6.0系统权限问题android.permission.WRITE_SETTINGS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } else {
                //有了权限，具体的动作
                Settings.System.putInt(activity.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        }
    }

    /**
     * 开启亮度自动调节 *
     *
     * @param activity
     */
    public static void startAutoBrightness(Activity activity) {

        //TODO: 18/3/2 yangwenwu 安卓6.0系统权限问题android.permission.WRITE_SETTINGS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } else {
                //有了权限，具体的动作
                Settings.System.putInt(activity.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
        }
    }

    /**
     * @param mContext
     * @param po       需要改变的亮度刻度
     * @return 返回改变后的亮度值
     */
    public static float changeBright(Activity mContext, int po) {
        float screenBright = 0;
        WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow()
                .getAttributes();
        lpa.screenBrightness = po / 255f;
        screenBright = lpa.screenBrightness;
        ((Activity) mContext).getWindow().setAttributes(lpa);
        return screenBright;
    }

    /**
     * 获取当前亮度
     *
     * @param mContext
     * @return
     */
    public static int getBright(Activity mContext) {
        int bright = 0;
        ContentResolver cr = mContext.getContentResolver();
        try {
            bright = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS);
//            Log.i("getCurrentVolume", "亮度值：" + bright);
        } catch (Settings.SettingNotFoundException e) {

        }
        return bright;
    }

    /**
     * 获取当前音量
     *
     * @param mAudioManager
     * @return
     */
    public static int getCurrentVolume(AudioManager mAudioManager) {
        int sound = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        Log.i("getCurrentVolume","音量值：" + sound);
        return sound;
    }

    /**
     * 获取最大音量值
     *
     * @param mAudioManager
     * @return
     */
    public static int getMaxVolume(AudioManager mAudioManager) {
        int streamMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        Log.i("getCurrentVolume","最大音量值：" + streamMaxVolume);
        return streamMaxVolume;
    }

    /**
     * @param mAudioManager
     * @param changSound    传入需要改变值
     */
    public static void changeVolume(AudioManager mAudioManager, int changSound) {
        try {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, changSound,
                    AudioManager.FX_FOCUS_NAVIGATION_DOWN);
        }catch (Exception e){
            NotificationManager notificationManager = (NotificationManager) (App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    && !notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
               App.getInstance().startActivity(intent);
            }
        }
        }
}
