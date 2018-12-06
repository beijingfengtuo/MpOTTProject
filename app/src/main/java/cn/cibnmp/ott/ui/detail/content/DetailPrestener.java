package cn.cibnmp.ott.ui.detail.content;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationBlock;
import cn.cibnmp.ott.bean.NavigationInfoBlockBean;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherBean;
import cn.cibnmp.ott.ui.detail.bean.DetailAutherResultBean;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoResultBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlInfoBean;
import cn.cibnmp.ott.ui.detail.bean.VideoUrlResultBean;

/**
 * Created by axl on 2017/12/27.
 */

public class DetailPrestener {

   public String epgIdParam;

    public Long vid;

    public String contentIdParam;

    private Handler mHandler;

    public String videoType;

    public boolean  hasNavBlock;

    public long sid;

    public String url;

     public boolean need_sid;

     public boolean isLive;//是否是直播

    public final String seriesTypeVideo = "1";//点播type
    public final String seriesTypeLive = "2";//直播type

    public int pay_status= 0;//付费状态  // 0免费不付费，1付费，2付费鉴权通过



    private static final String TAG = "DetailPrestener";

    public VideoUrlResultBean videoUrlResultBean;
    public VideoUrlInfoBean videoUrlInfoBean;

    public void requestDetailContent(){
        HttpRequest.getInstance().excute("getDetailContent", new Object[]{App.epgUrl,
                epgIdParam, vid + "", 10 * 60, new SimpleHttpResponseListener<DetailInfoResultBean>() {

            @Override
            public void onSuccess(DetailInfoResultBean response) {
                        handleRequestContentSuccess(response);
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(2006);
            }
        }});

}

    protected DetailInfoResultBean resultBean;
    protected DetailBean detailBean;

    protected void handleRequestContentSuccess(DetailInfoResultBean response) {

        try {
            Log.d(TAG, "getDetailNavContent : result--> " + response.toString());
            resultBean = response;
            //数据判断
            if (resultBean == null||!resultBean.getCode().equalsIgnoreCase("1000")||resultBean.getData() == null) {
                Log.e(TAG, "getDetailNavContent response parse to entity failed , data is invalid !!!");
                mHandler.sendEmptyMessage(2006);
                return;
            }  else if (resultBean.getCode().equalsIgnoreCase("1001")) {
                Log.e(TAG, "getDetailNavContent result's data is 1001 !!!");
                mHandler.sendEmptyMessage(20064);
                return;
            } else {

                videoType = resultBean.getData().getVideoType();

                if (resultBean.getData().getChild() != null && resultBean.getData().getChild().size() > 0) {
                    if (!need_sid) {
                        sid = resultBean.getData().getChild().get(0).getSid();
                    }
                } else {
//                    initPlayer(video_C);
//                    normalPlayer.setPlayDataSource("", 0l);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            if (playerPage == null) {
//                                initPlayer(video_C);
//                            }
//                            url = "";
//                            normalPlayer.setPlayDataSource("", 0l);
                        }
                    }, 0);
                }

                mergeData(resultBean.getData());

                //更新数据
                mHandler.sendEmptyMessageDelayed(2001, 200);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 详情页鉴权
     *
     */

