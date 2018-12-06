package cn.cibnmp.ott.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ta.utdid2.android.utils.StringUtils;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.adapter.HomeOneViewType;
import cn.cibnmp.ott.bean.ActionParams;
import cn.cibnmp.ott.bean.LayoutItem;
import cn.cibnmp.ott.bean.NavigationInfoItemBean;
import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.ui.categoryList.HomeOnItemClickListener;
import cn.cibnmp.ott.utils.TimeUtils;
import cn.cibnmp.ott.utils.img.ImageFetcher;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListItemHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonListView;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.CommonViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.SpaceViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TagViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.TitleViewHolder;
import cn.cibnmp.ott.widgets.pmrecyclerview.holders.ViewPagerViewHolder;

/**
 * Created by yangwenwu on 18/1/19.
 */

public class HomeOneViewHolderHelper {

    private static String TAG = "HomeOneViewHolderHelper";

    /**
     * 标题栏
     *
     * @param flag
     * @param holder
     * @param itemBean
     */
    public static void ViewHolderHelper0(int flag, TitleViewHolder holder, final NavigationInfoItemBean itemBean, final HomeOnItemClickListener listener) {

        if (itemBean == null) {
            Log.e(TAG, "ViewHolderHelper0-->> itemBean.getContents() == null");
            return;
        }

        if (!StringUtils.isEmpty(itemBean.getName())) {
            holder.title.setText(itemBean.getName());

            if (flag == 0) {
                holder.img1.setVisibility(View.GONE);
            } else {
                holder.img1.setVisibility(View.VISIBLE);
            }
        }

        if (!StringUtils.isEmpty(itemBean.getDisplayName())) {
            holder.fu_title.setText(itemBean.getDisplayName());
            holder.fu_title_relativelayout.setVisibility(View.VISIBLE);
        } else {
            holder.fu_title_relativelayout.setVisibility(View.GONE);
        }

        holder.rl_title.setVisibility(View.VISIBLE);

        holder.fu_title_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                    bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                    listener.onItemClickListener(bundle);
                }
            }
        });

        holder.fu_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                    bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                    listener.onItemClickListener(bundle);
                }
            }
        });
    }

    /**
     * 4张大图轮播
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param listener
     */
    public static void ViewHolderHelper1(final Context context, boolean visible, final ViewPagerViewHolder holder,
                                         final NavigationInfoItemBean itemBean, final HomeOnItemClickListener listener) {

        if (itemBean != null && itemBean.getContents() != null && itemBean.getContents().size() > 0) {
            holder.mViewPager.setVisibility(View.VISIBLE);
            holder.notifyData(itemBean.getContents(), listener);
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
        } else {
            holder.mViewPager.setVisibility(View.GONE);
            Log.e(TAG, "ViewHolderHelper1-->> itemBean.getContents() == null");
            return;
        }
    }

    /**
     * 只有四个按钮
     *
     * @param holder
     * @param itemBean
     * @param listener
     */
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
                            bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean.getTagContents().get(0));
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            } else {
                holder.name1.setVisibility(View.GONE);
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
                            bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean.getTagContents().get(1));
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            } else {
                holder.name2.setVisibility(View.GONE);
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
                            bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean.getTagContents().get(2));
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            } else {
                holder.name3.setVisibility(View.GONE);
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
                            bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean.getTagContents().get(3));
                            listener.onItemClickListener(bundle);
                        }
                    }
                });
            } else {
                holder.name4.setVisibility(View.GONE);
            }
        } else {
            holder.tag_relativelayout.setVisibility(View.GONE);
            Log.e(TAG, "ViewHolderHelper2-->>itemBean.getContents()== null");
            return;
        }
    }

    /**
     * 四张小图,24号文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper3(final Context context, final boolean visible, CommonViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final ImageView imag = holder.recyclerview_imagview;
        final TextView marqueeTextView = holder.recyclerview_text;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marqueeTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper3-->> itemBean.getContents() == null");
            holder.recyclerview_relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 四张小图,24文字,22号文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper4(final Context context, boolean visible, final CommonListHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final ImageView imag = holder.common_list_imagview;
        final TextView marqueeTextView = holder.common_list_text;
        final TextView marDesTextView = holder.common_list_text1;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marqueeTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getScrollMarquee())) {
                marDesTextView.setText(itemBean.getScrollMarquee());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.common_list_mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper4-->> itemBean.getContents() == null");
            holder.common_list_relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 一张大图,24文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper5(final Context context, boolean visible, final SpaceViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {
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
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.space_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper5-->> itemBean.getContents() == null");
            holder.space_relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 一张大图,24文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper6(final Context context, final boolean visible, CommonViewHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final ImageView imag = holder.recyclerview_imagview;
        final TextView view_text = holder.recyclerview_text;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                view_text.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loodingImage(itemBean.getImg(), imag, 0);
            } else {
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper6-->> itemBean.getContents() == null");
            holder.recyclerview_relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 一张大图,24文字,四张小图,24文字,24号文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper7(final Context context, boolean visible, final CommonListHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final ImageView imag = holder.common_list_imagview;
        final TextView marqueeTextView = holder.common_list_text;
        final TextView marDesTextView = holder.common_list_text1;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marqueeTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getScrollMarquee())) {
                marDesTextView.setText(itemBean.getScrollMarquee());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.common_list_mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper7-->> itemBean.getContents() == null");
            holder.common_list_relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 三张长图,24文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper8(final Context context, boolean visible, final CommonListView holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final TextView textView1 = holder.person_text1;
        final TextView textView2 = holder.person_text2;
        final TextView textView3 = holder.person_text3;
        final ImageView posterImg1 = holder.person_imageview1;
        final ImageView posterImg2 = holder.person_imageview2;
        final ImageView posterImg3 = holder.person_imageview3;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getTagContents().get(0).getDisplayName())) {
                textView1.setVisibility(View.VISIBLE);
                textView1.setText(itemBean.getTagContents().get(0).getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(1).getDisplayName())) {
                textView2.setVisibility(View.VISIBLE);
                textView2.setText(itemBean.getTagContents().get(1).getDisplayName());

            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(2).getDisplayName())) {
                textView3.setVisibility(View.VISIBLE);
                textView3.setText(itemBean.getTagContents().get(2).getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getTagContents().get(0).getImg())) {

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(0).getImg(), posterImg1, 0);

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(1).getImg(), posterImg2, 0);

                ImageFetcher.getInstance().loadImage(itemBean.getTagContents().get(2).getImg(), posterImg3, 0);

                holder.person_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                            bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                            listener.onItemClickListener(bundle);
                        } else {
                            return;
                        }
                    }
                });

            } else {
                posterImg1.setImageResource(R.color.home_bg_color);
                posterImg2.setImageResource(R.color.home_bg_color);
                posterImg3.setImageResource(R.color.home_bg_color);
            }
        } else {
            Log.e(TAG, "ViewHolderHelper8-->> itemBean.getContents() == null");
            holder.person_linearlayout.setVisibility(View.GONE);
            posterImg1.setImageResource(R.color.home_bg_color);
            posterImg2.setImageResource(R.color.home_bg_color);
            posterImg3.setImageResource(R.color.home_bg_color);
            return;
        }
    }

    /**
     * 四张小图,24文字,24号文字
     *
     * @param context
     * @param visible
     * @param holder
     * @param itemBean
     * @param layoutItem
     * @param listener
     */
    public static void ViewHolderHelper9(final Context context, boolean visible, final CommonListItemHolder holder,
                                         final NavigationInfoItemBean itemBean, LayoutItem layoutItem, final HomeOnItemClickListener listener) {

        final ImageView imag = holder.imglistview;
        final TextView marqueeTextView = holder.textlistView;

        if (visible) {
            if (!TextUtils.isEmpty(itemBean.getDisplayName())) {
                marqueeTextView.setVisibility(View.VISIBLE);
                marqueeTextView.setText(itemBean.getDisplayName());
            }

            if (!TextUtils.isEmpty(itemBean.getImg())) {
                ImageFetcher.getInstance().loadImage(itemBean.getImg(), imag, 0);
            } else {
                imag.setImageResource(R.color.home_bg_color);
            }

            holder.person_list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BUNDLE_ACTION, itemBean.getAction());
                        bundle.putSerializable(Constant.BUBDLE_NAVIGATIONINFOITEMBEAN, itemBean);
                        listener.onItemClickListener(bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Log.e(TAG, "ViewHolderHelper9-->> itemBean.getContents() == null");
            holder.relativelayout.setVisibility(View.GONE);
            imag.setImageResource(R.color.home_bg_color);
            return;
        }
    }
}
