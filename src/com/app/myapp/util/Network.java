package com.app.myapp.util;

import com.app.myapp.app.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class Network {
	public static int checkNetworkInfo(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return 1;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return 2;
		context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

		return 0;
	}

	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		int tryNo = 0;
		boolean result = false;
		while (tryNo < 2) {
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info == null) {
				WaitOne(1);
				tryNo++;
			} else {
				result = info.isConnected();
				if (result)
					break;
				else {
					WaitOne(1);
					tryNo++;
				}
			}
		}
		return result;
	}

	public static boolean hasNetwork() {
		ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		int tryNo = 0;
		boolean result = false;
		while (tryNo < 2) {
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info == null) {
				WaitOne(1);
				tryNo++;
			} else {
				result = info.isConnected();
				if (result)
					break;
				else {
					WaitOne(1);
					tryNo++;
				}
			}
		}
		return result;
	}

	public static void WaitOne(int min) {
		try {
			Thread.sleep(200 * min);
		} catch (Exception e) {
		}
	}
}
