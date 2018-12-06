package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.BaseUrl;
import cn.cibnmp.ott.bean.HostUpgradeDataBean;
import cn.cibnmp.ott.bean.HostUpgradeResultBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.ResponseCode;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.MD5FileUtil;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Util;
import cn.cibntv.downloadsdk.download.DownloadInfo;
import cn.cibntv.downloadsdk.download.DownloadManager;
import cn.cibntv.downloadsdk.download.ErrorCode;
import cn.cibntv.downloadsdk.listener.DownloadListener;
import cn.cibntv.downloadsdk.request.GetRequest;

/**
 * Created by yanjing on 2017/9/14.
 *
 * 版本升级检测管理
 */

public class HostUpgradeManager {

    private static final String TAG = "HostUpgradeManager";

    private static final String upgradeApkPath = Constant.DOWNLOADPATH + "/host_upgrade.apk";

    public static final int loop_check_time = 60; //分钟,默认是60分钟，升级轮询的间隔时间60分钟

    public static final int boot_upgreade_state = 1;  //开机检测升级状态
    public static final int loop_upgrade_state = 1 << 1;  //轮询检测升级状态，每隔一个小时检测一次
    public static final int day_upgrade_state = 1 << 2;  //当用户暂时忽略升级提示后，24小时内不再弹出提示框，24小时之后再次弹出提示框
    public static final int setting_upgrade_state = 1 << 3; //设置界面检测升级状态
    public static final int first_loop_upgrade_state = 1 << 4; //轮询第一次检测时，需要弹出提示框

    //用户选择暂不升级的时间点，精确到小时和分，只判断当前时间的小时和分;
    // 同时也要根据是否有值来判断用户是否选择了暂不升级的状态
    public static final String user_option_time = "user_option_upgrade_time";

    private Context context;
    private ProgressDialog mProgressDlg;
    private HostUpgradeDialog hostUpgradeDialog;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 101:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_error));
                        }
                        break;
                    case 102:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_null));
                        }

                        break;
                    case 103:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_format_error));
                        }
                        break;

                    case 104:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_detection_error));
                        }
                        break;

                    case 105:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_detection_illegal));
                        }
                        break;
                    case 200:
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                        }
                        // new HostUpgradeDialog(context, R.style.UpgradeDialog, (HostUpgradeDataBean) msg.obj).show();
                        showUpgradeDialog(msg);
                        break;
                    case 201:
                        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing())
                            hostUpgradeDialog.dismiss();
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                            ToastUtils.show_common_style3(context, context.getString(R.string.updata_request_format_ok));
                        }
                        break;

                    case 202:  //接口请求的结果
                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state) {
                            if (mProgressDlg != null)
                                mProgressDlg.dismiss();
                        }

                        HostUpgradeDataBean hostUpgradeDataBean = (HostUpgradeDataBean) msg.obj;
                        if (hostUpgradeDialog != null)
                            hostUpgradeDialog.updateData(hostUpgradeDataBean);
                        DownloadInfo downloadInfo = App.downloadManager.getDownloadInfo(hostUpgradeDataBean.getUrl());
                        if (downloadInfo == null) {
                            try {
                                //有新的版本要下载，先清空旧的下载包
                                FileUtils.cleanDirectory(new File(Constant.DOWNLOADPATH));
                            } catch (IOException e) {
                            }
                            max_try_times = 3;
                            download(hostUpgradeDataBean);
//                            if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state)
//                                ToastUtils.show_common_style3(context, "安装包开始下载");
                        } else {
                            switch (downloadInfo.getState()) {
                                case DownloadManager.PAUSE:
                                case DownloadManager.NONE:
                                case DownloadManager.ERROR:
                                    download(hostUpgradeDataBean);
//                                    if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state)
//                                        ToastUtils.show_common_style3(context, "安装包正在下载中...");
                                    break;
                                case DownloadManager.DOWNLOADING:
                                    Lg.i(TAG, context.getString(R.string.updata_request_downloading));
//                                    if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state)
//                                        ToastUtils.show_common_style3(context, "安装包正在下载中...");
                                    break;
                                case DownloadManager.FINISH:
//                                    //此处还需要判断下载文件是否存在，判断文件是否完整
//                                    if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state)
//                                        ToastUtils.show_common_style3(context, "安装包已下载成功，请重启应用安装新版本");
                                    break;
                            }
                        }

                        Log.e(TAG,context.getString(R.string.updata_request_prompt_dialogbox));
                        //弹出升级提示框
                        showUpgradeDialog(msg);

                        break;

                    case 203:
