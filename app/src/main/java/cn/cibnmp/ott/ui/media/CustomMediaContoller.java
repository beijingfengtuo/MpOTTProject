package cn.cibnmp.ott.ui.media;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import cn.cibnhp.grand.R;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 播放器管理类
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/30
 */
public class CustomMediaContoller implements IMediaController, View.OnClickListener {
    private static String TAG = CustomMediaContoller.class.getName();

    private static final int SET_VIEW_HIDE = 1;  //触摸屏幕时
    private static final int TIME_OUT = 5000;
    private static final int MESSAGE_SHOW_PROGRESS = 2; //显示当前播放时长
    private static final int PAUSE_IMAGE_HIDE = 3; //背景图片隐藏
    private static final int MESSAGE_SEEK_NEW_POSITION = 4; //手势滑动设置播放进度
    private static final int MESSAGE_HIDE_CONTOLL = 5; //隐藏进度条偏移量显示布局、音量设置布局、屏幕亮度显示布局
    private static final int MESSAGE_SEEK_POSITION = 6; //拖动进度条设置播放进度

    private Context context;

    //音频管理类
    private AudioManager audioManager;
    //本Layout的根布局，播放、播放进度条、全屏等按钮的布局(播放控制布局)
    private View view, itemView;
    //播放器控件
    private IjkVideoView videoView;
    //加载进度条、缓冲进度条
    private ProgressBar progressBar;
    //暂停、停止后显示的背景图片
    private ImageView pauseImage;
    //音量布局、亮度布局
    private RelativeLayout layoutSound, layoutBrightness;
    private SeekBar seekSound, seekBrightness;
    //播放进度、音量、亮度数值滑动变化时显示的偏移量
    private RelativeLayout rlSeekFast;
    private ImageView imgSeekFast;
    private TextView tvSeekContext;

    //播放控制布局
    private RelativeLayout layoutContoller;
    //播放器底部控制布局进度条
    private RelativeLayout layoutFootSeek;
    //返回按钮、播放按钮、底部播放按钮、音量按钮、全屏按钮
    private ImageView btnBack, btnPlay, btnButtonPlay, btnSound, btnFull;
    private SeekBar seekBar;
    //节目名称、当前播放进度、播放总时长
    private TextView tvTitleName, tvCurrent, tvDuration;

    //获取当前视屏截图
    private Bitmap bitmap;
    //播放总时长
    private long duration;
    //View的手势处理类
    private GestureDetector detector;
    //移动后的播放进度
    private long newPosition = -1;
    //获取音量最大值
    private int mMaxVolume;
    //获取当前播放的音量
    private int volume = -1;
    //获取当前屏幕亮度
    private float brightness = -1;

    private boolean isShowContoller;
    //播放控制布局是否显示 true 显示、false 隐藏（默认）
    private boolean isShow = false;
    //是否静音 true 静音、false 取消静音
    private boolean isSound = false;
    //是否拖拽进度条 true 是、false 否
    private boolean isDragging;

    //播放进度手势滑动偏移量
    private int progress;

    //TODO 是否播放结束,仅限本项目使用
    private boolean isStop = false;
    //TODO 是否缓冲结束,仅限本项目使用 true 缓冲结束、false 开始缓冲
    private boolean isBuffering = true;
    //当前屏幕方向
    private int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    //是否触摸结束
    private boolean isTouchStop = true;
    private int isTouchTime;

    //构造方法
    public CustomMediaContoller(Context context, View root) {
        this.context = context;
        this.view = root;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        isShow = false;
        isDragging = false;
        isShowContoller = true;
        initView();
        initAction();

    }

    /**
     * 初始化View
     */
    private void initView() {

        //播放器页面
        videoView = (IjkVideoView) view.findViewById(R.id.video_view);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_video_view_loading);
        pauseImage = (ImageView) view.findViewById(R.id.img_video_view_pause_bg);
        layoutSound = (RelativeLayout) view.findViewById(R.id.rl_video_view_sound);
        seekSound = (SeekBar) view.findViewById(R.id.sb_video_view_sound);
        layoutBrightness = (RelativeLayout) view.findViewById(R.id.rl_video_view_brightness);
        seekBrightness = (SeekBar) view.findViewById(R.id.sb_video_view_brightness);
        rlSeekFast = (RelativeLayout) view.findViewById(R.id.rl_video_view_seek_fast);
        imgSeekFast = (ImageView) view.findViewById(R.id.img_video_view_seek_fast_pic);
        tvSeekContext= (TextView) view.findViewById(R.id.tv_video_view_seek_context);

