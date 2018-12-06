package cn.cibnmp.ott.ui.screening;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.ActionParams;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.base.BaseFragment;
import de.greenrobot.event.EventBus;

/**
 * 筛选页面
 *
 * @Description 描述：
 * @author zhangxiaoyang create at 2018/1/4
 */
public class ScreeningActivity extends BaseActivity implements View.OnClickListener{
    private static String TAG = ScreeningActivity.class.getName();

    //页面标题部分
    private View home_title;
    private TextView tvTitle;

    private LinearLayout llScreeningMenu;

    /**
     * epgID：
     * contentID：栏目ID（等于columnID 997/p1）
     * p1：栏目ID（等于columnID 997/contentID）
     * p2：频道ID（等于subjectID /p2/p3）
     * p3：频道ID（等于subjectID /p2/p3）
     */
    private int epgID, contentID, p1, p2, p3;

    //筛选项列表页面、筛选节目列表页面
    private BaseFragment screeningMenuFragment, screeningListFragment;

    //首页推荐位类型 1006 显示筛选项、1008 不显示筛选项
    private int viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        setUI();
    }

    /**
     * 初始化布局
     */
    private void setUI() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(Constant.activityBundleExtra);
            NavigationInfoItemBean navigationInfoItemBean = (NavigationInfoItemBean) bundle.get(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN);
            if (navigationInfoItemBean != null) {
                viewType = navigationInfoItemBean.getViewtype();
                epgID = Integer.parseInt(App.hostEpgId);
                contentID = Integer.parseInt(navigationInfoItemBean.getContentId());
                String json = navigationInfoItemBean.getActionParams();
                ActionParams actionParams = JSON.parseObject(json, ActionParams.class);
                p1 = actionParams.getP1();
                p2 = actionParams.getP2();
                p3 = actionParams.getP3();
            }
        }

        /*页面标题*/
        home_title = findViewById(R.id.home_title_head);
        tvTitle = (TextView) home_title.findViewById(R.id.tv_home_title_my);
        tvTitle.setVisibility(View.VISIBLE);

        // 返回
        home_title.findViewById(R.id.home_title_back).setVisibility(View.VISIBLE);
        home_title.findViewById(R.id.home_title_back).setOnClickListener(this);

        llScreeningMenu = (LinearLayout) findViewById(R.id.ll_screening_menu);

        initFragment();
    }

    /**
     * 初始化筛选项列表页面、筛选节目列表页面
     */
    private void initFragment() {
        //获取页面传来的参数
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BUNDLE_EPGID, epgID);
        bundle.putInt(Constant.BUNDLE_CONTENTID, contentID);
        bundle.putInt(Constant.BUNDLE_P1, p1);
        bundle.putInt(Constant.BUNDLE_P2, p2);
        bundle.putInt(Constant.BUNDLE_P3, p3);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (viewType) {
            case HomeOneViewType.bottom_viewType: //显示筛选项
                tvTitle.setText(getString(R.string.txt_home_three_title));
                llScreeningMenu.setVisibility(View.VISIBLE);
                screeningMenuFragment = ScreeningMenuFragment.newInstance(bundle);
                transaction.replace(R.id.ll_screening_menu, screeningMenuFragment);
                screeningListFragment = ScreeningListFragment.newInstance(null);
                transaction.replace(R.id.ll_screening_list, screeningListFragment);
                transaction.commit();
                break;

            case HomeOneViewType.title_complete_viewType: //不显示筛选项
                tvTitle.setText(getString(R.string.txt_home_three_title_1));
                llScreeningMenu.setVisibility(View.GONE);
                screeningListFragment = ScreeningListFragment.newInstance(bundle);
                transaction.replace(R.id.ll_screening_list, screeningListFragment);
                transaction.commit();
                break;
            default:

                    break;
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.home_title_back:
                finish();
                break;
        }
    }

    /**
     * 根据筛选选项卡参数（传到筛选节目列表页面）
     *
     * @param epgID
     * @param columnID
     * @param subjectID
     * @param keyWords
     */
    public void getFromeScreeningListFrag(int epgID, int columnID, int subjectID, String keyWords) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BUNDLE_EPGID, epgID);
        bundle.putInt(Constant.BUNDLE_CONTENTID, columnID);
        bundle.putInt(Constant.BUNDLE_SUBJECTID, subjectID);
        bundle.putString(Constant.BUNDLE_KEYWORDS, keyWords);
        screeningListFragment.setData(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