//                        if ((msg.arg1 & setting_upgrade_state) == setting_upgrade_state)
//                            ToastUtils.show_common_style3(context, "安装包已下载成功，请重启应用安装新版本");
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    private void showUpgradeDialog(Message msg) {

        if (((HostUpgradeDataBean) msg.obj).getIsforce() == HostUpgradeDataBean.force_upgrade_type
                || (msg.arg1 & setting_upgrade_state) == setting_upgrade_state
                || (msg.arg1 & boot_upgreade_state) == boot_upgreade_state
                || (msg.arg1 & day_upgrade_state) == day_upgrade_state
                || (msg.arg1 & first_loop_upgrade_state) == first_loop_upgrade_state) {
            //升级不能在播放器界面弹出
            try {
                String clsName = App.getAppManager().currentActivity().toString();
                clsName = clsName.substring(clsName.lastIndexOf(".") + 1, clsName.indexOf("@"));
//                System.out.println("----------activity 名称：" + clsName);
                if (clsName.equals("DetailActivity") || clsName.equals("LiveDetailActivity") || clsName.equals("CarouselActivity")) {
                    return;
                }
            } catch (Throwable e) {
            }

            //弹出了升级提示框，就将上次保存的升级取消状态删除掉
            App.userOptionNoUpradeTime = -1;
            SharePrefUtils.removePreferences(App.getInstance(), HostUpgradeManager.user_option_time);

            if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing()) {
                Activity activity = hostUpgradeDialog.getActivity();
                if (activity != null && activity.toString().equals(App.getAppManager().currentActivity().toString())) {
                    hostUpgradeDialog.updateData((HostUpgradeDataBean) msg.obj);
                    return;
                }
                hostUpgradeDialog.dismiss();
                hostUpgradeDialog = null;
            }
            hostUpgradeDialog = new HostUpgradeDialog(App.getAppManager().currentActivity(), R.style.UpgradeDialog, HostUpgradeManager.this);
            hostUpgradeDialog.updateData((HostUpgradeDataBean) msg.obj);
            hostUpgradeDialog.show();
        }
    }

    public void dismissDialog() {
        if (hostUpgradeDialog != null && hostUpgradeDialog.isShowing()) {
            hostUpgradeDialog.dismiss();
        }
    }


    private void download(final HostUpgradeDataBean hostUpgradeDataBean) {
        GetRequest request = new GetRequest(hostUpgradeDataBean.getUrl());
        App.downloadManager.addTask(hostUpgradeDataBean.getFid(), hostUpgradeDataBean.getUrl(), request, new DownloadListener() {
            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                Lg.d(TAG, "download onProgress : " + downloadInfo.getDownloadLength() + " , fileName : " + downloadInfo.getTargetPath());
                if (hostDownloadCallback != null) {
                    hostDownloadCallback.onProgress(downloadInfo);
                }
            }

            @Override
            public void onFinish(DownloadInfo downloadInfo) {
                Lg.d(TAG, "download onFinish , fileName : " + downloadInfo.getTargetPath());
                boolean bo = new File(downloadInfo.getTargetPath()).renameTo(new File(upgradeApkPath));
                if (bo) {
                    if (!checkLocalFile(hostUpgradeDataBean)) {
                        onError(downloadInfo, context.getString(R.string.updata_request_md5_error), 6, null);
                        return;
                    }
                    Lg.i(TAG, context.getString(R.string.updata_request_prompt_name));
                    handler.sendEmptyMessage(203);
                    App.downloadManager.removeAllTask();
                    if (hostDownloadCallback != null) {
                        hostDownloadCallback.onFinish(downloadInfo);
                    }
                } else {
                    onError(downloadInfo, context.getString(R.string.updata_request_prompt_nameerror), ErrorCode.unknown_error, null);
                }
            }

            @Override
            public void onError(DownloadInfo downloadInfo, String s, int code, Exception e) {
                Lg.e(TAG, "download onError , errorMsg : " + s + " , " + getErrorDesc(code));
                if (code == ErrorCode.breadpoint_error || code == ErrorCode.unknown_error || code == 6) {
                    App.downloadManager.removeAllTask();
                    File f = new File(downloadInfo.getTargetPath());
                    if (f.exists())
                        FileUtils.deleteQuietly(f);
                }

                //md5值校验失败,直接回调失败
                if (code == 6) {
                    reportDownloadFailError(downloadInfo, code);
                    if (hostDownloadCallback != null) {
                        hostDownloadCallback.onError(downloadInfo, code);
                    }
                    return;
                }


                if (max_try_times > 0) {
                    max_try_times--;
                    download(hostUpgradeDataBean);
                } else {
                    max_try_times = 3;
                    //重试3次之后再上报下载错误原因
                    reportDownloadFailError(downloadInfo, code);
                    if (hostDownloadCallback != null) {
                        hostDownloadCallback.onError(downloadInfo, code);
                    }
                }

            }
        });
    }

    public void reDownload(HostUpgradeDataBean hostUpgradeDataBean) {
        max_try_times = 3;
        download(hostUpgradeDataBean);
    }

    private int max_try_times = 3;

    public HostUpgradeManager(Context context) {
        this.context = context;
    }

    public void clean() {
        try {
            if (hostUpgradeDialog != null)
                hostUpgradeDialog.dismiss();
            if (mProgressDlg != null)
                mProgressDlg.dismiss();
            hostUpgradeDialog = null;
            mProgressDlg = null;
        } catch (Throwable e) {
        }
    }

    /**
     * 获取当前是否有升级的方法
     *
     * @param upgradeListener
     */
    public void checkUprade(final UpgradeListener upgradeListener) {
        ThreadExecutor.getInstance().excute(new Runnable() {
            @Override
            public void run() {
                String s = App.getCache().getAsString(Constant.HOST_UPGRADE_KEY);
                if (!TextUtils.isEmpty(s)) {
                    HostUpgradeDataBean hostUpgradeDataBean = null;
                    try {
                        hostUpgradeDataBean = JSON.parseObject(s, HostUpgradeDataBean.class);
                    } catch (Exception e) {
                    }
                    if (hostUpgradeDataBean != null) {
                        if (hostUpgradeDataBean.getVerno() > transform(App.versionName)
                                && checkLocalFile(hostUpgradeDataBean)) {
                            if (upgradeListener != null)
                                upgradeListener.onUpgrade(true);
                            return;
                        } else if (hostUpgradeDataBean.getVerno() == transform(App.versionName)) {
                            try {
                                //已经是最新的版本，清空下载包
                                FileUtils.cleanDirectory(new File(Constant.DOWNLOADPATH));
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                if (upgradeListener != null)
                    upgradeListener.onUpgrade(false);
                //删除本地保存的升级信息
                App.getCache().remove(Constant.HOST_UPGRADE_KEY);
            }
        });
    }


    /**
     * 有过渡效果的检测升级方法，设置界面调用
     */
    public void checkUpgrade(int state, Activity activity) {
        Util.verifyStoragePermissions(activity);
        check(state);
    }

    private void check(int state) {
        if ((state & setting_upgrade_state) == setting_upgrade_state) {

            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
                mProgressDlg = null;
            }
            mProgressDlg = new CustomDialog(App.getAppManager().currentActivity(), R.style.CustomDialog);
            mProgressDlg.show();
        }

        requestVersionInfo(state);
    }


    /**
     * 是否跟本地
     *
     * @param state
     * @param bean
     * @return
     */
    private boolean checkLocalCacheData(final int state, HostUpgradeDataBean bean) {
        String s = App.getCache().getAsString(Constant.HOST_UPGRADE_KEY);
        if (!TextUtils.isEmpty(s)) {
            HostUpgradeDataBean hostUpgradeDataBean = null;
            try {
                hostUpgradeDataBean = JSON.parseObject(s, HostUpgradeDataBean.class);
            } catch (Exception e) {
                //删除本地保存的升级信息
                App.getCache().remove(Constant.HOST_UPGRADE_KEY);
                return false;
            }
            if (hostUpgradeDataBean != null && bean.equals(hostUpgradeDataBean)) {
                if (hostUpgradeDataBean.getVerno() > transform(App.versionName)
                        && checkLocalFile(hostUpgradeDataBean)) {
                    Lg.i(TAG, context.getString(R.string.updata_request_format_highest));
                    Message message = Message.obtain();
                    message.what = 200;
                    message.arg1 = state;
                    message.obj = hostUpgradeDataBean;
                    handler.sendMessage(message);
                    return true;
                } else if (hostUpgradeDataBean.getVerno() == transform(App.versionName)) {
                    Lg.i(TAG, context.getString(R.string.updata_request_format_install_ok));
                    try {
                        //已经是最新的版本，清空下载包
                        FileUtils.cleanDirectory(new File(Constant.DOWNLOADPATH));
                    } catch (IOException e) {
                    }
                }
            }

            //删除本地保存的升级信息
            App.getCache().remove(Constant.HOST_UPGRADE_KEY);
            return false;
        }

        return false;

    }


    /**
     * 检查本地是否已经有适合的下载文件
     *
     * @param hostUpgradeDataBean
     * @return
     */
    private boolean checkLocalFile(HostUpgradeDataBean hostUpgradeDataBean) {
        File f = new File(upgradeApkPath);
        if (!f.exists())
            return false;
        String md5 = MD5FileUtil.getMD5(f);
        Lg.d(TAG, context.getString(R.string.updata_md5) + md5 + context.getString(R.string.updata_request_md5) + hostUpgradeDataBean.getFid());
        if (md5.equalsIgnoreCase(hostUpgradeDataBean.getFid())) {
            return true;
        } else {
            return false;
        }
    }


    private long transform(String verName) {
        try {
            long verno = 0;
            String[] s = verName.split("\\.");

            int val32 = Integer.valueOf(s[0]);
            verno <<= 8;
            verno += (val32 & 0xFF);

            val32 = Integer.valueOf(s[1]);
            verno <<= 16;
            verno += (val32 & 0xFFFF);

            val32 = Integer.valueOf(s[2]);
            verno <<= 16;
            verno += (val32 & 0xFFFF);

            val32 = Integer.valueOf(s[3]);
            verno <<= 24;
            verno += (val32 & 0xFFFFFF);
            return verno;
        } catch (Exception e) {
            return 0;
        }

    }

    private void requestVersionInfo(final int state) {

//        final String result = "{\"code\":200,\"msg\":\"success\",\"data\":{\"verno\":72057594121814020,\"intro\":\"新功能增加，旧弄能优化，修改了bug。sds\",\"vername\":\"高清影视 1.0.4.0 stable\",\"isforce\":0,\"url\":\"http://114.247.94.87:8080/play?fid=DA70E83F7FCFF0CED3D92A3590900C6E\",\"fid\":\"DA70E83F7FCFF0CED3D92A3590900C6E\"}}";

//        final String result1 = "{\"code\":200,\"msg\":\"success\",\"data\":{}}";

        HttpRequest.getInstance().excute("getUpgradeInfo", new Object[]{BaseUrl.utermUrl,
                new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {

                        if (TextUtils.isEmpty(response)) {
                            Lg.e(TAG, "getUpgradeInfo onSuccess --> response is null or empty !!!");
                            Message message = Message.obtain();
                            message.what = 102;
                            message.arg1 = state;
                            handler.sendMessage(message);
                            return;
                        }
                        Lg.d(TAG, "getUpgradeInfo onSuccess --> response : " + response);

                        HostUpgradeResultBean hostUpgradeResultBean;
                        try {
                            hostUpgradeResultBean = JSON.parseObject(response, HostUpgradeResultBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Lg.e(TAG, "getUpgradeInfo onSuccess --> response parse failed !!!");
                            Message message = Message.obtain();
                            message.what = 103;
                            message.arg1 = state;
                            handler.sendMessage(message);
                            return;
                        }

                        //已经是最新的版本
                        if (hostUpgradeResultBean.getCode() == ResponseCode.uterm_noupgrade_code) {
                            Message message = Message.obtain();
                            message.what = 201;
                            message.arg1 = state;
                            handler.sendMessage(message);
                            App.downloadManager.removeAllTask();   //暂时先全删了
                            try {
                                //已经是最新的版本，清空下载包
                                FileUtils.cleanDirectory(new File(Constant.DOWNLOADPATH));
                            } catch (IOException e) {
                            }
                            Lg.i(TAG, context.getString(R.string.updata_request_locality_format));
                            return;
                        } else if (hostUpgradeResultBean.getCode() == ResponseCode.uterm_success_code) {
                            HostUpgradeDataBean hostUpgradeDataBean = hostUpgradeResultBean.getData();
                            if (hostUpgradeDataBean == null || hostUpgradeDataBean.getVerno() == 0 ||
                                    (TextUtils.isEmpty(hostUpgradeDataBean.getFid()) && TextUtils.isEmpty(hostUpgradeDataBean.getUrl()))) {
                                Message message = Message.obtain();
                                message.what = 105;
                                message.arg1 = state;
                                handler.sendMessage(message);
                                Lg.e(TAG, context.getString(R.string.updata_request_url_null));
                                return;
                            } else {
                                Lg.i(TAG, context.getString(R.string.updata_install));

                                if (checkLocalCacheData(state, hostUpgradeDataBean)) {
                                    return;
                                }

                                Lg.i(TAG, context.getString(R.string.updata_request_format_nook));

                                App.getCache().put(Constant.HOST_UPGRADE_KEY, JSON.toJSONString(hostUpgradeDataBean));

                                //有可能只是更新了介绍信息，下载的文件就不必重新下载了，直接执行安装流程
                                if (checkLocalCacheData(state, hostUpgradeDataBean)) {
                                    return;
                                }

                                Message message = Message.obtain();
                                message.what = 202;
                                message.arg1 = state;
                                message.obj = hostUpgradeDataBean;
                                handler.sendMessage(message);
                                return;
                            }
                        } else {
                            Message message = Message.obtain();
                            message.what = 104;
                            message.arg1 = state;
                            handler.sendMessage(message);
                            return;
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Lg.e(TAG, "getUpgradeInfo onError , " + error == null ? "" : error);
                        Message message = Message.obtain();
                        message.what = 101;
                        message.arg1 = state;
                        handler.sendMessage(message);
                    }
                }});
    }

    /**
     * 上报下载升级文件失败的错误原因
     *
     * @param downloadInfo
     */
    private void reportDownloadFailError(DownloadInfo downloadInfo, int code) {
        Util.reportRequestDataError(downloadInfo.getUrl(),
                String.valueOf(getErrorCode(code)), getErrorDesc(code));
    }

    private int getErrorCode(int code) {
        switch (code) {
            case ErrorCode.unknown_error:
                return 700;
            case ErrorCode.breadpoint_error:
                return 701;
            case ErrorCode.internet_error:
                return 702;
            case ErrorCode.io_error:
                return 703;
            case ErrorCode.server_error:
                return 704;
            case 6:
                return 705;
        }
        return 700;
    }

    private String getErrorDesc(int code) {
        switch (code) {
            case ErrorCode.unknown_error:
                return context.getString(R.string.updata_on_problem);
            case ErrorCode.breadpoint_error:
                return context.getString(R.string.updata_request_breakpoint_error);
            case ErrorCode.internet_error:
                return context.getString(R.string.updata_network_error);
            case ErrorCode.io_error:
                return context.getString(R.string.updata_read_write_error);
            case ErrorCode.server_error:
                return context.getString(R.string.updata_server_error);
            case 6:
                return context.getString(R.string.updata_md5_error);
        }
        return context.getString(R.string.updata_on_problem);
    }

    public interface UpgradeListener {
        void onUpgrade(boolean hasUprade);
    }


    public interface HostDownloadCallback {
        void onProgress(DownloadInfo downloadInfo);

        void onFinish(DownloadInfo downloadInfo);

        void onError(DownloadInfo downloadInfo, int code);
    }

    private HostDownloadCallback hostDownloadCallback;

    public void setHostDownloadCallback(HostDownloadCallback hostDownloadCallback) {
        this.hostDownloadCallback = hostDownloadCallback;
    }

}
