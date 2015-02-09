package com.app.myapp.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.app.myapp.util.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpImage 
{
	private static String SDCARD_DATA_PATH = "/sdcard/com.app.myapp/";
	
	public static Bitmap getImage(String url) 
	{
		Bitmap map = null;
		if (isInFile(url)) {
			map = getFileFromLocal(url);
		} else {
			map = getFileFromLetao(url);
		}
		return map;
	}

	private static Bitmap getFileFromLetao(String url) {
		try {
			String imageUrl = "";
			imageUrl = url;
			byte[] data = BitmapHelper.newInstance().getBitmap(imageUrl);
			Save2Local(data, url);
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean Save2Local(byte[] data, String url) {
		if (!Helper.hasSDCard()) {
			return false;
		}
		if (data == null || data.length == 0)
			return false;
		String filePath = getCacheImgPath(url);
		File file = new File(filePath);
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (parent != null) {
				parent.mkdirs();
			}
		}
		try {
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			os.write(data);
			os.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private static Bitmap getFileFromLocal(String url) {
		String filePath = getCacheImgPath(url);
		Bitmap map = BitmapFactory.decodeFile(filePath);
		File f = new File(filePath);
		if (f.exists())
			f.setLastModified(new Date().getTime());
		return map;

	}

	private static boolean isInFile(String url) {
		String filePath = getCacheImgPath(url);
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}
		return true;
	}

	private static String getCacheImgPath(String url) {
		String pirName = Helper.md5(url);
		String parentPath = SDCARD_DATA_PATH + pirName.substring(0, 1) + "/";
		String filePath = parentPath + pirName;
		return filePath;
	}

	public static void ClearCache() {
		String parentPath = SDCARD_DATA_PATH;
		ArrayList<File> images = new ArrayList<File>(); 
		getFiles(images, parentPath); 
		 for (int i = 0; i < images.size(); i++)
		 {
			 images.get(i).delete();
		 }
	}
	
	private static void getFiles(ArrayList<File> fileList, String path) { 
	    File[] allFiles = new File(path).listFiles(); 
	    for (int i = 0; i < allFiles.length; i++) { 
	        File file = allFiles[i]; 
	        if (file.isFile()) { 
	            fileList.add(file); 
	        } else if (!file.getAbsolutePath().contains(".thumnail")) { 
	            getFiles(fileList, file.getAbsolutePath()); 
	        } 
	    } 
	} 
}
