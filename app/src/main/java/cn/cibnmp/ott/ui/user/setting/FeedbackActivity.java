package cn.cibnmp.ott.ui.user.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.FeedBackDialog;
import cn.cibnmp.ott.widgets.pmrecyclerview.widget.FeedBackSuccessDialog;

public class FeedbackActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private CheckBox cb;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;
    private CheckBox cb6;
    private CheckBox cb7;
    private CheckBox[] checkBoxes;
    private EditText ed_question;
    private EditText ed_num;
    private TextView tv_submit;
    private RelativeLayout feedback_title;
    private TextView home_title_text;
    private ImageButton ibBack;
    private TextView tv_tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        initListener();
    }

    public void initView() {
        cb = (CheckBox) findViewById(R.id.feedback_rb1);
        cb1 = (CheckBox) findViewById(R.id.feedback_rb2);
        cb2 = (CheckBox) findViewById(R.id.feedback_rb3);
        cb3 = (CheckBox) findViewById(R.id.feedback_rb4);
        cb4 = (CheckBox) findViewById(R.id.feedback_rb5);
        cb5 = (CheckBox) findViewById(R.id.feedback_rb6);
        cb6 = (CheckBox) findViewById(R.id.feedback_rb7);
        cb7 = (CheckBox) findViewById(R.id.feedback_rb8);
        checkBoxes = new CheckBox[]{cb, cb1, cb2, cb3, cb4, cb5, cb6, cb7};
        ibBack = (ImageButton) findViewById(R.id.home_title_back);
        ed_num = (EditText) findViewById(R.id.feedback_num_ed);
        ed_question = (EditText) findViewById(R.id.feedback_question_ed);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        feedback_title = (RelativeLayout) findViewById(R.id.feedback_title);
        home_title_text = (TextView) feedback_title
                .findViewById(R.id.tv_home_title_my);
        tv_tid = (TextView) findViewById(R.id.tv_tid);
        // 更改文字，并设置搜索键隐藏
        home_title_text.setText(Utils.getInterString(this, R.string.user_advise));
        home_title_text.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.VISIBLE);
        initID();
    }

    private void initID() {
        String id;
        if (App.UserSignInNO == 2) {
            id = Utils.getUserOpenId();
            tv_tid.setText("UID:" + id);
        } else {
            id = App.publicTID;
            tv_tid.setText("TID:" + id);
        }
    }

    public void initListener() {
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setOnCheckedChangeListener(this);
        }
        ibBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 监听字体限制 长度不能超过200
        ed_question.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_question.getText().toString().length() > 200) {
                    ToastUtils.show(FeedbackActivity.this, getResources().getString(R.string.advance));
                    String string = ed_question.getText().toString()
                            .substring(0, 200);
                    ed_question.setText(string);
                    ed_question.setSelection(200);
                }
            }
        });
        tv_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(ed_question.getText().toString().trim()) && "".equals(getQu())) {
                    FeedBackDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.qu_content));
                    return;
                }

                if ("".equals(ed_num.getText().toString().trim())) {
                    FeedBackDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.contact_infomation));
                    return;
                }
                long cTime = SharePrefUtils.getLong("time",-1);
                if (cTime == -1) {
                    SharePrefUtils.saveLong("time", System.currentTimeMillis());
                } else if (System.currentTimeMillis() - cTime <= 10*60*1000) {
                    FeedBackSuccessDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.submit_frequently), getResources().getString(R.string.try_again_later), new FeedBackSuccessDialog.IClickListener() {
                        @Override
                        public void confirm() {
                            FeedBackSuccessDialog.dismissDialog();
                        }
                    });
                    return;
                } else {
                    SharePrefUtils.saveLong("time", System.currentTimeMillis());
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("question", getQu());
                map.put("suggest", ed_question.getText().toString());
                map.put("contact", ed_num.getText().toString());
                FeedBackSuccessDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.submit_success), getResources().getString(R.string.close), new FeedBackSuccessDialog.IClickListener() {
                    @Override
                    public void confirm() {
                        FeedbackActivity.this.finish();
                    }
                });
//                String jsonString = JSON.toJSONString(map);
//                HttpRequest.getInstance().excute(
//                        "reportUserFeedback",
//                        new Object[]{App.epgUrl, jsonString,
//                                new HttpResponseListener() {
//
//                                    @Override
//                                    public void onSuccess(final String response) {
//                                        runOnUiThread(new Runnable() {
//
//                                            @Override
//                                            public void run() {
//                                                FeedBackSuccessDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.submit_success), getResources().getString(R.string.close), new FeedBackSuccessDialog.IClickListener() {
//                                                    @Override
//                                                    public void confirm() {
//                                                        FeedbackActivity.this.finish();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onError(final String error) {
//                                        Log.d("userReport", error);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                FeedBackSuccessDialog.showDialog(FeedbackActivity.this, getResources().getString(R.string.submit_success), getResources().getString(R.string.close), new FeedBackSuccessDialog.IClickListener() {
//                                                    @Override
//                                                    public void confirm() {
//                                                        FeedbackActivity.this.finish();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//                                }});
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(getResources().getColor(R.color.white));
            buttonView.setBackgroundResource(R.mipmap.comment_but_bg);
        } else {
            buttonView.setTextColor(getResources().getColor(R.color.colore_home9));
            buttonView.setBackgroundResource(R.mipmap.player_white_bg1);
        }
    }

    /**
     * 获取问题字符串
     *
     * @return
     */
    public String getQu() {
        StringBuilder sb = new StringBuilder();
        String s = "";
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                s = checkBoxes[i].getText().toString().trim() + ",";
                sb.append(s);
            }
        }
        if (sb.toString().length() > 1) {
            s = sb.toString().substring(0, sb.toString().length() - 1);
        }
        return s;
    }
}
