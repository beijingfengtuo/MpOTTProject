package cn.cibnmp.ott.ui.screening;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.ScreeningDataBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.library.pullable.PullToRefreshLayout;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.NetWorkUtils;

/**
 * 筛选页面 - 节目列表页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/4
 */
public class ScreeningListFragment extends BaseFragment {
    private static String TAG = ScreeningListFragment.class.getName();

    private ScreeningActivity mActivity;
    private View root;

    //页面加载数据进图条布局
    private RelativeLayout octv_looding;
    private ImageView octv_ivpi;
    private RotateAnimation animation;

    //上拉下拉控件
    private PullToRefreshLayout mToRefreshLayout;
    //GridView列表控件
    private GridView gridView;
    private ScreeningListAdapter screeningListAdapter;
    private RelativeLayout rlNullData;

    //筛选搜索内容列表
    private List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> contentBeanList;

    //页面传值参数：epgID、频道ID(搜索：0全部)、栏目ID
    private int epgID, subjectID, columnID;
    //页面传值参数：搜索内容
    private String keywords;

    /**
     * 加载View
     **/
    private final int LODING_DATE = 666;
    private final int LODING_ERROR_500 = 500;

    //分页加载
    private int PAGESIZE = 0;
    private int PAGENUM = 30;
    private boolean isLoadMoreFinish = false; //是否加载完成

    public static ScreeningListFragment newInstance(Bundle args) {
        ScreeningListFragment fragment = new ScreeningListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.screening_list, container, false);
        mActivity = (ScreeningActivity) getActivity();

