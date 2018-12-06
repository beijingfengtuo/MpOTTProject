package cn.cibnmp.ott.config;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;

/**
 * Created by yanjing on 2017/2/24.
 */

public class MyJurisdictionUtils {

    public static MyJurisdictionUtils myJurisdictionUtils = null;

    public final int REQUEST_CODE_ASK_PERMISSIONS = 54007;
    //危险权限（运行时权限）
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static MyJurisdictionUtils myJurisdictionUtils() {
        if (myJurisdictionUtils == null) {
            myJurisdictionUtils = new MyJurisdictionUtils();
        }
        return myJurisdictionUtils;
    }

    public boolean isJurisdiction(Activity activity) {
        if (isGrantedAllPermission(activity)) {
//            Toast.makeText(this, "所有权限已开启", Toast.LENGTH_LONG).show();
            return true;
        } else {
            //使用本应用前请打开应用权限
//                Toast.makeText(activity, "Use this application need to open the application rights", Toast.LENGTH_LONG).show();
            insertDummyContactWrapper(activity);
            return false;
        }
    }

    private void insertDummyContactWrapper(final Activity activity) {
        for (int i = 0; i < PERMISSIONS.length; i++) {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity, PERMISSIONS[i]);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(activity, "所有权限已开启" + hasWriteContactsPermission, Toast.LENGTH_LONG).show();
                Lg.e("所有权限已开启", "" + ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[i]));
                if (!SharePrefUtils.getBoolean("AppShow", false)) {
                    SharePrefUtils.saveBoolean("AppShow", true);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSIONS[i])) {
                        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
                        break;
                    }
                } else {
                    showMessageOKCancel(activity);
                    break;
                }
            }
        }
    }

    /**
     * 所有权限是否都已授权
     *
     * @return
     */
    public boolean isGrantedAllPermission(Context context) {
        List<String> needRequestPermissonList = getDeniedPermissions(context, PERMISSIONS);
        return needRequestPermissonList.size() == 0;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(permission);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 所有权限是否都已授权
     *
     * @return
     */
    private boolean isGrantedAllPermission(Activity activity) {
        List<String> needRequestPermissonList = getDeniedPermissions(activity, PERMISSIONS);
        return needRequestPermissonList.size() == 0;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(Activity activity, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                needRequestPermissonList.add(permission);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private Dialog dialog = null;

    public void showMessageOKCancel(final Activity activity) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(activity)
                    .setTitle("使用此应用程序需要打开应用程序权限")
//                    .setMessage("Go set:Apps->OCTV->Permissions open")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings(activity);
                            App.getAppManager().AppExit(App.getInstance());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getAppManager().AppExit(App.getInstance());
                        }
                    }).create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    startAppSettings(activity);
                    App.getAppManager().AppExit(App.getInstance());
                }
            });
        }
        dialog.show();
    }

}
