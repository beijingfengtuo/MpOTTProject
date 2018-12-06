package cn.cibntv.ott.lib.ijk;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Map;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;
import tv.danmaku.ijk.media.player.misc.IjkMediaFormat;

/**
 * Created by sh on 16/7/13.
 */

public class NVideoView extends SurfaceView {
    private String TAG = "NVideoView";
    // settable by the client
    private Uri mUri;
    private Map<String, String> mHeaders;

    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    public static final int PV_PLAYER__AndroidMediaPlayer = 0;
    public static final int PV_PLAYER__IjkMediaPlayerD = 1;
    public static final int PV_PLAYER__IjkMediaPlayerC = 2;
    public static final int PV_PLAYER__YoukuPlayer = 3;

    private SurfaceHolder mSurfaceHolder = null;
    private IMediaPlayer mMediaPlayer = null;

    private IMediaPlayer.OnPreparedListener mPreparedListener;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener;

    private String url = "";

    private int media_decode_type = 0;

    private Context mContext;

    int a = 0;

    public boolean isChangeDataSrc = false;
    public int surface_status;

    public NVideoView(Context context) {
        super(context);
        mContext = context;
    }

    public NVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // 其实就是在这里做了一些处理。
//        int width = getDefaultSize(0, widthMeasureSpec);
//        int height = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(width, height);
//    }

    public void initVideoView(int media_decode_type) {

        initPlayer(media_decode_type);
        initSurface();

    }

