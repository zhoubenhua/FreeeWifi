package com.zhephone.hotspot.app.common;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.zhephone.hotspot.app.AppConfig;

import android.os.Environment;
import android.util.Log;

public class LogUtil {
	private static String TAG = "wifi_free";
	static boolean logControl = true;
	public static void recordLog(String savePathStr, String saveFileNameS, String saveDataStr, boolean saveTypeStr) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			File folder = new File(savePathStr);
			if(folder.exists() == false || folder.isDirectory() == false){
				folder.mkdirs();
			}
			File f = new File(folder, saveFileNameS);
			fw = new FileWriter(f, saveTypeStr);

			pw = new PrintWriter(fw);
			pw.print(saveDataStr+"\r\n");
			pw.flush();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {}
			}
			if(pw != null){
				pw.close();
			}
		}
	}

	public static void log(String log) {
		if (logControl) {
			Log.d(TAG, log);
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File path = new File(AppConfig.getInstance().getLog_dir());
				recordLog(path.getPath(), "wifi_log.txt",TimeUtil.getCurrentDate()+":"+log, true);
			}
		}
	}

}