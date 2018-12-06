package cn.cibnmp.ott.ui.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.util.ArrayList;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.ui.HomeActivity;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.WXUtils;

/**
 * Created by yanjing on 2016/7/29.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    private Activity context;
    private LayoutInflater mInflater;
    private LinearLayout rootLayout;
    private ImageView btnClose;
    private RelativeLayout share_item1, share_item2, share_item3, share_item4, share_item5, share_item6;
    private ImageView img1, img2, img3, img4, img5, img6;
    private TextView text1, text2, text3, text4, text5, text6;
    private int[] imgList = {R.drawable.share_wxhy, R.drawable.share_wxpyq, R.drawable.share_wxsc, R.drawable.share_kj_qq, R.drawable.share_qq, R.drawable.share_xlwb};
    private String[] textList = {"微信好友", "朋友圈", "收藏", "QQ空间", "QQ好友", "新浪微博"};
    private long vid;
    //   private ScreeningDataBean.DataBean.ListcontentBean.ContentBean datas;
    private IWXAPI api;
    private Tencent mTencent;
    private String clsName;
    private String description;
    private String fid;

    //微信分享图片的缩略图
    private Bitmap thumb = null;
    //新浪微博
    private IWeiboShareAPI mWeiboShareAPI;

    private int type = 1;

    public ShareDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ShareDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        api = App.getWxApi();
        mTencent = App.getQQ();
//        // 创建微博分享接口实例
//        mWeiboShareAPI = App.getWeiboShare();

//        if (type == 0) {
//            this.setContentView(R.layout.share_dialog);
//        } else {
//            this.setContentView(R.layout.share_dialog_frag);
//        }
        this.setContentView(R.layout.share_dialog_frag);
        setUI();
    }

    /**
     * 初始化View
     *
     * @param activity
     * @param vid
     * @param fid String类型网络图片
     * @param description
     * @param clsName
     * @param type
     */
    public void initView(Activity activity, long vid, String fid, String description, String clsName, int type) {
        this.context = activity;
        this.vid = vid;
        this.type = type;
        this.fid = fid;
        this.description = description;
        this.clsName = clsName;
    }

    /**
     * 初始化View
     *
     * @param activity
     * @param vid
     * @param fid Bitmap类型图片
     * @param description
     * @param clsName
     * @param type
     */
    public void initView(Activity activity, long vid, Bitmap fid, String description, String clsName, int type) {
        this.context = activity;
        this.vid = vid;
        this.type = type;
        this.thumb = fid;
        this.description = description;
        this.clsName = clsName;
    }

    private void setUI() {
        share_item1 = (RelativeLayout) findViewById(R.id.share_item1);
        share_item2 = (RelativeLayout) findViewById(R.id.share_item2);
        share_item3 = (RelativeLayout) findViewById(R.id.share_item3);
        share_item4 = (RelativeLayout) findViewById(R.id.share_item4);
        share_item5 = (RelativeLayout) findViewById(R.id.share_item5);
        share_item6 = (RelativeLayout) findViewById(R.id.share_item6);

        img1 = (ImageView) share_item1.findViewById(R.id.share_item_img);
        img2 = (ImageView) share_item2.findViewById(R.id.share_item_img);
        img3 = (ImageView) share_item3.findViewById(R.id.share_item_img);
        img4 = (ImageView) share_item4.findViewById(R.id.share_item_img);
        img5 = (ImageView) share_item5.findViewById(R.id.share_item_img);
        img6 = (ImageView) share_item6.findViewById(R.id.share_item_img);

        text1 = (TextView) share_item1.findViewById(R.id.share_item_text);
        text2 = (TextView) share_item2.findViewById(R.id.share_item_text);
        text3 = (TextView) share_item3.findViewById(R.id.share_item_text);
        text4 = (TextView) share_item4.findViewById(R.id.share_item_text);
        text5 = (TextView) share_item5.findViewById(R.id.share_item_text);
        text6 = (TextView) share_item6.findViewById(R.id.share_item_text);

        btnClose = (ImageView) findViewById(R.id.img_share_dialog_close);

        share_item1.setOnClickListener(this);
        share_item2.setOnClickListener(this);
        share_item3.setOnClickListener(this);
        share_item4.setOnClickListener(this);
        share_item5.setOnClickListener(this);
        share_item6.setOnClickListener(this);

        btnClose.setOnClickListener(this);

        for (int i = 0; i < imgList.length; i++) {
            switch (i) {
                case 0:
                    img1.setBackgroundResource(imgList[i]);
                    text1.setText(textList[i]);
                    break;
                case 1:
                    img2.setBackgroundResource(imgList[i]);
                    text2.setText(textList[i]);
                    break;
                case 2:
                    img3.setBackgroundResource(imgList[i]);
                    text3.setText(textList[i]);
                    break;
                case 3:
                    img4.setBackgroundResource(imgList[i]);
                    text4.setText(textList[i]);
                    break;
                case 4:
                    img5.setBackgroundResource(imgList[i]);
                    text5.setText(textList[i]);
                    break;
                case 5:
                    img6.setBackgroundResource(imgList[i]);
                    text6.setText(textList[i]);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_item1://微信好友
                shareWX(0);
                dismiss();
                break;
            case R.id.share_item2://朋友圈
                shareWX(1);
                dismiss();
                break;
            case R.id.share_item3://收藏
                shareWX(2);
                dismiss();
                break;
            case R.id.share_item4://QQ空间
//                saveImage();
//                shareToQzone();
                dismiss();
                break;
            case R.id.share_item5://QQ好友
//                saveImage();
//                shareQQ();
                dismiss();
                break;
            case R.id.share_item6://新浪微博
//                testShareImage();
                dismiss();
                break;
            case R.id.img_share_dialog_close://关闭对话框
                dismiss();
                break;

        }
    }

    private void saveImage() {
        String path = Environment.getExternalStorageDirectory() + "/ic_launcher.png";
        File file = new File(path);
        if (!file.exists()) {
            Utils.saveImageToSDCard(context, R.drawable.ic_launcher);
        }
    }

    public ArrayList<String> urlList;

    private void shareToQzone() {
        final Bundle params = new Bundle();
        urlList = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory() + "/ic_launcher.png";
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        urlList.add(path);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
//        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, datas.getVname());//必填
//        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,datas.getStoryplot() );//选填
        //TODO zxy
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "aa");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "aa");//选填

        if (App.ShareUrl == null || App.ShareUrl.equals("")) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://haokan.wx.cibn.cc/detail" + "?vid=" + vid);//必填
        } else {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, App.ShareUrl + "?vid=" + vid);//必填
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, urlList);
        doShareToQQ(params, 1);
    }

    /**
     * qq 分享
     */
    private void shareQQ() {
        String path = Environment.getExternalStorageDirectory() + "/ic_launcher.png";
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, datas.getVname());
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, datas.getStoryplot());

        //TODO zxy
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "aa");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "aa");//选填

        if (App.ShareUrl == null || App.ShareUrl.equals("")) {
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://haokan.wx.cibn.cc/detail" + "?vid=" + vid);
        } else {
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, App.ShareUrl + "?vid=" + vid);
        }
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        doShareToQQ(params, 0);
    }

    private void doShareToQQ(final Bundle params, final int type) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (null != mTencent) {
                    if (type == 0) {
                        mTencent.shareToQQ(context, params, ((HomeActivity) context).qqShareListener);
                    } else if (type == 1) {
                        mTencent.shareToQzone(context, params, ((HomeActivity) context).qqShareListener);
                    }
                }
            }
        });
    }

    /**
     * type 0 好友  1 朋友圈
     *
     * @param type
     */
    private void shareWX(int type) {
        WXWebpageObject webpage = new WXWebpageObject();

        if (this.type == 1) {
            //测试环境地址 http://dajuyuantest.wx.cibn.cc/
            webpage.webpageUrl = "http://dajuyuan2.wx.cibn.cc/vod/livedetail/" + vid;
        } else {
            webpage.webpageUrl = "http://dajuyuan2.wx.cibn.cc/vod/detail/" + vid;
        }
        WXMediaMessage msg = new WXMediaMessage(webpage);
        //TODO zxy
        msg.title = clsName;
        msg.description = description;
        try {
          //  if (thumb == null) {

                thumb =getHttpBitmap( BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher),true);
             //   thumb=BitmapFactory.de
           // }
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            e.printStackTrace();
        }
        msg.thumbData = WXUtils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (type == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneFavorite;
        }
        api.sendReq(req);
        Lg.i("分享",req+"....");
        if (thumb != null) {
            thumb.recycle();
            thumb = null;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //TODO weibo 新浪微博分享
    public void testShareImage() {
        WeiboMessage weiboMessage = new WeiboMessage();

//        ImageObject imageObject = new ImageObject();
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        imageObject.setImageObject(bitmap);
//        weiboMessage.mediaObject = imageObject;

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
//        mediaObject.title = datas.getVname();
//        mediaObject.description = datas.getStoryplot();
        //TODO zxy
        mediaObject.title = clsName;
        mediaObject.description = description;
        Bitmap thumb = null;
        try {
            thumb = Glide.with(getContext()).load(fid).asBitmap().centerCrop().into(500, 500).get();
        } catch (Exception e) {
            thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            e.printStackTrace();
        }
        //BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        mediaObject.setThumbImage(thumb);


        if (this.type == 1) {
            mediaObject.actionUrl = "http://dajuyuan2.wx.cibn.cc/vod/livedetail/" + vid;
        } else {
            mediaObject.actionUrl = "http://dajuyuan2.wx.cibn.cc/vod/detail/" + vid;
        }

        mediaObject.defaultText = "cibn东方大剧院分享";
        weiboMessage.mediaObject = mediaObject;

        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        mWeiboShareAPI.sendRequest(context, request);
    }
    private Bitmap getHttpBitmap(Bitmap bitMap, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 80;
        int newHeight = 80;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        if (needRecycle) bitMap.recycle();
        return newBitMap;
    }
}