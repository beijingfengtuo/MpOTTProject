package cn.cibnmp.ott.ui.detail.player.widgets;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.player.PlayerLogCatchTools;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.detail.LiveDetailActivity;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailChildBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;
import cn.cibnmp.ott.utils.BrightUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibntv.ott.lib.ijk.NVideoView;
import de.greenrobot.event.EventBus;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 播放器页面
 *
 * @author yanjing
 */
public class LivePlayerFrag extends BasePlayerFrag implements OnClickListener {

    private View contextView;
    public long times;

    private int detail_type = 0; // 0电影 1电视剧 2综艺
    private String action = "open_movie_detail_page";
    private DetailBean data;
    // private ShareDialog shareDialog;

    private NVideoView videoView;

    private ProgressBar pb;
    private SeekBar sb, sound_sb, bright_sb;

    private RelativeLayout top_lay, botm_lay, wifi_lay, seek_lay, sound_lay, bright_lay, pre_lay, pay_big_layout, bottom_payL, bottom_payR;
    private TextView tv_title, tv_now, tv_total, tv_no_url, seek_tv, txt_buy, txt_rantitle, video_wait_msg_tv;
    private Button btn_play, btn_full, btn_back, btn_pre_back, btn_wifi, btn_lock, btn_share, pay_big_buy, pay_big_login, pay_small_buy, pay_small_login, video_yuyue_btn,btn_pre_back1,btn_pre_back2,video_wait_time_back,video_buy_btn,pay_small_login1 ;
    private ImageView seek_img;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    private String fid = "";

    private MHandler handler;

    private int pgress = 0; // 进度条当前进度
    private int current, duration;


    private Timer timer;
    private TextView tvLoading;

    private PlayerLogCatchTools LgTools;


    private long cctime; // log上报的一个标志时间，无逻辑作用
    private long cctime2; // log上报的一个标志时间，无逻辑作用

    private LiveDetailActivity activity;
    //TODO zxy
    private LinearLayout llPlayLoading;

    private DetailInfoBean infoBean;


    private long endTime;
    private long startTime;
    private RelativeLayout linearLayout1, linearLayout3;
    private Button live_btn1;
    //是否可以直播的开关，0为未开始，1为正在直播，2为已结束，3为回看， 5为数据异常
    private int status = 0;


    public static LivePlayerFrag newInstance(int type, long vid) {
        LivePlayerFrag fragment = new LivePlayerFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putLong(VID, vid);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String epgId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.player_live_frag_layout, container, false);
        if (getActivity() != null && getActivity() instanceof LiveDetailActivity) {
            activity = (LiveDetailActivity) getActivity();
            epgId = activity.epgIdParam;
        }


        Bundle bundle = getArguments();
        if (bundle != null) {
            detail_type = bundle.getInt(TYPE);
            vid = bundle.getLong(VID);
        }
        detail_type = 4;
        action = "open_live_detail_page";

        handler = new MHandler();

        able4G = SharePrefUtils.getBoolean("SETTING_ITEM_RL3", false);

        initPlayer();
        initView();

        initOther();

        initLgCatch();

        startTimer();

        return contextView;
    }

    private int width;
    private int height;
    WindowManager wm;
    AudioManager mAudioManager;
    float sound, bright;
    int maxSound;


    private void initOther() {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        mAudioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);

        ContentResolver cr = getActivity().getContentResolver();
        boolean isAutoBrightness = BrightUtils.isAutoBrightness(cr);
        if (isAutoBrightness) {
            BrightUtils.stopAutoBrightness(getActivity());
        }

        sound = BrightUtils.getCurrentVolume(mAudioManager);
        bright = BrightUtils.getBright(getActivity());
        maxSound = BrightUtils.getMaxVolume(mAudioManager);
        sound_sb.setMax(maxSound);
