package cn.cibnhp.grand.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.BaseUrl;
import cn.cibnmp.ott.bean.UserInBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Utils;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cibn-lyc on 2018/1/10.
 */

public class MainLoginWenxin extends BaseActivity {
    private final String TAG = "MainLoginWenxin";
    private String weixinCode = "";
    private String get_url = "";
    // 获取第一步的code后，请求以下链接获取access_token
    public String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    public String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    private UserHttpClient userHttpClient;
    private String josn;
    private String nickname;
    private String headimgurl;
    private int start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_wenxin);

        userHttpClient = new UserHttpClient();
        start = 1;
    }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		int result = data.getExtras().getInt("result");
//		if (App.weixinCode == null&&start > 1&&result==2) {
//			finish();
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

    @Override
    public void onResume() {
        super.onResume();
        /*
         * resp是你保存在全局变量中的
		 */
        weixinCode = SharePrefUtils.getString("weixinCode", "");
        if (!"".equals(weixinCode)) {
//            if (App.access_token == null && App.tokenOpenid == null) {
                /*
                 * 将你前面得到的AppID、AppSecret、code，拼接成URL
                 */
                get_url = getCodeRequest(weixinCode);
                satrtThread(getTokenRun);
//            } else {
//                isExpireAccessToken(App.access_token, App.tokenOpenid);
//            }
        } else {
            if (start <= 1) {
                handler.sendEmptyMessageDelayed(START_INTENT, 50);
            } else {
                finish();
            }
        }
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }

    /**
     * 判断accesstoken是过期
     *
     * @param accessToken token
     * @param openid      授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        httpRequest(url, new ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                if (validateSuccess(response)) {
                    Log.i(TAG, "onSuccess: 没有过期，获取用户信息");
                    // accessToken没有过期，获取用户信息
                    get_url = getUserInfo(accessToken, openid);
                    satrtThread(getInfoRun);
                } else {
                    Log.i(TAG, "过期了，使用refresh_token来刷新accesstoken");
                    // 过期了，使用refresh_token来刷新accesstoken
                    refreshAccessToken();
                }
            }

            @Override
            public void onError(int errorCode, final String errorMsg) {
                Log.i(TAG, "isExpireAccessToken=== errorCode" + errorCode);
            }

            @Override
            public void onFailure(IOException e) {
            }
        });
    }

    /**
     * 刷新获取新的access_token
     */
    private void refreshAccessToken() {
        // 从本地获取以存储的refresh_token
        final String refreshToken = SharePrefUtils.getString("refresh_token", "");
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + Constant.WX_APP_ID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;
        // 请求执行
        httpRequest(url, new ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "refreshAccessToken ==== onSuccess: ");
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                handler.sendEmptyMessage(GET_ACCESS_TOKEN);
            }

            @Override
            public void onError(int errorCode, final String errorMsg) {
                Log.i(TAG, "refreshAccessToken ==== onError: " + errorCode);
                // 重新请求授权
                handler.sendEmptyMessageDelayed(START_INTENT, 50);
            }

            @Override
            public void onFailure(IOException e) {
                // 重新请求授权
                handler.sendEmptyMessageDelayed(START_INTENT, 50);
            }
        });
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code 授权时，微信回调给的
     * @return URL
     */
    public String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID", urlEnodeUTF8(Constant.WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET", urlEnodeUTF8(Constant.WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        Log.i("lyc", "getCodeRequest: " + result);
        return result;
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token 获取access_token时给的
     * @param openid       获取access_token时给的
     * @return URL
     */
    public String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        Log.i("lyc", "getUserInfo: " + result);
        return result;
    }

    public String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取access_token等等的信息(微信)
     */
    private void WXGetAccessToken(String url) {
        getHttpClient(url, GET_ACCESS_TOKEN);
    }

    /**
     * 获取微信用户个人信息
     *
     * @param get_user_info_url 调用URL
     */
    private void WXGetUserInfo(String get_user_info_url) {
        getHttpClient(get_user_info_url, GET_USER_INFO);
    }

    @SuppressWarnings("static-access")
    private void getHttpClient(String url, final int msg) {
        josn = userHttpClient.doGet(url);
        Lg.d("WENXIN", msg + "WENXIN----josn-------------------" + josn);
        if (josn != null) {
            handler.sendEmptyMessage(msg);
        } else {
            handler.sendEmptyMessage(GET_ONFAILURE);
        }
    }

    private final int START_INTENT = 6000;
    private final int ACTIVITY_CLOSE = 4444;
    private final int GET_ONFAILURE = 4000;
    private final int GET_ACCESS_TOKEN = 5555;
    private final int GET_USER_INFO = 6666;
    private final int USER_ONE = 1111;
    private final int USER_TWO = 2222;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject jsonObj;
                switch (msg.what) {
                    case GET_ACCESS_TOKEN:
                        jsonObj = JSONObject.parseObject(josn);
                        if (!jsonObj.containsKey("errcode")) {
                            App.access_token = (String) jsonObj.get("access_token");
                            String refresh_token = (String) jsonObj.get("refresh_token");
                            App.tokenOpenid = (String) jsonObj.get("openid");
                            SharePrefUtils.saveString("refresh_token", refresh_token);
                        }
                        if (App.access_token != null && App.tokenOpenid != null) {
                            get_url = getUserInfo(App.access_token, App.tokenOpenid);
                            satrtThread(getInfoRun);
                        } else {
                            handler.sendEmptyMessage(ACTIVITY_CLOSE);
                        }
                        break;
                    case GET_USER_INFO:
                        jsonObj = JSONObject.parseObject(josn);
                        if (!jsonObj.containsKey("errcode")) {
                            App.weixinOpenid = (String) jsonObj.get("openid");
                            App.weixinUnionid = (String) jsonObj.get("unionid");
                            nickname = (String) jsonObj.get("nickname");
                            headimgurl = (String) jsonObj.get("headimgurl");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("openid", App.weixinOpenid);
                            jsonObject.put("unionid", App.weixinUnionid);
                            jsonObject.put("nickname", nickname);
                            jsonObject.put("portraiturl", headimgurl);
                            String json = jsonObject.toJSONString();
                            userStart(json);
                        } else {
                            handler.sendEmptyMessage(ACTIVITY_CLOSE);
                        }
                        break;
                    case GET_ONFAILURE:
                        Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case ACTIVITY_CLOSE:
                        finish();
                        break;
                    case START_INTENT:
                        Intent intent = new Intent(getApplicationContext(), WXEntryActivity.class);
                        intent.putExtra(Constant.USER_SIGN_IN, Constant.USER_SIGN_IN);
                        startActivityForResult(intent, 1);
                        start = 2;
                        break;
                    case USER_ONE:
                        EventBus.getDefault().post(Constant.HOME_USER_SIGN_IN);
                        finish();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 第三方微信登陆
    private void userStart(String json) {
        HttpRequest.getInstance().excute("comcaMobileWxlogin", new Object[]{BaseUrl.utermUrl, json, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "json:------------>>>>>>" + response);
                App.mUserInBean = JSON.parseObject(response, UserInBean.class);
                if (App.mUserInBean != null) {
                    Utils.setUserOpenId(App.weixinOpenid);
                    Utils.setTokenId(App.access_token);
                }
                handler.sendEmptyMessage(USER_ONE);
            }

            @Override
            public void onError(String error) {
                Lg.e("getRequestThirdLogin", "cccccccccc:------------>>>>>>" + error);
                handler.sendEmptyMessage(GET_ONFAILURE);
            }
        }});
    }

    private void satrtThread(Runnable loadRun) {
        Thread thread = new Thread(loadRun);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Runnable getTokenRun = new Runnable() {
        @Override
        public void run() {
            WXGetAccessToken(get_url);
        }
    };
    public Runnable getInfoRun = new Runnable() {
        @Override
        public void run() {
            WXGetUserInfo(get_url);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && start <= 1) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.HOME_USER_SIGN_OUT)) {
            start = 0;
        }
    }

    private OkHttpClient mHttpClient = new OkHttpClient.Builder().build();
    private Handler mCallbackHandler = new Handler(Looper.getMainLooper());

    /**
     *  * 通过Okhttp与微信通信
     *  * * @param url 请求地址
     *  * @throws Exception
     *  
     */
    public void httpRequest(String url, final ApiCallback<String> callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mCallbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 请求失败，主线程回调
                            callback.onFailure(e);
                        }

                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    if (!response.isSuccessful()) {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 请求出错，主线程回调
                                callback.onError(response.code(), response.message());
                            }
                        });
                    } else {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // 请求成功，主线程返回请求结果
                                    callback.onSuccess(response.body().string());
                                } catch (final IOException e) {
                                    // 异常出错，主线程回调
                                    mCallbackHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onFailure(e);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    // Api通信回调接口
    public interface ApiCallback<T> {
        /**
         *  * 请求成功
         *  *
         *  * @param response 返回结果
         *  
         */
        void onSuccess(T response);

        /**
         *  * 请求出错
         *  *
         *  * @param errorCode 错误码
         *  * @param errorMsg 错误信息
         *  
         */
        void onError(int errorCode, String errorMsg);

        /**
         *  * 请求失败
         *  
         */
        void onFailure(IOException e);
    }
}