        //播放控制布局
        itemView = view.findViewById(R.id.vw_video_view_contoller);
        itemView.setVisibility(View.GONE);
        layoutContoller = (RelativeLayout) itemView.findViewById(R.id.ll_video_view_contoller_layout);
        layoutFootSeek = (RelativeLayout) itemView.findViewById(R.id.rl_video_view_contoller_foot_seek);
        btnBack = (ImageView) itemView.findViewById(R.id.img_video_view_contoller_title_back);
        btnPlay = (ImageView) itemView.findViewById(R.id.img_video_view_contoller_btn_play);
        btnButtonPlay = (ImageView) itemView.findViewById(R.id.img_video_view_contoller_bottom_btn_play);
        tvTitleName = (TextView) itemView.findViewById(R.id.tv_video_view_contoller_title_name);
        tvCurrent = (TextView) itemView.findViewById(R.id.tv_video_view_contoller_current);
        tvDuration = (TextView) itemView.findViewById(R.id.tv_video_view_contoller_duration);
        seekBar = (SeekBar) itemView.findViewById(R.id.sb_video_view_contoller_seekbar);
        btnSound = (ImageView) itemView.findViewById(R.id.img_video_view_contoller_sound);
        btnFull = (ImageView) itemView.findViewById(R.id.img_video_view_contoller_full);
    }

    /**
     * 设置节目名称
     */
    public void setVideoName(String videoName) {
        if (tvTitleName != null) {
            tvTitleName.setText(videoName);
        }
    }

    /**
     * 初始化监听
     */
    private void initAction() {

        btnPlay.setOnClickListener(this);
        btnButtonPlay.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnFull.setOnClickListener(this);
        //音量、亮度默认不可点击
        seekSound.setEnabled(false);
        seekBrightness.setEnabled(false);
        isSound = false;

        detector = new GestureDetector(context, new PlayGestureListener());
        mMaxVolume = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE))
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (detector.onTouchEvent(event)) {
                    return true;
                }

                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });

        layoutFootSeek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("custommedia", "event");
                isTouchStop = false;
                isTouchTime = videoView.getCurrentPosition();
                // 处理手势结束
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }

                Rect seekRect = new Rect();
                layoutFootSeek.getHitRect(seekRect);

                if ((event.getY() >= (seekRect.bottom - 80)) && (event.getY() <= (seekRect.bottom))) {

                    float y = event.getY();
                    //seekBar only accept relative x
                    float x = event.getX();
                    if (x < 0) {
                        x = 0;
                    } else if (x > seekRect.width()) {
                        x = seekRect.width();
                    }
                    MotionEvent me = MotionEvent.obtain(event.getDownTime(), event.getEventTime(),
                            event.getAction(), x, y, event.getMetaState());
                    return seekBar.onTouchEvent(me);
                }
                return false;
            }
        });

        /**
         * 播放器监听回调
         */
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                Log.e(TAG, "setOnInfoListener===========" + what);
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        isBuffering = false;
                        //开始缓冲
                        btnPlay.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        isBuffering = true;
                        //结束缓冲
                        progressBar.setVisibility(View.GONE);
                        break;

                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                        statusChange(STATUS_PLAYING);
                        progressBar.setVisibility(View.GONE);
                        break;

                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        /**
         * 播放进度条监听
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= seekBar.getMax()) {
                    CustomMediaContoller.this.progress = seekBar.getMax() - 1000;
                } else if (progress <= 0) {
                    CustomMediaContoller.this.progress = 0;
                } else {
                    CustomMediaContoller.this.progress = progress;
                }

                String string = generateTime(CustomMediaContoller.this.progress);
                tvCurrent.setText(string);

                if (!isTouchStop ) {
                    if (rlSeekFast.getVisibility() == View.GONE
                            && getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        btnPlay.setVisibility(View.GONE);
                        rlSeekFast.setVisibility(View.VISIBLE);
                    }

                    //设置快进快退显示
                    if (CustomMediaContoller.this.progress > isTouchTime) {
                        imgSeekFast.setImageResource(R.mipmap.img_home_three_video_fast);
                    } else {
                        imgSeekFast.setImageResource(R.mipmap.img_home_three_video_retreat);
                    }

                    String current = generateTime(CustomMediaContoller.this.progress);
                    //显示偏移量
                    tvSeekContext.setText(current);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (videoView != null) {
                    isTouchTime = videoView.getCurrentPosition();
                }
                isTouchStop = false;
                isDragging = true;
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouchStop = true;
                isDragging = false;
                if (rlSeekFast.getVisibility() == View.VISIBLE) {
                    rlSeekFast.setVisibility(View.GONE);
                }
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                handler.sendEmptyMessage(MESSAGE_SEEK_POSITION);

            }
        });
    }

    /**
     * 显示播放控制布局
     */
    public void showContollerView() {
        show();
    }

    /**
     * 是否显示播放控制布局
     *
     * @param isShowContoller true 隐藏、false 显示
     */
    public void setShowContoller(boolean isShowContoller) {
        this.isShowContoller = isShowContoller;
        handler.removeMessages(SET_VIEW_HIDE);
        itemView.setVisibility(View.GONE);
    }

    /**
     * 隐藏背景图片、播放器控制布局、显示加载进度条
     */
    public void hindContollerView() {
        pauseImage.setVisibility(View.GONE);
        itemView.setVisibility(View.GONE);
        btnPlay.setImageResource(R.mipmap.img_home_three_video_stop_da);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 开始播放
     */
    public void reStart() {
        if (videoView != null) {
            btnPlay.setImageResource(R.mipmap.img_home_three_video_stop_da);
            btnButtonPlay.setImageResource(R.mipmap.img_home_three_video_stop);
            videoView.start();
            if (bitmap != null) {
                handler.sendEmptyMessage(PAUSE_IMAGE_HIDE);
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (videoView != null && videoView.isPlaying()) {
            btnPlay.setImageResource(R.mipmap.img_home_three_video_play_da);
            btnButtonPlay.setImageResource(R.mipmap.img_home_three_video_play);
            videoView.pause();
            //获取当前视频截图
            bitmap = videoView.getBitmap();
            if (bitmap != null) {
                pauseImage.setImageBitmap(bitmap);
                pauseImage.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 播放结束
     */
    public void stop() {
        if (videoView != null) {
            isBuffering = true;
            hide();
            videoView.stopPlayback();
        }
    }

    /**
     * TODO 是否播放结束（仅限本项目使用）
     */
    public boolean isStop() {
        return isStop;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_video_view_contoller_btn_play:
                //播放按钮
                if (videoView != null && videoView.isPlaying()) {
                    pause();
                } else {
                    reStart();
                }
                break;

            case R.id.img_video_view_contoller_bottom_btn_play:
                //底部播放按钮
                if (videoView != null && videoView.isPlaying()) {
                    pause();
                } else {
                    reStart();
                }
                break;

            case R.id.img_video_view_contoller_sound:
                //静音按钮
                if (isSound) {
                    //静音
                    btnSound.setImageResource(R.mipmap.img_home_three_video_sound_mult);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                } else {
                    //取消静音
                    btnSound.setImageResource(R.mipmap.img_home_three_video_sound_open);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }
                isSound = !isSound;
                break;

            case R.id.img_video_view_contoller_full:
                //全屏切换按钮
                if (getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;

            case R.id.img_video_view_contoller_title_back:
                //头部返回按钮
                if (getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    /**
     * 横竖屏状态
     *
     * @param activity
     * @return
     */
    public int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    /**
     * 设置横竖屏切换后显示的View
     *
     * @param orientation
     */
    public void setScreenOrientationView(int orientation) {
        this.orientation = orientation;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        ((Activity) context).setRequestedOrientation(orientation);
        //横屏显示
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            btnPlay.setVisibility(View.GONE);
            tvTitleName.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
        } else {
            //竖屏显示
            btnPlay.setVisibility(View.VISIBLE);
            tvTitleName.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置播放进度条
     *
     * @return
     */
    private long setProgress() {
        long position = 0;
        long duration = 0;
        if (videoView != null) {
            position = videoView.getCurrentPosition();
            duration = videoView.getDuration();
            this.duration = duration;
            if (!generateTime(duration).equals(tvDuration.getText().toString())) {
                tvDuration.setText(generateTime(duration));
                //TODO
                seekBar.setMax((int) duration);
            }

            if (seekBar != null) {
                if (duration > 0) {
                    seekBar.setProgress((int) position);
                }

//TODO            //获取缓冲进度值
//TODO             int percent = videoView.getBufferPercentage();
//TODO             seekBar.setSecondaryProgress(percent);
            }
            tvCurrent.setText(generateTime(position));
        }

        return position;
    }

    /**
     * Handler处理
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_VIEW_HIDE:
                    //触摸item
                    if (isTouchStop) {
                        isShow = false;
                        itemView.setVisibility(View.GONE); //隐藏播放管理布局
                    }

                    break;

                case MESSAGE_SHOW_PROGRESS:
                    if (!isDragging && isShow) {
                        //显示播放进度条
                        setProgress();
//                        //TODO
//                        if (seekBar.getProgress() >= duration - 5000) {
//                            //如果当前是横屏
//                            if (getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                                if (videoView != null) {
//                                    pause();
//                                    isStop = true;
//                                    setScreenOrientationView(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                } else {
//                                    handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
//                                }
//                            }
//
//                        } else {
                            handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
//                        }
                    }
                    break;

                case PAUSE_IMAGE_HIDE:
                    //背景图片隐藏
                    pauseImage.setVisibility(View.GONE);
                    break;

                case MESSAGE_SEEK_NEW_POSITION:
                    if (videoView != null) {
                        //设置播放进度
                        if (newPosition >= 0) {
                            videoView.seekTo((int) (newPosition));
                            newPosition = -1;
                            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
                            show();
                        }
                    }
                    break;

                case MESSAGE_SEEK_POSITION:
                    if (videoView != null) {
                        //拖动进度条设置播放进度
                        if (progress >= 0) {
                            videoView.seekTo(progress);
                            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
                            show();
                        }
                    }
                    break;

                case MESSAGE_HIDE_CONTOLL:
                    //隐藏进度条偏移量显示布局、音量设置布局、屏幕亮度显示布局
                    rlSeekFast.setVisibility(View.GONE);
                    layoutSound.setVisibility(View.GONE);
                    layoutBrightness.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    public void hide() {
        if (isShow) {
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            isShow = false;
            handler.removeMessages(SET_VIEW_HIDE);
            itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {

    }

    @Override
    public void show(int timeout) {
        //隐藏播放管理布局
        handler.sendEmptyMessageDelayed(SET_VIEW_HIDE, timeout);
    }

    @Override
    public void show() {
        if (!isShowContoller) {
            return;
        }

        if (!isBuffering) {
            return;
        }

        isShow = true;
        //TODO 仅限本项目使用
        isStop = false;
        progressBar.setVisibility(View.GONE);
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            btnPlay.setVisibility(View.GONE);
        } else {
            btnPlay.setVisibility(View.VISIBLE);
        }
        itemView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        show(TIME_OUT);
    }

    @Override
    public void showOnce(View view) {

    }




    /**
     * View的触摸处理类
     *
     * @Description 描述：
     * @author zhangxiaoyang create at 2018/1/31
     */
    public class PlayGestureListener extends GestureDetector.SimpleOnGestureListener {

        //第一次触摸
        private boolean firstTouch;
        //移动方向 true 横向、false纵向
        private boolean moveDirection;
        //垂直方向滑动时，true 触摸左侧滑动、false 触摸右侧滑动
        private boolean volumeControl;


        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            handler.removeMessages(SET_VIEW_HIDE);
            //横屏下拦截事件
            if (getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                return true;
            }else {
                //TODO 间隔5秒后隐藏播放器控制布局
                show(TIME_OUT);
                return super.onDown(e);
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float x = e1.getX() - e2.getX();
            float y = e1.getY() - e2.getY();
            if (firstTouch) {
                //水平方向移动的距离是否大于垂直方向移动的距离
                moveDirection = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = e1.getX() < view.getMeasuredWidth() * 0.5;
                firstTouch = false;
            }

            //移动方向 true 横向、false纵向
            if (moveDirection) {
                onProgressSlide(x, -x / view.getWidth(), e1.getX() / view.getWidth());
            } else {
                //垂直方向滑动的比例
                float percent = y / view.getHeight();
                //垂直方向滑动时，true 触摸左侧控制亮度滑动、false 触摸右侧控制音量滑动
                if (volumeControl) {
                    onBrightnessSlide(percent);
                } else {
                    onVolumeSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 滑动改变播放进度
     *
     * @param x 手势滑动方向 大于0快退， 小于0快进
     * @param percent 移动比例
     * @param downPer 按下比例
     */
    private void onProgressSlide(float x, float percent,float downPer) {
        if (videoView != null) {
            if (rlSeekFast.getVisibility() == View.GONE
                    && getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                btnPlay.setVisibility(View.GONE);
                rlSeekFast.setVisibility(View.VISIBLE);
            }

            long position = videoView.getCurrentPosition(); //当前播放进度
            long duration = videoView.getDuration(); //播放总时长
            //TODO
            long seekDelta = (long) (percent * duration);

            newPosition = seekDelta + position; //实际移动的偏移量 + 原播放进度 = 移动后的播放进度
            //判断移动后的播放进度 是否大于 播放总时长
            if (newPosition > duration) {
                newPosition = duration - 1000;
            } else if (newPosition <= 0) {
                newPosition = 0;
            }

            //设置快进快退显示
            if (x > 0) {
                imgSeekFast.setImageResource(R.mipmap.img_home_three_video_retreat);
            } else {
                imgSeekFast.setImageResource(R.mipmap.img_home_three_video_fast);
            }

            String current = generateTime(newPosition);
            //显示偏移量
            tvSeekContext.setText(current);
        }
    }

    /**
     * 将long类型转换为时分秒类型 1200000000 ======》 00:15:12
     *
     * @param time
     * @return
     */
    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent 移动比例
     */
    private void onVolumeSlide(float percent) {
        if (layoutSound.getVisibility()==View.GONE) {
            btnPlay.setVisibility(View.GONE);
            layoutSound.setVisibility(View.VISIBLE);
        }

        if (volume == -1) {
            //获取当前媒体音量
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }
        //移动后的音量 = 移动偏移量 + 原有音量
        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }

        //修改音量
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 音量进度条（当前音量在进度条中所占比例）
        int i = (int) (index * 1.0f / mMaxVolume * 100);
        //设置音量图标背景
        if (i == 0) {
            btnSound.setImageResource(R.mipmap.img_home_three_video_sound_mult);
        } else {
            btnSound.setImageResource(R.mipmap.img_home_three_video_sound_open);
        }
        seekSound.setProgress(i);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent 移动比例
     */
    private void onBrightnessSlide(float percent) {
        if (layoutBrightness.getVisibility()==View.GONE) {
            btnPlay.setVisibility(View.GONE);
            layoutBrightness.setVisibility(View.VISIBLE);
        }

        if (brightness < 0) {
            //获取当前屏幕亮度
            brightness = ((Activity) context).getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }
//        Log.d(TAG, "brightness:" + brightness + ",percent:" + percent);
        WindowManager.LayoutParams lpa = ((Activity) context).getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }

        seekBrightness.setProgress((int) (lpa.screenBrightness * 100));
        ((Activity) context).getWindow().setAttributes(lpa);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        volume = -1;
        brightness = -1f;
        isTouchStop = true;
        //设置播放进度
        if (newPosition >= 0) {
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }

        //
        handler.removeMessages(MESSAGE_HIDE_CONTOLL);
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTOLL, 500);

        //TODO 间隔5秒后隐藏播放器控制布局
        show(TIME_OUT);

    }
}
