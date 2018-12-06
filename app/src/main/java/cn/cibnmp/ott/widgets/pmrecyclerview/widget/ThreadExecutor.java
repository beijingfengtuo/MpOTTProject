package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.cibnmp.ott.utils.Lg;


/**
 * Created by wanqi on 2017/5/17.
 * 线程执行者，所有的线程由此类来执行
 */

public class ThreadExecutor {

    private final static String TAG = "ThreadExecutor";

    private static final int MAX_THREAD_NUMBER = Integer.MAX_VALUE;

    private static final int CORE_THREAD_NUMBER = 0;

    private ExecutorService threadPool = new ThreadPoolExecutor(CORE_THREAD_NUMBER, MAX_THREAD_NUMBER,
            60 * 1000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    private ThreadExecutor() {
        if (threadPool == null)
            threadPool = new ThreadPoolExecutor(CORE_THREAD_NUMBER, MAX_THREAD_NUMBER,
                    60 * 1000L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
    }

    public void excute(Runnable runnable) {
        if (runnable != null)
            threadPool.submit(runnable);
        else
            Lg.e(TAG, "can't excute null runnable !!!");
    }

    public void close() {
        threadPool.shutdownNow();
    }


    public static ThreadExecutor getInstance() {
        return InnerClass.instance;
    }

    private static class InnerClass {
        private static ThreadExecutor instance = new ThreadExecutor();
    }
}
