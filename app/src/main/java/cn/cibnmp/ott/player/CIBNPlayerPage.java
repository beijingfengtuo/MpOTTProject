package cn.cibnmp.ott.player;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.utils.DisplayUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibntv.ott.lib.ijk.NVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;

public class CIBNPlayerPage extends BasePlayerPage {

    private static final String TAG = "LOG_PLAYER";
//    private static final String TAG = "shenfei";

    private View contextView;

    private Context context;

    private NVideoView videoView;

    private PlayerLogCatchTools logTools;
    private IntentFilter filter;

    private LinearLayout content_lay, par_lay;

    private PlayerCallBack callBack;

    private int playerType = 0; //0原生，1硬解，2软解，3youku

    private String url;

    private String lastStatus;

    public CIBNPlayerPage(Context context, AttributeSet attrs, int playerType) {
        super(context, attrs);

        contextView = View.inflate(context, R.layout.player_frag_lay, this);
        this.context = context;
        this.playerType = playerType;

        if (isInEditMode())
            return;

        initView();

        initPlayer();
    }

    private void initView() {
        content_lay = (LinearLayout) contextView.findViewById(R.id.content_lay);
        par_lay = (LinearLayout) contextView.findViewById(R.id.player_page_par_lay);
    }

    @Override
    public void onActivityDestroy() {
        if (logTools != null) {
            logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
            logTools.setPlayerCurrentPosition(getCurrentPosition());
            logTools.stopCatch();
            logTools = null;
        }
        destoryPlayer();
    }

    public void destoryPlayer() {
        pause();
        stop();
        release();
    }

