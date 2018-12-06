package cn.cibnmp.ott.player.bean;

/**
 *
 */

public class PlayerLogBeanC {

	private String logtype = "play";
	private String playid;
	private String playevent = "1";  //start-开始播放  flash-播放中  end-播放结束
	private String playstatus = "1";  //ready-准备中 playing-播放中 pause-暂停中 buffer-缓存中 end-播放结束
	private long starttime;
	private long endtime;
	private long realplaytime;
	private int ispeed;			//瞬时下载速度(Kb/s)
	private int avgspeed;		//平均下载速度(Kb/s)
	private long startbuftime; //开始播放前的缓冲时间(ms) 启播时间
	private int playerrornum; //起播前的失败次数
	private int pausenum; //暂停次数
	private long pausetime;  //暂停时长(ms)
	private int loadingnum;  //卡顿次数
	private long loadingtime; //卡顿时长(ms)
	private int buffernum;  //缓冲次数
	private long buffertime;  //缓冲时长
	private int videokind; //视频类型1-点播 2-直播 3-轮播
	private String videoname; // 视频名称/直播名称/轮播导航 名称
	private String videotype; // 视频媒资类型
	private int playtype; // 介质类型
	private int cdnhostname; // cdn域名对应的的名


	private int category; //分类比如：0电影、1电视剧、2综艺
	private long vid; //点播媒资标识
	private String chid; //轮播导航ID
	private int sid; //点播媒资剧集
	private String fid; //点播介质标识
	private String playurl; //播放地址
	private String youkuvideocode; //优酷视频CODE
	private long duration; //点播介质时长(ms)
	private long currentposition;
	private int cdnpostnum; //CDN跳转次数
	private String cdnpostaddress; //CDN跳转路径，用分号隔开



	public PlayerLogBeanC(String playId) {
		this.playid = playId;
	}

	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		if (sid == 0) {
			sid = 1;
		}
		this.sid = sid;
	}

	public String getPlayurl() {
		return playurl;
	}

	public String getPlayid() {
		return playid;
	}

	public String getPlayevent() {
		return playevent;
	}

	public String getLogtype() {
		return logtype;
	}

	public int getAvgspeed() {
		return avgspeed;
	}

	public int getBuffernum() {
		return buffernum;
	}

	public int getCategory() {
		return category;
	}

	public int getCdnpostnum() {
		return cdnpostnum;
	}

	public int getIspeed() {
		return ispeed;
	}

	public int getLoadingnum() {
		return loadingnum;
	}

	public int getPausenum() {
		return pausenum;
	}

	public int getPlayerrornum() {
		return playerrornum;
	}

	public int getVideokind() {
		return videokind;
	}

	public long getBuffertime() {
		return buffertime;
	}

	public long getCurrentposition() {
		return currentposition;
	}

	public long getDuration() {
		return duration;
	}

	public long getEndtime() {
		return endtime;
	}

	public long getLoadingtime() {
		return loadingtime;
	}

	public long getPausetime() {
		return pausetime;
	}

	public long getRealplaytime() {
		return realplaytime;
	}

	public long getStarttime() {
		return starttime;
	}

	public long getVid() {
		return vid;
	}

	public String getCdnpostaddress() {
		return cdnpostaddress;
	}

	public String getChid() {
		return chid;
	}

	public String getFid() {
		return fid;
	}

	public String getPlaystatus() {
		return playstatus;
	}

	public String getVideoname() {
		return videoname;
	}

	public void setVid(long vid) {
		this.vid = vid;
	}

	public void setRealplaytime(long realplaytime) {
		this.realplaytime = realplaytime;
	}

	public void setPlayurl(String playurl) {
		this.playurl = playurl;
	}

	public void setPlaystatus(String playstatus) {
		this.playstatus = playstatus;
	}

	public void setAvgspeed(int avgspeed) {
		this.avgspeed = avgspeed;
	}

	public void setBuffernum(int buffernum) {
		this.buffernum = buffernum;
	}

	public void setBuffertime(long buffertime) {
		this.buffertime = buffertime;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setCdnpostaddress(String cdnpostaddress) {
		this.cdnpostaddress = cdnpostaddress;
	}

	public void setCdnpostnum(int cdnpostnum) {
		this.cdnpostnum = cdnpostnum;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}

	public void setCurrentposition(long currentposition) {
		this.currentposition = currentposition;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public void setIspeed(int ispeed) {
		this.ispeed = ispeed;
	}

	public void setLoadingnum(int loadingnum) {
		this.loadingnum = loadingnum;
	}

	public void setLoadingtime(long loadingtime) {
		this.loadingtime = loadingtime;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public void setPausenum(int pausenum) {
		this.pausenum = pausenum;
	}

	public void setPausetime(long pausetime) {
		this.pausetime = pausetime;
	}

	public void setPlayerrornum(int playerrornum) {
		this.playerrornum = playerrornum;
	}

	public void setPlayevent(String playevent) {
		this.playevent = playevent;
	}

	public void setPlayid(String playid) {
		this.playid = playid;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public void setVideokind(int videokind) {
		this.videokind = videokind;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public void setYoukuvideocode(String youkuvideocode) {
		this.youkuvideocode = youkuvideocode;
	}

	public String getYoukuvideocode() {
		return youkuvideocode;
	}

	public String getVideotype() {
		return videotype;
	}

	public void setVideotype(String videotype) {
		this.videotype = videotype;
	}

	public int getPlaytype() {
		return playtype;
	}

	public void setPlaytype(int playtype) {
		this.playtype = playtype;
	}

	public int getCdnhostname() {
		return cdnhostname;
	}

	public void setCdnhostname(int cdnhostname) {
		this.cdnhostname = cdnhostname;
	}

	public long getStartbuftime() {
		return startbuftime;
	}

	public void setStartbuftime(long startbuftime) {
		this.startbuftime = startbuftime;
	}
}
