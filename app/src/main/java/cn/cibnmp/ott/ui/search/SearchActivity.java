package cn.cibnmp.ott.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.bean.NavigationItemBean;
import cn.cibnmp.ott.config.CacheConfig;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.jni.SimpleHttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.detail.Main;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.view.CustomListView;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.GridViewForScrollView;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.PointsDialog;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.ProgressDialogUtil;

public class SearchActivity extends BaseActivity implements View.OnClickListener ,TextView.OnEditorActionListener{

    // 开始搜索
    public final int START_SEARCH = 1;
    // 推荐搜索
    public final int PROMPT_SEARCH = 2;
    // 无搜索结果
    public final int SEARCH_NO_RESULT = 3;
    // 设置无搜索结果
    public final int SET_NO_RESULT = 4;
    // 设置搜索记录
    public final int SET_RECORD_DATA = 5;
    // 无推荐搜索
    public final int NO_PROMPT_SEARCH = 6;
    // 初始化大家都在搜
    public final int INIT_DATA_SEARCH = 7;
    // 初始化类型
    public final int TYPE_INIT_DATA = 0;
    // 无搜索结果类型
    public final int TYPE_NO_RESULT = 1;
    // 猜你喜欢
    public final int GESS_LIKE_DATA= 11;
    //无搜索结果显示
    public final int No_GESS_LIKE = 12 ;

    private boolean textChangeFlag = true;

    private String search = "0";

    @ViewInject(R.id.ib_back)
    private ImageButton mBack;

    @ViewInject(R.id.ll_content)
    private LinearLayout llContent;

    @ViewInject(R.id.no_result_layout)
    private LinearLayout llNoResult;

    @ViewInject(R.id.tv_search)
    private TextView mSearch;

    @ViewInject(R.id.tv_no_result)
    private TextView mNoResult;

    @ViewInject(R.id.tv_everyone_search)
    private TextView mTvEveryone;

    @ViewInject(R.id.et_search_content)
    private EditText mEdit;

    @ViewInject(R.id.iv_clear)
    private ImageView mClearHis;

    @ViewInject(R.id.rl_history)
    private View mRL;

    @ViewInject(R.id.viewline)
    private View viewline;

    @ViewInject(R.id.lv_history)
    private ListView mListView;

    @ViewInject(R.id.ll_result_search)
    private LinearLayout llResult;

    @ViewInject(R.id.tv_result_title)
    private TextView tvResultTitle;

    @ViewInject(R.id.gv_history)
    private GridViewForScrollView mGridView;

    @ViewInject(R.id.lv_recommend)
    private RecyclerView mListNoResult;

    @ViewInject(R.id.lv_prompt)
    private CustomListView mPromptView;

    @ViewInject(R.id.ll_chage_keyword)
    private LinearLayout llChangeWord;

    private SearchResultEntity searchResult;
    private List<NavigationItemBean> labelList;

    private InputMethodManager imm;
    private String editKeyword;
    private SearchAdapter listAdapter;
    private GessLikeAdapter gessLikeadapter;
    private Bundle mBundle;
    private GessListBean gessListBean;
    private SearchRecord recordBean;
    private SearchRecordAdapter mRecordAdapter;
    private PromptListAdapter mPromptListAdapter;
    private String epjId;

