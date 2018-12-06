package cn.cibnmp.ott.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.AboutUsDataBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.player.bean.PlayerLogBeanA;
import cn.cibnmp.ott.player.bean.PlayerLogBeanB;
import cn.cibnmp.ott.player.bean.PlayerLogBeanC;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;
import cn.cibnmp.ott.utils.TimeUtils;

public class PlayerLogCatchTools {

    private Context context;

    private Timer timer;
    private TimerTask task;

    private PlayerLogBeanA log_a;
    private PlayerLogBeanB log_b;
    private PlayerLogBeanC log_c;

    private long initovertime; // 初始化完成的时间

    private long pausetime_a, pausetime_b; // 卡顿开始的时间

    private long bufftime_a, bufftime_b; // 缓冲开始时间

    private int s_num; // 速度采样次数
    private int s_sun; // 速度采样总和

    private int total_play; // 尝试播放次数
    private int play_er_num; // 起播前的失败次数(持续累加结果)(鉴权失败,网络原因,边缘节点无数据下载等)
    private int play_end_num; // 播放过程中，用户正常退出次数
    private int play_er_end_num; // 播放过程中，用户故障退出次数

    private String playid;

    private String bitRate = "1404kbps";

    private SharedPreferences sp;

    public boolean isLogStart = false;

    public PlayerLogCatchTools(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("PLAYERLOGCATCH", 0);

        playid = System.currentTimeMillis() + App.publicTID;

        log_a = new PlayerLogBeanA(playid);
        log_b = new PlayerLogBeanB(playid);
        log_c = new PlayerLogBeanC(playid);

//		startCatch();

        int r = 1360 + (int) ((1 + new Random().nextInt(10)) * 11);
        bitRate = r + "kbps";
    }

