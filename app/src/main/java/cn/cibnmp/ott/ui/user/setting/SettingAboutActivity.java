package cn.cibnmp.ott.ui.user.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/1/12.
 */

public class SettingAboutActivity extends BaseActivity {
    // title布局
    private RelativeLayout my_title;
    // 标题
    private TextView tvTitle;
    // 返回
    private ImageButton ibBack;
    // 版本号
    private TextView user_about_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_about);
        initView();
    }

    private void initView() {
        my_title = (RelativeLayout) findViewById(R.id.my_title);
        // 关于我们
        tvTitle = (TextView) my_title.findViewById(R.id.tv_home_title_my);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(Utils.getInterString(this, R.string.about_me));
        // 版本号
        user_about_version = (TextView) findViewById(R.id.user_about_version);
        user_about_version.setText(Utils.getVersionName());
        // 返回
        ibBack = (ImageButton) my_title.findViewById(R.id.home_title_back);
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingAboutActivity.this.finish();
            }
        });
    }
}