        initView();
        return root;
    }

    private void initView() {
        //进度条布局
        octv_looding = (RelativeLayout) root.findViewById(R.id.octv_looding);
        octv_ivpi = (ImageView) root.findViewById(R.id.octv_ivpi);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);

        mToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.screening_list_refresh);
        gridView = (GridView) root.findViewById(R.id.screening_list_grid);
        mToRefreshLayout.stopPull();

        rlNullData = (RelativeLayout) root.findViewById(R.id.rl_null_data);

        // 下拉和上拉回调监听
        setRefresh();
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            handleLoadScreeningBeanErrorResponse();
            return;
        }
        setData(getArguments());
    }

    /**
     * 上拉下拉回调监听
     */
    private void setRefresh() {
        mToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            /**
             * 下拉刷新
             *
             * @param pullToRefreshLayout
             */
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                getRefresh();
                getScreeningDataBean();
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        ToastUtils.show(context, "施主,没有内容了,请回头吧");
                        try {
                            if (pullToRefreshLayout != null) {
                                //加载完毕
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

            /**
             * 上拉加载
             *
             * @param pullToRefreshLayout
             */
            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                getLoadMore();

                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        ToastUtils.show(context, "施主,没有内容了,请回头吧");
                        try {
                            if (pullToRefreshLayout != null) {
                                // 加载完毕
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void getRefresh() {
        if (contentBeanList != null) {
            contentBeanList.clear();
        }
        PAGESIZE = 0;
        isLoadMoreFinish = false;
    }

    /**
     * 上拉加载
     */
    private void getLoadMore() {
        if (!isLoadMoreFinish) {
            PAGESIZE += 1;
            getScreeningDataBean();
        }
    }

    /**
     * 获取筛选选项卡的内容
     *
     * @param bundle 接口参数
     */
    @Override
    public void setData(Bundle bundle) {
        super.setData(bundle);
        if (bundle == null) {
            return;
        }
        epgID = bundle.getInt(Constant.BUNDLE_EPGID, 0);
        columnID = bundle.getInt(Constant.BUNDLE_CONTENTID, 0);
        subjectID = bundle.getInt(Constant.BUNDLE_SUBJECTID, 0);
        keywords = bundle.getString(Constant.BUNDLE_KEYWORDS);
        getRefresh();
        getScreeningDataBean();
    }

    /**
     * 获取筛选页面选项卡数据
     */
    private void getScreeningDataBean() {
        //测试数据
//        String json = "{\"code\":\"1000\",\"msg\":\"返回成功\",\"data\":{\"subjectId\":999,\"name\":\"好戏连台栏目\",\"epgId\":1031,\"layout\":{\"layoutId\":null,\"name\":null,\"layoutType\":null,\"layoutJson\":null,\"description\":null,\"state\":null},\"blocks\":[],\"content\":[],\"newblocks\":[],\"newcontent\":[],\"listcontent\":{\"total\":12,\"pageSize\":0,\"pageNum\":100,\"content\":[{\"displayName\":\"跟随绍百下乡去（三）\",\"slogan\":\"东方大剧院跟随绍百下乡演出的步伐，为大家带来别开生面的幕后直播。我们将独家采访吴凤花、吴素英、陈飞、张琳等绍百当家台柱，听她们讲述下乡经历与趣事；看他们在后台的点点滴滴以及如何化妆扮戏和演出等。也将带来难得一见的民间戏曲仪式“扫台”。跟随我们的镜头一起观看独家直播吧！\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-08 12:19:43\",\"img\":\"http://114.247.94.68:8181/view/e3b05f7e7b2f0ec156771b7b59013659-260-346\",\"imgh\":\"\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"269459\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2018\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"\"},{\"displayName\":\"王志萍、黄慧《甄嬛》（下本）选段\",\"slogan\":\"王志萍、黄慧上越《甄嬛》（下本）选段\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:35\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/683f072a0c6809e5dd1b0dceff9b0e25-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"196838\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2018\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"京剧大戏《十年之约之龙凤呈祥》\",\"slogan\":\"京剧《龙凤呈祥》是出自《三国演义》中的重要选段，讲述的是孙权因刘备占据荆州不还，与周瑜设美人计，假称孙权之妹孙尚香许婚刘备，以换荆州的故事。在京剧界被誉之为“吉祥戏”，是人们在逢年过节搭台唱戏必点的合家欢式的经典传统剧目。角色众多展示每行当精华；三国多表男人戏，唯此剧彰显巾帼英气；化戾气为祥和的智慧胸襟符合中国人的观念；三国戏偏好杀伐争斗，难得这样暖心之作；无怪乎各院团年节都要尽遣主力\\\"龙凤呈祥\\\"。\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 16:04:18\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/2a11f4fef1a58230efc70504d5a73e3b-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"194580\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"章海灵《韩非子》选段\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 16:04:31\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/3a58d6e37500812fc08b4581f5d36139-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"196845\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"《三看御妹》不得不看的越剧欢喜姻缘\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:04\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/557f688ca59569e266154fd4b52debe2-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"197796\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"\"},{\"displayName\":\"越剧《三看御妹》\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:08\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/f53ae7cd323eb67614e20ad663c0cf2d-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"197693\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"王清《甄嬛》选段\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:32\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/ce402abb150dd61d77a2a7eabe02943b-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"196843\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"吴凤花：我愿为越剧放弃容颜\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 16:09:17\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/12b338a061ff1b20e494fc70f6a46605-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"197793\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"吴素英：舞台上的俏佳人，生活中的女汉子\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 16:09:31\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/73e65d09a7d8176d5cfea9440f4662e1-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"197794\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"《迎春∙反串——越剧经典折子戏》宣传片\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:12\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/6fe2819d4a4299b5819b546952d04c1b-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"196833\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"0\"},{\"displayName\":\"钱惠丽《甄嬛》选段\",\"slogan\":\"\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:06:15\",\"img\":\"\",\"imgh\":\"http://114.247.94.68:8181/view/fa91d3925eb4d55397c6c5c618ac8eea-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_detail_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"196842\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2017\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"\"},{\"displayName\":\"枕上无梦\",\"slogan\":\"万历二十六年，汤显祖向吏部告了长假，回到家乡临川，潜心写戏。\\r\\n远离仕途，与诗文戏曲为伴，这一生到此便再无波澜。\\r\\n当真甘心于此？\\r\\n为何始终烦恼萦怀？为何仍感寂寞气闷？\\r\\n不再信『紫钗』的浪漫，却放不下『还魂』的执着。\\r\\n『南柯』一梦依旧彷徨，『邯郸』之梦又去向何方？\\r\\n戏中人梦里梦外，谁能救赎汤显祖？\\r\\n这半生该何去何从？\",\"scrollMarquee\":\"\",\"playTime\":\"2018-01-02 17:07:13\",\"img\":\"http://114.247.94.68:8181/view/14796825f37ca101cc4cc2f17a38d0b2-260-346\",\"imgh\":\"http://114.247.94.68:8181/view/0af9e3ea595d5c2865af53fbc65c9247-454-256\",\"viewtypecode\":null,\"action\":\"open_normal_list_page\",\"actionUrl\":\"\",\"actionParams\":\"{'p1':'','p2':'','p3':''}\",\"contentType\":\"video\",\"contentId\":\"192565\",\"epgId\":1031,\"vipType\":0,\"topLeftCorner\":\"\",\"bottomLeftCorner\":\"\",\"topRightCorner\":\"\",\"bottomRightCorner\":\"\",\"year\":\"2016\",\"score\":\"0\",\"content\":null,\"pkgName\":\"\",\"clsName\":\"\",\"sid\":\"0\",\"duration\":\"7200\"}]}}}";
//        ScreeningDataBean screeningDataBean = JSON.parseObject(json, ScreeningDataBean.class);
//        handleLoadScreeningBeanSuccessResponse(screeningDataBean);
        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            //当前网络不可用
            Lg.i(TAG, "当前网络异常，请重试连接!");
            mHandler.sendEmptyMessage(LODING_ERROR_500);
            return;
        }

        startLooding();

        Lg.i(TAG, "搜索筛选词：" + keywords);
        if (StringUtils.isEmpty(keywords)) {
            getListDataByAll();
        } else {
            if (keywords.indexOf(";") != -1) {
                boolean isAll = true;
                String[] keyArray = keywords.split(";");
                if (keyArray != null && keyArray.length > 0) {
                    for (String s : keyArray) {
                        if (!StringUtils.isEmpty(s)) {
                            isAll = false;
                            break;
                        }
                    }
                }

                if (isAll) {
                    getListDataByAll();
                } else {
                    getListDataByTag();
                }
            } else {
                getListDataByTag();
            }


        }
    }

    /**
     * 获取节目列表数据（获取全部)
     */
    private void getListDataByAll() {
        startLooding();
        //TODO 参数需重写
//        final String url = App.epgUrl + "/listContent?epgId=" + epgID + "&columnId=" + "999"+ "&pageSize=" + "0"+ "&pageNum=" + "100";
        final String url = App.epgUrl + "/listContent?epgId=" + epgID + "&columnId=" + columnID+ "&pageSize=" + PAGESIZE + "&pageNum=" + PAGENUM;
        HttpRequest.getInstance().excute("getListProgramData", new Object[]{url,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<ScreeningDataBean>() {

            @Override
            public void onSuccess(ScreeningDataBean screeningDataBean) {
                Lg.i(TAG, "筛选页面 - 获取全部筛选后的数据列表：" + screeningDataBean.toString());
                handleLoadScreeningBeanSuccessResponse(screeningDataBean);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "筛选页面 - 获取全部筛选后的数据列表：getHomeNavContent : error--> " + error);
                handleLoadScreeningBeanErrorResponse();
            }
        }});
    }

    /**
     * 获取节目列表数据（根据筛选标签)
     */
    private void getListDataByTag() {
        startLooding();
        //TODO 参数需重写
//        final String url = App.epgUrl + "/searchFilter?epgId=" + epgID + "&pageSize=" + "0"+ "&pageNum=" + "100" + "&keywords=" + "997_year_2018" + "&subjectId=" + "997";
        final String url = App.epgUrl + "/searchFilter?epgId=" + epgID + "&pageSize=" + PAGESIZE + "&pageNum=" + PAGENUM + "&keywords=" + keywords + "&subjectId=" + subjectID;
        HttpRequest.getInstance().excute("getFitlerData", new Object[]{url,
                CacheConfig.cache_no_cache, new SimpleHttpResponseListener<ScreeningDataBean>() {

            @Override
            public void onSuccess(ScreeningDataBean screeningDataBean) {
                Lg.i(TAG, "筛选页面 - 根据筛选标签筛选后的数据列表：" + screeningDataBean.toString());
                handleLoadScreeningBeanSuccessResponse(screeningDataBean);
            }

            @Override
            public void onError(String error) {
                Lg.i(TAG, "筛选页面 - 根据筛选标签筛选后的数据列表：getHomeNavContent : error--> " + error);
                handleLoadScreeningBeanErrorResponse();
            }
        }});

    }

    /**
     * 获取数据成功
     *
     * @param screeningDataBean
     */
    private void handleLoadScreeningBeanSuccessResponse(ScreeningDataBean screeningDataBean) {
        try {
            if (screeningDataBean == null || screeningDataBean.getData() == null || screeningDataBean.getData().getListcontent() == null) {
                handleLoadScreeningBeanErrorResponse();
                return;
            }

            List<ScreeningDataBean.DataBean.ListcontentBean.ContentBean> list = screeningDataBean.getData().getListcontent().getContent();

            if (list != null && list.size() > 0) {
                if (contentBeanList != null) {
                    contentBeanList.addAll(list);
                } else {
                    contentBeanList = new ArrayList<ScreeningDataBean.DataBean.ListcontentBean.ContentBean>();
                    contentBeanList.addAll(list);
                }

                if (list.size() < PAGENUM) {
                    isLoadMoreFinish = true;
                } else {
                    isLoadMoreFinish = false;
                }
            } else {
                if (PAGESIZE > 0) {
                    PAGESIZE -= 1;
                } else {
                    PAGESIZE = 0;
                }
            }

            if (contentBeanList == null || contentBeanList.size() <= 0) {
                handleLoadScreeningBeanErrorResponse();
                return;
            }

            mHandler.sendEmptyMessage(LODING_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据失败
     *
     */
    private void handleLoadScreeningBeanErrorResponse() {
        mHandler.sendEmptyMessage(LODING_ERROR_500);
    }

    /**
     * 添加列表数据
     **/
    private void setGridData() {
        if(screeningListAdapter == null) {
            screeningListAdapter = new ScreeningListAdapter(context, contentBeanList, new HomeOnItemClickListener() {

                @Override
                public void onItemClickListener(Bundle bundle) {
                    HomeJumpDetailUtils.actionToDetailByScreening((ScreeningDataBean.DataBean.ListcontentBean.ContentBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN), getActivity());
                }
            });
            gridView.setAdapter(screeningListAdapter);
        } else {
            screeningListAdapter.setData(contentBeanList);
        }
    }

    /**
     * Handler处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODING_DATE:
                    stopLooding();
                    gridView.setVisibility(View.VISIBLE);
                    rlNullData.setVisibility(View.GONE);
                    setGridData();
                    break;

                case LODING_ERROR_500:
                    stopLooding();
                    rlNullData.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    Lg.i(TAG, getResources().getString(R.string.error_toast));
//                    Toast.makeText(context, getResources().getString(R.string.error_toast), Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开始加载动画
     */
    private void startLooding() {
        if (octv_looding.getVisibility() == View.GONE) {
            octv_looding.setVisibility(View.VISIBLE);
            octv_ivpi.startAnimation(animation);
        }

    }

    /**
     * 停止加载动画
     */
    private void stopLooding() {
        if (octv_looding != null && animation != null) {
            octv_looding.setVisibility(View.GONE);
            animation.cancel();
            octv_ivpi.clearAnimation();
        }
    }

}