    public void startCatch() {
        isLogStart = true;

//		addPlayerPlayErNum(1);

        long starttime = System.currentTimeMillis();
        log_a.setStarttime(starttime);
        log_b.setStarttime(starttime);
        log_c.setStarttime(starttime);

        timer = new Timer(true);
        task = new TimerTask() {

            @Override
            public void run() {
                long ispeed = NetWorkUtils.getNetSpeed(context);
                if (ispeed >= 50) {
                    setPlayerIspeed((int) ispeed);
                    s_num++;
                    s_sun = s_sun + (int) ispeed;
                    setPlayerAvgspeed(s_sun / s_num);
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void stopCatch() {
        isLogStart = false;

        setPlayerEndTime();
        if (bufftime_b != 0) {
            addPlayerBuffTime_end_B();
        }

        if (log_b.getStartbuftime() == 0) {
            log_b.setStartbuftime(System.currentTimeMillis() - log_b.getStarttime());
        }

        writeEndLogToDb();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    private void postLog(String json) {
        HttpRequest.getInstance().excute("ReportPlayLog", new Object[]{json});
    }

    public void writeLogToDb() {
        if (bufftime_a != 0) {
            addPlayerBuffTime_end_A();
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("alogdata", JSON.toJSONString(log_a));
//		Lg.e("shenfei", log_a.getVid() + "----" + log_a.getVideoname());
        if (log_a.getVid() > 0 || !TextUtils.isEmpty(log_a.getVideoname())) {
            Lg.e("logcatchtool", "A:" + JSON.toJSONString(log_a));
            postLog(JSON.toJSONString(log_a));
        }

        SharedPreferences.Editor localEditor = sp.edit();
        localEditor.putInt("play_total", 0);
        localEditor.putInt("play_end", 0);
        localEditor.putInt("play_er_end", 0);
        localEditor.putInt("play_er", 0);
        localEditor.commit();
        log_a = new PlayerLogBeanA(playid);
        log_a.setVid(log_b.getVid());
        log_a.setVideoname(log_b.getVideoname());
        log_a.setSid(log_b.getSid());
        log_a.setFid(log_b.getFid());
        log_a.setPlayurl(log_b.getPlayurl());
        log_a.setDuration(log_b.getDuration());
        log_a.setStarttime(System.currentTimeMillis());
        log_a.setCdnpostnum(postNum);
        log_a.setCdnpostaddress(postAddress);
        log_a.setCdnhostname(log_c.getCdnhostname());
        log_a.setPlaytype(log_c.getPlaytype());
        log_a.setVideotype(log_c.getVideotype());
        log_a.setPlaystatus(playstatus);
        log_a.setCategory(log_c.getCategory());
        log_a.setChid(log_c.getChid());

    }

    public void writeEndLogToDb() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("blogdata", JSON.toJSONString(log_b));
//		Lg.e("shenfei", log_a.getVid() + "----" + log_a.getVideoname());
        if (log_b.getVid() > 0 || !TextUtils.isEmpty(log_a.getVideoname())) {
            Lg.e("logcatchtool", "B:" + JSON.toJSONString(log_b));
            postLog(JSON.toJSONString(log_b));
        }
    }

    public void writeStartLogToDb() {
        if (log_c.getVid() > 0 || !TextUtils.isEmpty(log_a.getVideoname())) {
            Lg.e("logcatchtool", "C:" + JSON.toJSONString(log_c));
            postLog(JSON.toJSONString(log_c));
        }
    }

    public void addPlayerPlayErNum(int i) {
        log_b.setPlayerrornum(i);

//		if (i == 1) {
//			log_a.setPlayErrorNum(sp.getInt("play_er", 0));
//			int a = log_a.getPlayErrorNum();
//			log_a.setPlayErrorNum(a + 1);
//		} else {
//			log_a.setPlayErrorNum(sp.getInt("play_er", 0));
//			int a = log_a.getPlayErrorNum();
//			if (a <= 0) {
//				log_a.setPlayErrorNum(0);
//			} else {
//				log_a.setPlayErrorNum(a - 1);
//			}
//		}
//		SharedPreferences.Editor localEditor = sp.edit();
//		localEditor.putInt("play_er", log_a.getPlayErrorNum());
//		localEditor.commit();
    }

    public void setPlayerError(String code, String msg) {
        log_b.setPlayerrornum(1);
        log_b.setErrorcode(code);
        log_b.setErrormsg(msg);
    }

    public void addPlayerBuffNum_start_A() {
        int a = log_a.getBuffernum();
        log_a.setBuffernum(a + 1);

        bufftime_a = System.currentTimeMillis();
    }

    public void addPlayerBuffNum_start_B() {
        int b = log_b.getBuffernum();
        log_b.setBuffernum(b + 1);

        bufftime_b = System.currentTimeMillis();
    }

    public void addPlayerBuffTime_end_A() {
        long ab = log_a.getBuffertime();
        ab = ab + System.currentTimeMillis() - bufftime_a;
        log_a.setBuffertime(ab);
    }

    public void addPlayerBuffTime_end_B() {
        long bb = log_b.getBuffertime();
        bb = bb + System.currentTimeMillis() - bufftime_b;
        log_b.setBuffertime(bb);
    }

    public void playerLoadingNum_start_A() {
        int a = log_a.getLoadingnum();
        log_a.setLoadingnum(a + 1);

        pausetime_a = System.currentTimeMillis();
    }

    public void playerLoadingNum_start_B() {
        int b = log_b.getLoadingnum();
        log_b.setLoadingnum(b + 1);

        pausetime_b = System.currentTimeMillis();
    }

    private long ss;

    public void playerLoadingTime_end_A() {
        long ap = log_a.getLoadingtime();
        long s = System.currentTimeMillis() - pausetime_a;
        if (ss != s && s < 36000000 && s >= 500) {   //判断卡顿时间太离谱
            ap = ap + s;
            ss = s;
        } else {
            int a = log_a.getLoadingnum();
            a = a - 1;
            if (a < 0)
                a = 0;
            log_a.setLoadingnum(a);
        }
        log_a.setLoadingtime(ap);
    }

    public void playerLoadingTime_end_B() {
        long bp = log_b.getLoadingtime();
        long s = System.currentTimeMillis() - pausetime_b;
        if (ss != s && s < 36000000 && s >= 500) {   //判断卡顿时间太离谱
            bp = bp + s;
        } else {
            int b = log_b.getLoadingnum();
            b = b - 1;
            if (b < 0)
                b = 0;
            log_b.setLoadingnum(b);
        }
        log_b.setLoadingtime(bp);
    }

    public void setStartBufferTime() {
        initovertime = TimeUtils.getCurrentTimeInLong();
        log_b.setStartbuftime(initovertime - log_b.getStarttime());
    }

    public void setPlayerEndTime() {
        log_b.setEndtime(System.currentTimeMillis());
    }

    String playstatus;

    public void setPlayStatus(String status) {
        playstatus = status;
        log_a.setPlaystatus(status);
    }

    public String getPlayStatus() {
        return log_a.getPlaystatus();
    }

    public void setPlayerUrl(String url) {
//		Lg.e("shenfei", "setPlayerUrl");
        try {
            if (TextUtils.isEmpty(url)) {
                log_a.setPlayurl(url);
                log_b.setPlayurl(url);
                log_c.setPlayurl(url);
                writeStartLogToDb();
            } else {
                if (url.startsWith("http")) {
//					String a = URLEncoder.encode(url, "UTF-8");
                    log_a.setPlayurl(url);
                    log_b.setPlayurl(url);
                    log_c.setPlayurl(url);
                    postAddress = url;
                    findCdnHostCode(postAddress);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            finalUrl(postAddress);
                            try {
                                postAddress = URLEncoder.encode(postAddress, "UTF-8");
                            } catch (Exception e) {
                            }
                            log_c.setCdnpostnum(postNum);
                            log_c.setCdnpostaddress(postAddress);
                            log_a.setCdnpostnum(postNum);
                            log_a.setCdnpostaddress(postAddress);
                            writeStartLogToDb();
                        }
                    }).start();
                } else {
                    String a = URLEncoder.encode(url, "UTF-8");
                    log_a.setPlayurl(a);
                    log_b.setPlayurl(a);
                    log_c.setPlayurl(a);
                    writeStartLogToDb();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//			Lg.e("shenfei", "http url error");
        }
    }

    public void setPlayerVid(long vid) {
        log_a.setVid(vid);
        log_b.setVid(vid);
        log_c.setVid(vid);
    }

    public void setPlayerVideoName(String vname) {
        log_a.setVideoname(vname);
        log_b.setVideoname(vname);
        log_c.setVideoname(vname);
    }

    public void setVideoKind(int kind) {
        log_a.setVideokind(kind);
        log_b.setVideokind(kind);
        log_c.setVideokind(kind);
    }

    public void setVideoType(String type) {
        log_c.setVideotype(type);
        log_a.setVideotype(type);
        log_b.setVideotype(type);
    }

    public void setPlayType(int type) {
        log_c.setPlaytype(type);
        log_a.setPlaytype(type);
        log_b.setPlaytype(type);
    }

    public void setCategory(int cat) {
        log_a.setCategory(cat);
        log_b.setCategory(cat);
        log_c.setCategory(cat);
    }

    public void setChId(String chId) {
        log_a.setChid(chId);
        log_b.setChid(chId);
        log_c.setChid(chId);
    }

    public void setPlayerSid(int sid) {
        log_a.setSid(sid);
        log_b.setSid(sid);
        log_c.setSid(sid);
    }

    public void setFid(String fid) {
        log_a.setFid(fid);
        log_b.setFid(fid);
        log_c.setFid(fid);
    }

    public void setYoukuVideoCode(String youkuVideoCode) {
        log_a.setYoukuvideocode(youkuVideoCode);
        log_b.setYoukuvideocode(youkuVideoCode);
        log_c.setYoukuvideocode(youkuVideoCode);
    }

    public void setPlayerDuration(long duration) {
        log_a.setDuration(duration);
        log_b.setDuration(duration);
        log_c.setDuration(duration);
    }

    public void setPlayerCurrentPosition(long currentPosition) {
        log_a.setCurrentposition(currentPosition);
        log_b.setCurrentposition(currentPosition);
    }

    public void setPlayerIspeed(int ispeed) {
        log_a.setIspeed(ispeed);
        log_b.setIspeed(ispeed);
        log_c.setIspeed(ispeed);
    }

    public void setPlayerAvgspeed(int avg) {
        log_a.setAvgspeed(avg);
        log_b.setAvgspeed(avg);
        log_c.setAvgspeed(avg);
    }

    //计算实际播放时长方法
    private long realtime = 0l;
    private long realpos = 0l;

    public void realBeforeSeekOrPreMinTime(long p) {
        if (p <= 0) {
            return;
        }
//		if (realpos == 0) {
//			realpos = p;
//		} else {
        realtime += p - realpos;
        realpos = p;
        log_a.setRealplaytime(realtime);
        log_b.setRealplaytime(realtime);
//		}
    }

    public void realAfterSeek(long p) {
        realpos = p;
    }

    private int postNum;
    private String postAddress = "";

    public String finalUrl(String url) {
        String to = url;
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setConnectTimeout(5 * 60 * 1000);
            conn.setReadTimeout(5 * 60 * 1000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.addRequestProperty("Referer", to);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {// 重点注意这句话的递归调用
                to = conn.getHeaderField("Location");
                postAddress = postAddress + ";" + to;
                postNum++;
                return finalUrl(to);
            } else {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return to;
    }

    public void findCdnHostCode(String url) {
        setCdnHostName("0");
        url = url.substring(7);
        String[] c = url.split("/");
        url = c[0];
//		Lg.e("shenfei", url);
        try {
            String s = App.mCache.getAsString(Constant.about_us_config);
//			Lg.e("shenfei", s);
            if (!TextUtils.isEmpty(s)) {
                AboutUsDataBean ab = JSON.parseObject(s, AboutUsDataBean.class);
                if (ab != null && !TextUtils.isEmpty(ab.getCdnHostName())) {
                    String[] a = ab.getCdnHostName().split(";");
                    for (int i = 0; i < a.length; i++) {
//						Lg.e("shenfei", a[i]);
                        String[] b = a[i].split(":");
                        if (url.equals(b[0])) {
//							Lg.e("shenfei", ""+b[1]);
                            setCdnHostName(b[1]);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCdnHostName(String name) {
        if (!TextUtils.isEmpty(name)) {
            try {
                int a = Integer.parseInt(name);
                log_a.setCdnhostname(a);
                log_b.setCdnhostname(a);
                log_c.setCdnhostname(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
