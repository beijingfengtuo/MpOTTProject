package cn.cibnmp.ott.glide;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.utils.FileSizeUtil;
import cn.cibnmp.ott.utils.Lg;

public class GlideDiskCache {

	private static GlideDiskCache instance = null;

	private GlideDiskCache() {

	}

	public static GlideDiskCache getInstance() {
		if (instance == null) {
			instance = new GlideDiskCache();
		}
		return instance;
	}

	public void getDiskCacheSize(final GlideDisCacheSizeCallBack callBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					File f = Glide.getPhotoCacheDir(App.getInstance()
							.getApplicationContext());
					Lg.d("-------Glide图片缓存路径:" + f.toString() + "-------");
					double size = FileSizeUtil.getFileOrFilesSize(f.toString(),
							FileSizeUtil.SIZETYPE_MB);
					Lg.d("-------Glide图片缓存大小:" + size + "MB-------");
					if (callBack != null) {
						callBack.getSize(size);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void clearDiskCache(final GlideClearDiskCacheCallBack callBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Glide.get(App.getInstance().getApplicationContext())
							.clearDiskCache();
					if (callBack != null) {
						callBack.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public interface GlideDisCacheSizeCallBack {
		void getSize(double d);
	}

	public interface GlideClearDiskCacheCallBack {
		void finish();
	}

}
