package cn.cibnmp.ott.ui.detail.player.widgets;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
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
import java.util.Timer;
import java.util.TimerTask;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.player.PlayerLogCatchTools;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.detail.DetailActivity;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.RecordBean;
import cn.cibnmp.ott.ui.detail.bean.SidEvent;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;
import cn.cibnmp.ott.ui.share.ShareDialog;
import cn.cibnmp.ott.utils.BrightUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibntv.ott.lib.ijk.NVideoView;
import de.greenrobot.event.EventBus;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_ERROR_UNSUPPORTED;


/**
 * 播放器页面
 *
 * @author axl
 *
 * MEDIA_ERROR_MALFORMED


作者：Gongjia
链接：https://www.jianshu.com/p/b5da0db9fd73
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class PlayerFrag extends BasePlayerFrag implements OnClickListener {

    private View contextView;


    private String action = "open_movie_detail_page";
    private DetailBean data;
    private VideoUrlInfoBean p_datas;
    private ShareDialog shareDialog;

    private NVideoView videoView;

    private ProgressBar pb;
    private SeekBar sb, sound_sb, bright_sb;

    private RelativeLayout top_lay, botm_lay, wifi_lay, seek_lay, sound_lay, bright_lay, pre_lay, pay_big_layout, bottom_payL, bottom_payR;
    private TextView tv_title, tv_now, tv_total, tv_no_url, seek_tv, txt_buy;
    private Button btn_play, btn_full, btn_back, btn_pre_back, btn_wifi, btn_lock, btn_share, pay_big_buy, pay_big_login, pay_small_buy, pay_small_login,btn_back1;
    private ImageView seek_img;

    private ImageView test_img1;

    private MHandler handler;

    private int pgress = 0; // 进度条当前进度
    private int current;

    private long durtion;
    private Timer timer;
    private TextView tvLoading;

    private PlayerLogCatchTools LgTools;


    private long cctime; // log上报的一个标志时间，无逻辑作用
    private long cctime2; // log上报的一个标志时间，无逻辑作用

    private DetailActivity activity;
    //TODO zxy
    private LinearLayout llPlayLoading;

    private DetailInfoBean infoBean;


    private long endTime;
    private long startTime;


    public static PlayerFrag newInstance(int type, long vid) {
        PlayerFrag fragment = new PlayerFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putLong(VID, vid);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String epgId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.player_frag_layout, container, false);
        if (getActivity() != null && getActivity() instanceof DetailActivity) {
            activity = (DetailActivity) getActivity();
            epgId = activity.epgIdParam;
        }


        Bundle bundle = getArguments();
        if (bundle != null) {
            vid = bundle.getLong(VID);
        }

        action = "open_normal_detail_page";

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
//        if (isAutoBrightness){
//            BrightUtils.stopAutoBrightness(getActivity());
//        }

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
        if (!payBtn)
            addRecord();

        handler.removeMessages(107);
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
        //bottom_payL = (RelativeLayout) contextView.findViewById(R.id.bottom_payL);
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
        btn_wifi = (Button) contextView.findViewById(R.id.video_wifi_lay_btn);
        btn_lock = (Button) contextView.findViewById(R.id.video_screen_lock);
        btn_share = (Button) contextView.findViewById(R.id.video_share);
        pay_big_buy = (Button) contextView.findViewById(R.id.pay_big_buy);
        pay_big_login = (Button) contextView.findViewById(R.id.pay_big_login);
        test_img1 = (ImageView) contextView.findViewById(R.id.test_img);


        pay_small_buy = (Button) contextView.findViewById(R.id.pay_small_buy);
        pay_small_login = (Button) contextView.findViewById(R.id.pay_small_login);
        btn_back1 = (Button) contextView.findViewById(R.id.video_pre_back1);

        llPlayLoading = (LinearLayout) contextView.findViewById(R.id.ll_play_loading);

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
        btn_back1.setOnClickListener(this);

        if(App.isLogin&& App.userId>0){
            pay_big_login.setVisibility(View.GONE);
            pay_small_login.setVisibility(View.GONE);
            pay_small_login.setText(" 切换账号 ");
            pay_big_login.setText(" 切换账号 ");
        }


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
                                        if(sound_lay.getVisibility()==View.VISIBLE){
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
                                        if(bright_lay.getVisibility()==View.VISIBLE){
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
                                        if(sound_lay.getVisibility()==View.VISIBLE){
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
                                        if(bright_lay.getVisibility()==View.VISIBLE){
                                            bright_lay.setVisibility(View.GONE);
                                        }
                                        sound_lay.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else if (isXmove == 1) {  // 水平位移
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

                        mmY = moveY;

                        break;
                    case MotionEvent.ACTION_UP:

                        if (isXmove == 1) {
                            if (videoView != null && isPrepare) {
                                seekTo((int) (videoView.getDuration() * ds));
                                popLay();
                            }
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


        popLay();
    }


    private boolean isOncomp = false;//用来辨别curentpos 播放结束后 currentpos不刷新
    private void initPlayer() {
        videoView = (NVideoView) contextView.findViewById(R.id.video_view);
        videoView.initVideoView(NVideoView.PV_PLAYER__IjkMediaPlayerC);

        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onPrepared(IMediaPlayer arg0) {
                isPrepare = true;
             //   startTimer();
                    isOncomp= false;
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
           //         bottom_payL.setVisibility(View.VISIBLE);
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
                if (activity != null && need2seek && activity.record_current_pos >= videoView.getDuration() - 5 * 1000) {

                } else {
                    if (activity.need_Playcurrent_pos) {
                        need2seek = false;
                        current = (int) activity.record_current_pos;
                        seekTo(current);
                        activity.record_current_pos = 0;
                        activity.need_Playcurrent_pos = false;
                    }
                    //   seekTo((int) activity.record_current_pos);


                    // 日志采集
                    //   LgTools.setPlayerStartSeek(data.getVideoRecord().getCurrentPos());
                    LgTools.realAfterSeek(activity.record_current_pos);
                }


                handler.sendEmptyMessage(103);
            }
        });
/*8
比特流不符合相关的编码标准和文件规范
常量值：-1007

MEDIA_ERROR_IO
本地文件或网络相关错误
常量值：-1004

MEDIA_ERROR_TIMED_OUT
播放超时错误
常量值：-110

MEDIA_ERROR_UNKNOWN
播放错误，未知错误。
常量值：0

MEDIA_INFO_UNKNOWN
媒体信息错误
常量值：1

MEDIA_INFO_VIDEO_RENDERING_START
视频开始渲染，显示图像
常量值：3

MEDIA_ERROR_SERVER_DIED
媒体服务器挂机（应用必须释放对象，然后开启新的实例）
常量值：100

MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
播放错误（一般视频播放比较慢或视频本身有问题会引发）。
常量值：200

MEDIA_INFO_VIDEO_TRACK_LAGGING
视频过于复杂，无法解码：不能快速解码帧。此时可能只能正常播放音频。参见MediaPlayer.OnInfoListener。
常量值：700

MEDIA_INFO_BUFFERING_START
MediaPlayer暂停播放等待缓冲更多数据。
常量值：701

MEDIA_INFO_BUFFERING_END
缓冲结束
常量值：702

MEDIA_INFO_BAD_INTERLEAVING
当音频和视频数据不正确的交错时
常量值：800

MEDIA_INFO_NOT_SEEKABLE
媒体不支持Seek，例如直播流。
常量值：801

MEDIA_INFO_METADATA_UPDATE
常量值：802

MEDIA_ERROR_UNSUPPORTED
比特流符合相关编码标准和文件规范，但是media框架不被支持。
常量值：1010
 */
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
                        if (pre_lay != null && pre_lay.getVisibility() != View.GONE) {
                            pre_lay.setVisibility(View.GONE);
                        }
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        break;
                    case -1007:
                        //比特流不符合相关的编码标准和文件规范
                        break;
                    case -1004:
                        //本地文件或网络相关错误
                        break;
                    case -110:
                        //播放超时错误
                        break;
                    case 1:
                        //媒体信息错误 或播放错误 未知错误
                        break;
                    case 100:
                        //媒体服务器挂机（应用必须释放对象，然后开启新的实例）
                        break;
                    case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                            //200  播放错误（一般视频播放比较慢或视频本身有问题会引发）
                            break;
