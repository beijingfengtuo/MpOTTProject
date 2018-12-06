package cn.cibnmp.ott.ui.media.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.media.CustomMediaContoller;
import cn.cibnmp.ott.ui.media.IjkVideoView;
import cn.cibnmp.ott.utils.DisplayUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 播放器View
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/2/2
 */
public class VideoPlayView extends RelativeLayout {
    private static String TAG = VideoPlayView.class.getName();

    private Context mContext;
    //播放器管理类
    private CustomMediaContoller mediaController;
    //本Layout的根布局，播放、播放进度条、全屏等按钮的布局(播放控制布局)
    private View root, view;
    //播放器控件
    private IjkVideoView mVideoView;

    private Handler handler = new Handler();
    //是否是横屏
    private boolean portrait;
    //是否播放结束，只在横屏时判断（在最后5秒是结束播放）
    private boolean isStop;

    public VideoPlayView(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {
        root = LayoutInflater.from(mContext).inflate(R.layout.home_three_other_variety_video_view, this, true);
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        //播放控制布局
        view = root.findViewById(R.id.vw_video_view_contoller);
        mediaController = new CustomMediaContoller(mContext, root);
        mVideoView.setMediaController(mediaController);
    }

    /**
     * 设置节目名称
     */
    public void setVideoName(String videoName) {
        if (mediaController != null) {
            mediaController.setVideoName(videoName);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        /**
         * 播放结束监听
         */
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                //TODO zxy 2018.2.22
//                view.setVisibility(View.GONE);
//                //如果是横屏状态，改为竖屏状态
                if (mediaController.getScreenOrientation((Activity) mContext)
                        == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    //横屏播放完毕，重置
                    setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                //播放结束监听回调
                if (playerListener != null) {
                    playerListener.onCompletion(mp);
                }
            }
        });
    }

    /**
     * 显示播放控制布局
     */
    public void setContorllerVisiable(){
        mediaController.showContollerView();
    }

    /**
     * 是否显示播放控制布局
     *
     * @param isShowContoller true 隐藏、false 显示
     */
    public void setShowContoller(boolean isShowContoller) {
        mediaController.setShowContoller(isShowContoller);
    }

    /**
     * 设置横竖屏切换后显示的View
     *
     * @param orientation
     */
    public void setScreenOrientationView(int orientation) {
        mediaController.setScreenOrientationView(orientation);
    }

    /**
     * 是否播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    /**
     * 开始播放
     */
    public void start(){
        if (mVideoView.isPlaying()){
            mVideoView.start();
        }
    }

