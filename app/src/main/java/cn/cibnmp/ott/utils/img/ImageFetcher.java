package cn.cibnmp.ott.utils.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.lib.CustomGlideDrawableImageViewTarget;
import cn.cibnmp.ott.utils.ImageUtils;
import cn.cibnmp.ott.utils.Lg;

public class ImageFetcher {
    private static final String TAG = "ImageFetcher";
    private static ImageFetcher sInstance = null;
    private ConcurrentHashMap<String, String> fileCache = new ConcurrentHashMap<String, String>();
    private HashMap<Integer, Bitmap> localImageCache = new HashMap<Integer, Bitmap>();
    private HashMap<Integer, Integer> referenceCache = new HashMap<Integer, Integer>();
    private Context mContext;
    private Handler mHandler;

    public ImageFetcher(final Context context) {
        this.mContext = context;
        mHandler = new Handler();
    }

    public static final synchronized ImageFetcher getInstance() {
        if (sInstance == null) {
            sInstance = new ImageFetcher(App.getInstance().getApplicationContext());
        }
        return sInstance;
    }

    /**
     * 读取本地大背景并且不压缩图片方法, 如果图片可以被压缩，使用glide框架加载图片
     *
     * @param resId
     * @param imageView
     */
    public void loadLocalBigImage(int resId, ImageView imageView) {
        if (imageView == null) {
            Lg.e(TAG, "imageView can't be null . ");
            return;
        }
        try {
            if (localImageCache.containsKey(resId)) {
                Lg.d("resId = " + resId + " has exist in cache , read directly from cache .");
                referenceCache.put(resId, referenceCache.get(resId) + 1);
                Bitmap bm = localImageCache.get(resId);
                if (bm == null || bm.isRecycled()) {
                    bm = ImageUtils.ReadBitmapById(mContext, resId);
                }
                imageView.setImageBitmap(bm);
            } else {
                // BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inDither = false;
                // options.inPreferredConfig = null;
                // options.inPurgeable = true;
                // options.inInputShareable = true;
                // Bitmap bitmap = BitmapFactory.decodeResource(
                // mContext.getResources(), resId, null);
                Lg.d("-----read big picture from local.-----");
                Bitmap bitmap = ImageUtils.ReadBitmapById(mContext, resId);
                imageView.setImageBitmap(bitmap);
                localImageCache.put(resId, bitmap);
                referenceCache.put(resId, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除界面对某一个大图片的引用,方便大图片缓存清除无引用状态的bitmap对象
     *
     * @param resId
     * @return
     */
    public void removeReference(int resId) {
        if (localImageCache.containsKey(resId)) {
            int refCount = referenceCache.get(resId);
            if (refCount - 1 <= 0) {
                Lg.d("resId = " + resId + " has no reference in referenceCache , remove the bitmap from localImageCache .");
                Bitmap bm = localImageCache.get(resId);
                if (!bm.isRecycled()) {
                    bm.recycle();
                }
                bm = null;
                localImageCache.remove(resId);
                System.gc();
            } else {
                Lg.d("resId = " + resId + " has " + (refCount - 1) + " reference .");
                referenceCache.put(resId, refCount - 1);
            }
        }
    }

    public Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
                System.out.println(Build.VERSION.SDK_INT + "---------" + bitmap + "-------->>>>>>>" + pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 设置缩略图
     *
     * @param bitMap 需要处理的图片
     * @param newWidth 设置想要的大小
     * @param newHeight 设置想要的大小
     * @param needRecycle 是否释放Bitmap
     * @return 缩略图
     */
    public static Bitmap getBitmapThumbnail(Bitmap bitMap, int newWidth, int newHeight, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
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

    public void loadImage(final String fid, final ImageView imageView, final int holderResId) {
        this.loadImage(fid, imageView, holderResId, false, false);
    }

    public void loadBlurImage(final String fid, final ImageView imageView) {
        this.loadImage(fid, imageView, 0, true, false);
    }

    public void loadCircleImage(final String fid, final int holderResId, final ImageView imageView) {
        this.loadImage(fid, imageView, holderResId, false, true);
    }

    // 加载一般网络图片的方法
    public void loodingImage(String fid, final ImageView imageView, final int holderResId) {
        Glide.with(mContext).load(fid).placeholder(holderResId) // 占位图
                .error(holderResId).into(imageView);
    }

    // 加载竖图截取
    public void loadImageCenterCrop(String fid, final ImageView imageView, final int holderResId){
        Glide.with(mContext).load(fid).placeholder(holderResId) // 占位图
                .error(holderResId).centerCrop().into(imageView);
    }

    // 加载微信园图片的方法
    public void loodingImageYuan(Context context, String fid, ImageView imageView, int holderResId) {
        Glide.with(mContext).load(fid).placeholder(holderResId) // 占位图
                .error(holderResId).transform(new GlideCircleTransform(context)).into(imageView);
    }

    /**
     * 获取网络图片
     *
     * @param fid
     * @param imageView
     * @param holderResId 占位图
     */
    public void loadImage(final String fid, final ImageView imageView, final int holderResId, final boolean blur, final boolean circle) {
        if (fid == null || imageView == null) {
            Lg.e(TAG, "loadImage params invalid.");
            if (imageView != null && holderResId != 0) {
                Glide.with(mContext).load(holderResId).into(imageView);
            }
            return;
        }
        imageView.setTag(R.id.tag_first, fid);
        if (holderResId != 0) {
            Glide.with(mContext).load(holderResId).into(imageView);
        }
        if (fileCache.containsKey(fid)) {
            if (blur) {
                Glide.with(mContext).load(fileCache.get(fid)).override(400, 223).transform(new BlurTransformation(mContext)).placeholder(holderResId).into(imageView);
            } else if (circle) {
                Glide.with(mContext).load(fileCache.get(fid)).transform(new GlideCircleTransform(mContext)).placeholder(holderResId).into(imageView);
            } else {
                // 加载一般网络图片的方法
                loodingImage(fileCache.get(fid), imageView, holderResId);
            }
        } else {
//            String fidUrl = App.cdnPicUrl + "play?fid=" + fid;
            String fidUrl = fid;

            fileCache.put(fid, fidUrl);
            if (blur) {
                Glide.with(mContext).load(fidUrl).override(400, 223).transform(new BlurTransformation(mContext)).placeholder(holderResId) // 占位图
                        .into(imageView);
            } else if (circle) {
                Glide.with(mContext).load(fidUrl).transform(new GlideCircleTransform(mContext)).placeholder(holderResId) // 占位图
                        .into(imageView);
            } else {
                // 加载一般网络图片的方法
                Log.d("LocalFilepic", fidUrl);
//                loodingImage(fidUrl, imageView, holderResId);
                loodingImage(fid, imageView, holderResId);
            }
        }

//			HttpRequest.getInstance().excute("getImageByFid", new Object[] { fid, new HttpResponseListener() {
//				@Override
//				public void onSuccess(String response) {
//					if (response == null) {
//						Lg.e(TAG, "httpRequest getImageByFid response is null . fid = " + fid);
//						return;
//					}
//					try {
//						final JSONObject json = new JSONObject(response);
//						if (!json.has("FID") || !json.has("LocalFile") || !json.has("FileSize") || !json.has("FileExist")) {
//							Lg.e(TAG, "httpRequest getImageByFid response's field is invalid. fid = " + fid + " , response = " + response);
//							return;
//						}
//						// 文件存在
//						if (json.optString("FileExist").equals(Constant.LOCALCACHEEXIST)) {
//							fileCache.put(fid, json.optString("LocalFile"));
//							// 有可能在gridview中imageView又被设置了新的fid
//							if (imageView.getTag(R.id.tag_first).equals(fid))
//								mHandler.post(new Runnable() {
//									@Override
//									public void run() {
//										if (blur) {
//											Glide.with(mContext).load(json.optString("LocalFile")).override(400, 223).transform(new BlurTransformation(mContext)).placeholder(holderResId) // 占位图
//													.into(imageView);
//										} else if (circle) {
//											Glide.with(mContext).load(json.optString("LocalFile")).transform(new GlideCircleTransform(mContext)).placeholder(holderResId) // 占位图
//													.into(imageView);
//										} else {
//											// 加载一般网络图片的方法
//											Log.d("LocalFilepic", json.optString("LocalFile"));
//											loodingImage(json.optString("LocalFile"), imageView, holderResId);
//										}
//									}
//								});
//						}
//					} catch (JSONException e) {
//						Lg.e(TAG, "httpRequest getImageByFid response isn't jsonString .");
//						e.printStackTrace();
//					}
//				}
//				@Override
//				public void onError(String error) {
//					Lg.e(TAG, "fid = " + fid + " , getImageByFid onError : " + ((error == null) ? "" : error));
//				}
//			} });
    }

    public void loadImage(final String fid, final ImageView imageView) {
        this.loadImage(fid, imageView, 0);
    }


    /**
     * 获取网络图片
     *
     * @param fid
     * @param imageView
     */
    public void loadImageAndUpdateSize(final String fid, final ImageView imageView) {
        try {
            if (TextUtils.isEmpty(fid) || imageView == null) {
                Lg.e(TAG, "loadImage params invalid.");
                return;
            }

            String url = "";
            if (fid.startsWith("http://")) {
                url = fid;
            } else {
                url = App.cdnPicUrl + "/play?fid=" + fid;
            }

            Glide.with(mContext).load(url).dontAnimate()
                    .into(new CustomGlideDrawableImageViewTarget(imageView));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载本地资源图片到imageView
     *
     * @param resId
     * @param imageView
     */
    public void loadLocalImage(int resId, ImageView imageView) {
        if (imageView != null)
            Glide.with(mContext).load(resId).into(imageView);
    }
}