    public void getDetailProductAuth(){
        final DetailAutherBean detailAutherBean = new DetailAutherBean();
        if(isLive){
            if(contentIdParam!=null)
                detailAutherBean.setSeriesId(Long.parseLong(contentIdParam));

            detailAutherBean.setSeriesType(Integer.valueOf(seriesTypeLive));
        }else {

                detailAutherBean.setSeriesId(vid);

            detailAutherBean.setSeriesType(Integer.valueOf(seriesTypeVideo));
        }

        HttpRequest.getInstance().excute("getVideoinfProductAuthen",new Object[]{App.bmsurl, JSON.toJSONString(detailAutherBean),new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                if(!TextUtils.isEmpty(response)){
                    try {
                        DetailAutherResultBean detailAutherResultBean = JSON.parseObject(response,DetailAutherResultBean.class);
                        if(detailAutherResultBean!=null && detailAutherResultBean.getResultCode().equals("2")){
                            pay_status= 2;
                        }
                        mHandler.sendEmptyMessage(2003);
                    }catch (Exception e){
                        mHandler.sendEmptyMessage(2003);
                    }


                }
                mHandler.sendEmptyMessage(2003);
            }

            @Override
            public void onError(String error) {
                mHandler.sendEmptyMessage(2003);
            }
        }});

    }

    /**
     * 请求播放地址
     */

    public void reuquestPlayerDataSource(){
        if (vid == 0l || sid == 0l) {
//            if (playerPage == null) {
//                initPlayer(video_C);
//            }
            url="";
           // normalPlayer.setPlayDataSource("", 0l);
            return ;
        }

        HttpRequest.getInstance().excute("getVideoUrl",new Object[]{App.epgUrl,epgIdParam,vid+"",sid+""+pingUrl(),
                CacheConfig.cache_an_hour,new SimpleHttpResponseListener<VideoUrlResultBean>() {
            @Override
            public void onSuccess(VideoUrlResultBean response) {
                handleRequestUrlSuccess(response);
            }

            @Override
            public void onError(String error) {

            }
        }});

    }


    public List<LayoutItem> layoutItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> infoItemBeanList = new ArrayList<>();

    public List<LayoutItem> newLaytItemList = new ArrayList<>();

    public List<NavigationInfoItemBean> newInfoItemBeanList = new ArrayList<>();



    /**
     * 带有容错处理的数据合并
     *
     * @param data
     */
    private void mergeData(DetailInfoBean data) {
        newLaytItemList.clear();
        newInfoItemBeanList.clear();
        if (data.getBlocks() == null || data.getBlocks().isEmpty()
                || data.getContent() == null || data.getContent().isEmpty()) {
//            Lg.e(TAG, "mergeData failed , navId = " + navId);
//            sendContentError("数据异常");
            return;
        }

        int blockCount;  //block的数量
        NavigationBlock block;
        NavigationInfoBlockBean infoBlockBean;
        NavigationInfoItemBean infoItemBean;

        blockCount = Math.min(data.getBlocks().size(), data.getContent().size());

        if (newLaytItemList == null || newInfoItemBeanList == null)
            return;

//        newLaytItemList.clear();
//        newInfoItemBeanList.clear();

        JSONObject object = null;
        LayoutItem layoutItem = null;
        JSONObject jsonObject = null;
        JSONArray array = null;
        String layout = null;
        int lineSize = 0;
        NavigationInfoItemBean itemBean;

        for (int j = 0; j < blockCount; j++) {
            block = data.getBlocks().get(j);
            infoBlockBean = data.getContent().get(j);
            if (block.getLayout() != null && !TextUtils.isEmpty(block.getLayout().getLayoutJson())) {
                layout = block.getLayout().getLayoutJson();
                try {

                    //设置block的标题的布局和内容，只要当nameType都是0的时候，才显示标题内容
                    if (block.getNameType() == NavigationInfoBlockBean.SHOWNAV
                            && infoBlockBean.getNameType() == NavigationInfoBlockBean.SHOWNAV) {
                        layoutItem = new LayoutItem();
                        layoutItem.setC(120l);
                        layoutItem.setR(6l);
                        newLaytItemList.add(layoutItem);

                        itemBean = new NavigationInfoItemBean();
                        itemBean.setViewtype(HomeOneViewType.title_viewType);
                        itemBean.setName(infoBlockBean.getName());
                        newInfoItemBeanList.add(itemBean);
                    }

                    //wanqi，测试布局使用
//                    if (navId.equals("6") && block.getPosition() == 1) {
//                        layout = "{\"layout\":[{\"c\":120,\"r\":15.7},{\"c\":26.5,\"r\":34.6},{\"c\":67.9,\"r\":34.6},{\"c\":26.5,\"r\":34.6},{\"c\":30,\"r\":17.3},{\"c\":30,\"r\":17.3},{\"c\":30,\"r\":17.3},{\"c\":30,\"r\":17.3}]}";
//                    }

                    //wanqi,测试容错性
//                    infoBlockBean.getIndexContents().remove(infoBlockBean.getIndexContents().size() - 1);
//                    infoBlockBean.getIndexContents().addAll(infoBlockBean.getIndexContents());


                    jsonObject = new JSONObject(layout);

//                    lineSize = jsonObject.getInt("lineSize");
                    array = jsonObject.getJSONArray("layout");
//                    int aac = 0;
//                    if (lineSize > 0) {
//                        aac = lineSize;
//                    } else {
//                        aac = array.length();
//                    }
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        layoutItem = new LayoutItem();
                        if (object.has("c"))
                            layoutItem.setC(object.getDouble("c"));
                        else if (object.has("C"))
                            layoutItem.setC(object.getDouble("C"));

                        if (object.has("r"))
                            layoutItem.setR(object.getDouble("r"));
                        else if (object.has("R"))
                            layoutItem.setR(object.getDouble("R"));
                        newLaytItemList.add(layoutItem);


                        if (infoBlockBean.getIndexContents() != null && i < infoBlockBean.getIndexContents().size()) {
                            infoItemBean = infoBlockBean.getIndexContents().get(i);

                            //wanqi,test
//                            if (infoItemBean.getViewtype() == ViewHolderHelper.viewpager_viewType)
//                                infoItemBean.setViewtype(ViewHolderHelper.common_viewType);
//                            if(infoItemBean.getViewtype() == ViewHolderHelper.record_viewType)
//                                infoItemBean.setViewtype(ViewHolderHelper.common_viewType);
                            //wanti,test
//                            infoItemBean.setViewtype(ViewType.commonList_viewType);

                            newInfoItemBeanList.add(infoItemBean);


                        } else {
                            newInfoItemBeanList.add(new NavigationInfoItemBean(HomeOneViewType.space_viewType));
                        }


                    }

//                    if (layoutLoadListener != null) {
//                        layoutLoadListener.OnLayoutLoad(navId, newLaytItemList);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    sendContentError("数据异常");
//                    Lg.e(TAG, "mergeLayout : navId = " + navId + " , blockId = " + block.getBlockId()
//                            + " , parse layout failed , layout json is : " + layout);
                }

            } else if (block.getLayout() == null && block.getLayout().getLayoutJson() != null) {

            } else {

                if (infoBlockBean.getIndexContents() != null && infoBlockBean.getIndexContents().size() > 0 && (infoBlockBean.getIndexContents().get(0).getViewtype() == 10 || infoBlockBean.getIndexContents().get(0).getViewtype() == 11)) {
                    layoutItem = new LayoutItem();
                    layoutItem.setC(120l);
                    layoutItem.setR(6l);
                    newLaytItemList.add(layoutItem);

                    itemBean = new NavigationInfoItemBean();
                    itemBean.setViewtype(HomeOneViewType.title_viewType);
                    itemBean.setName(infoBlockBean.getName());
                    newInfoItemBeanList.add(itemBean);
                    initTag(infoBlockBean);
                }
//                sendContentError("数据异常");
//                Lg.e(TAG, "mergeLayout : navId = " + navId + " , blockId = " + block.getBlockId()
//                        + " , layout is invalid , layout is null or layoutJson is null !");
            }
        }

