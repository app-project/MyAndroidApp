package com.app.myapp.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapHelper 
{
	public static BitmapHelper newInstance() {
		return new BitmapHelper();
	}

	HttpURLConnection conn;
	public byte[] getBitmap(String url) throws IOException {
		if (url != null && !url.equals("")) 
		{
			InputStream is = null;
			URL url1 = new URL(url);
			conn = (HttpURLConnection) url1.openConnection();

			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			return receiveData(is);
		} else {
			return null;
		}
	}

	public byte[] receiveData(InputStream in) throws IOException 
	{

		ByteArrayOutputStream bos = null;

		byte[] retval = null;
		try {
			bos = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];
			while (true) {
				int len = in.read(buf);
				if (len == -1) {
					break;
				}
				bos.write(buf, 0, len);
			}
			retval = bos.toByteArray();
		} finally {
			in.close();
			bos.close();
			conn.disconnect();
		}
		return retval;
	}

}
