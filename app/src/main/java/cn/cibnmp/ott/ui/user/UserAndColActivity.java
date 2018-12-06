package cn.cibnmp.ott.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.user.fragment.UserAndListFragment;
import cn.cibnmp.ott.ui.user.fragment.UserMesageListFragment;
import cn.cibnmp.ott.ui.user.fragment.UserPlayHistoryFragment;
import cn.cibnmp.ott.ui.user.fragment.UserRecordListFragment;
import cn.cibnmp.ott.ui.user.fragment.UserYuyueListFragment;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/5.
 */

public class UserAndColActivity extends BaseActivity implements View.OnClickListener{

    // 标识id
    private int subjectID;

    private RelativeLayout user_title;
    private ImageButton titleBack;
    private TextView titleText;
    private TextView titleRight;

    private UserListInterface fatherFragment;
    // 收藏
    private UserAndListFragment listFragment;
    // 播放记录
    private UserPlayHistoryFragment playHistoryFragment;
    // 订单记录
    private UserRecordListFragment userRecordListFragment;
    // 预约
    private UserYuyueListFragment userYuyueListFragment;
    // 消息
    private UserMesageListFragment mesageListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_and_col_activity);

        getAtIntent();
        initView();
    }

    private void getAtIntent() {
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(Constant.activityBundleExtra);
        if (bundleExtra != null) {
            subjectID = (int) bundleExtra.getLong(Constant.contentIdKey);
        }
    }

    private void initView() {
        // title
        user_title = (RelativeLayout) findViewById(R.id.user_title);
        user_title.setBackgroundResource(R.color.white);
        // 返回
        titleBack = (ImageButton)user_title.findViewById(R.id.home_title_back);
        titleBack.setOnClickListener(this);

        // 标题
        titleText = (TextView) user_title.findViewById(R.id.home_title_text);

        // 编辑
        titleRight = (TextView) user_title.findViewById(R.id.home_title_right);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (subjectID == Constant.userIntent1) {
            // 收藏
            listFragment = new UserAndListFragment();
            titleText.setText(Utils.getInterString(this, R.string.collection));
            titleRight.setText(Utils.getInterString(this, R.string.edit));
            fragmentTransaction.add(R.id.content, listFragment);
            fatherFragment = listFragment;
        } else if (subjectID == Constant.userIntent2) {
            // 我的消息
            mesageListFragment = new UserMesageListFragment();
            titleText.setText(Utils.getInterString(this, R.string.message));
            titleRight.setBackgroundResource(R.mipmap.del);
            fragmentTransaction.add(R.id.content, mesageListFragment);
            fatherFragment = mesageListFragment;
        } else if (subjectID == Constant.userIntent3) {
            // 播放记录
            playHistoryFragment = new UserPlayHistoryFragment();
            titleText.setText(Utils.getInterString(this, R.string.user_play_record));
            titleRight.setBackgroundResource(R.mipmap.del);
            fragmentTransaction.add(R.id.content, playHistoryFragment);
            fatherFragment = playHistoryFragment;
        } else if (subjectID == Constant.userIntent6) {
            // 订单记录
            userRecordListFragment = new UserRecordListFragment();
            titleText.setText(Utils.getInterString(this, R.string.user_trading_record));
            fragmentTransaction.add(R.id.content, userRecordListFragment);
            fatherFragment = userRecordListFragment;
        } else if (subjectID == Constant.userIntent7) {
            // 预约
            userYuyueListFragment = new UserYuyueListFragment();
            titleText.setText(Utils.getInterString(this, R.string.user_yuyue));
            titleRight.setText(Utils.getInterString(this, R.string.edit));
            fragmentTransaction.add(R.id.content, userYuyueListFragment);
            fatherFragment = userYuyueListFragment;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.home_title_back:
                finish();
                break;
            // 编辑
            case R.id.home_title_right:
                boolean isAll = fatherFragment.getEdit();
                if (subjectID < 10) {
                    if (isAll) {
                        titleRight.setText(Utils.getInterString(this, R.string.finish));
                    } else {
                        titleRight.setText(Utils.getInterString(this, R.string.edit));
                    }
                }
                break;
        }
    }

    public void onEventMainThread(Object event) {
        if (event.equals(Constant.USER_AND_CLEAR)) {
            titleRight.setText(Utils.getInterString(this, R.string.edit));
        } else if (event.equals(Constant.HOME_USER_SIGN_IN)) {
            fatherFragment.notifyDate();
        }
    }
}