//        Lg.e("shenfei", "maxSound" + maxSound);

        sound_sb.setProgress((int) sound);
        bright_sb.setProgress((int) bright);

    }

    @Override
    public void onDestroyView() {
        if (videoView != null) {
            LgTools.setPlayerCurrentPosition(videoView.getCurrentPosition());
        }
        // addRecord();
        handler.removeMessages(107);
        addRecord();
        stopTimer();
        stop();
        release();
        LgTools.stopCatch();
        super.onDestroyView();
    }


    private void initView() {
        pb = (ProgressBar) contextView.findViewById(R.id.video_pb);
        tvLoading = (TextView) contextView.findViewById(R.id.tv_loading);

        sb = (SeekBar) contextView.findViewById(R.id.video_seek);
        sound_sb = (SeekBar) contextView.findViewById(R.id.video_voice_change_sb);
        bright_sb = (SeekBar) contextView.findViewById(R.id.video_bright_change_sb);

        top_lay = (RelativeLayout) contextView.findViewById(R.id.video_top_lay);
        botm_lay = (RelativeLayout) contextView.findViewById(R.id.video_bottom_lay);
        wifi_lay = (RelativeLayout) contextView.findViewById(R.id.video_wifi_lay);
        seek_lay = (RelativeLayout) contextView.findViewById(R.id.video_seek_lay);
        sound_lay = (RelativeLayout) contextView.findViewById(R.id.video_voice_change_lay);
        bright_lay = (RelativeLayout) contextView.findViewById(R.id.video_bright_change_lay);
        pre_lay = (RelativeLayout) contextView.findViewById(R.id.video_pre_lay);

        pay_big_layout = (RelativeLayout) contextView.findViewById(R.id.pay_big_layout);
    //    bottom_payL = (RelativeLayout) contextView.findViewById(R.id.bottom_payL);
        bottom_payR = (RelativeLayout) contextView.findViewById(R.id.bottom_payR);

        seek_img = (ImageView) contextView.findViewById(R.id.video_seek_img);

        tv_title = (TextView) contextView.findViewById(R.id.video_title);
        tv_now = (TextView) contextView.findViewById(R.id.video_nowtime);
        tv_total = (TextView) contextView.findViewById(R.id.video_totaltime);
        tv_no_url = (TextView) contextView.findViewById(R.id.video_no_url);
        txt_buy = (TextView) contextView.findViewById(R.id.txt_buy);
        seek_tv = (TextView) contextView.findViewById(R.id.video_seek_tv);

        btn_play = (Button) contextView.findViewById(R.id.video_btn_play);
        btn_full = (Button) contextView.findViewById(R.id.video_btn_full);
        btn_back = (Button) contextView.findViewById(R.id.video_back);
        btn_pre_back = (Button) contextView.findViewById(R.id.video_pre_back);
       btn_pre_back2 = (Button) contextView.findViewById(R.id.video_pre_back2);
        btn_pre_back1 = (Button) contextView.findViewById(R.id.video_pre_back1);
        btn_wifi = (Button) contextView.findViewById(R.id.video_wifi_lay_btn);
        btn_lock = (Button) contextView.findViewById(R.id.video_screen_lock);
        btn_share = (Button) contextView.findViewById(R.id.video_share);
        pay_big_buy = (Button) contextView.findViewById(R.id.pay_big_buy);
        pay_big_login = (Button) contextView.findViewById(R.id.pay_big_login);
        video_yuyue_btn = (Button) contextView.findViewById(R.id.video_yuyue_btn);

        pay_small_buy = (Button) contextView.findViewById(R.id.pay_small_buy);
        pay_small_login = (Button) contextView.findViewById(R.id.pay_small_login);

        llPlayLoading = (LinearLayout) contextView.findViewById(R.id.ll_play_loading);
        txt_rantitle = (TextView) contextView.findViewById(R.id.txt_rantitle);
        video_wait_msg_tv = (TextView) contextView.findViewById(R.id.video_wait_msg_tv);
        linearLayout1 = (RelativeLayout) contextView.findViewById(R.id.linearLayout1);

        video_wait_time_back= (Button) contextView.findViewById(R.id.video_wait_time_back);
        live_btn1 = (Button) contextView.findViewById(R.id.live_btn1);
        video_buy_btn= (Button) contextView.findViewById(R.id.video_buy_btn);
        linearLayout3 = (RelativeLayout) contextView.findViewById(R.id.video_wait_time_lay);
        pay_small_login1 = (Button) contextView.findViewById(R.id.pay_small_login1);
//        if (App.isLogin && App.userId > 0) {
//            pay_big_login.setVisibility(View.GONE);
//            pay_small_login.setVisibility(View.GONE);
//        }

        btn_play.setOnClickListener(this);
        btn_full.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_pre_back.setOnClickListener(this);
        btn_wifi.setOnClickListener(this);
        btn_lock.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        pay_big_login.setOnClickListener(this);
        pay_big_buy.setOnClickListener(this);
        pay_small_buy.setOnClickListener(this);
        pay_small_login.setOnClickListener(this);
        video_yuyue_btn.setOnClickListener(this);
        btn_pre_back1.setOnClickListener(this);
        btn_pre_back2.setOnClickListener(this);
        video_wait_time_back.setOnClickListener(this);
        video_buy_btn.setOnClickListener(this);
        pay_small_login1.setOnClickListener(this);

        videoView.setOnTouchListener(new OnTouchListener() {

            int downX = 0; // 按下位置记录
            int downY = 0;
            int disX = 0;  // 移动的偏移大小
            int disY = 0;
            int dtX, dtY;  // 每次移动的变化量
            int isXmove = 0; // 是否是水平方向的位移 1表示处在水平位移中,2表示处在竖直位移中
            int mmX, mmY;  // 移动时候记录上次移动的位置

            float ds;  // 当前播放时间占总时间百分比

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                return false;
                if (pay_big_layout.getVisibility() == View.VISIBLE) {
                    return false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        try {
                            sound = BrightUtils.getCurrentVolume(mAudioManager);
                        } catch (Exception e) {
                        }

                        if (isLayPop) {
                            handler.sendEmptyMessage(101);
                        } else {
                            handler.sendEmptyMessage(102);
                        }

                        downX = (int) event.getX();  // 记录按下时候的位置
                        downY = (int) event.getY();

                        if (videoView != null && isPrepare) {
                            ds = (float) videoView.getCurrentPosition() / (float) videoView.getDuration();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getX();
                        int moveY = (int) event.getY();
                        disX = moveX - downX;
                        disY = moveY - downY;

                        if (mmX == 0) {
                            mmX = moveX;
                        }

                        int gX = moveX - mmX;

                        if (isXmove == 0) {
                            if (Math.abs(disY) > Math.abs(disX) + 50) {
                                isXmove = 2;
                                Lg.e("shenfei", "isXmove = 2");
                            } else if (Math.abs(disX) > Math.abs(disY) + 50) {
                                isXmove = 1;
                                Lg.e("shenfei", "isXmove = 1");
                            }
                        }

                        if (isXmove == 2) { // 竖直位移
                            if (moveY - mmY > 0) { // 向下移动
                                if (downX > 0 && downX < (wm.getDefaultDisplay().getWidth() / 2)) { // 亮度调节
                                    bright = bright - 2;
                                    if (bright < 0) bright = 0;
                                    BrightUtils.changeBright(getActivity(), (int) bright);
                                    bright_sb.setProgress((int) bright);
                                    if (bright_lay.getVisibility() == View.GONE) {
                                        if (sound_lay.getVisibility() == View.VISIBLE) {
                                            sound_lay.setVisibility(View.GONE);
                                        }
                                        bright_lay.setVisibility(View.VISIBLE);
                                    }
                                } else {                                // 音量调节
                                    sound -= 0.1;
                                    if (sound < 0) sound = 0;
                                    BrightUtils.changeVolume(mAudioManager, (int) sound);
                                    sound_sb.setProgress((int) sound);
                                    if (sound_lay.getVisibility() == View.GONE) {
                                        if (bright_lay.getVisibility() == View.VISIBLE) {
                                            bright_lay.setVisibility(View.GONE);
                                        }
                                        sound_lay.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else if (moveY - mmY < 0) {        // 向上移动
                                if (downX > 0 && downX < (wm.getDefaultDisplay().getWidth() / 2)) { // 亮度调节
                                    bright = bright + 2;
                                    if (bright > 255) bright = 255;
                                    BrightUtils.changeBright(getActivity(), (int) bright);
                                    bright_sb.setProgress((int) bright);
                                    if (bright_lay.getVisibility() == View.GONE) {
                                        if (sound_lay.getVisibility() == View.VISIBLE) {
                                            sound_lay.setVisibility(View.GONE);
                                        }
                                        bright_lay.setVisibility(View.VISIBLE);
                                    }
                                } else {                                // 音量调节
                                    sound += 0.1;
                                    if (sound > maxSound) sound = maxSound;
                                    BrightUtils.changeVolume(mAudioManager, (int) sound);
                                    sound_sb.setProgress((int) sound);
                                    if (sound_lay.getVisibility() == View.GONE) {
                                        if (bright_lay.getVisibility() == View.VISIBLE) {
                                            bright_lay.setVisibility(View.GONE);
                                        }
                                        sound_lay.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else if (isXmove == 1) {  // 水平位移
                        if(activity.isPlay==3) {
                            //直播需要屏蔽快进快退
                            if (status == 3) {
                                if (gX > 2) { // 向右移动
                                    if (videoView != null && isPrepare) {
                                        if (seek_lay.getVisibility() == View.GONE) {
                                            seek_lay.setVisibility(View.VISIBLE);
                                            seek_img.setImageResource(R.drawable.forward);
                                        } else {
                                            seek_img.setImageResource(R.drawable.forward);
                                        }
                                        ds += 0.005;
                                        if (ds > 1) ds = 1f;
                                        seek_tv.setText(TimeUtils.getCurTime((int) (videoView.getDuration() * ds)));
                                        mmX = moveX;
                                    }
                                } else if (gX < -2) {        // 向左移动
                                    if (videoView != null && isPrepare) {
                                        if (seek_lay.getVisibility() == View.GONE) {
                                            seek_lay.setVisibility(View.VISIBLE);
                                            seek_img.setImageResource(R.drawable.backward);
                                        } else {
                                            seek_img.setImageResource(R.drawable.backward);
                                        }
                                        ds -= 0.005;
                                        if (ds < 0) ds = 0f;
                                        seek_tv.setText(TimeUtils.getCurTime((int) (videoView.getDuration() * ds)));
                                        mmX = moveX;
                                    }
                                }
                            }
                        }
                        }

                        mmY = moveY;

                        break;
                    case MotionEvent.ACTION_UP:

                        if (isXmove == 1) {
                            if (activity.isPlay == 3) {
                                if (videoView != null && isPrepare) {
                                    seekTo((int) (videoView.getDuration() * ds));
                                }
                            }

                            popLay();
                            //   }
                        }

                        isXmove = 0;

                        handler.sendEmptyMessageDelayed(110, 1000);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        top_lay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        botm_lay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Lg.i("NvideoView", "pgress1:" + pgress);
                if (videoView != null)
                    seekTo((int) ((float) pgress / 1000f * videoView.getDuration()));
                Lg.i("NvideoView", "pgress3:" + pgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pgress = progress;
                Lg.i("NvideoView", "pgress2:" + pgress);
            }
        });
        if(App.isLogin&& App.userId>0){
            //   pay_big_login.setVisibility(View.GONE);
            //  pay_small_login.setVisibility(View.GONE);
            pay_small_login.setVisibility(View.GONE);
            pay_big_login.setVisibility(View.GONE);
            pay_big_login.setText(" 切换账号 ");
            pay_small_login1.setText(" 切换账号 ");
        }

        popLay();
    }

    private void initPlayer() {
        videoView = (NVideoView) contextView.findViewById(R.id.video_view);
        videoView.initVideoView(NVideoView.PV_PLAYER__IjkMediaPlayerC);
        handler.sendEmptyMessageDelayed(900,500);


        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onPrepared(IMediaPlayer arg0) {
                isPrepare = true;

                // 日志采集
                if (LgTools != null) {
                    LgTools.setStartBufferTime();
                    LgTools.setPlayerDuration(videoView.getDuration());
                    LgTools.addPlayerPlayErNum(0);
                }
                pb.setVisibility(View.GONE);
                tvLoading.setVisibility(View.GONE);

                if (wifi_lay != null && wifi_lay.getVisibility() == View.VISIBLE) {
                    // 在提示wifi状态下 不允许播放
                    pause();
                } else {
                    start();
                }

                //判断是否试看
                if (payBtn) {
                    if (endTime > 1)
                        txt_buy.setText("试看" + (int) (endTime / 60000) + "分钟");
                    else
                        txt_buy.setText("试看0分钟");
                    hiddentLay();
        //            bottom_payL.setVisibility(View.VISIBLE);
                    if(endTime>0)
                    bottom_payR.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(1005, 3000);
                }

//                Log.i("ZXY", "上次播放记录：" + JSON.toJSON(data.getVideoRecord()));
                //   if (data != null && data.getEpisodelist() != null && need2seek && data.getVideoRecord() != null && data.getVideoRecord().getCurrentPos() > 0) {
                //     if ((data.getVideoRecord().getSid() + 1) >= data.getEpisodelist().size() && data.getVideoRecord().getCurrentPos() >= videoView.getDuration() - 5 * 1000) {
                if (!context.isDestroyed()) {
//                            ToastUtils.show(context, "上次已经观看结束，将为您从头播放");
                }
//                if (activity != null && need2seek && activity.record_current_pos >= videoView.getDuration() - 5 * 1000) {
//
//                } else {
//                    if(activity.need_Playcurrent_pos){
//                        need2seek = false;
//                        current = (int) activity.record_current_pos;
//                        seekTo(current);
//                        activity.record_current_pos = 0;
//                        activity.need_Playcurrent_pos = false;
//                    }
//                    //   seekTo((int) activity.record_current_pos);
//
//
//                    // 日志采集
//                    //   LgTools.setPlayerStartSeek(data.getVideoRecord().getCurrentPos());
//                    LgTools.realAfterSeek(activity.record_current_pos);
//                }
//
//
//                handler.sendEmptyMessage(103);
            }
        });

        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(IMediaPlayer arg0, int what, int arg2) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        pb.setVisibility(View.VISIBLE);
                        tvLoading.setVisibility(View.VISIBLE);
                        // 日志采集
                        long c2 = System.currentTimeMillis();
                        if ((c2 - cctime2) > 100) {
                            //  LgTools.playerPauseNum_start_B();
                            //  LgTools.playerPauseNum_start_A();
                            LgTools.addPlayerBuffNum_start_B();
                            LgTools.addPlayerBuffNum_start_A();
                            cctime2 = c2;
                        }

                        tvLoading.setVisibility(View.VISIBLE);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        pb.setVisibility(View.GONE);
                        tvLoading.setVisibility(View.GONE);

                        // 日志采集
                        long c = System.currentTimeMillis();
                        if ((c - cctime) > 100) {
                            // LgTools.playerPauseTime_end_B();
                            LgTools.playerLoadingTime_end_B();
                            LgTools.playerLoadingTime_end_A();
                            // LgTools.playerPauseTime_end_A();
                            cctime = c;
                        }
                        tvLoading.setVisibility(View.GONE);
                        break;
                    case 3:
                        if (linearLayout1 != null && linearLayout1.getVisibility() != View.GONE) {
                            linearLayout1.setVisibility(View.GONE);
                        }
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        videoView.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {


            @Override
            public void onBufferingUpdate(IMediaPlayer arg0, int arg1) {
                buf_time = System.currentTimeMillis();
                Lg.i("AXLvideoView", arg0.getDuration() + "...." + arg0.getCurrentPosition() + "..." + arg1);
                if (payBtn) {
                    // if(arg0.getDuration())
                    if (times < startTime)
                        seekTo((int) startTime);
                    else if (times >= endTime) {
                        pause();
                        //处理付费
                        toastPayLayout();
                    }

                }
            }
        });

        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(IMediaPlayer arg0) {
                // changePlayData(url);
                Lg.i("playFragment", "onComplist");
                handler.sendEmptyMessage(1091);
                if (infoBean != null && infoBean != null && infoBean.getChild().size() > 2) {
                    for (int i = 0; i < infoBean.getChild().size(); i++) {
                        if (sid == infoBean.getChild().get(i).getSid()) {
                            if (i + 1 < infoBean.getChild().size()) {
                                sid = (int) (infoBean.getChild().get(i + 1).getSid());
                                SidEvent se = new SidEvent();
                                se.setSid(sid);
                                EventBus.getDefault().post(se);
                                break;
                            }
                        }
                    }
                } else {
                    if (screen_o == 1) {
                        setScreenOrientation();
                    }


                }
            }
        });

    }

    private void toastPayLayout() {
        if (payBtn) {
            if (times >= endTime && (activity.isPlay == 1 || activity.isPlay == 3)) {
                if(wifi_lay.getVisibility()==View.VISIBLE){}else{
                pay_big_layout.setVisibility(View.VISIBLE);
    //            bottom_payL.setVisibility(View.GONE);
                bottom_payR.setVisibility(View.GONE);}
                pause();
            } else {
                pay_big_layout.setVisibility(View.GONE);
            }
        } else {
            pay_big_layout.setVisibility(View.GONE);
 //           bottom_payL.setVisibility(View.GONE);
            bottom_payR.setVisibility(View.GONE);
        }
    }


    private void showBottomRight() {
        if (pay_big_layout.getVisibility() != View.VISIBLE) {
       //     bottom_payL.setVisibility(View.GONE);
            if (activity.isPlay != 1 && activity.isPlay != 3) {
                bottom_payR.setVisibility(View.GONE);
                return;
            }
            if (bottom_payR.getVisibility() == View.VISIBLE) {
                bottom_payR.setVisibility(View.GONE);
            } else {
                bottom_payR.setVisibility(View.VISIBLE);
            }

        }
    }

    public void initLgCatch() {
        if (LgTools != null) {
            LgTools.realBeforeSeekOrPreMinTime(videoView.getCurrentPosition());
            LgTools.setPlayerCurrentPosition(videoView.getCurrentPosition());
            LgTools.stopCatch();
            LgTools = null;

            LgTools = new PlayerLogCatchTools(context);
            LgTools.startCatch();
        } else {
            LgTools = new PlayerLogCatchTools(context);
            LgTools.startCatch();
        }
        //    startBuffListener();

        //     LgTools = new PlayerLogCatchTools(context);
        startBuffListener(); // 打开缓冲监听 采集日志
    }

    public void recLogCatch() {
        if (LgTools != null) {
            LgTools.realBeforeSeekOrPreMinTime(videoView.getCurrentPosition());
            LgTools.setPlayerCurrentPosition(videoView.getCurrentPosition());
            LgTools.stopCatch();
            LgTools = null;

            LgTools = new PlayerLogCatchTools(context);
            LgTools.startCatch();
        } else {
            LgTools = new PlayerLogCatchTools(context);
            LgTools.startCatch();
        }
    }

    @Override
    public void setPlayData(String url, int sid, VideoUrlInfoBean datas, DetailInfoBean infoBean) {
        handler.sendEmptyMessage(1023);
        this.sid = sid;
        this.infoBean = infoBean;
        LgTools.setPlayerSid(sid);

    }

    public void updateDetail() {
        pause();
        pb.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPlaySource(final String url, long seek) {
        if (linearLayout1 != null && linearLayout1.getVisibility() == View.GONE) {
            linearLayout1.setVisibility(View.VISIBLE);
        }
        Lg.i("video1111111", "videoView" + videoView + "...." + url);
        isPrepare = false;
        if (videoView == null || url == null || url.equals("")) {
            Lg.i("video", "videoView" + videoView + "...." + url);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (videoView == null || url == null || url.equals("")) {

                        pb.setVisibility(View.GONE);
                        tvLoading.setVisibility(View.GONE);
                        tv_no_url.setVisibility(View.VISIBLE);
                        llPlayLoading.setVisibility(View.GONE);
                    }
                }
            });
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                pause();
                pb.setVisibility(View.VISIBLE);
                tvLoading.setVisibility(View.VISIBLE);
            }

        });


        videoView.changeDataSource(url);
        seekTo((int) seek);


        // 日志采集
        LgTools.setPlayerUrl(url);

        LgTools.setFid(fid);

        LgTools.setStartBufferTime();
    }


    public void setDetailData(DetailBean dBean) {

        if(dBean!=null) {
            data = dBean;
            if(tv_title==null){
                return;
            }//不知道为啥它能为空 Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)
            tv_title.setText(data.getVname());
            LgTools.setPlayerVid(data.getLiveId());
            LgTools.setPlayerVideoName(data.getLiveName());
            LgTools.setPlayerSid(sid);
        }
        }

    // 网络状况改变
    public void setNetState(boolean wifi, boolean moble) {
        wificonn = wifi;
        mobleconn = moble;
        if (!able4G && !wificonn && mobleconn) {
            pause();
            if (wifi_lay != null) {
                if(pay_big_layout.getVisibility()!=View.VISIBLE)
                wifi_lay.setVisibility(View.VISIBLE);
            }
        } else {
            if (wifi_lay != null) {
                wifi_lay.setVisibility(View.GONE);
                seekTo(videoView.getCurrentPosition());
            }
        }
    }

    public void removeUrltv() {
        if (tv_no_url != null) {
            tv_no_url.setVisibility(View.GONE);
        }
    }

    public void upProgressBar() {
        pb.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
    }


    @Override
    public void addRecord() {
        Lg.i("playFragment", "addRecord" + sid + "...." + vid);
        setSK(sid, times);
    }

    //存储播放进度  36514
    private void setSK(long sidTo, long progress) {

        if (payBtn && sidTo >= 0) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Lid", String.valueOf(vid));
            hashMap.put("sid", String.valueOf(sidTo));
            hashMap.put("progress", String.valueOf(progress));
            sid = -1;
            vid = 0;
            //    Lg.i(TAG, "getComcaLivePreviewTimeGet progress  progress----------->>" + progress);
            HttpRequest.getInstance().excute("getComcaLivePreviewTimeAdd", new Object[]{JSON.toJSONString(hashMap), new HttpResponseListener() {
                @Override
                public void onSuccess(String response) {
                    Lg.i("liveFrag", "getComcaLivePreviewTimeAdd response!!!!!!!----------->>" + response);
                    if (TextUtils.isEmpty(response)) {
                        Lg.e("liveFrag", "getComcaLivePreviewTimeAdd response is null or empty!!!");
                        //弹出存储失败，请重试，或者其他处理，需要处理，wanqi
                        return;
                    }

                }

                @Override
                public void onError(String error) {
                    Lg.e("liveFrag", "getComcaLivePreviewTimeAdd onError!!!!!!------->> " + error);
                }
            }});
        }
    }


    public void popLay() {
        if (top_lay == null || botm_lay == null || btn_lock == null)
            return;
        if (!isLayPop) {
            top_lay.setVisibility(View.VISIBLE);
         //   botm_lay.setVisibility(View.VISIBLE);
            if(payBtn&&pay_big_layout.getVisibility()==View.VISIBLE){

            }else{
                botm_lay.setVisibility(View.VISIBLE);
            }
            //           btn_lock.setVisibility(View.VISIBLE);
//            visibleTitleBar();
            isLayPop = true;
            if (payBtn)
                showBottomRight();
            if (handler != null)
                handler.sendEmptyMessageDelayed(101, 5000);
        }
    }

    public void hiddentLay() {
        if (top_lay == null || botm_lay == null || btn_lock == null)
            return;
        if (isLayPop) {
            top_lay.setVisibility(View.GONE);
            botm_lay.setVisibility(View.GONE);
            btn_lock.setVisibility(View.GONE);
            isLayPop = false;
            if (payBtn&&endTime>0)
                showBottomRight();
            if (handler != null)
                handler.removeMessages(101);
        }
    }

    public void updateSeek() {
    //    Lg.i("LivePlayerFrag", "updateSeek" + startTime + "...." + times + "....." + endTime+"...."+videoView.getCurrentPosition());
        if (videoView == null) {
            return;
        }

        if (isPrepare && videoView.getDuration() != 0) {
            tv_now.setText(TimeUtils.getCurTime(videoView.getCurrentPosition()));
            current = videoView.getCurrentPosition();
            duration = videoView.getDuration();
            sb.setProgress((int) ((float) videoView.getCurrentPosition() / (float) videoView.getDuration() * 1000));
        }
        buf_time = System.currentTimeMillis();
        //   Lg.i("AXLvideoView", arg0.getDuration() + "...." + arg0.getCurrentPosition() + "..." + arg1);
        if (payBtn) {
            // if(arg0.getDuration())
            if (times < startTime)
                seekTo((int) startTime);
            else if (times >= endTime) {
                pause();
                //处理付费
                toastPayLayout();
            }

        }
    }

    // 设置屏幕亮度
    public void setBrightness(int po) {
        WindowManager.LayoutParams lpa = getActivity().getWindow().getAttributes();
        lpa.screenBrightness = po / 255f;
        getActivity().getWindow().setAttributes(lpa);
    }

    // 设置屏幕横竖屏效果
    public void setScreenOrientation() {
        if (screen_o == 0) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
            screen_o = 1;
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (screen_o == 1) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
            screen_o = 0;
        }
        if (!screen_lock) {
            handler.sendEmptyMessageDelayed(107, 3000);
        }
    }

    public void changeScreenOrientation(int so) {
        screen_o = so;
        if (screen_o == 1) {
            Log.d("changeScreenOrientation", "横屏");
            btn_share.setVisibility(View.GONE);
        } else {
            Log.d("changeScreenOrientation", "竖屏");
            btn_share.setVisibility(View.GONE);
        }

    }


    public void pause() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            handler.sendEmptyMessage(1091);
        }
    }

    public void btnPause() {
        if (btn_play != null) {
            btn_play.setBackgroundResource(R.drawable.play);
        }

    }

    public void seekTo(int seektime) {
        Lg.i("playerFrag", seektime + "seekTimer");
        final int s = seektime;
        if (videoView != null) {
            if (pb != null) {
                pb.setVisibility(View.VISIBLE);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    videoView.seekTo(s);
                    if (!videoView.isPlaying()) {
                        handler.sendEmptyMessage(109);
                        if (wifi_lay != null && wifi_lay.getVisibility() == View.VISIBLE) {
                            // 在提示wifi状态下 不允许播放
                            pause();
                        } else {

                            start();
                        }
                    }
                }
            }).start();
        }
    }

    public void start() {
        Lg.i("LivePlayer", "start:Live");
        if (videoView != null&&surface_show) {
            Lg.i("LivePlayer", "start:Live1");
            //要不有时候会报空指针
            if(TextUtils.isEmpty(videoView.getDataSource())){
                return ;
            }
            videoView.start();
            isStartPlay = true;
            handler.sendEmptyMessage(109);

        }
    }

    public void stop() {
        if (videoView != null) {
            // videoView.stop();
        }
    }

    public void release() {
        if (videoView != null) {
            videoView.release();
            videoView = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_play) {
            if (videoView != null && videoView.isPlaying()) {
                pause();
                btn_play.setBackgroundResource(R.drawable.play);
            } else if (videoView != null && !videoView.isPlaying()) {
                btn_play.setBackgroundResource(R.drawable.stop);
                start();
            }
        } else if (v == btn_full) {
            setScreenOrientation();
        } else if (v == btn_back || v == btn_pre_back||v==btn_pre_back1||v==btn_pre_back2||v==video_wait_time_back) {
            if (screen_o == 1) {
                setScreenOrientation();
            } else {
                getActivity().finish();
            }
        } else if (v == btn_wifi) {
            wifi_lay.setVisibility(View.GONE);
            start();
        } else if (v == btn_lock) {
            if (!screen_lock) {
                screen_lock = true;
                btn_lock.setBackgroundResource(R.drawable.scn_close);
                if (screen_o == 1) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            } else {
                screen_lock = false;
                btn_lock.setBackgroundResource(R.drawable.scn_open);
                handler.sendEmptyMessageDelayed(107, 2000);
            }
        } else if (v == btn_share) {
            // 处理分享事件
            activity.shareDialog();
        } else if (v == pay_big_buy || v == pay_small_buy||v==video_buy_btn) {
            //点 1直2 轮3
            Bundle bundle = new Bundle();
            bundle.putLong("seriesId", vid);
            bundle.putInt("seriesType", 2);
            startActivity(Action.getActionName(Action.OPEN_PRODUCT_PACKAGE_PAGE), bundle);
        } else if (v == pay_big_login || v == pay_small_login|| v==pay_small_login1) {
            Bundle bundle = new Bundle();
            bundle.putLong("seriesId", vid);
            bundle.putInt("seriesType", 2);
            startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), bundle);
        } else if (v == video_yuyue_btn) {
            activity.addReserveBean();
        }
    }



    // 打开缓冲监听
    private long buf_time;
    private long buff_time;
    private boolean buffing = false;
    private boolean pauseing = false;


    public void startBuffListener() {
        handler.sendEmptyMessage(104);
    }

    public void updateRanTime(String timeName) {
        //目前不需要倒计时
        //txt_rantitle.setText(timeName);
        txt_rantitle.setVisibility(View.VISIBLE);
    }

    public void setLiveData(Integer integer, List<DetailChildBean> child, int detail_ty) {
        status = integer;

    }

    public View getLinearLayout1() {
        return linearLayout1;
    }

    public void setLiveBtn1(String s, int i) {
        if(i==2) {
            if (times >= endTime&&endTime>0) {
                live_btn1.setText("试看结束");
                return;
            }
        }
            live_btn1.setText(s);


    }

    @Override
    public void setPayBtn(boolean payBtn) {
        super.setPayBtn(payBtn);
        if(App.isLogin&& App.userId>0){
               pay_big_login.setVisibility(View.GONE);
              pay_small_login.setVisibility(View.GONE);
            pay_small_login.setText(" 切换账号 ");
            pay_big_login.setText(" 切换账号 ");
        }
    }

    public View getLinearLayout3() {
        return linearLayout3;
    }

    public void gongRanTitle() {
        txt_rantitle.setVisibility(View.GONE);
    }

    public void startRanTime(String liveCurTime2) {
        video_wait_msg_tv.setText(liveCurTime2);
    }

    public void setLiveBtn3(String s, int i) {
     //   linearLayout3.setVisibility(View.VISIBLE);
        video_yuyue_btn.setText(s);
        if(!payBtn){
            video_buy_btn.setVisibility(View.GONE);
        }
    }


    public void setSid(long sid) {
        this.sid = (int) sid;
    }

    public long getCurrentPos() {
        if (videoView != null)
            return videoView.getCurrentPosition();
        else
            return 0;
    }

    @SuppressLint("HandlerLeak")
    private class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 101:
                    hiddentLay();
                    break;
                case 102:
                    popLay();
                    break;
                case 103:
                    if (videoView != null && tv_total != null)
                        tv_total.setText(TimeUtils.getCurTime(videoView.getDuration()));
                    break;
                case 104:
                    if (buff_time == buf_time) {
                        if (buffing) {
                            LgTools.addPlayerBuffTime_end_B();
                            LgTools.addPlayerBuffTime_end_A();
                            buffing = false;
                        }
                    } else {
                        if (!buffing) {
                            LgTools.addPlayerBuffNum_start_B();
                            LgTools.addPlayerBuffNum_start_A();
                            buffing = true;
                        }
                    }
                    buff_time = buf_time;
                    handler.sendEmptyMessageDelayed(104, 1000);
                    break;
                case 105:
                    break;
                case 106:
                    break;
                case 107:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    break;
                case 108:
                    if (videoView != null) {
                        if (payBtn && videoView.isPlaying()) {
                            times = times + 1000;
                        }
                        updateSeek();
                    }
                    break;
                case 109:
                    if (btn_play != null) {
                        btn_play.setBackgroundResource(R.drawable.stop);
                    }
                    break;
                case 1091:
                    if (btn_play != null) {
                        btn_play.setBackgroundResource(R.drawable.play);
                    }
                case 110:
                    if (sound_lay.getVisibility() == View.VISIBLE) {
                        sound_lay.setVisibility(View.GONE);
                    }
                    if (bright_lay.getVisibility() == View.VISIBLE) {
                        bright_lay.setVisibility(View.GONE);
                    }
                    if (seek_lay.getVisibility() == View.VISIBLE) {
                        seek_lay.setVisibility(View.GONE);
                    }
                    break;
                case 1005:
                //    bottom_payL.setVisibility(View.GONE);

                    break;
                case 1023:
                    pb.setVisibility(View.VISIBLE);
                    tvLoading.setVisibility(View.VISIBLE);
                    break;
                case 1031:
                    //解决试看还显示问题

                    if (linearLayout1.getVisibility() == View.VISIBLE) {
                        linearLayout1.setVisibility(View.GONE);
                    }
                    toastPayLayout();
                    break;
                case 900:
                    if(activity.isPlay==3){
                        sb.setVisibility(View.VISIBLE);
                    }else{
                        sb.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    public void startTimer() {
        timer = new Timer(true);
        timer.schedule(task, 0, 1000); // 延时1000ms后执行，1000ms执行一次
    }

    public void stopTimer() {
        timer.cancel(); // 退出计时器
    }

    TimerTask task = new TimerTask() {
        public void run() {
            handler.sendEmptyMessage(108);
        }
    };

    // 每分钟整点上报 日志采集
    public void logPost_min() {
        if (videoView != null) {
            LgTools.setPlayerCurrentPosition(videoView.getCurrentPosition());
        }
        if (pauseing) {
            //   LgTools.();
        }
        LgTools.writeLogToDb();
        if (buffing) {
            LgTools.addPlayerBuffNum_start_A();
        }
        if (pauseing) {
            //    LgTools.playerPauseNum_start_A();
        }
    }

    @Override
    public void setErrorLog(String code, String msg) {
        super.setErrorLog(code, msg);
        //下面这俩判空都是坑
        if(LgTools!=null)
        LgTools.setPlayerError(code, msg);
        if(tv_no_url!=null)
        tv_no_url.setText("播放错误码:" + code + "  错误信息:" + msg);
    }

    @Override
    public void initFreeTimeLay(boolean b, long startTime, long endTime) {
        super.initFreeTimeLay(b, startTime, endTime);
        Lg.i("initFreeTime", endTime + "...."+b);
        payBtn = b;
        this.startTime = startTime;
        this.endTime = endTime;
        toastPayLayout();
        if(!b){
            video_buy_btn.setVisibility(View.GONE);
        }
        if(App.isLogin&& App.userId>0){
              pay_big_login.setVisibility(View.GONE);
              pay_small_login.setVisibility(View.GONE);
            pay_small_login.setText(" 切换账号 ");
            pay_big_login.setText(" 切换账号 ");
        }
    }

//    public void visibleTitleBar(){
//        if ( Build.VERSION.SDK_INT >= 17) {
//            View decorView = getActivity().getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    public void initFreeTimeLay(boolean b, long i) {
        payBtn = b;
        times = i;
        if (times >= endTime) {
            //解决界面不同步的问题
            Lg.i("LivePlayFrag", "linea1 is visible");
            handler.sendEmptyMessageDelayed(1031, 200);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            pause();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (videoView != null && !videoView.isPlaying()) {
            start();
        }
    }
}
