package cn.cibnmp.ott.utils;

import android.app.Activity;
import android.content.Context;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: Activity管理类：用于管理Activity和退出程序
 */
public class AppManager {
    // Activity
    private static CopyOnWriteArrayList<Activity> activityStack;
    // 单例模式
    private static AppManager instance;

    public static Class<?> isCliss = null;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new CopyOnWriteArrayList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.get(activityStack.size() - 1);
        return activity;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getSecondTopActivity() {
        if (activityStack.size() - 2 < 0) {
            return null;
        }
        return activityStack.get(activityStack.size() - 2);
    }

    /**
     * 检测某ActivityUpdate是否在当前Task的栈顶
     */
    public boolean isTopActivy(Activity activity) {
        Activity topactivity = activityStack.get(activityStack.size() - 1);
        return activity.toString().equals(topactivity.toString());
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.get(activityStack.size() - 1);
        activityStack.remove(activity);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 将已经关闭的Activity从栈中清除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activityStack.remove(activity);
                activity.finish();
            }
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishTopActivity(int num) {
        if (activityStack != null) {
            for (int i = 0; i < num; i++) {
                finishActivity();
            }
        }
    }

    /**
     * 结束所有Activity 除了指定的Activity
     */
    public void finishAllActivityExcept(Class<?> cls) {
        while (activityStack.size() > 0) {
            Activity activity = activityStack.get(activityStack.size() - 1);
            if (!activity.getClass().equals(cls)) {
                activityStack.remove(activityStack.size() - 1);
                activity.finish();
            } else {
                if (activityStack.size() == 1)
                    break;
            }
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {

//            EventBus.getDefault().post(new AppExitEvent());
//            JNIRequest.getInstance().reportAppExit();  // 应用退出上报
//            MobclickAgent.onKillProcess(context);
            finishAllActivity();
//            stopNewsService();
//            ThreadExecutor.getInstance().close();
//            HttpRequestExecutor.getInstance().close();
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出应用程序
     */
    public void crashAppExit(Context context) {
        try {

//            EventBus.getDefault().post(new AppExitEvent());

//            JNIRequest.getInstance().reportAppExit();  // 应用退出上报
//            MobclickAgent.onKillProcess(context);
//            stopNewsService();
//            ThreadExecutor.getInstance().close();
//            HttpRequestExecutor.getInstance().close();
//            restartApp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存单一实例的Activity
     */
    public void onlyActivity(Class<?> cls) {
        boolean isCls = true;
        int onlySize = activityStack.size() - 1;
        for (int i = onlySize; i > 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                if (isCls) {
                    isCls = false;
                } else {
                    activityStack.remove(i);
                    activity.finish();
                }
            }
        }
    }

}
