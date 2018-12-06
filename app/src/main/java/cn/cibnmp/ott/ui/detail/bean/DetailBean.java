package cn.cibnmp.ott.ui.detail.bean;

public class DetailBean {

    private long vid;
    private String vname;
    private String category;
    private String duration;
    private String score;
    private int priceType;
    private int vipType;
    private int zan;
    private int cai;
    private String source;
    private String cpname;
    private String star;
    private String actor;
    private String director;
    private String screenWriter;  //编剧
    private String showHost;
    private String showGuest;
    private String singer;
    private String lyricist; //作词
    private String composer; //作曲
    private String dubbing; //配音
    private String specialdesc; //特色描述
    private String specialcate; //特色类型
    private String studio; //工作室
    private String tvstation; //电视台
    private String musicstyle; //音乐风格
    private String goodsname; //商品名称
    private String goodsbrand; //商品产地
    private String goodsid;
    private String goodssn;
    private String goodsprice; //商品价格
    private String orderphone; //订购价格
    private String sponsor; //供应商
    private String co_sponsor;
    private String buyUrl; //购买二维码
    private String organ; //剧院
    private String award;
    private String level;
    private String brand;
    private String model;
    private String price;
    private String keywords;
    private String gname;
    private String showDate;
    private int transaction;
    private String presenter;
    private String videoType;
    private String troupe;
    private String found;

    public String getTroupe() {
        return troupe;
    }

