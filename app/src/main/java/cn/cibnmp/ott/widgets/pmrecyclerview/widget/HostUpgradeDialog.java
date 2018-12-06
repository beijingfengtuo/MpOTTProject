package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.HostUpgradeDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.MD5FileUtil;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Util;
import cn.cibntv.downloadsdk.download.DownloadInfo;
import cn.cibntv.downloadsdk.download.ErrorCode;

/**
 * Created by yangwenwu on 2018/1/9.
 * 升级中弹出Dialog
 */

public class HostUpgradeDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "HostUpgradeDialog";
    private static final String upgradeApkPath = Constant.DOWNLOADPATH + "/host_upgrade.apk";
    private Activity activity;
    private HostUpgradeManager manager;
    private RoundedImageView cancel;
    private cn.cibnmp.ott.widgets.pmrecyclerview.widget.UpgradeGradientColorProgressView progressBar;
    private TextView verInfo, vername, proTV, errorText;
    private View forceLayout, optionLayout, downloadLayout, layout1, layout2, logo;
    private HostUpgradeDataBean dataBean;
    private Button ok, errorOk;
    private ImageView sure;
    private Bitmap bm;

    public HostUpgradeDialog(Context context) {
        super(context);
    }

    public HostUpgradeDialog(Activity context, int themeResId, HostUpgradeManager
            hostUpgradeManager) {
        super(context, themeResId);
        this.activity = context;
        this.manager = hostUpgradeManager;
    }

    public void updateData(HostUpgradeDataBean dataBean) {
        this.dataBean = dataBean;
        updateUI();
    }

    public Activity getActivity() {
        return activity;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (dataBean != null && dataBean.getIsforce() == HostUpgradeDataBean
                            .force_upgrade_type) {
                        ok.requestFocus();
                    } else {
                        sure.requestFocus();
                    }
                    break;
                case 101:
                    progressBar.setCurrentCount(msg.arg1);
                    proTV.setText(activity.getString(R.string.is_downloading) + msg.arg1 + "%");
                    break;
                case 102:
                    proTV.setText(activity.getString(R.string.the_download_is_complete));
                    if (isShowing() && downloadLayout.getVisibility() == View.VISIBLE) {
                        installApk(getContext());
                    }
                    if (dataBean.getIsforce() == HostUpgradeDataBean.force_upgrade_type)
                        showForceLayout();
                    else
                        showOptionLayout();
                    break;
                case 103:
                    if (isShowing() && downloadLayout.getVisibility() == View.VISIBLE) {
                        installApk(getContext());
                    }
                    if (dataBean.getIsforce() == HostUpgradeDataBean.force_upgrade_type)
                        showForceLayout();
                    else
                        showOptionLayout();
                    break;
                case 201:
                    if (isShowing()) {
                        showErrorLayout(msg.arg1);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private HostUpgradeManager.HostDownloadCallback downloadCallback = new HostUpgradeManager
            .HostDownloadCallback() {
        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            int pro = 0;
            if (downloadInfo.getTotalLength() == 0)
                pro = 0;
            else
                pro = (int) Math.round((double) downloadInfo.getDownloadLength() * 100 /
                        downloadInfo.getTotalLength());
            Message message = Message.obtain();
            message.what = 101;
            message.arg1 = pro;
            handler.sendMessage(message);
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Lg.d(TAG, " 文件下载完成！");
            handler.sendEmptyMessage(102);

        }

        @Override
        public void onError(DownloadInfo downloadInfo, int code) {
            Message message = Message.obtain();
            message.what = 201;
            message.arg1 = code;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(activity);
    }

    private void init(Context context) {

        setContentView(R.layout.host_upgrade_layout);

        getWindow().setWindowAnimations(R.style.UpgradeDialogStyle); //设置窗口弹出动画

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);

        optionLayout = findViewById(R.id.optionLayout);
        downloadLayout = findViewById(R.id.downloadLayout);
        forceLayout = findViewById(R.id.forceLayout);
        ok = (Button) findViewById(R.id.ok);
        logo = findViewById(R.id.logo);

        proTV = (TextView) findViewById(R.id.proTv);
        progressBar = (cn.cibnmp.ott.widgets.pmrecyclerview.widget.UpgradeGradientColorProgressView) findViewById(R.id.progressBar);
        progressBar.setSeekHeight(DisplayUtils.getValue(16));
        progressBar.setMaxCount(100f);
        progressBar.setCurrentCount(0);

        sure = (ImageView) findViewById(R.id.sure);
        cancel = (RoundedImageView) findViewById(R.id.cancel);
        errorOk = (Button) findViewById(R.id.errorOk);

        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        errorOk.setOnClickListener(this);

        vername = (TextView) findViewById(R.id.vername);
        verInfo = (TextView) findViewById(R.id.verinfo);
        errorText = (TextView) findViewById(R.id.errorText);
        verInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        verInfo.setScrollbarFadingEnabled(false);
        verInfo.setLineSpacing(DisplayUtils.getValue(17), 1.0f);

        if (dataBean != null) {
            updateUI();
        }

        manager.setHostDownloadCallback(downloadCallback);

    }

    private void updateUI() {
        if (layout1 == null || optionLayout == null || verInfo == null || vername == null)
            return;

        if (layout2.getVisibility() == View.VISIBLE) {
            showErrorLayout(Integer.MIN_VALUE);
        } else if (layout1.getVisibility() == View.VISIBLE) {
            if (downloadLayout.getVisibility() == View.VISIBLE) {
                showDownloadLayout();
            } else {
                if (dataBean != null && dataBean.getIsforce() == HostUpgradeDataBean
                        .force_upgrade_type) {
                    layout1.setVisibility(View.VISIBLE);
                    optionLayout.setVisibility(View.VISIBLE);
                } else {
                    layout1.setVisibility(View.VISIBLE);
                    optionLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        if (dataBean != null) {
            vername.setText(TextUtils.isEmpty(dataBean.getVername()) ?
                    activity.getString(R.string.updata_discover_versions) :
                    activity.getString(R.string.updata_discover_versions) + " " +
                            Util.getVersionName(dataBean.getVerno()) + " " +
                            activity.getString(R.string.be_coming));
            verInfo.setText(TextUtils.isEmpty(dataBean.getIntro()) ? activity.getString(R.string.no_introduction) : dataBean.getIntro());
        }
    }

    @Override
    public void show() {
        try {
            super.show();
            handler.sendEmptyMessageDelayed(100, 200);
        } catch (Throwable e) {
        }
    }

    private void installApk(Context context) {
        App.isForeground = false;
        if (!Util.installApk(context, upgradeApkPath)) {
            Util.appInstall(context, upgradeApkPath);
        }
    }

    @Override
    public void onBackPressed() {
        if (dataBean != null && dataBean.getIsforce() == HostUpgradeDataBean.force_upgrade_type)
            return;
        if (dataBean != null && downloadLayout.getVisibility() == View.VISIBLE)
            return;

        saveUserOption();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(sure)) {
            if (isDownloadSuccess()) {
                installApk(App.getInstance());
            } else {
                showDownloadLayout();
            }
        } else if (v.equals(cancel)) {
            if (dataBean.getIsforce() == 1) {
                System.exit(0);
            } else {
                saveUserOption();
                dismiss();
            }
        } else if (v.equals(errorOk)) {
            if (dataBean != null && dataBean.getIsforce() == HostUpgradeDataBean
                    .force_upgrade_type) {
                App.getAppManager().AppExit(App.getInstance());
            } else {
                dismiss();
            }
        } else {
            if (isDownloadSuccess()) {
                installApk(App.getInstance());
            } else {
                showDownloadLayout();
            }
        }
    }

    private void saveUserOption() {
        //精确到分钟的时间，秒给去掉！！！
        long t = System.currentTimeMillis() / 60000 * 60000;
        SharePrefUtils.saveLong(HostUpgradeManager.user_option_time, t);
        App.userOptionNoUpradeTime = t;
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            if (bm != null) {
                bm.recycle();
                bm = null;
            }
        } catch (Throwable e) {
        }
    }

    //点击升级的时候
    private void showDownloadLayout() {
        forceLayout.setVisibility(View.GONE);
        optionLayout.setVisibility(View.GONE);
        downloadLayout.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        verInfo.setVisibility(View.GONE);
        vername.setVisibility(View.GONE);
        logo.setVisibility(View.VISIBLE);
    }

    //下载的时候
    private void showOptionLayout() {
        forceLayout.setVisibility(View.GONE);
        optionLayout.setVisibility(View.VISIBLE);
        downloadLayout.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        verInfo.setVisibility(View.VISIBLE);
        vername.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        sure.requestFocus();
    }

    //点击升级后跳转（系统升级）在返回
    private void showForceLayout() {
        forceLayout.setVisibility(View.GONE);
        optionLayout.setVisibility(View.VISIBLE);
        downloadLayout.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        verInfo.setVisibility(View.VISIBLE);
        vername.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        ok.requestFocus();
    }

    private void showErrorLayout(int code) {
        layout1.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        verInfo.setVisibility(View.GONE);
        vername.setVisibility(View.GONE);
        downloadLayout.setVisibility(View.GONE);
        optionLayout.setVisibility(View.GONE);
        if (code == 6) {
            errorText.setText(activity.getString(R.string.updata_md5_error));
        } else if (code == ErrorCode.server_error) {
            errorText.setText(activity.getString(R.string.server_error_customer_service));
        } else if (code == ErrorCode.io_error) {
            errorText.setText(activity.getString(R.string.file_write_failed_customer_service));
        } else if (code == ErrorCode.internet_error) {
            errorText.setText(activity.getString(R.string.network_is_not_connected_network_first));
        } else if (code == ErrorCode.breadpoint_error || code == ErrorCode.unknown_error) {
            errorText.setText(activity.getString(R.string.download_file_exception_customer_service));
        } else {
            //errorText文本不改变
        }
        layout2.setVisibility(View.VISIBLE);
        layout2.post(new Runnable() {
            @Override
            public void run() {
                errorOk.requestFocus();
            }
        });
    }

    private boolean isDownloadSuccess() {
        File f = new File(upgradeApkPath);
        if (!f.exists()) {
            manager.reDownload(dataBean);
            return false;
        }

        String md5 = MD5FileUtil.getMD5(f);
        Lg.d(TAG, "下载文件MD5：" + md5 + " , 接口中md5：" + dataBean.getFid());
        if (md5.equalsIgnoreCase(dataBean.getFid())) {
            return true;
        } else {
            manager.reDownload(dataBean);
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dataBean.getIsforce() == 1) {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
