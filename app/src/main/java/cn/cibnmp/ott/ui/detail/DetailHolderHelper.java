package cn.cibnmp.ott.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.cibnmp.ott.bean.ActionParams;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.base.Action;
import cn.cibnmp.ott.ui.base.BaseActivity;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.Lg;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibnmp.ott.utils.Utils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.pmrecyclerview.UtilRc;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListItemHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListView;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.SpaceViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TagViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TitleViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.ViewPagerViewHolder;

/**
 * Created by axl on 2018/2/1.
 */

public class DetailHolderHelper {
    private static String TAG = "DetailHolderHelper";


    //(标题栏)
    public static void ViewHolderHelper0(int flag, TitleViewHolder holder, NavigationInfoItemBean itemBean) {

        if (itemBean != null && itemBean.getName() != null) {
            holder.rl_title.setVisibility(View.VISIBLE);
            holder.title.setText(itemBean.getName());
            if (flag == 0) {
                holder.img1.setVisibility(View.GONE);
            } else {
                holder.img1.setVisibility(View.VISIBLE);
            }
        } else {
            holder.rl_title.setVisibility(View.GONE);
            Log.e(TAG, "ViewHolderHelper0-->> itemBean.getContents() == null");
            return;
        }
    }

    //(4张大图轮播)
    public static void ViewHolderHelper1(final Context context, boolean visible, final ViewPagerViewHolder holder,
                                         final NavigationInfoItemBean itemBean, HomeOnItemClickListener listener) {

        if (itemBean != null && itemBean.getContents() != null && itemBean.getContents().size() > 0) {
            holder.notifyData(itemBean.getContents());
            holder.startSwitch();
            holder.mViewPager.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Log.e(TAG, "-----viewPager hasFocus-----");
                        holder.stopSwitch();
                        holder.startSwitch();

                    } else {
                        Log.e(TAG, "-----viewPager loseFocus-----");
                    }

                }
            });

            holder.mViewPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper1-->> itemBean.getContents() == null");
            return;
        }
    }

    //(只有四个按钮)
    public static void ViewHolderHelper2(TagViewHolder holder, final NavigationInfoItemBean itemBean, final HomeOnItemClickListener listener) {

        if (itemBean.getTagContents() != null) {

            if (!StringUtils.isEmpty(itemBean.getTagContents().get(0).getDisplayName())) {
                holder.name1.setText(itemBean.getTagContents().get(0).getDisplayName());
                holder.name1.setVisibility(View.VISIBLE);
                holder.name1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.BUNDLE_ACTION, itemBean.getTagContents().get(0).getAction());
                            bundle.putInt(Constant.BUNDLE_EPGID, itemBean.getTagContents().get(0).getEpgId());
                            String json = itemBean.getTagContents().get(0).getActionParams();
                            ActionParams actionParams = JSON.parseObject(json, ActionParams.class);
                            bundle.putInt(Constant.BUNDLE_COLUMNID, actionParams.getP1());
                            bundle.putInt(Constant.BUNDLE_SUBJECTID, actionParams.getP2());
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            }

            if (!StringUtils.isEmpty(itemBean.getTagContents().get(1).getDisplayName())) {
                holder.name2.setText(itemBean.getTagContents().get(1).getDisplayName());
                holder.name2.setVisibility(View.VISIBLE);
                holder.name2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.BUNDLE_ACTION, itemBean.getTagContents().get(1).getAction());
                            bundle.putInt(Constant.BUNDLE_EPGID, itemBean.getTagContents().get(1).getEpgId());
                            String json = itemBean.getTagContents().get(1).getActionParams();
                            ActionParams actionParams = JSON.parseObject(json, ActionParams.class);
                            bundle.putInt(Constant.BUNDLE_COLUMNID, actionParams.getP1());
                            bundle.putInt(Constant.BUNDLE_SUBJECTID, actionParams.getP2());
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            }

            if (!StringUtils.isEmpty(itemBean.getTagContents().get(2).getDisplayName())) {
                holder.name3.setText(itemBean.getTagContents().get(2).getDisplayName());
                holder.name3.setVisibility(View.VISIBLE);
                holder.name3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.BUNDLE_ACTION, itemBean.getTagContents().get(2).getAction());
                            bundle.putInt(Constant.BUNDLE_EPGID, itemBean.getTagContents().get(2).getEpgId());
                            String json = itemBean.getTagContents().get(2).getActionParams();
                            ActionParams actionParams = JSON.parseObject(json, ActionParams.class);
                            bundle.putInt(Constant.BUNDLE_COLUMNID, actionParams.getP1());
                            bundle.putInt(Constant.BUNDLE_SUBJECTID, actionParams.getP2());
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            }

            if (!StringUtils.isEmpty(itemBean.getTagContents().get(3).getDisplayName())) {
                holder.name4.setText(itemBean.getTagContents().get(3).getDisplayName());
                holder.name4.setVisibility(View.VISIBLE);
                holder.name4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.BUNDLE_ACTION, itemBean.getTagContents().get(3).getAction());
                            bundle.putInt(Constant.BUNDLE_EPGID, itemBean.getTagContents().get(3).getEpgId());
                            String json = itemBean.getTagContents().get(3).getActionParams();
                            ActionParams actionParams = JSON.parseObject(json, ActionParams.class);
                            bundle.putInt(Constant.BUNDLE_COLUMNID, actionParams.getP1());
                            bundle.putInt(Constant.BUNDLE_SUBJECTID, actionParams.getP2());
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            }
        } else {
            Log.e(TAG, "ViewHolderHelper2-->>itemBean.getContents()== null");
            return;
        }
    }

    //(四张小图,24号文字)
    public static void ViewHolderHelper3(final Context context, final boolean visible, CommonViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem) {

        final ImageView imag = holder.recyclerview_imagview;
        final TextView marqueeTextView = holder.recyclerview_text;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getName())) {
                marqueeTextView.setText(itemBean.getName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.recyclerview_imagview);
            }

           holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper3-->> itemBean.getContents() == null");
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.recyclerview_imagview);
            return;
        }
    }

    //(四张小图,24文字,22号文字)
    public static void ViewHolderHelper4(final Context context, boolean visible, final CommonListHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, HomeOnItemClickListener listener) {
        final ImageView imag = holder.common_list_imagview;
        final TextView marqueeTextView = holder.common_list_text;
        final TextView marDesTextView = holder.common_list_text1;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getName())) {
                marqueeTextView.setText(itemBean.getName());
            }

            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marDesTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.common_list_imagview);
            }

            holder.common_list_mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper4-->> itemBean.getContents() == null");
            holder.common_list_relativelayout.setVisibility(View.GONE);
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.common_list_imagview);
            return;
        }
    }

    //(一张大图,24文字)
    public static void ViewHolderHelper5(final Context context, boolean visible, final SpaceViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, HomeOnItemClickListener listener) {
        final ImageView imag = holder.space_imagview;
        final TextView space_textviewtime = holder.space_textviewtime;
        final TextView space_textview = holder.space_textview;
        final TextView space_textview1 = holder.space_textview1;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                space_textview.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getScrollMarquee())) {
                space_textview1.setText(itemBean.getScrollMarquee());
            }

            if (!TextUtils.isEmpty(itemBean.getPlayTime())) {
                space_textviewtime.setVisibility(View.VISIBLE);
                space_textviewtime.setText("时长: " + TimeUtils.getCurTime4(Long.valueOf(itemBean.getPlayTime()).longValue()));
            } else {
                space_textviewtime.setVisibility(View.INVISIBLE);
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                imag.setVisibility(View.VISIBLE);
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.space_imagview);
            }
           holder.space_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper5-->> itemBean.getContents() == null");
            holder.space_relativelayout.setVisibility(View.GONE);
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.space_imagview);
            return;
        }
    }

    //(一张大图,24文字)
    public static void ViewHolderHelper6(final Context context, final boolean visible, CommonViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem) {

        final ImageView imag = holder.recyclerview_imagview;
        final TextView view_text = holder.recyclerview_text;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getName())) {
                view_text.setText(itemBean.getName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loodingImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage(layoutItem.getC(), layoutItem.getR())).into(holder.recyclerview_imagview);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper6-->> itemBean.getContents() == null");
            Glide.with(context).load(UtilRc.getPlaceHolderImage(layoutItem.getC(), layoutItem.getR())).into(holder.recyclerview_imagview);
            return;
        }
    }

    //(一张大图,24文字,四张小图,24文字,24号文字)
    public static void ViewHolderHelper7(final Context context, boolean visible, final CommonListHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, HomeOnItemClickListener listener) {
        final ImageView imag = holder.common_list_imagview;
        final TextView marqueeTextView = holder.common_list_text;
        final TextView marDesTextView = holder.common_list_text1;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getName())) {
                marqueeTextView.setText(itemBean.getName());
            }

            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marDesTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.common_list_imagview);
            }
            holder.common_list_mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper7-->> itemBean.getContents() == null");
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.common_list_imagview);
            return;
        }
    }

    //(三张长图,24文字)
    public static void ViewHolderHelper8(final Context context, boolean visible, final CommonListView holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, HomeOnItemClickListener listener) {
        final TextView textView1 = holder.person_text1;
        final TextView textView2 = holder.person_text2;
        final TextView textView3 = holder.person_text3;
        final ImageView posterImg1 = holder.person_imageview1;
        final ImageView posterImg2 = holder.person_imageview2;
        final ImageView posterImg3 = holder.person_imageview3;

        if (visible) {

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(0).getName())) {
                textView1.setText(itemBean.getTagContents().get(0).getName());
            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(1).getName())) {
                textView2.setText(itemBean.getTagContents().get(1).getName());

            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(2).getName())) {
                textView3.setText(itemBean.getTagContents().get(2).getName());
            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(0).getImg())) {

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(0).getImg(), posterImg1, 0);

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(1).getImg(), posterImg2, 0);

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(2).getImg(), posterImg3, 0);

                holder.person_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailHolderHelper.onClick(itemBean, context);
                        }

                });

            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview1);
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview2);
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview3);
            }
        } else {
            Log.e(TAG, "ViewHolderHelper8-->> itemBean.getContents() == null");
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview1);
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview2);
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.person_imageview3);
            return;
        }
    }

    //(四张小图,24文字,24号文字)
    public static void ViewHolderHelper9(final Context context, boolean visible, final CommonListItemHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, HomeOnItemClickListener listener) {
        final ImageView imag = holder.imglistview;
        final TextView marqueeTextView = holder.textlistView;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getName())) {
                marqueeTextView.setText(itemBean.getName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.imglistview);
            }

            imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailHolderHelper.onClick(itemBean, context);
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper9-->> itemBean.getContents() == null");
            Glide.with(context).load(UtilRc.getPlaceHolderImage2(layoutItem.getC(), layoutItem.getR())).into(holder.imglistview);
            return;
        }
    }


    public static void onClick(NavigationInfoItemBean itemBean, Context context){
        String p1 = "";
        String p2 = "";
        String p3 = "";
        String ationUrl = "";

        if (!TextUtils.isEmpty(itemBean.getActionParams())) {
            try {
                JSONObject jsonObject = new JSONObject(itemBean.getActionParams());
                p1 = jsonObject.optString("p1", "");
                p2 = jsonObject.optString("p2", "");
                p3 = jsonObject.optString("p3", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            ationUrl = itemBean.getActionUrl();
            //actionUrl为http://时，参数传递失败
            if (!TextUtils.isEmpty(ationUrl))
                ationUrl = URLEncoder.encode(ationUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p1为http://时，参数传递失败
            if (!TextUtils.isEmpty(p1))
                p1 = URLEncoder.encode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p2为http://时，参数传递失败
            if (!TextUtils.isEmpty(p2))
                p2 = URLEncoder.encode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            //p3为http://时，参数传递失败
            if (!TextUtils.isEmpty(p3))
                p3 = URLEncoder.encode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




        Lg.i("HolderHelper",itemBean.toString());
        ((BaseActivity) context).startActivity(
                itemBean.getAction(),
                Utils.getUrlParameter(Constant.epgIdKey, String.valueOf(itemBean.getEpgId())),
                Utils.getUrlParameter(Constant.contentIdKey, String.valueOf(itemBean.getContentId())),
                Utils.getUrlParameter(Constant.actionKey, String.valueOf(itemBean.getAction())),
                Utils.getUrlParameter(Constant.p1ParamKey, p1),
                Utils.getUrlParameter(Constant.p2ParamKey, p2),
                Utils.getUrlParameter(Constant.p3ParamKey, p3),
                Utils.getUrlParameter(Constant.actionUrlKey, ationUrl),
                Utils.getUrlParameter(Constant.sidKey, String.valueOf(itemBean.getSid())));

}
}