    public void initPlayer() {
        Lg.d(TAG, "initPlayer()");
        videoView = new NVideoView(context);
//        videoView.setBackgroundColor(0xff000000);
//        videoView.setZOrderOnTop(true);
//        videoView.setZOrderMediaOverlay(true);
//        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        content_lay.addView(videoView);
        if (playerType == 2) {
            Lg.d(TAG, "playerType == 2");
            videoView.initVideoView(NVideoView.PV_PLAYER__IjkMediaPlayerC);
        } else if (playerType == 1) {
            Lg.d(TAG, "playerType == 1");
            videoView.initVideoView(NVideoView.PV_PLAYER__IjkMediaPlayerD);
        } else {
            Lg.d(TAG, "playerType == 0");
            videoView.initVideoView(NVideoView.PV_PLAYER__AndroidMediaPlayer);
        }

        if (videoView == null) {
            return;
        }

        // 添加prepared监听
        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer arg0) {
                Lg.d(TAG, "isprepared");

                videoView.isChangeDataSrc = false;

                pause();
                start();

                if (usetea) {
                    teaPrepare = true;
                    //00000000000000000
//                    Repacker.getInstance(context).onPrepared(arg0);
                } else {
                    videoPrepare = true;
                    if (isStartWithSeek && startSeek > 0) {
                        seekTo((int) startSeek);
                    }

                    // 日志采集
                    if (logTools != null) {
                        logTools.setStartBufferTime();
                        logTools.setPlayerDuration(getDuration());
                    }

                    if (callBack != null) {
                        callBack.isprepared();
                        callBack.ffStart();
                    }
                }

            }
        });

        // 添加info监听
        videoView.setOnInfoListener(new OnInfoListener() {

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Lg.d(TAG, "BUFFERING_START");
                        // 日志采集
                        long c2 = System.currentTimeMillis();
                        if ((c2 - cctime2) > 100 && logTools != null) {
                            logTools.playerLoadingNum_start_B();
                            logTools.playerLoadingNum_start_A();
                            cctime2 = c2;
                            lastStatus = logTools.getPlayStatus();
                            logTools.setPlayStatus("4");
                        }
                        if (callBack != null) {
                            callBack.bufStart();
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        Lg.d(TAG, "BUFFERING_end");
                        // 日志采集
                        long c = System.currentTimeMillis();
                        if ((c - cctime) > 100 && logTools != null) {
                            logTools.playerLoadingTime_end_B();
                            logTools.playerLoadingTime_end_A();
                            cctime = c;
                            if (TextUtils.isEmpty(lastStatus)) {
                                logTools.setPlayStatus("2");
                            } else {
                                logTools.setPlayStatus(lastStatus);
                            }
                        }
                        if (callBack != null) {
                            callBack.bufEnd();
                        }
                    case 3:
                        if (callBack != null) {
                            callBack.ffStart();
                        }
                        break;
                }
                if (usetea) {
                    //00000000000000000
//                    Repacker.getInstance(context).onInfo(mp, what, extra);
                }
                return false;
            }
        });

        // 添加buffer监听
        videoView.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(IMediaPlayer arg0, int po) {
                if (callBack != null) {
                    callBack.bufUpdate(po);
                }
            }
        });

        // 添加completion监听
        videoView.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(IMediaPlayer arg0) {
//                Lg.e("shenfei", "OnCompletion");
//                if (videoView.surface_status != 10) {
//                    Lg.e("shenfei", "111111");
//                    return;
//                }

//                Lg.e("shenfei", "222222");
                if (playerType == 0 && videoView.isChangeDataSrc) {
                    return;
                }

                if (usetea && teaPrepare) {
                    teaPrepare = false;
                    //00000000000000000
//                    Repacker.getInstance(context).onCompletion(arg0);
                } else if (videoPrepare) {
                    videoPrepare = false;
                    // 日志采集
                    if (logTools != null) {
                        logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
                        logTools.setPlayerCurrentPosition(getCurrentPosition());
                        logTools.stopCatch();
                        logTools = null;
                    }

                    if (callBack != null) {
                        callBack.completion();
                    }
                }
            }
        });

        // 添加error监听
        videoView.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (callBack != null) {
                    callBack.error(what, extra, "");
                }
                return false;
            }
        });
    }

    @Override
    public void setErrorLogCatch(String code, String msg) {
        if (logTools != null) {
            logTools.setPlayerError(code, msg);
            logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
            logTools.setPlayerCurrentPosition(getCurrentPosition());
            logTools.stopCatch();
            logTools = null;
        }
    }

    @Override
    public NVideoView getVideoView() {
        return videoView;
    }

    @Override
    public void setPlayerCallback(PlayerCallBack callBack) {
        this.callBack = callBack;
    }

    private boolean isStartWithSeek = false;
    private long startSeek;

    @Override
    public void setStartSeek(long startSeek) {
        isStartWithSeek = true;
        this.startSeek = startSeek;
    }

    @Override
    public void setPlayDataSource(String url, long seek) {
//        url = "http://gs.hls.ott.cibntv.net/2017/07/03/d6c0977a236e44a09a71abdcf8ce1f85/233a62cc65383e3ffa58448f27399126.m3u8?k=d65fbbc05aac5aaabfbe0ca83ce663a1&channel=cibn&t=1499740728&ttl=86400";
        if (seek > 0) {
            isStartWithSeek = true;
            startSeek = seek;
        } else {
            isStartWithSeek = false;
            startSeek = 0;
        }
        this.url = url;
        Lg.d(TAG, url);
        changeDataSource();

        // 日志采集
        reLogCatch();
        getVideoDisc();
        if (logTools != null) {
            logTools.setPlayerUrl(url);
        }
    }

    @Override
    public String getPlayDataSource() {
        if (videoView != null) {
            return videoView.getDataSource();
        } else {
            return "";
        }
    }

    @Override
    public void setPlayDataSourceForTea(String url) {
        this.url = url;
        Lg.d(TAG, url);
        changeDataSource();
    }

    public void changeDataSource() {
        //国网项目地址替换
//        if (url.startsWith("http://hls01.ott.disp.cibntv.net")) {
//            url = url.replace("http://hls01.ott.disp.cibntv.net", "http://test.entry.cibn.cc/9098938600071449183");
//        }
        //阿里p2p服务地址替换
//        if (url.startsWith("http://cachegs.cdn.cibn.cc")) {
//            url = url.replace("http://cachegs.cdn.cibn.cc", "http://test.zdh.ali.cibn.cc");
//            Lg.e("shenfei", "url = " + url);
//            url = url.substring(0, 70);
//            Lg.e("shenfei", "url = " + url);
////            url = "http://test.zdh.ali.cibn.cc/m3u8/ee9aabc5f291108942636f54cfd2098f.m3u8";
//            url = PcdnManager.PCDNAddress(PcdnType.VOD, url);
////            url = "http://127.0.0.1:8912/test.zdh.ali.cibn.cc/m3u8/ee9aabc5f291108942636f54cfd2098f.m3u8?chaos_secret=UzUIibM%2F%2FaFJoriVFJheCBfIXaT2bARzBpAkMYBdtbgd6qvgsYMzD3HjNeyY%2Byipzbbn6CLJuUyETJHTjylULNnBnsSpylEGTN6Ysb8FJbQ%3D";
//        }
        Lg.e("shenfei", "url = " + url);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        if (videoView != null) {
            videoView.changeDataSource(url);
        }
//            }
//        }).start();

    }

    private int screenType = 0;

    public int getScreenType() {
        return screenType;
    }

    @Override
    public void closeAdView() {
    }

    @Override
    public void playerIsSmall(boolean b) {
    }

    /**
     * 设置显示比例
     *
     * @param percent 0:原始, 1 : 铺满, 2:16-9， 3:4-3
     */
    @Override
    public void setPlayerScreen(int percent) {
        if (videoView == null || percent == screenType) {
            return;
        }

        int width, height;

        width = DisplayUtils.getValue(1920);
        height = DisplayUtils.getValue(1080);

        LayoutParams params = (LayoutParams) content_lay.getLayoutParams();

        if (percent == 1) {
            screenType = 1;
            params.height = LayoutParams.MATCH_PARENT;
            params.width = LayoutParams.MATCH_PARENT;
            content_lay.setLayoutParams(params);
        } else if (percent == 2) {
            screenType = 2;
            params.height = width * 21 / 50;
            params.width = LayoutParams.MATCH_PARENT;
            content_lay.setLayoutParams(params);
        } else if (percent == 3) {
            screenType = 3;
            params.height = LayoutParams.MATCH_PARENT;
            params.width = height * 4 / 3;
            params.gravity = Gravity.LEFT;
            content_lay.setGravity(Gravity.LEFT);
            content_lay.setLayoutParams(params);
        } else if (percent == 0) {
            screenType = 0;
            params.height = LayoutParams.MATCH_PARENT;
            params.width = LayoutParams.MATCH_PARENT;
            content_lay.setLayoutParams(params);
        }


    }

    @Override
    public void setScreenNomral() {
        setPlayerScreen(screenType);
    }

    private int decodeType = -1;

    @Override
    public void setPlayerDecode(int ctype) {
        if (videoView == null || decodeType == ctype) {
//            Lg.e("shenfei", "44444");
            return;
        }
        if (ctype == 2) {
//            Lg.e("shenfei", "55555");
            decodeType = 2;
            videoView.changeMediaCode(NVideoView.PV_PLAYER__IjkMediaPlayerC);
        } else if (ctype == 1) {
//            Lg.e("shenfei", "66666");
            decodeType = 1;
            videoView.changeMediaCode(NVideoView.PV_PLAYER__IjkMediaPlayerD);
        } else {
//            Lg.e("shenfei", "77777");
            decodeType = 0;
            videoView.changeMediaCode(NVideoView.PV_PLAYER__AndroidMediaPlayer);
        }
    }

    @Override
    public void start() {
        if (videoView != null) {

//            Lg.e("shenfei", need2pauseForAct +" ------ "+usetea);

            if (need2pauseForAct && !usetea) {
            } else {
                videoView.start();
                if (callBack != null) {
                    callBack.start();
                }

                if (logTools != null) {
                    logTools.setPlayStatus("2");
                }
            }
        }
    }

    @Override
    public void pause() {
        if (videoView != null) {
            videoView.pause();
            if (callBack != null) {
                callBack.pause();
            }

            if (logTools != null) {
                logTools.setPlayStatus("3");
            }
        }
    }

    @Override
    public void stop() {
        if (videoView != null) {
            // videoView.stop();
        }
    }

    @Override
    public void seekTo(long seektime) {
        final long s = seektime;
        if (logTools != null) {
            logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
            logTools.setPlayStatus("2");
        }

//        getHandler().removeCallbacks(seekRunnable);
//        seekRunnable.setS(s);
//        getHandler().postDelayed(seekRunnable, 250l);

//        ThreadExecutor.getInstance().excute(new Runnable() {
//            @Override
//            public void run() {
        playerSeek(s);
//            }
//        });


    }

    private void playerSeek(long s) {
        if (videoView != null) {
            videoView.seekTo((int) s);
            if (!videoView.isPlaying()) {
                if (need2pauseForAct) {

                } else {
                    start();
                }
            }
            if (logTools != null) {
                logTools.realAfterSeek(s);
            }
        }
    }

    private SeekRunnable seekRunnable = new SeekRunnable();

    class SeekRunnable implements Runnable {
        private long s;

        public void setS(long s) {
            this.s = s;
        }

        @Override
        public void run() {
            if (videoView != null) {
                videoView.seekTo((int) s);
                if (!videoView.isPlaying()) {
                    if (need2pauseForAct) {

                    } else {
                        start();
                    }
                }
                if (logTools != null) {
                    logTools.realAfterSeek(s);
                }
            }
        }
    }

    @Override
    public void reset() {
        if (videoView != null) {
            Lg.d(TAG, "reset");
            videoView.reset();
        }
    }

    @Override
    public void release() {
        if (videoView != null) {
            Lg.d(TAG, "relesae");
            videoView.release();
            videoView = null;
        }
    }

    @Override
    public long getCurrentPosition() {
        if (videoView != null) {
            return videoView.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (videoView != null) {
            return videoView.getDuration();
        } else {
            return 0;
        }
    }

    public String getByteRate() {
        if (videoView != null) {
            return videoView.getVideoByteRate();
        } else {
            return "";
        }
    }

    @Override
    public boolean isPlaying() {
        if (videoView != null) {
            return videoView.isPlaying();
        } else {
            return false;
        }
    }

    @Override
    public IMediaPlayer getMediaPlayer() {
        if (videoView != null) {
            return videoView.getMediaPlayer();
        } else {
            return null;
        }
    }


    private long cctime; // log上报的一个标志时间，无逻辑作用
    private long cctime2; // log上报的一个标志时间，无逻辑作用
    private boolean bufpause = false; // 播放卡住

    public void initLogCatch() {
        logTools = new PlayerLogCatchTools(context);
    }

    public void reLogCatch() {
        if (logTools != null) {
            logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
            logTools.setPlayerCurrentPosition(getCurrentPosition());
            logTools.stopCatch();
            logTools = null;

            logTools = new PlayerLogCatchTools(context);
            logTools.startCatch();
        } else {
            logTools = new PlayerLogCatchTools(context);
            logTools.startCatch();
        }
    }

    private long vid, sid;
    private String vname, chId, fid, youkuVideoCode, videotype;
    private int videoKind, category, playType;
    private boolean hasNewDisc = false;

    public void setVideoDisc(long vid, long sid, String vname, int videoKind, String videoType, int category, String chId, String fid, String youkuVideoCode, int playType) {
        try {
            hasNewDisc = true;
            this.vid = vid;
            this.sid = sid;
            this.vname = vname;
            this.videoKind = videoKind;
            this.category = category;
            this.chId = chId;
            this.fid = fid;
            this.youkuVideoCode = youkuVideoCode;
            this.videotype = videoType;
            this.playType = playType;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setHasNewDisc(boolean hasNewDisc) {
        this.hasNewDisc = hasNewDisc;
    }

    private void getVideoDisc() {
        try {
            if (logTools != null && hasNewDisc) {
                hasNewDisc = false;
                logTools.setPlayerVid(vid);
                logTools.setPlayerSid((int) sid);
                logTools.setPlayerVideoName(vname);
                logTools.setVideoKind(videoKind);
                logTools.setCategory(category);
                logTools.setChId(chId);
                logTools.setFid(fid);
                logTools.setYoukuVideoCode(youkuVideoCode);
                logTools.setVideoType(videotype);
                logTools.setPlayType(playType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public long getVidDisc() {
        if (vid <= 0) {
            return Long.parseLong(chId);
        }
        return vid;
    }

    @Override
    public void cleanYouKuPlayer() {

    }

    private boolean isNetConn = true;
    private boolean error_net = false;

    public void onTimeNetReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_TIME_TICK)) {
            Lg.d(TAG, "ACTION_TIME_TICK");
            if (logTools != null && logTools.isLogStart) {
                // 每分钟整点上报 日志采集
//                Lg.e("shenfei", "每分钟整点上报 日志采集");
                logTools.realBeforeSeekOrPreMinTime(getCurrentPosition());
                logTools.setPlayerCurrentPosition(getCurrentPosition());
                if (bufpause) {
                    logTools.playerLoadingTime_end_A();
                }
                logTools.writeLogToDb();
                if (bufpause) {
                    logTools.playerLoadingNum_start_A();
                }
            }
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            isNetConn = NetWorkUtils.isNetworkAvailable(App.getInstance());
            if (isNetConn) {
                //网络联接
                if (callBack != null) {
                    callBack.netchange(1);
                }
            } else {
                //网络断开
                if (callBack != null) {
                    callBack.netchange(0);
                }
            }
        }

    }

}
