package cn.cibnmp.ott.ui.detail.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.ui.detail.bean.DetailBean;
import cn.cibnmp.ott.ui.detail.bean.DetailInfoBean;
import cn.cibnmp.ott.utils.Lg;

/**
 * Created by axl on 2018/1/16.
 */

public class DetailContentView extends LinearLayout implements View.OnClickListener {

    private Context context;

    private View contextView;

    private TextView tv_name, detail_storyplot, detail_storyplot1, detail_storyplot2, detail_tv1;

    private ImageView btn_pull;

    private DetailInfoBean datas;

    private boolean isPull = false;
    private LinearLayout detiles_paly_ll;

    private TextView play_number;

    public DetailContentView(Context context) {
        super(context);
    }

    public DetailContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        context = context;
        contextView = View.inflate(context, R.layout.detail_frag_content, this);

        if (isInEditMode()) {
            return;
        }

        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.detail_name);
        detail_tv1 = (TextView) findViewById(R.id.detail_tv1);
        play_number = (TextView) findViewById(R.id.play_number);
        detiles_paly_ll = (LinearLayout) findViewById(R.id.detiles_paly_ll);

        detail_storyplot = (TextView) findViewById(R.id.detail_storyplot);
        detail_storyplot1 = (TextView) findViewById(R.id.detail_storyplot1);
        detail_storyplot2 = (TextView) findViewById(R.id.detail_storyplot2);

        btn_pull = (ImageView) contextView.findViewById(R.id.detail_btn_pull);
        btn_pull.setOnClickListener(this);

    }

    public void updateContent(DetailInfoBean data, int detail_ty) {
        Lg.i("DetailContentView", "type" + detail_ty);
        this.datas = data;
        DetailBean datas;
        if (detail_ty == 4)
            datas = this.datas.getEpglive();
        else
            datas = this.datas.getEpgvideo();

        if (datas == null) {
            return;
        }
        if (detail_ty != 4) {
            tv_name.setText((datas.getVname() == null ? "未知" : datas.getVname()));
            //      play_number.setText(datas.getPlayrecordcount() == 0 ? "10万次" : datas.getPlayrecordcount() + "次");
            Lg.i("contentView", datas.getVname());
        }
        Lg.i("contentView", datas.getStoryPlot());
        String bottomName = (datas.getStoryPlot() == null ? "" : "<font color='#a0a0a0'>" + "简介：" + datas.getStoryPlot()) + "</font>";
        String bottomName2 = "";

        String jutuan = "";
        String zhuyan = "";
        String zhuchuang = "";
        if (detail_ty == 4) {
            tv_name.setText((datas.getLiveName() == null ? "无" : datas.getLiveName()));
            //  play_number.setText(datas.getPlayrecordcount() == 0 ? "10万次" : datas.getPlayrecordcount() + "次");

            String jut = TextUtils.isEmpty(datas.getTroupe()) ? "无" : datas.getTroupe();
            jutuan = "<font color='#d7ab6a'>" + "剧团：" + "</font>" + "<font color='#a0a0a0'>" + jut + "</font>" + "\n";

            String zy = TextUtils.isEmpty(datas.getGuest())?"无":datas.getGuest();
            zhuyan = "<font color='#d7ab6a'>" + "主演：" + "</font>" + "<font color='#a0a0a0'>" + zy + "</font>" + "\n";


            String zc = TextUtils.isEmpty(datas.getFound())?"无":datas.getFound();
            zhuchuang = "<font color='#d7ab6a'>" + "主创：" + "</font>" + "<font color='#a0a0a0'>" + zc + "</font>" + "\n";


            bottomName2 = jutuan + zhuyan + zhuchuang;
//            bottomName2 = ((datas.getTroupe() == null || "".equals(datas.getTroupe()))? "" : "<font color='#d7ab6a'>" + "剧团：" + "</font>" + "<font color='#a0a0a0'>" + datas.getTroupe()) + "</font>" + "\n"
//                    + (datas.getGuest() == null ? "" : "<font color='#d7ab6a'>" + "主演：" + "</font>" + "<font color='#a0a0a0'>" + datas.getGuest())+ "</font>"  + "\n"
//                    + (datas.getFound() == null ? "" : "<font color='#d7ab6a'>" + "主创：" + "</font>" + "<font color='#a0a0a0'>" + datas.getFound())+ "</font>"  + "\n";
            detiles_paly_ll.setVisibility(GONE);
            detail_storyplot.setVisibility(GONE);
            //TODO zxy
            bottomName2 = bottomName2.replaceAll(";", "  ");
            bottomName2 = bottomName2.replace("\n", "<br />");
            bottomName = bottomName2 + bottomName;
        } else {
            // 通用详情类型显示
            String TAG = (datas.getTag() == null ? "" : datas.getTag());
            TAG = TAG.replaceAll(";", "/");
            if (TAG.endsWith("/")) {
                TAG = TAG.substring(0, TAG.length() - 1);
            }
            detail_tv1.setText(TAG);
            String jut = TextUtils.isEmpty(datas.getOrgan()) ? "无" : datas.getOrgan();
            jutuan = "<font color='#d7ab6a'>" + "剧团：" + "</font>" + "<font color='#a0a0a0'>" + jut + "</font>" + "\n";

            String zy = TextUtils.isEmpty(datas.getStar())?"无":datas.getStar();
            zhuyan = "<font color='#d7ab6a'>" + "主演：" + "</font>" + "<font color='#a0a0a0'>" + zy + "</font>" + "\n";


            String zc = TextUtils.isEmpty(datas.getScreenWriter())?"无":datas.getScreenWriter();
            zhuchuang = "<font color='#d7ab6a'>" + "主创：" + "</font>" + "<font color='#a0a0a0'>" + zc + "</font>" + "\n";

            bottomName2 = jutuan + zhuyan + zhuchuang;
            // detiles_paly_ll.setVisibility(GONE);
            //detail_storyplot.setVisibility(GONE);
            bottomName2 = bottomName2.replaceAll(";", "  ");
            bottomName2 = bottomName2.replace("\n", "<br />");
            bottomName = bottomName2 + bottomName;
        }
//        SpannableStringBuilder textStyle = new SpannableStringBuilder(bottomName);
//        textStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colore_home11)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colore_home9)), 3, bottomName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        detail_storyplot1.setText(Html.fromHtml(bottomName));
        detail_storyplot2.setText(Html.fromHtml(bottomName));
    }

    @Override
    public void onClick(View v) {
        if (v == btn_pull) {
            if (!isPull) {
                detail_storyplot2.setVisibility(View.VISIBLE);
                detail_storyplot1.setVisibility(View.GONE);
                isPull = true;
                btn_pull.setImageResource(R.drawable.tab_arrow_up);

            } else {
                detail_storyplot2.setVisibility(View.GONE);
                detail_storyplot1.setVisibility(View.VISIBLE);
                isPull = false;
                btn_pull.setImageResource(R.drawable.tab_arrow_dpwm);
            }
        }
    }
}