//        object = null;
//        layoutItem = null;
//        jsonObject = null;
//        block = null;
//        array = null;
//        layout = null;
//        infoBlockBean = null;
//        infoItemBean = null;
//        itemBean = null;
//        data = null;

        if (newInfoItemBeanList.size() > 0 && newLaytItemList.size() > 0) {
            hasNavBlock = true;
        }

        Log.d(TAG, "------merge layout result --> " + newLaytItemList.toString());

    }

    protected void initTag(NavigationInfoBlockBean infoBlockBean) {

        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher;

        for (NavigationInfoItemBean infoBean : infoBlockBean.getIndexContents()) {
            LayoutItem layoutItem = new LayoutItem();
            String s = infoBean.getDisplayName();
            if (s != null) {
                char[] c = s.toCharArray();
                double w = 0;
                for (int x = 0; x < c.length; x++) {
                    matcher = pattern.matcher(c[x] + "");
                    if (matcher.matches()) {
                        w += 3d;
                    } else {
                        w += 1.8d;
                    }
                }
                layoutItem.setC(w + 4d);
                layoutItem.setR(6d);
                newLaytItemList.add(layoutItem);
                newInfoItemBeanList.add(infoBean);
            }
        }
    }

    //拼接播放地址
    public String  pingUrl(){
        StringBuilder sb =new StringBuilder() ;
        sb.append("&projectId=");
        sb.append(String.valueOf(App.projId));
        sb.append("&appId=");
        sb.append(String.valueOf(App.appId));
        sb.append("&channelId=");
        sb.append(String.valueOf(App.channelId));
        sb.append("&cibnUserId=");
        sb.append(String.valueOf(App.userId));
        sb.append("&termId=");
        sb.append(App.publicTID);
        sb.append("&sessionId=");
        sb.append(App.sessionId);

        return  sb.toString();

    }

      public void   handleRequestUrlSuccess(VideoUrlResultBean bean){

            Log.i(TAG,bean.toString());

            videoUrlResultBean= bean;
            if(videoUrlResultBean!=null||!videoUrlResultBean.getCode().equalsIgnoreCase("1000")|| videoUrlResultBean.getData()==null
                    ||videoUrlResultBean.getData().getMedia() == null||videoUrlResultBean.getData().getMedia().size() <= 0){
                //开启错误播放事件

            }
            videoUrlInfoBean = videoUrlResultBean.getData();
            //延时200毫秒处理
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(videoUrlInfoBean.getMedia()!=null&&videoUrlInfoBean.getMedia().size()>0){

                        if(pay_status==1){
                            if(!videoUrlInfoBean.getAuthCode().equals("3")){
                                //f
                            }
                        }
                    }

                }
            },200);

      }

}