    private String TAG="SearchActivity";
    private SearchResultEntity.DataBean.ListcontentBean listcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        x.view().inject(this); // 注入view和事件
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.activityBundleExtra);
        epjId = App.hostEpgId;

        mEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textChangeFlag) {
                    editKeyword = s.toString().trim();
                    if (TextUtils.isEmpty(editKeyword)) {
                        //没有内容显示刚进入的页面
//						mPromptView.setVisibility(View.GONE);
//						mRL.setVisibility(View.VISIBLE);1

//						mGridView.setVisibility(View.VISIBLE);
//						llChangeWord.setVisibility(View.GONE);
                    } else {
                        judgenull(true, editKeyword,search);
                    }
                }
                textChangeFlag = true;
            }

        });

        // 猜你喜欢
        requesBlockContent(TYPE_INIT_DATA);
        // 历史记录
        searchRecordData();

    }


    private void initView() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mClearHis.setOnClickListener(this);
        mEdit.setOnEditorActionListener(this);
        // 显示软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(mEdit.getWindowToken(), 0);
    }

    private void onRecyclerItemClickListener() {

        gessLikeadapter.setOnRecyclerViewItemListener(new GessLikeAdapter.OnRecyclerViewItemListener() {
            @Override
            public void onItemClickListener(View view, int position) {

                if (gessListBean == null || gessListBean.getData().getIndexContents() == null) {

                    return;
                }
                addRecord(gessListBean.getData().getIndexContents().get(position).getName());
                String action=gessListBean.getData().getIndexContents().get(position).getAction();
                //TODO 跳转
                SearchUtils.actionTo(gessListBean,SearchActivity.this,position);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {

            }
        });

    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_SEARCH:
                    // 搜索结果
                    ProgressDialogUtil.dismissDialog();
                    showSearchResult();
                    break;
                case PROMPT_SEARCH:
                    // 推荐历史搜索
                    ProgressDialogUtil.dismissDialog();
                      setPromptData();
                    break;
                case SEARCH_NO_RESULT:
                    // 无搜索接口调用
                    ProgressDialogUtil.dismissDialog();
                    llChangeWord.setVisibility(View.VISIBLE);
                    setNoResultData();
//                    searchNoResultData(TYPE_NO_RESULT);
                    break;
                case SET_NO_RESULT:
                    // 无搜索接口调用
                    ProgressDialogUtil.dismissDialog();
                    llChangeWord.setVisibility(View.VISIBLE);
                    setNoResultData();
                    break;
                case SET_RECORD_DATA:
                    // 设置搜索记录
                    setGridData();
                    break;
                case NO_PROMPT_SEARCH:
                    ProgressDialogUtil.dismissDialog();
                    // 无相关联想词
                    break;
                case INIT_DATA_SEARCH:
                    ProgressDialogUtil.dismissDialog();
                    // 热搜
                    llChangeWord.setVisibility(View.GONE);
                    break;
                case GESS_LIKE_DATA:
                    ProgressDialogUtil.dismissDialog();
                    // 猜你喜欢
                    setGessLike();
                    break;
                case No_GESS_LIKE:
                    setGessLikeTwo();
                    break;

            }
            super.handleMessage(msg);
        }
    };


    private SearchResultEntity promptResultBean;
    private void setPromptData() {
        llContent.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        viewline.setVisibility(View.GONE);
        mRL.setVisibility(View.GONE);
        mGridView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        mPromptView.setVisibility(View.VISIBLE);

        promptResultBean = searchResult ;
        if (promptResultBean == null || promptResultBean.getData()== null) {
            mPromptListAdapter = new PromptListAdapter(this, null,editKeyword);

        } else{
            mPromptListAdapter = new PromptListAdapter(this,
                    promptResultBean.getData().getListcontent().getContent(), editKeyword);
            mPromptView.setAdapter(mPromptListAdapter);
        }

        mPromptView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (promptResultBean == null || promptResultBean.getData() == null) {
                    return;
                }
                editKeyword = promptResultBean.getData().getListcontent().getContent().get(position).getDisplayName();
                textChangeFlag = false;
                mEdit.setText(editKeyword);
                ProgressDialogUtil.showDialog(SearchActivity.this);
                addRecord(promptResultBean.getData().getListcontent().getContent().get(position).getDisplayName());
                judgenull(false, promptResultBean.getData().getListcontent().getContent().get(position).getDisplayName(),"1");
            }
        });
    }

    // 搜索失败
    private void setNoResultData() {
        // 隐藏键盘
        imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
        llContent.setVisibility(View.GONE);
        llNoResult.setVisibility(View.VISIBLE);
        if (listcontent.getContent() == null || listcontent.getContent().size() == 0) {
            mTvEveryone.setVisibility(View.GONE);
            mListNoResult.setVisibility(View.GONE);
        } else {
            mTvEveryone.setVisibility(View.VISIBLE);
            mListNoResult.setVisibility(View.VISIBLE);
        }
        String str = "<font color='#a40000'>" + editKeyword + "</font>";
        mNoResult.setText(Html.fromHtml(getString(R.string.lmsorry)+"“" + str + "”"+getString(R.string.searchresult)));
        // 猜你喜欢
        requesBlockContentTwo();
    }


    /**
     *  设置历史记录
     */
    private void setGridData() {
        if (recordBean == null || recordBean.getVideoCollectList() == null
                || recordBean.getVideoCollectList().size() == 0) {
            mGridView.setVisibility(View.GONE);
            mRL.setVisibility(View.GONE);
            return;
        }
        mRL.setVisibility(View.VISIBLE);
        mRecordAdapter = new SearchRecordAdapter(this,
                recordBean.getVideoCollectList());
        mGridView.setAdapter(mRecordAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                search = "1";
                editKeyword = recordBean.getVideoCollectList().get(position).keyword;
                textChangeFlag = false;
                mEdit.setText(editKeyword);
                if (editKeyword.equals("")) {
                    ToastUtils.show(SearchActivity.this, "请输入您想要搜的关键字");
                    return;
                }
                ProgressDialogUtil.showDialog(SearchActivity.this);
                judgenull(false, recordBean.getVideoCollectList().get(position).keyword,search);
            }
        });
    }



    /**
     * 显示搜索的结果
     */
    private void showSearchResult() {
        // 隐藏键盘
        imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
        llContent.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        viewline.setVisibility(View.GONE);
        mRL.setVisibility(View.GONE);
        mGridView.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mPromptView.setVisibility(View.GONE);

        if (listcontent == null || listcontent.getContent() == null
                || listcontent.getContent().size() == 0) {
            return;
        }
        String str = "<font color='#e50009'>" + editKeyword + "</font>";
        tvResultTitle.setText(Html.fromHtml("“" + str + "”的搜索结果:"));
        listAdapter = new SearchAdapter(this, searchResult.getData().getListcontent().getContent(),
                editKeyword);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               SearchOneUtils.actionTo(searchResult,SearchActivity.this,position);
//                mBundle = new Bundle();
//                mBundle.putString(Constant.contentIdKey,
//                        listcontent.getContent().get(position).getAction());
//                startActivity(listcontent.getContent().get(position).getAction(),
//                        mBundle);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                // 隐藏键盘
                imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
                editKeyword = mEdit.getText().toString().trim();
                if (editKeyword == null || "".equals(editKeyword)) {
                    ToastUtils.show(SearchActivity.this, "请输入您想要搜的关键字");
                    return;
                }
                ProgressDialogUtil.showDialog(this);
                judgenull(false, editKeyword ,search);
                break;

            case R.id.tv_search:
                finish();
                break;

            case R.id.iv_clear:
                PointsDialog.showDialog(SearchActivity.this,
                        new PointsDialog.IClickListener() {
                            @Override
                            public void confirm() {
                                // 清空历史记录
                                deleteLocalData();
                                mRL.setVisibility(View.GONE);
                            }
                        });
                PointsDialog.setPoints(getResources().getString(
                        R.string.clear_record));
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 键盘上的搜索按钮
            editKeyword = mEdit.getText().toString().trim();
            if (editKeyword.equals("")) {
                ToastUtils.show(SearchActivity.this, "请输入您想要搜的关键字");
                return true;
            }
            judgenull(false, editKeyword,search);
            return true;
        }
        return false;
    }
    /**
     * 判断edit是否为""
     *
     * @param isPrompt
     * @param editKeyword
     */
    private void judgenull(boolean isPrompt, String editKeyword,String search) {
        if (editKeyword.equals("")) {
            ToastUtils.show(this, "请输入您想要搜的关键字");
        } else {

            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(editKeyword);
            if (m.find()) {
                requesetNetWork(isPrompt, editKeyword.replaceAll("[-]"," "),search);
            }else {
                requesetFzzuyNetWork(isPrompt, editKeyword);
            }


        }
    }

    //搜索
    private void requesetNetWork(final boolean isPrompt, String editKeyword, String search) {
        //        String url = "http://114.247.94
// .86:8010/search?epgId=1000&pageSize=0&pageNum=20&keywords=111&subjectId=0";
        final String endiKeywordHistory = editKeyword;
        try {
            editKeyword =URLEncoder.encode(editKeyword, "UTF-8");
            final String url = App.epgUrl;
            HttpRequest.getInstance().excute("getSearchResult", url, epjId,
                    editKeyword, 0, CacheConfig.cache_no_cache, "1" , new
                            SimpleHttpResponseListener<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    Log.i(TAG, "onSuccess: "+ response);
                                    searchResult = JSON.parseObject(response,
                                            SearchResultEntity.class);
                                    listcontent = searchResult.getData().getListcontent();
                                    Message msg = Message.obtain();
                                    if (listcontent.getContent() == null || listcontent.getContent()
                                            .size() == 0) {
                                        if (!isPrompt) {
                                            // 没有搜索结果的消息
                                            msg.what = SEARCH_NO_RESULT;
                                        } else {
                                            // 没有推荐的搜索
                                            msg.what = NO_PROMPT_SEARCH;
                                        }

                                    } else if (searchResult
                                            .getData().getListcontent() == null
                                            || searchResult
                                            .getData().getListcontent().getContent().size() == 0) {

                                        if (!isPrompt) {
                                            msg.what = SEARCH_NO_RESULT;
                                        } else {
                                            msg.what = NO_PROMPT_SEARCH;
                                        }
                                    } else {
                                        if (isPrompt) {
                                            msg.what = PROMPT_SEARCH;
                                        } else {
                                            //搜寻结果
                                            addRecord(endiKeywordHistory);
                                            msg.what = START_SEARCH;
                                        }
                                    }
                                    handler.sendMessage(msg);
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("TAG", "getHotword onError , " + error == null ? "" : error);
                                    ProgressDialogUtil.dismissDialog();
                                    SearchActivity.this
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgressDialogUtil
                                                            .dismissDialog();
                                                    ToastUtils
                                                            .show(SearchActivity.this,
                                                                    getResources()
                                                                            .getString(
                                                                                    R.string.error_toast));
                                                }
                                            });
                                    //弹出获取失败，请重试，或者其他处理，需要处理，wanqi
                                }
                            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //模糊搜索
    private void requesetFzzuyNetWork(final boolean isPrompt, String editKeyword) {
        //        String url = http://114.247.94.86:8010/
        // searchFeferral?epgId=1031&subjectId=0&pageSize=10&pageNum=0&keywords=j
        final String endiKeywordHistory = editKeyword;
        try {
            editKeyword =URLEncoder.encode(editKeyword, "UTF-8");
            final String url = App.epgUrl;
            HttpRequest.getInstance().excute("getFuzzySearchResult", url, epjId,
                    0, 10, 0, editKeyword, CacheConfig.cache_no_cache, new
                            SimpleHttpResponseListener<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    Log.i(TAG, "onSuccess: "+ response);
                                    searchResult = JSON.parseObject(response,
                                            SearchResultEntity.class);
                                    listcontent = searchResult.getData().getListcontent();
                                    Message msg = Message.obtain();
                                    if (listcontent.getContent() == null || listcontent.getContent()
                                            .size() == 0) {
                                        if (!isPrompt) {
                                            // 没有搜索结果的消息
                                            msg.what = SEARCH_NO_RESULT;
                                        } else {
                                            // 没有推荐的搜索
                                            msg.what = NO_PROMPT_SEARCH;
                                        }

                                    } else if (searchResult
                                            .getData().getListcontent() == null
                                            || searchResult
                                            .getData().getListcontent().getContent().size() == 0) {

                                        if (!isPrompt) {
                                            msg.what = SEARCH_NO_RESULT;
                                        } else {
                                            msg.what = NO_PROMPT_SEARCH;
                                        }
                                    } else {
                                        if (isPrompt) {
                                            msg.what = PROMPT_SEARCH;
                                        } else {
                                            addRecord(endiKeywordHistory);
                                            msg.what = START_SEARCH;
                                        }
                                    }
                                    handler.sendMessage(msg);
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("TAG", "getHotword onError , " + error == null ? "" : error);
                                    ProgressDialogUtil.dismissDialog();
                                    SearchActivity.this
                                            .runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgressDialogUtil
                                                            .dismissDialog();
                                                    ToastUtils
                                                            .show(SearchActivity.this,
                                                                    getResources()
                                                                            .getString(
                                                                                    R.string.error_toast));
                                                }
                                            });
                                    //弹出获取失败，请重试，或者其他处理，需要处理，wanqi
                                }
                            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 猜你喜欢
    private void requesBlockContent(final int type){
        //  String url="http://114.247.94.86:8010/blockContent?epgId=1000&type=3";
        String url = App.epgUrl;
        HttpRequest.getInstance().excute("getBlockContent", url, epjId, 1, CacheConfig.cache_one_day, new
                SimpleHttpResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i(TAG, "onSuccess: "+response);
                        gessListBean = JSON.parseObject(response,
                                GessListBean.class);
                        Message message = new Message();
                        if (type == TYPE_INIT_DATA){
                            message.what = GESS_LIKE_DATA;
                        }else if (type == TYPE_NO_RESULT){
                            message.what = SET_NO_RESULT;
                        }

                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(String error) {
                        ProgressDialogUtil
                                .dismissDialog();
                        ToastUtils
                                .show(SearchActivity.this,
                                        getResources()
                                                .getString(
                                                        R.string.error_toast));
                    }
                });
    }

    public void setGessLikeTwo(){
        // 获取布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        // 设置布局管理器
//        grid_view.setLayoutManager(gridLayoutManager);
        int spanCount =4 ;
        int spacing = 2;
        boolean includeEdge = false ;
        mListNoResult.setVisibility(View.VISIBLE);
        mTvEveryone.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.VISIBLE);
        //设置图片之间的间距
        // grid_view.addItemDecoration(new GridSpcingItemDecortion(spanCount,spacing,includeEdge));
        gessLikeadapter = new GessLikeAdapter(this,gessListBean.getData().getIndexContents());
        mListNoResult.setAdapter(gessLikeadapter);
        onRecyclerItemClickListener();
    }

    public void setGessLike(){
        // 获取布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        // 设置布局管理器
        mListNoResult.setLayoutManager(gridLayoutManager);
        int spanCount =4 ;
        int spacing = 2;
        boolean includeEdge = false ;
        //设置图片之间的间距
//         grid_view.addItemDecoration(new SpaceItemDecoration(2,3));
        gessLikeadapter = new GessLikeAdapter(this,gessListBean.getData().getIndexContents());
        mListNoResult.setAdapter(gessLikeadapter);
        mListNoResult.setVisibility(View.VISIBLE);
        llChangeWord.setVisibility(View.GONE);
        llNoResult.setVisibility(View.VISIBLE);
        onRecyclerItemClickListener();
    }

    private boolean isPrompt;

    // 猜你喜欢
    private void requesBlockContentTwo(){
        //  String url="http://114.247.94.86:8010/blockContent?epgId=1000&type=3";
        String url = App.epgUrl;
        HttpRequest.getInstance().excute("getBlockContent", url, epjId, 1, CacheConfig.cache_one_day, new
                SimpleHttpResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i(TAG, "onSuccess: "+response);
                        gessListBean = JSON.parseObject(response,
                                GessListBean.class);
                        if (gessListBean == null || gessListBean.getData().getIndexContents().size() ==0){
                            return;
                        }else {
                            Message message = new Message();
                            message.what = No_GESS_LIKE;
                            handler.sendMessage(message);
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    /**
     * 清除历史记录
     */
    private void deleteLocalData(){
        HttpRequest.getInstance().excute("deleteLocalData",
                 new Object[]{"record","keyword", true, "1" ,
                        new HttpResponseListener() {
                            @Override
                            public void onSuccess(String response) {

                            }

                            @Override
                            public void onError(String error) {

                            }
                        }});
        mRecordAdapter.setData(null);
        mGridView.setVisibility(View.GONE);

    }

    /**
     * 添加历史记录
     */

    private void addRecord( String endiKeywordHistory){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("keyword", endiKeywordHistory);
        String json = jsonObject.toString();
        HttpRequest.getInstance().excute("addLocalData",
                new Object[] {"record","keyword",json ,new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                }});
    }

    /**
     *  查询历史记录
     */
    private void searchRecordData(){
        HttpRequest.getInstance().excute("getLocalDataList",
                new Object[] {"record", "keyword",0 ,10 ,
                        new HttpResponseListener() {
                    @Override
                    public void onSuccess(String response) {

                        recordBean = JSON.parseObject(response,
                                SearchRecord.class);
                        Message msg = Message.obtain();
                        if (recordBean.getVideoCollectList() == null || recordBean.getVideoCollectList().size() ==0){
                            return;
                        }else {
                            msg.what = SET_RECORD_DATA;
                        }
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(String error) {

                    }
                }});

    }

}

