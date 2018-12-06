package cn.cibnmp.ott.jni;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by wanqi on 2017/5/17.
 */

public class HttpRequest {

    private static final String TAG = "HttpRequest";

    private static HttpRequest instance;

    private HttpRequest() {
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    /**
     * @param name   要调用的方法名称
     * @param params 参数
     */
    public void excute(String name, Object... params) {
        if (TextUtils.isEmpty(name)) {
            Log.e(TAG,
                    "start httpRequest failed , httpRequest method name can't be null!!!");
            return;
        }

        HttpRequestExecutor.getInstance().excute(new Task(name, params));
    }

    class Task implements Runnable {

        private String name;
        private Object[] params;

        public Task(String name, Object[] params) {
            this.name = name;
            this.params = params;
        }

        @Override
        public void run() {
            try {
                Class cl = JNIRequest.class;
                Method m;
                if (params == null) {
                    m = cl.getDeclaredMethod(name);
                    Log.d(TAG, "call JNIRequest method " + name);
                    m.setAccessible(true);
                    m.invoke(JNIRequest.getInstance());
                } else {
                    Class[] cls = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] instanceof HttpResponseListener) {
                            cls[i] = HttpResponseListener.class;
                        } else if (params[i] instanceof SimpleHttpResponseListener) {
                            cls[i] = SimpleHttpResponseListener.class;
                        } else {
                            cls[i] = params[i].getClass();
                        }
                    }
                    m = cl.getDeclaredMethod(name, cls);
                    Log.d(TAG, "call JNIRequest method " + name);
                    m.setAccessible(true);
                    m.invoke(JNIRequest.getInstance(), params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