    public void setTroupe(String troupe) {
        this.troupe = troupe;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    //>>>>>>>>>>>>>>>>>>>>>公共的
    private String storyPlot;
    private String tag;//类型
    private String img; //图片
    private String imgh;
    private String area;
    private String language;
    private int series;
    private int updateNum;
    private String year;

    //--------------------------->>>>>
    //直播的id
    private int liveId;
    //直播的名称
    private String liveName;
    //子集ID
    private int childId;
    //直播状态 0未开始 ， 1已开始 ， 2已结束, 3回看
    private int status;
    //开始时间
    private long startDate;
    //结束时间
    private long endDate;
    private int liveType;
    private int playNum;
    private String guest;
    private long limitTime;

    public long getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    // 旧的字段
//	private String videotype;
//	private String pinyin;
//	private String valias;
//	private int season;
//	private String period;
//	private String section;
//	private String issueyear;
//	private String issuedate;
//	private String videopoint;
//	private String screenwriter;
//	private String posterfid;
//	private long cpid;
//	private int inuse;
//	private int status;
//	private String addstaff;
//	private long addtime;
//	private String updatestaff;
//	private long updatetime;
//	private int scoreflag;
//	private int collectflag;
//	private int ApproveFlag;
//	private RecordBean videoRecord;
//	private long CurStamp;

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getStoryPlot() {
        return storyPlot;
    }

    public void setStoryPlot(String storyPlot) {
        this.storyPlot = storyPlot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setShowGuest(String showGuest) {
        this.showGuest = showGuest;
    }

    public String getShowGuest() {
        return showGuest;
    }

    public void setShowHost(String showHost) {
        this.showHost = showHost;
    }

    public String getShowHost() {
        return showHost;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getSpecialdesc() {
        return specialdesc;
    }

    public void setSpecialdesc(String specialdesc) {
        this.specialdesc = specialdesc;
    }

    public String getSpecialcate() {
        return specialcate;
    }

    public void setSpecialcate(String specialcate) {
        this.specialcate = specialcate;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getCai() {
        return cai;
    }

    public int getPriceType() {
        return priceType;
    }

    public int getUpdateNum() {
        return updateNum;
    }

    public int getVipType() {
        return vipType;
    }

    public String getDuration() {
        return duration;
    }

    public String getCategory() {
        return category;
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public String getCo_sponsor() {
        return co_sponsor;
    }

    public String getComposer() {
        return composer;
    }

    public String getDubbing() {
        return dubbing;
    }

    public String getGoodsbrand() {
        return goodsbrand;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public String getGoodssn() {
        return goodssn;
    }

    public String getImg() {
        return img;
    }

    public String getLyricist() {
        return lyricist;
    }

    public String getMusicstyle() {
        return musicstyle;
    }

    public String getOrderphone() {
        return orderphone;
    }

    public String getOrgan() {
        return organ;
    }

    public String getScreenWriter() {
        return screenWriter;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getTvstation() {
        return tvstation;
    }

    public void setCai(int cai) {
        this.cai = cai;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCo_sponsor(String co_sponsor) {
        this.co_sponsor = co_sponsor;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setDubbing(String dubbing) {
        this.dubbing = dubbing;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setGoodsbrand(String goodsbrand) {
        this.goodsbrand = goodsbrand;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public void setGoodssn(String goodssn) {
        this.goodssn = goodssn;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public void setMusicstyle(String musicstyle) {
        this.musicstyle = musicstyle;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public void setScreenWriter(String screenWriter) {
        this.screenWriter = screenWriter;
    }

    public void setTvstation(String tvstation) {
        this.tvstation = tvstation;
    }

    public void setUpdateNum(int updateNum) {
        this.updateNum = updateNum;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setOrderphone(String orderphone) {
        this.orderphone = orderphone;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTransaction() {
        return transaction;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGname() {
        return gname;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }

    public String getImgh() {
        return imgh;
    }

    public void setImgh(String imgh) {
        this.imgh = imgh;
    }
}

/*
  字段名称	类型	描述
code
string	状态码
msg	string	描述
data		内容集合
data:vid	int	媒资id
data:name	string	名称
data:childType	int	详情页单多级类型0单1电视剧多2综艺多

data:epgvideo		媒资详情对象
data:epgvideo:vid	string	媒资id
data:epgvideo:vname	string	名称
data:epgvideo:series	int	总集数
data:epgvideo:updateNum	int	更新集数
data:epgvideo:storyPlot	string	简介
data:epgvideo:category	string	分类
data:epgvideo:language	string	语言
data:epgvideo:year	string	年份
data:epgvideo:area	string	区域
data:epgvideo:duration	int	时长(秒)
data:epgvideo:tag	string	标签
data:epgvideo:score	string	评分
data:epgvideo:priceType	int	是否收费 1免费 2收费
data:epgvideo:vipType	int	是否是vip
data:epgvideo:zan	string	赞
data:epgvideo:cai	string	踩
data:epgvideo:source	string	来源
data:epgvideo:cpname	string	来源名称
data:epgvideo:star	string	明星
data:epgvideo:actor	string	演员
data:epgvideo:director	string	导演
data:epgvideo:screenWriter	string	编剧
data:epgvideo:showHost	string	节目主持人
data:epgvideo:showGuest	string	嘉宾
data:epgvideo:singer	string	歌手
data:epgvideo:lyricist	string	作词
data:epgvideo:composer	string	作曲家
data:epgvideo:dubbing	string	配音
data:epgvideo:specialdesc	string	特色描述
data:epgvideo:specialcate	string	特色类型
data:epgvideo:studio	string	工作室
data:epgvideo:tvstation	string	电视台
data:epgvideo:musicstyle	string	音乐风格
data:epgvideo:goodsname	string	商品名称
data:epgvideo:goodsbrand	string	商品产地
data:epgvideo:goodsid	string
data:epgvideo:goodssn	string
data:epgvideo:goodsprice	string	商品价格
data:epgvideo:orderphone	string	电话
data:epgvideo:sponsor	string	供应商
data:epgvideo:co_sponsor	string
data:epgvideo:buyUrl	string	二维码地址
data:epgvideo:organ	string	剧院
data:epgvideo:img	string	图片
data:epgvideo:award	string	所获奖项
data:epgvideo:level	string	级别
data:epgvideo:brand	string	品牌
data:epgvideo:model	string	车型
data:epgvideo:price	string	价格
data:epgvideo:keywords	string	关键字
data:epgvideo:gname	string	游戏名称
data:epgvideo:showDate	string	上映日期
data:epgvideo:transaction	int	纪录片版权购买 1可以购买 0 不能购买
data:epgvideo:presenter	string	主持人
data:child		子集详情对象
data:child:sid	string	子集id
data:child:sname	string	子集名称
data:isLimitTime	string	是否试看 0无试看 1试看


data:child		子集详情对象
data:child:sid	string	子集id
data:child:sname	string	子集名称
data:isLimitTime	string	是否试看 0无试看 1试看
data:startTime	string	试看开始时间（毫秒）
data:endTime	string	试看结束时间（毫秒）
data:layout		布局对象
data:layout:layoutId	int	布局id
data:layout:name	string	布局名称
data:layout:layoutType	int	布局类型
data:layout:description	string	布局描述
data:layout:layoutJson	string	布局json
data:layout:state	int	布局状态
data:blocks		Block集合
data:blocks:blockId	int	Blockid
data:blocks:name	String	Block名称
data:blocks:nameType	int	是否显示名称
data:blocks:position	int	Block顺序
data:blocks:positionCount	int	Block内容数量
data:blocks:state	int	Block状态
Data:blocks:layout		布局对象
data:blocks:layout:layoutId	int	布局id
data:blocks:layout:name	string	布局名称
data:blocks:layout:layoutType	int	布局类型
data:blocks:layout:description	string	布局描述
data:blocks:layout:layoutJson	string	布局json
data:blocks:layout:state	int	布局状态
data:conntent		推荐内容集合
data:content:blockId	int	block id
data:content:name	string	block名称
data:content:nameType	int	0显示名称 1不显示名称
data:content:position	int	顺序
data:content:indexContents		内容集合
data:content:indeContents:blockId	int	blockid
data:content:indeContents:position	int	位置
data:content:indeContents:displayName	string	显示名称
data:content:indeContents:name	string	名称
data:content:indeContents:slogan	string	简介
data:content:indeContents:scrollMarquee	string	滚动字幕
data:content:indeContents:playTime	string	播放时间
data:content:indeContents:descInfo	string	描述
data:content:indeContents:img	string	图片地址
data:content:indeContents:imgh	string	图片地址
data:content:indeContents:imgBack	string	图片地址
data:content:indeContents:viewtype	string	viewtype
data:content:indeContents:action	string	跳转aciton
data:content:indeContents:actionUrl	string	actionurl
data:content:indeContents:actionParams	string	action参数
data:content:indeContents:contentType	string	内容类型
data:content:indeContents:contentId	string	内容id
data:content:indeContents:epgId	int	epgid
data:content:indeContents:vipType	int	是否是vip
data:content:indeContents:topLeftCorner	string	左上角标图片
data:content:indeContents:bottomLeftCorner	string	左下角标图片
data:content:indeContents:topRightCorner	string	右上角标图片
data:content:indeContents:bottomRightCorner	string	右下角标图片

 * */