    /**
     * 开始播放
     *
     * @param path 播放地址
     */
    public void start(String path) {
        Uri uri = Uri.parse(path);
        //隐藏背景图片、播放器控制布局、显示加载进度条
        if (mediaController != null) {
            mediaController.hindContollerView();
        }

        if (!mVideoView.isPlaying()) {
//            mVideoView.setVideoURI(uri);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        } else {
            mVideoView.stopPlayback();
//            mVideoView.setVideoURI(uri);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
    }

    /**
     * 重新开始播放
     */
    public void reStart() {
        if (mVideoView != null) {
            if (mediaController != null) {
                mediaController.reStart();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mVideoView.isPlaying()) {
            if (mediaController != null) {
                mediaController.pause();
            }
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mVideoView != null) {
            if (mediaController != null) {
                mediaController.stop();
            }
        }
    }

    /**
     * TODO 是否播放结束（仅限本项目使用）
     */
    public boolean isStop() {
        if (mVideoView != null) {
            if (mediaController != null) {
                return mediaController.isStop();
            }
        }
        return false;
    }

    /**
     * 释放播放资源
     */
    public void release() {
        mVideoView.release(true);
    }

    /**
     * 获取当前截屏
     */
    public Bitmap getBitmap() {
        return mVideoView.getBitmap();
    }

    /**
     * 移除所有消息
     */
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
//        orientationEventListener.disable();
    }

    /**
     * 跳转播放进度
     *
     * @param progress 播放进度值
     */
    public void seekTo(int progress){
        mVideoView.seekTo(progress);
    }

    /**
     * 获取当前播放进度
     *
     * @return
     */
    public long getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    /**
     * 获取播放总时长
     *
     * @return
     */
    public long getDuration() {
        return mVideoView.getDuration();
    }

    /**
     * 获取当前播放状态
     *
     * @return
     */
    public int VideoStatus() {
        return mVideoView.getCurrentStatue();
    }

    /**
     * 获取当前横竖屏状态
     */
    public int getScreenOrientation() {
        if (mediaController == null) {
            return -1;
        }
        return mediaController.getScreenOrientation((Activity) mContext);
    }

    /**
     * 设置横竖屏切换
     *
     * @param configuration
     */
    public void onChanged(Configuration configuration) {
        portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
        doOnConfigurationChanged(portrait);
    }

    /**
     * 设置全屏
     *
     * @param portrait true 竖屏、false横屏
     */
    public void doOnConfigurationChanged(final boolean portrait) {
        if (mVideoView != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    if (portrait) {
                        params.height = DisplayUtils.getValue(440);
                        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        setLayoutParams(params);
                        requestLayout();
                    } else {
                        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        //   getWindow().addFlags(WindowManager.LayoutParams.NOT);
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.TITLE_CHANGED);
                        setLayoutParams(params);
                    }

                }
            });


            //TODO zxy 被替换
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    setFullScreen(!portrait);
//                    if (portrait) {
//                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                        layoutParams.height = DisplayUtils.getValue(440);
//                        Log.e("handler", "440");
//                        setLayoutParams(layoutParams);
//                        requestLayout();
//                    } else {
//                        int heightPixels = ((Activity) mContext).getResources().getDisplayMetrics().heightPixels;
//                        int widthPixels = ((Activity) mContext).getResources().getDisplayMetrics().widthPixels;
//                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                        layoutParams.height = heightPixels;
//                        layoutParams.width = widthPixels;
//                        Log.e("handler", "height==" + heightPixels + "\nwidth==" + widthPixels);
//                        setLayoutParams(layoutParams);
//                    }
//                }
//            });

//            orientationEventListener.enable();
        }
    }

    /**
     * 设置全屏切换
     *
     * @param fullScreen
     */
    private void setFullScreen(boolean fullScreen) {
        if (mContext != null && mContext instanceof Activity) {
            WindowManager.LayoutParams attrs = ((Activity) mContext).getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                ((Activity) mContext).getWindow().setAttributes(attrs);
                ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity) mContext).getWindow().setAttributes(attrs);
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }

    }

    /**
     * 日志上报
     *
     * @param vid
     * @param sid
     * @param vname
     * @param videoKind
     * @param videoType
     * @param category
     * @param chId
     * @param fid
     * @param youkuVideoCode
     * @param playType
     */
    public void setVideoDisc(long vid, long sid, String vname, int videoKind, String videoType, int category, String chId, String fid, String youkuVideoCode, int playType) {
        mVideoView.setVideoDisc(vid, sid, vname, videoKind, videoType, category, chId, fid, youkuVideoCode, playType);
    }

    /**
     * 开启播放日志上报
     */
    public void getLogCatch() {
        if (mVideoView != null) {
            mVideoView.getLogCatch();
        }
    }

    /**
     * 关闭播放日志上报
     */
    public void getStopCatch() {
        if (mVideoView != null) {
            mVideoView.getStopCatch();
        }
    }

    /*=====================================================================*/
    /**
     * 播放器监听回调
     */
    private PlayerListener playerListener;

    /**
     * 设置播放器监听
     *
     * @param playerListener
     */
    public void setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    /**
     * 播放器监听回调
     */
    public interface PlayerListener {
        //播放结束监听回调
        void onCompletion(IMediaPlayer mp);
    }

}
