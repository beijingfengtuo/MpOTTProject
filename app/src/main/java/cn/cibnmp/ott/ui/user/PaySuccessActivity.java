package cn.cibnmp.ott.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.utils.Utils;

/**
 * Created by cibn-lyc on 2018/2/4.
 */

public class PaySuccessActivity extends BaseActivity implements View.OnClickListener{

    private View pay_success_title;
    private ImageButton tv_pay_success_back;
    private TextView tv_pay_success_title;
    private TextView tv_pay_success_price;
    private TextView tv_pay_success_number;
    private TextView tv_pay_success_validity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_activity);
        initView();
    }

    private void initView() {
        pay_success_title = findViewById(R.id.pay_success_title);
        // 返回
        tv_pay_success_back = (ImageButton) pay_success_title.findViewById(R.id.home_title_back);
        tv_pay_success_back.setOnClickListener(this);
        // 标题
        tv_pay_success_title = (TextView) pay_success_title.findViewById(R.id.home_title_text);
        tv_pay_success_title.setText(Utils.getInterString(this, R.string.pay));
        // 订单金额
        tv_pay_success_price = (TextView) findViewById(R.id.tv_pay_success_price1);
        // 订单编号
        tv_pay_success_number = (TextView) findViewById(R.id.tv_pay_success_number1);
        // 有效期
        tv_pay_success_validity = (TextView) findViewById(R.id.tv_pay_success_validity1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_title_back:
                finish();
                break;
        }
    }
}
