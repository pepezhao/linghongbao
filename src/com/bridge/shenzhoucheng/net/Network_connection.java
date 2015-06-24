package com.bridge.shenzhoucheng.net;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bridge.shenzhoucheng.utils.FormFile;

public class Network_connection {
	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: <FORM METHOD=POST
	 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
	 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
	 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
	 * @param hm_list
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String post(String path, Map<String, String> params,
			List<FormFile> files) throws Exception {
		String TAG = "";
		String result = "";
		HttpURLConnection con = null;

		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int fileDataLength = 0;
		for (FormFile uploadFile : files) {// 得到文件类型数据的总长度

			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("\r\n");
			fileExplain.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilename() + "\"\r\n");
			fileExplain.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			fileExplain.append("\r\n");
			fileDataLength += fileExplain.length();
			if (uploadFile.getInStream() != null) {
				fileDataLength += uploadFile.getFile().length();
			} else {
				fileDataLength += uploadFile.getData().length;
			}
		}
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n"); 
			textEntity.append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8));
			textEntity.append("\r\n");
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + endline.getBytes().length;

		try {
			//if (Thread.interrupted())
			//	throw new InterruptedException();
			URL url = new URL(path);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000 /* milliseconds */);
			con.setConnectTimeout(20000 /* milliseconds */);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */

			con.setRequestProperty(
					"Accept",
					"image/gif,image/x-xbitmap,image/jpeg,image/pjpeg,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword,application/x-shockwave-flash,application/x-quickviewplus,*/*");
			con.setRequestProperty("keep-alive", "300");
			con.setRequestProperty("accept-language", "zh-cn,zh;q=0.5"); 
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("user-agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			con.connect();	
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				ds.writeBytes(URLEncoder.encode(entry.getValue(), HTTP.UTF_8));
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + end);
			}
			
			
			for(FormFile uploadFile : files){
				ds.writeBytes(twoHyphens + boundary + end);
	            ds.writeBytes("Content-Disposition: form-data; " + "name=\""+ uploadFile.getParameterName()+"\";filename=\"" + uploadFile.getParameterName()+ "\"" + end);
				ds.writeBytes(end);
				/* 取得文件的FileInputStream */
				FileInputStream fStream = new FileInputStream(uploadFile.getFile());
				
				 byte[] buffer = new byte[1024];
	             int len = 0;
	             while((len = fStream.read(buffer, 0, 1024))!=-1){
	            	 ds.write(buffer, 0, len);
	             }
	             fStream.close();
	             ds.writeBytes(end);
	        }
			
			ds.writeBytes(twoHyphens + boundary + end);
			ds.flush();
			
			
			/*for (FormFile uploadFile : files) {
				System.out.println("lllll="+uploadFile.toString());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\""
						+ uploadFile.getParameterName() + "\";filename=\""
						+ uploadFile.getFilename() + "\"" + end);
				ds.writeBytes(end);
				 取得文件的FileInputStream 
				FileInputStream fStream = new FileInputStream(uploadFile.getFile());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fStream.read(buffer, 0, 1024)) != -1) {
					ds.write(buffer, 0, len);
				}
				fStream.close();
				ds.writeBytes(end);
			}
			ds.writeBytes(twoHyphens + boundary + end);
			ds.flush();*/
			ds.close();
			//if (Thread.interrupted())
			//	throw new InterruptedException();
			// 获取数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine = null;
			// 使用循环来读取获得的数据
			while (((inputLine = reader.readLine()) != null)) {
				result += inputLine;
			}
			reader.close();
			con.disconnect();
			Log.d(TAG, "---------------------------[" + result + "]");
			if (Thread.interrupted())
				throw new InterruptedException();

		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = "InterruptedException";
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return result;
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String post(String path, Map<String, String> params,
			FormFile file) throws Exception {
		List<FormFile> file_ = new ArrayList<FormFile>();
		file_.add(file);
		return post(path, params, file_);
		// return post(path, params, new FormFile[]{file});
	}

	/**
	 * 上传图片
	 * 
	 * @param picUrl
	 *            请求地址
	 * @param list
	 *            图片路径集合
	 * @return 返回数据
	 * @throws NetworkException
	 *             异常
	 */
	public static String uploadFile(String picUrl, List list) {
		List picList = list;
		StringBuffer buffer = buffer = new StringBuffer();
		try {
			String BOUNDARY = "------------------------7dc3482080a10"; // 定义数据分隔线
			URL url = new URL(picUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			int leng = picList.size();
			for (int i = 0; i < leng; i++) {
				String fname = (String) picList.get(i);
				File file = new File(fname);
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file" + i
						+ "\";filename=\"" + file.getName() + "\"\r\n");
				sb.append("Content-Type:image/png\r\n\r\n");
				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();
			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			Log.e("Net Exception", "" + e);
		}
		return picUrl;

	}

	public static String postUrlData(String urlstr, String para) {
		String TAG = "";
		String result = "";
		Log.d(TAG, "[postUrlData]para=[" + para + "]");
		HttpURLConnection con = null;
		try {
			if (Thread.interrupted())
				throw new InterruptedException();
			URL url = new URL(urlstr);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000 /* milliseconds */);
			con.setConnectTimeout(15000 /* milliseconds */);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			con.setInstanceFollowRedirects(true);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.connect();
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(para);
			out.flush();
			out.close();
			if (Thread.interrupted())
				throw new InterruptedException();

			// 获取数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine = null;
			// 使用循环来读取获得的数据
			while (((inputLine = reader.readLine()) != null)) {
				result += inputLine;
			}
			reader.close();
			con.disconnect();
			Log.d(TAG, "[postUrlData]result=[" + result + "]");
			if (Thread.interrupted())
				throw new InterruptedException();

		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = "InterruptedException";
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return result;
	}

	/**
	 * 判断当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			System.out.println("**** newwork is off");
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				return false;
			}
			if (info == null) {
				System.out.println("**** newwork is off");
				return false;
			} else {
				if (info.isAvailable()) {
					System.out.println("**** newwork is on");
					return true;
				}
			}
		}
		System.out.println("**** newwork is off");
		return false;
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字：包名+类名
	 * @return true 在运行, false 不在运行
	 */

	public static boolean isServiceRunning(ActivityManager activityManager,
			String className) {
		boolean isRunning = false;
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		Log.e("IntroActivity-----", "service is running?==" + isRunning);
		return isRunning;
	}
}
