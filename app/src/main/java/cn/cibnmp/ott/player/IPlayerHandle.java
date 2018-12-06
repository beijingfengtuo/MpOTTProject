package cn.cibnmp.ott.player;

import android.content.Context;
import android.content.Intent;

/**
 * Created by sh on 17/3/21.
 */

public interface IPlayerHandle {

    void setPlayDataSource(String url, long seek);
    String getPlayDataSource();
    void start();
    void pause();
    void seekTo(long seektime);
    void stop();
    void reset();
    void release();
    long getCurrentPosition();
    long getDuration();
    boolean isPlaying();
    Object getMediaPlayer();

    void onActivityDestroy();
    void setPlayerCallback(PlayerCallBack callBack);
    void setPlayerScreen(int percent);
    void setPlayerDecode(int ctype);
    void onTimeNetReceive(Context context, Intent intent);

    Object getVideoView();

    void setStartSeek(long startSeek);
    void setPlayDataSourceForTea(String url);
    public void setVideoDisc(long vid, long sid, String vname, int videoKind, String videoType, int category, String chId, String fid, String youkuVideoCode, int playType);
    long getVidDisc();


    public void setScreenNomral();
    public void setErrorLogCatch(String code, String msg);

    public void closeAdView();
    public void playerIsSmall(boolean b);
    public void cleanYouKuPlayer();
    public void setHasNewDisc(boolean b);

}