//                    case 700:
//                        //视频过于复杂，无法解码：不能快速解码帧。此时可能只能正常播放音频。参见
//                        break;
                    case MEDIA_ERROR_UNSUPPORTED:
                        //比特流符合相关编码标准和文件规范，但是media框架不被支持。
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
                durtion = arg0.getDuration();
          //      Lg.i("AXLvideoView", arg0.getDuration() + "...." + arg0.getCurrentPosition() + "..." + arg1 + "...." + endTime + "...." + payBtn);
                if (payBtn) {
                    // if(arg0.getDuration())
                    if (arg0.getCurrentPosition() < startTime)
                        seekTo((int) startTime);
                    else if (arg0.getCurrentPosition() >= endTime) {
                        pause();
                        //处理付费
                       // toastPayLayout();
                    }

                }
            }
        });

        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(IMediaPlayer arg0) {
                // changePlayData(url);
                Lg.i("playFragment", "onComplist");
           //     stopTimer();
                isOncomp = true;
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
   private boolean isFirstToast=true;
    private void toastPayLayout() {

        if(videoView==null){
            return;
        }
        if (payBtn) {
            //这也是有报空
            if(videoView==null){
                if(payBtn)
                handler.sendEmptyMessageDelayed(1009,1000);
            }
            if(videoView.getCurrentPosition()>endTime+3000&&isOncomp){
                return;
            }
            Lg.i("PlayerFrag","toastPlayout"+payBtn+videoView.getCurrentPosition()+"....."+endTime);
            if(videoView.getCurrentPosition()>=endTime){
              if(wifi_lay.getVisibility()==View.VISIBLE){

              }  else{
                  if(endTime==0&&isFirstToast){
                      handler.sendEmptyMessageDelayed(1009,500);
                      isFirstToast= false;
                      return;
                  }
             pay_big_layout.setVisibility(View.VISIBLE);
       //     bottom_payL.setVisibility(View.GONE);
            bottom_payR.setVisibility(View.GONE);}
            pause();
            }else {
                pay_big_layout.setVisibility(View.GONE);
            }
        }else{
            pay_big_layout.setVisibility(View.GONE);
      //      bottom_payL.setVisibility(View.GONE);
            bottom_payR.setVisibility(View.GONE);
        }
    }


    private void showBottomRight() {
        if (pay_big_layout.getVisibility() != View.VISIBLE) {

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
        this.p_datas = datas;
        this.infoBean = infoBean;
        LgTools.setPlayerSid(sid);

    }

    public void updateDetail() {
        pause();
        pb.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    String url;
    @Override
    public void setPlaySource(final String url, long seek) {
        pay_big_layout.setVisibility(View.GONE);
        if (pre_lay != null && pre_lay.getVisibility() == View.GONE) {
            pre_lay.setVisibility(View.VISIBLE);
        }
        Lg.i("video1111111", "videoView" + videoView + "...." + url);
        isPrepare = false;
        if (videoView == null || url == null || url.equals("")) {
            Lg.i("video", "videoView" + videoView + "...." + url);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (videoView == null || url == null || url.equals("")) {
//            if (!context.isDestroyed()) {
//                ToastUtils.show(context, "暂无播放内容");
//            }
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
        this.url = url;
        seekTo((int) seek);


        // 日志采集
        LgTools.setPlayerUrl(url);
        if (p_datas != null && p_datas.getMedia() != null)
            LgTools.setFid(p_datas.getMedia().get(0).getFid());

        LgTools.setStartBufferTime();
    }


    public void setDetailData(DetailBean dBean) {
        data = dBean;
        tv_title.setText(data.getVname());
        LgTools.setPlayerVid(data.getVid());
        LgTools.setPlayerVideoName(data.getVname());
        LgTools.setPlayerSid(sid);
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

    // 保存播放记录
//    public void addRecord() {
//        if (data == null) {
//            return;
//        }
//        RecordBean rbean = new RecordBean();
//
//        rbean.setAction(action);
//        if (detail_type == 0) { // 电影
//            rbean.setSid(sid);
//        } else if (detail_type == 1) { // 电视剧
//            rbean.setSid(sid);
//            if (p_datas.getMedia() != null && p_datas.getMedia().size() > sid
//                    && p_datas.getMedia().get(sid) != null) {
//                rbean.setSname(da);
//            }
//        } else { // 综艺
//            rbean.setSid(sid);
//
//            if (data.getEpisodelist() != null && data.getEpisodelist().size() > 0) {
//                for (int i = 0; i < data.getEpisodelist().size(); i++) {
//                    if (sid == data.getEpisodelist().get(i).getSid()) {
//                        rbean.setSname(data.getEpisodelist().get(i).getSname());
//                        break;
//                    }
//                }
//            }
//        }
//
//        rbean.setVid(data.getVid());
//
//        if (data.getVname() != null) {
//            rbean.setVname(data.getVname());
//        }
//
//        rbean.setVideoType(data.getVideotype());
//
//        if (data.getPosterfid() != null) {
//            rbean.setPosterFid(data.getPosterfid());
//        }
//
//        rbean.setDuration(duration);
//        rbean.setCurrentPos(current);
//        rbean.setTime(TimeUtils.getCurrentTimeInLong());
//        Lg.e("shenfei", "record" + JSON.toJSON(rbean).toString());
//
//        HttpRequest.getInstance().excute("addLocalRecord",
//                new Object[]{JSON.toJSON(rbean).toString(), new HttpResponseListener() {
//
//                    @Override
//                    public void onSuccess(String response) {
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                    }
//
//                }});
//
//    }

    @Override
    public void addRecord() {
        Lg.i("playFragment", "addRecord");
        if (data == null) {
            return;
        }
        RecordBean rb = new RecordBean();

        rb.setAction("open_normal_detail_page");

        rb.setAction(action);

        if (TextUtils.isEmpty(epgId))
            epgId = "1031";
        else
            rb.setEpgId(Integer.valueOf(epgId));

        rb.setVid(vid);
        rb.setSid(sid);

        rb.setVname(data.getVname());

            if (data != null && !TextUtils.isEmpty(data.getImgh()))
                rb.setPosterFid(data.getImgh());
            else if (data != null)
                rb.setPosterFid(data.getImg());

        rb.setStoryPlot(data.getStoryPlot());
        rb.setCurrentPos(videoView.getCurrentPosition());
        //  ToastUtils.show(getActivity(),durtion+"");
        rb.setDuration(durtion);
        rb.setTimes(System.currentTimeMillis());

        HttpRequest.getInstance().excute("addLocalRecord", new Object[]{
                JSON.toJSONString(rb), new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.i("playFragment", "addRecord1");
            }

            @Override
            public void onError(String error) {
                Lg.i("playFragment", "addRecord2");
            }
        }
        });
    }


    public void popLay() {
        if (top_lay == null || botm_lay == null || btn_lock == null)
            return;
        if (!isLayPop) {
            top_lay.setVisibility(View.VISIBLE);
            if(payBtn&&pay_big_layout.getVisibility()==View.VISIBLE){

            }else{
                botm_lay.setVisibility(View.VISIBLE);
            }

        //    btn_lock.setVisibility(View.VISIBLE);
            //       visibleTitleBar();
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
        if (videoView == null) {
            return;
        }

        if (isPrepare && videoView.getDuration() != 0) {
            tv_now.setText(TimeUtils.getCurTime(videoView.getCurrentPosition()));
            current = videoView.getCurrentPosition();
            durtion = videoView.getDuration();
            sb.setProgress((int) ((float) videoView.getCurrentPosition() / (float) videoView.getDuration() * 1000));
        }

        buf_time = System.currentTimeMillis();

        //    Lg.i("AXLvideoView", arg0.getDuration() + "...." + arg0.getCurrentPosition() + "..." + arg1+"...."+endTime+"...."+payBtn);
        if (payBtn) {
            // if(arg0.getDuration())
            if (videoView.getCurrentPosition() < startTime)
                seekTo((int) startTime);
            else if (videoView.getCurrentPosition() >= endTime) {
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
//        if (shareDialog!= null && shareDialog.isShowing()) {
//            shareDialog.dismiss();
//        }
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
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
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
      //          }
//            }).start();
        }
    }

    public void start() {
        if (videoView != null && surface_show) {
            //要不有时候会报空指针
            if(TextUtils.isEmpty(videoView.getDataSource())){
                return;
            }
            videoView.start();
            isStartPlay = true;
            Lg.i("playerFrag","start");
            handler.sendEmptyMessage(109);

          // Bitmap bitmap= createBitmap(url);
        //   test_img1.setImageBitmap(bitmap);
//           bitmap.recycle();
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
        } else if (v == btn_back || v == btn_pre_back||v==btn_back1) {
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
            shareDialog();
        } else if (v == pay_big_buy || v == pay_small_buy) {
            //点 1直2 轮3
            Bundle bundle = new Bundle();
            bundle.putLong("seriesId", vid);
            bundle.putInt("seriesType", 1);
            startActivity(Action.getActionName(Action.OPEN_PRODUCT_PACKAGE_PAGE), bundle);
        } else if (v == pay_big_login || v == pay_small_login) {
            Bundle bundle = new Bundle();
            bundle.putLong("seriesId", vid);
            bundle.putInt("seriesType", 1);
            startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN), bundle);
        }
    }


    private void shareDialog() {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(getActivity(), R.style.transcutestyle);
            shareDialog.initView(getActivity(), vid, data.getImg(), data.getStoryPlot(), data.getVname(), 0);
            int width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
            int height = (int) (this.getActivity().getWindowManager().getDefaultDisplay().getHeight() / 2.3);
            Window dialogWindow = shareDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = width;
            lp.height = height;
            lp.gravity = Gravity.BOTTOM;
            dialogWindow.setWindowAnimations(R.style.fackstyle1);  //添加动画
            dialogWindow.setAttributes(lp);
            shareDialog.show();
        } else {
            shareDialog.show();
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
                    updateSeek();
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
               //     bottom_payL.setVisibility(View.GONE);

                    break;
                case 1023:
                    pb.setVisibility(View.VISIBLE);
                    tvLoading.setVisibility(View.VISIBLE);
                    break;
                case 1009:
                    toastPayLayout();
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
        LgTools.setPlayerError(code, msg);
        tv_no_url.setText("播放错误码:" + code + "  错误信息:" + msg);
    }

    @Override
    public void initFreeTimeLay(boolean b, long startTime, long endTime) {
        Lg.i("playerFrag","initFreeTime");
        super.initFreeTimeLay(b, startTime, endTime);
        payBtn = b;
        this.startTime = startTime;
        this.endTime = endTime;
        handler.sendEmptyMessageDelayed(1009,200);
        if(App.isLogin&& App.userId>0){
               pay_big_login.setVisibility(View.GONE);
              pay_small_login.setVisibility(View.GONE);
            pay_small_login.setText(" 切换账号 ");
            pay_big_login.setText(" 切换账号 ");
        }
      //  toastPayLayout();
    }


    @Override
    public void setPayBtn(boolean payBtn) {
        super.setPayBtn(payBtn);
     //   toastPayLayout();
        if(App.isLogin&& App.userId>0){
               pay_big_login.setVisibility(View.GONE);
              pay_small_login.setVisibility(View.GONE);
            pay_small_login.setText(" 切换账号 ");
            pay_big_login.setText(" 切换账号 ");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(videoView!=null&&videoView.isPlaying()){
            pause();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if(videoView!=null&&!videoView.isPlaying()){
            start();
        }
    }

    //获取视频第一帧的图像


    private Bitmap  createBitmap(String url){
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        Lg.i("PlayerFrag","url:"+url);
        Lg.i("PlayerFrag","url1:"+videoView.getDataSource()
        );
        try {

            mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
        }catch (Exception e){
            e.printStackTrace();
            Lg.i("PlayerFrag","获取视频忽略图失败");
        }finally {
            mediaMetadataRetriever.release();
        }
        return bitmap;
    }


}
