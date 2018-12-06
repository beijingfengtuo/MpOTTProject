package cn.cibnmp.ott.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnhp.grand.wxapi.MainLoginWenxin;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.BaseActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2018/2/1.
 */

public class UserSignInActivity extends BaseActivity implements View.OnClickListener{

    private View user_sign_title;
    private ImageButton ib_back_sign;
    private TextView tv_title_sign;
    private RelativeLayout btn_weixi_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_in_activity);
        initView();
    }

    private void initView() {
        user_sign_title = findViewById(R.id.user_sign_title);
        // 返回
        ib_back_sign = (ImageButton) user_sign_title.findViewById(R.id.home_title_back);
        ib_back_sign.setOnClickListener(this);
        // 返回
        tv_title_sign = (TextView) user_sign_title.findViewById(R.id.home_title_text);
        tv_title_sign.setText("登录");
        // 微信登陆
        btn_weixi_sign = (RelativeLayout) findViewById(R.id.rl_login);
        btn_weixi_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_title_back:
                finish();
                break;
            case R.id.rl_login:
                Intent intent = new Intent(this, MainLoginWenxin.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.HOME_USER_SIGN_IN)) {
            finish();
        }
    }
}
