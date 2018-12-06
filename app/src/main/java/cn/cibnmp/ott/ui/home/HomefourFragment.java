package cn.cibnmp.ott.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.RecordListBean;
import cn.cibnmp.ott.bean.UserInBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseFragment;
import cn.cibnmp.ott.ui.search.ToastUtils;
import cn.cibnmp.ott.utils.HomeJumpDetailUtils;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.SharePrefUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import de.greenrobot.event.EventBus;

/**
 * Created by cibn-lyc on 2017/12/22.
 */

public class HomefourFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = "HomefourFragment";
    private View root;
    private View home_title;
    private TextView tv_home_sys, tv_user_name, tv_user_vip;
    private ImageView img_my_user_heard;
    private Button btn_login_registered;
    private RelativeLayout rl_paly_record, rl_collection, rl_yuyue, rl_vip, rl_trading_record, rl_vouchers, rl_message, rl_advise, rl_setting;

    private boolean isSignin = true;
    private TextView tv_play_record, tv_open_vip;
    private View user_detail;
    private RelativeLayout rl_user_record, rl_user_record1, rl_user_record2;
    private ImageView img_user_record_item1, img_user_record_item2;
    private TextView tv_user_record_name1, tv_user_record_name2;
    private TextView tv_user_record_progress1, tv_user_record_progress2;
    private RecordListBean dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_four_fragment, container, false);
        initView();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(Constant.PUSH_PLAY_HISTORY);
    }

    private void loadListData(final int index, final int totalNum) {
        HttpRequest.getInstance().excute("getLocalRecordList", new Object[]{index, totalNum, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.d(TAG, "getLocalRecordList" + " response:-------------->>>" + response);
                if (response == null) {
                    Lg.e(TAG, "getLocalRecordList" + " response is null .");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                dataList = JSON.parseObject(response, RecordListBean.class);
                if (dataList == null) {
                    Lg.e(TAG, "getLocalRecordList" + " parseObject response result is null,response is invalid.");
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
                if (dataList.getVideoCollectList() != null) {
                    handler.sendEmptyMessage(START_MSG);
                } else {
                    handler.sendEmptyMessage(STOP_MSG);
                    return;
                }
            }

            @Override
            public void onError(String error) {
                handler.sendEmptyMessage(STOP_MSG);
            }
        }});
    }

    // 初始化布局
    private void initView() {
        home_title = root.findViewById(R.id.home_title_head);
        //title（我的）
        home_title.findViewById(R.id.tv_home_title_my).setVisibility(View.VISIBLE);
        //title（扫一扫）
        tv_home_sys = (TextView) home_title.findViewById(R.id.tv_home_title_right1);
        tv_home_sys.setText(Utils.getInterString(getActivity(), R.string.user_sys));
        // 头像
        img_my_user_heard = (ImageView) root.findViewById(R.id.img_my_user_heard);
        // 用户详情
        user_detail = root.findViewById(R.id.user_detail);
        // 昵称
        tv_user_name = (TextView) user_detail.findViewById(R.id.tv_user_name);
        // 登录/会员
        btn_login_registered = (Button) user_detail.findViewById(R.id.btn_login_registered);
        // 登录后显示昵称
        tv_user_vip = (TextView) user_detail.findViewById(R.id.tv_user_vip);
        // 观看记录
        rl_paly_record = (RelativeLayout) root.findViewById(R.id.rl_paly_record);
        // 观看记录外露布局
        rl_user_record = (RelativeLayout) root.findViewById(R.id.rl_user_record);
        // 第一个
        rl_user_record1 = (RelativeLayout) root.findViewById(R.id.rl_user_record1);
        // 第二个
        rl_user_record2 = (RelativeLayout) root.findViewById(R.id.rl_user_record2);
        // 背景图
        img_user_record_item1 = (ImageView) root.findViewById(R.id.img_user_record_item1);
        img_user_record_item2 = (ImageView) root.findViewById(R.id.img_user_record_item2);
        // 节目
        tv_user_record_name1 = (TextView) root.findViewById(R.id.tv_user_record_name1);
        tv_user_record_name2 = (TextView) root.findViewById(R.id.tv_user_record_name2);
        // 进度
        tv_user_record_progress1 = (TextView) root.findViewById(R.id.tv_user_record_progress1);
        tv_user_record_progress2 = (TextView) root.findViewById(R.id.tv_user_record_progress2);
        // 播放进度显示
        tv_play_record = (TextView) rl_paly_record.findViewById(R.id.tv_play_record);
        // 我的收藏
        rl_collection = (RelativeLayout) root.findViewById(R.id.rl_collection);
        // 我的预约
        rl_yuyue = (RelativeLayout) root.findViewById(R.id.rl_yuyue);
        // 会员中心
        rl_vip = (RelativeLayout) root.findViewById(R.id.rl_vip);
        // 开通会员显示
        tv_open_vip = (TextView) rl_vip.findViewById(R.id.tv_open_vip);
        // 交易记录
        rl_trading_record = (RelativeLayout) root.findViewById(R.id.rl_trading_record);
        // 代金券
        rl_vouchers = (RelativeLayout) root.findViewById(R.id.rl_vouchers);
        // 消息
        rl_message = (RelativeLayout) root.findViewById(R.id.rl_message);
        // 意见反馈
        rl_advise = (RelativeLayout) root.findViewById(R.id.rl_advise);
        // 设置
        rl_setting = (RelativeLayout) root.findViewById(R.id.rl_setting);
        if (App.isLogin) {
            setUserInfo();
        }
        setOnClick();
    }

    // 监听
    private void setOnClick() {
        tv_home_sys.setOnClickListener(this);
        btn_login_registered.setOnClickListener(this);
        rl_paly_record.setOnClickListener(this);
        rl_collection.setOnClickListener(this);
        rl_yuyue.setOnClickListener(this);
        rl_vip.setOnClickListener(this);
        rl_trading_record.setOnClickListener(this);
        rl_vouchers.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_advise.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_user_record1.setOnClickListener(this);
        rl_user_record2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 微信登录
            case R.id.btn_login_registered:
                if (App.userId == 0) {
                    startActivity(Action.getActionName(Action.OPEN_USER_SIGN_IN),
                            null);
                }
                break;
            // 播放记录
            case R.id.rl_paly_record:
                startActivity(Action.getActionName(Action.OPEN_COLLECTION),
                        setBundle(Constant.userIntent3));
                break;
            // 收藏
            case R.id.rl_collection:
                startActivity(Action.getActionName(Action.OPEN_COLLECTION),
                        setBundle(Constant.userIntent1));
                break;
            // 预约
            case R.id.rl_yuyue:
                startActivity(Action.getActionName(Action.OPEN_COLLECTION),
                        setBundle(Constant.userIntent7));
                break;
            // 会员中心
            case R.id.rl_vip:
                startActivity(Action.getActionName(Action.OPEN_VIP), null);
                break;
            // 交易记录
            case R.id.rl_trading_record:
                startActivity(Action.getActionName(Action.OPEN_COLLECTION),
                        setBundle(Constant.userIntent6));
                break;
            // 代金券
            case R.id.rl_vouchers:
                startActivity(Action.getActionName(Action.OPEN_USER_VOUCHERS), null);
                break;
            // 消息
            case R.id.rl_message:
                startActivity(Action.getActionName(Action.OPEN_COLLECTION),
                        setBundle(Constant.userIntent2));
                break;
            // 意见反馈
            case R.id.rl_advise:
                startActivity(Action.getActionName(Action.OPEN_FEEDBACK), null);
                break;
            // 设置
            case R.id.rl_setting:
                startActivity(Action.getActionName(Action.OPEN_STTING), null);
                break;
            case R.id.rl_user_record1:
                HomeJumpDetailUtils.actionToDetailByPlayHistory(dataList.getVideoCollectList().get(0), getActivity());
                break;
            case R.id.rl_user_record2:
                HomeJumpDetailUtils.actionToDetailByPlayHistory(dataList.getVideoCollectList().get(1), getActivity());
                break;
            default:
                break;
        }
    }

    private Bundle setBundle(int contentId) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.contentIdKey, contentId);
        return bundle;
    }

    // 通知登陆成功
    @Override
    public void onEventMainThread(String event) {
        super.onEventMainThread(event);
        if (event.equals(Constant.HOME_USER_SIGN_IN)) {
            if (App.mUserInBean != null) {
                App.saveUserInfo(App.mUserInBean.getData());
                HttpRequest.getInstance().excute("userLoginSet", App.userId);
                if (App.mUserInBean.getData().getUid() != 0) {
                    Utils.setUserUID(App.mUserInBean.getData().getUid());
                }
                ToastUtils.show(getActivity(), Utils.getInterString(getActivity(), R.string.login_success));
                tv_user_name.setVisibility(View.GONE);
                tv_user_vip.setText(App.mUserInBean.getData().getNickname());
                ImageFetcher.getInstance().loodingImage(App.mUserInBean.getData().getPortraiturl(),
                        img_my_user_heard, R.drawable.user_heard);
                btn_login_registered.setVisibility(View.GONE);
                tv_user_vip.setVisibility(View.VISIBLE);
            } else {
                handler.sendEmptyMessage(CIBN_EC_VERIFY_ERROR);
            }
        } else if (event.equals(Constant.HOME_USER_SIGN_OUT)) {
            App.logoutUser();
            App.UserSignInNO = 0;
            tv_user_name.setVisibility(View.VISIBLE);
            tv_user_name.setText(getString(R.string.user_sign_in_title));
            img_my_user_heard.setImageResource(R.drawable.user_heard);
            tv_user_vip.setVisibility(View.GONE);
            btn_login_registered.setVisibility(View.VISIBLE);
        } else if (event.equals(Constant.LINK_BEADS_TAG)) {
            SharePrefUtils.saveBoolean(Constant.LINK_BEADS_TAG, true);
        } else if (event.equals(Constant.PUSH_PLAY_HISTORY)){
            loadListData(0, 2);
        }
    }

    private void setUserInfo() {
        tv_user_name.setVisibility(View.GONE);
        tv_user_vip.setText(App.userName);
        ImageFetcher.getInstance().loodingImage(App.userPic,
                img_my_user_heard, R.drawable.user_heard);
        btn_login_registered.setVisibility(View.GONE);
        tv_user_vip.setVisibility(View.VISIBLE);
    }

    private final int USER_MSG = 888;
    private final int RESPONSE_MSG = 666;
    private final int STOP_MSG = 2000;
    private final int START_MSG = 2001;
    // 其他错误
    private final int CIBN_EC_VERIFY_ERROR = -44;
    private Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case RESPONSE_MSG:
                    EventBus.getDefault().post(Constant.HOME_USER_SIGN_IN);
                    break;
                case CIBN_EC_VERIFY_ERROR:
                    if (isSignin) {
                        isSignin = false;
                        handler.sendEmptyMessageDelayed(USER_MSG, 2000);
                    } else {
                        ToastUtils.show(getActivity(), Utils.getInterString(getActivity(), R.string.get_user_info_faile));
                    }
                    break;
                case USER_MSG:
                    EventBus.getDefault().post(Constant.HOME_USER_SIGN_IN);
                    break;
                case START_MSG:
                    rl_user_record.setVisibility(View.VISIBLE);
                    rl_user_record1.setVisibility(View.VISIBLE);
                    ImageFetcher.getInstance().loodingImage(dataList.getVideoCollectList().get(0).getPosterFid(),
                            img_user_record_item1, R.color.black_hui8);
                    if (dataList.getVideoCollectList().get(0).getVname() != null) {
                        tv_user_record_name1.setText(dataList.getVideoCollectList().get(0).getVname());
                    } else {
                        tv_user_record_name1.setText(Utils.getInterString(getActivity(), R.string.unknown));
                    }
                    if (dataList.getVideoCollectList().get(0).getDuration() != 0){
                        int pos = (int) (dataList.getVideoCollectList().get(0).getCurrentPos() * 100 / dataList.getVideoCollectList().get(0).getDuration());
                        if (pos >= 97) {
                            tv_user_record_progress1.setText(context.getString(R.string.video_look_finish));
                        } else {
                            tv_user_record_progress1.setText(context.getString(R.string.video_look_to) + pos + Utils.getInterString(getActivity(), R.string.percent));
                        }
                    } else {
                        tv_user_record_progress1.setText(context.getString(R.string.video_look_to_o));
                    }
                    if(dataList.getVideoCollectList().size() > 1) {
                        rl_user_record2.setVisibility(View.VISIBLE);
                        ImageFetcher.getInstance().loodingImage(dataList.getVideoCollectList().get(1).getPosterFid(),
                                img_user_record_item2, R.color.black_hui8);
                        if (dataList.getVideoCollectList().get(1).getVname() != null) {
                            tv_user_record_name2.setText(dataList.getVideoCollectList().get(1).getVname());
                        } else {
                            tv_user_record_name2.setText(Utils.getInterString(getActivity(), R.string.unknown));
                        }
                        if (dataList.getVideoCollectList().get(1).getDuration() != 0){
                            int pos = (int) (dataList.getVideoCollectList().get(1).getCurrentPos() * 100 / dataList.getVideoCollectList().get(1).getDuration());
                            if (pos >= 97) {
                                tv_user_record_progress2.setText(context.getString(R.string.video_look_finish));
                            } else {
                                tv_user_record_progress2.setText(context.getString(R.string.video_look_to) + pos + Utils.getInterString(getActivity(), R.string.percent));
                            }
                        } else {
                            tv_user_record_progress1.setText(context.getString(R.string.video_look_to_o));
                        }
                    }else {
                        rl_user_record2.setVisibility(View.GONE);
                    }
                    break;
                case STOP_MSG:
                    rl_user_record.setVisibility(View.GONE);
                    rl_user_record1.setVisibility(View.GONE);
                    rl_user_record2.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
}
