package cn.cibnmp.ott.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

	private static final String TAG = "cn.cibn.DaemonService";

	public FileUtils() {
	}

	public static boolean copyFile(InputStream in, String destFileName) {
		FileOutputStream out = null;
		byte[] buffer = new byte[10240];
		String bakDest = destFileName + ".bak";
		try {
			File destFile = new File(bakDest);
			destFile.delete();
			destFile.getParentFile().mkdirs();
			destFile.createNewFile();
			out = new FileOutputStream(destFile);
			int num = 0;
			while ((num = in.read(buffer)) != -1) {
				out.write(buffer, 0, num);
			}

			File oldFile = new File(destFileName);
			oldFile.delete();

			destFile.renameTo(oldFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "copyFile error: " + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void delFile(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			if (f.delete()) {
				Log.i(TAG, "delfile OK : " + filename);
			} else {
				Log.i(TAG, "delfile ERROR : " + filename);
			}
		}
	}

	public static boolean onlyCopyFile(String src, String dst) {
//		Log.i(TAG, "*****************Copy File*************");
//		Log.i(TAG, "file src:" + src);
//		Log.i(TAG, "file dst:" + dst);
//		Log.i(TAG, "**************************************");
//		File f = new File(dst);
//		try {
//			org.apache.commons.io.FileUtils.copyFile(new File(src), f);
//			Lg.i(TAG, "copy completed.");
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}

	private static boolean deleteDir(File dir) {

		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					Log.i(TAG, "delete file:" + children[i] + success);
					if (!success) {
						return false;
					}
				}
			}
		}
		return dir.delete();
	}

	public static File rename(String oldName, String newName) {
		if (oldName == null || newName == null) {
			return null;
		}
		File f = new File(oldName);
		if (f.exists()) {
			File renamedFile = new File(newName);
			boolean flg = f.renameTo(renamedFile);
			if (!flg) {
				if (f.delete()) {
					Log.i(TAG, "delfile OK : " + oldName);
				} else {
					Log.i(TAG, "delfile ERROR : " + oldName);
				}
			} else {
				return renamedFile;
			}
		}
		return null;
	}

	/**
	 * 创建目录
	 * 
	 * @param dirName
	 */
	public static File creatDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
}