    private void initPlayer(int media_decode_type) {

        release();

        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        mMediaPlayer = createPlayer(media_decode_type);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void initSurface() {

        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pause();
                surface_status = 1;
                mSurfaceHolder = null;
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(null);
                }
				Log.e("shenfei", "surfaceDestroyed");
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
				Log.e("shenfei", "surfaceCreated");
                surface_status = 2;
                mSurfaceHolder = holder;
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(mSurfaceHolder);
                    mMediaPlayer.setScreenOnWhilePlaying(true);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Log.e("shenfei", "surfaceChanged" + getWidth() + "-----" + getHeight());
//                holder.setFixedSize(getWidth(), getHeight());
//                holder.setFixedSize(300, 100);
            }

        });

        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.setDisplay(null);
        }
    }

    /*
     * release the media player in any state
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    public IMediaPlayer createPlayer(int media_decode_type) {

        this.media_decode_type = media_decode_type;

        IMediaPlayer mediaPlayer = null;

        switch (media_decode_type) {
            case PV_PLAYER__IjkMediaPlayerD: {
                IjkMediaPlayer ijkMediaPlayer = null;
                ijkMediaPlayer = new IjkMediaPlayer();
                 ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_FATAL);
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
                mediaPlayer = ijkMediaPlayer;
            }
            break;
            case PV_PLAYER__IjkMediaPlayerC: {
                IjkMediaPlayer ijkMediaPlayer = null;
                ijkMediaPlayer = new IjkMediaPlayer();
                 ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_FATAL);
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 1);

                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
                mediaPlayer = ijkMediaPlayer;
            }
            break;
            case PV_PLAYER__AndroidMediaPlayer: {
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
            }
            break;
            case PV_PLAYER__YoukuPlayer: {
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
            }
            break;
            default: {
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
            }
            break;
        }

        return mediaPlayer;
    }

    public void optionPlayer(IMediaPlayer mMediaPlayer) {
         ((IjkMediaPlayer) mMediaPlayer).native_setLogLevel(IjkMediaPlayer.IJK_LOG_FATAL);

        if (media_decode_type == PV_PLAYER__IjkMediaPlayerD) {
            ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        } else {
            ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        }

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format",
                IjkMediaPlayer.SDL_FCC_RV32);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 1);

        ((IjkMediaPlayer) mMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
    }

    public void setDataSource(String url) {
        if (mMediaPlayer == null || TextUtils.isEmpty(url)) {
            return;
        }
        this.url = url;
        try {
            mMediaPlayer.setDataSource(this.url);
            prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDataSource() {
        if (mMediaPlayer == null || TextUtils.isEmpty(url)) {
            return "";
        }

        try {
            return mMediaPlayer.getDataSource();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

//    public void setDataSource(FileDescriptor fd, long s, long l) {
//        if (mMediaPlayer == null) {
//            return;
//        }
//        // this.url = url;
//        // if (!this.url.equals("")) {
//
//        try {
//            mMediaPlayer.setDataSource(fd, s, l);
//            prepareAsync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // }
//    }

    public void changeDataSource(String url) {
        if (mMediaPlayer == null || TextUtils.isEmpty(url)) {
            return;
        }
        if (media_decode_type == PV_PLAYER__AndroidMediaPlayer) {
            isChangeDataSrc = true;
        }
        this.url = url;
        try {
            mMediaPlayer.pause();
            mMediaPlayer.reset();
            if (media_decode_type == PV_PLAYER__IjkMediaPlayerD || media_decode_type == PV_PLAYER__IjkMediaPlayerC) {
                optionPlayer(mMediaPlayer);
                mMediaPlayer.setDisplay(mSurfaceHolder);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            } else if (media_decode_type == PV_PLAYER__AndroidMediaPlayer) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            }

            mMediaPlayer.setDataSource(url);
            prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void changeMediaCode(int c) {
        if (mMediaPlayer == null) {
            return;
        }

        if (media_decode_type == PV_PLAYER__AndroidMediaPlayer) {
            if (c == 0) {
                pause();
                start();
            } else if (c == 1){
                pause();
                reset();
                release();
                initPlayer(PV_PLAYER__IjkMediaPlayerD);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            } else {
                pause();
                reset();
                release();
                initPlayer(PV_PLAYER__IjkMediaPlayerC);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            }
        } else if (media_decode_type == PV_PLAYER__IjkMediaPlayerD) {
            if (c == 0) {
                pause();
                stop();
                release();
                initPlayer(PV_PLAYER__AndroidMediaPlayer);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            } else if (c == 1){
                pause();
                start();
            } else {
                pause();
                release();
                initPlayer(PV_PLAYER__IjkMediaPlayerC);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            }
        } else if (media_decode_type == PV_PLAYER__IjkMediaPlayerC) {
            if (c == 0) {
                pause();
                stop();
                release();
                initPlayer(PV_PLAYER__AndroidMediaPlayer);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            } else if (c == 1){
                pause();
                release();
                initPlayer(PV_PLAYER__IjkMediaPlayerD);
                mMediaPlayer.setOnPreparedListener(mPreparedListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
                setVisibility(GONE);
                setVisibility(VISIBLE);
                setDataSource(url);
            } else {
                pause();
                start();
            }
        }
    }

    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void prepareAsync() {
        if (mMediaPlayer != null) {
            mMediaPlayer.prepareAsync();
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getDuration();
        } else {
            return 0;
        }

    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void seekTo(int msec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public int getAudioSessionId() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    public float getVideoOutputFramesPerSecond() {
        if (mMediaPlayer != null && media_decode_type == PV_PLAYER__IjkMediaPlayerD) {
            return ((IjkMediaPlayer) mMediaPlayer).getVideoOutputFramesPerSecond();
        } else {
            return 0.0f;
        }
    }

    public float getVideoDecodeFramesPerSecond() {
        if (mMediaPlayer != null && media_decode_type == PV_PLAYER__IjkMediaPlayerD) {
            return ((IjkMediaPlayer) mMediaPlayer).getVideoDecodeFramesPerSecond();
        } else {
            return 0.0f;
        }
    }

    public String getVideoByteRate() {
        String byteRate = "";
        ITrackInfo trackInfos[] = mMediaPlayer.getTrackInfo();
        if (trackInfos != null) {
            int index = -1;
            for (ITrackInfo trackInfo : trackInfos) {
                index++;

                int trackType = trackInfo.getTrackType();
                IMediaFormat mediaFormat = trackInfo.getFormat();
                if (mediaFormat == null) {
                } else if (mediaFormat instanceof IjkMediaFormat) {
                    switch (trackType) {
                        case ITrackInfo.MEDIA_TRACK_TYPE_VIDEO:
                            byteRate = mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI);
                            break;
                        case ITrackInfo.MEDIA_TRACK_TYPE_AUDIO:
                            break;
                    }
                }
            }
        }
        byteRate = byteRate.substring(0, byteRate.length()-5);
        float a = Integer.parseInt(byteRate) / 8.0f ;
        return a + "kB/s";
    }

    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mPreparedListener = l;
        mMediaPlayer.setOnPreparedListener(l);
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mCompletionListener = l;
        // mMediaPlayer.setOnCompletionListener(l);
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mErrorListener = l;
        mMediaPlayer.setOnErrorListener(l);
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mInfoListener = l;
        mMediaPlayer.setOnInfoListener(l);
    }

    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        mBufferingUpdateListener = l;
        mMediaPlayer.setOnBufferingUpdateListener(l);
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener l) {
        mSeekCompleteListener = l;
        mMediaPlayer.setOnSeekCompleteListener(l);
    }
}
