package com.bx.erp.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component("okHttpUtil")
public class OkHttpUtil {
	public static final String COOKIE = "cookie";
	public static final String URL_LocalHost = "http://localhost/";
	public static final String z1url = "http://192.168.1.157:8080";
	public static final String z2url = "http://192.168.1.178:8080";

	public static String httpUrl = URL_LocalHost; // 用于get和post的Http请求的地址，要访问哪个服务器修改这里
	public static String sessionID = null;

	/** get
	 *
	 * @param url
	 *            请求的url
	 * @param queries
	 *            请求的参数，在浏览器？后面的数据，没有可以传null
	 * @return */
	public static String get(String url, Map<String, String> queries) {
		String responseBody = "";
		StringBuffer sb = new StringBuffer(httpUrl + url);
		if (queries != null && queries.keySet().size() > 0) {
			boolean firstFlag = true;
			Iterator<Entry<String, String>> iterator = queries.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iterator.next();
				if (firstFlag) {
					sb.append("?" + entry.getKey() + "=" + entry.getValue());
					firstFlag = false;
				} else {
					sb.append("&" + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		Request request = new Request.Builder() //
				.addHeader(COOKIE, sessionID) //
				.url(sb.toString()) //
				.build();
		Response response = null;
		OkHttpClient okHttpClient = new OkHttpClient.Builder() //
				.connectTimeout(10, TimeUnit.SECONDS)// 设置连接超时时间
				.readTimeout(20, TimeUnit.SECONDS)// 设置读取超时时间
				.build();
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				System.out.println("+++++++++++++++++++调用OKHttp成功，时间:" + new Date());
				return response.body().string();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("机器人Http请求失败");
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}

	/** post
	 *
	 * @param url
	 *            请求的url
	 * @param params
	 *            post form 提交的参数
	 * @param okHttpClient
	 * @return */
	public static String post(String url, Map<String, String> params) {
		String responseBody = "";
		FormBody.Builder builder = new FormBody.Builder();
		// 添加参数
		if (params != null && params.keySet().size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) != null) {
					builder.add(key, params.get(key));
				}
			}
		}
		System.out.println(httpUrl + url);
		Request request = null;
		if (sessionID == null) {
			request = new Request.Builder() //
					.url(httpUrl + url) //
					.post(builder.build()) //
					.build(); //
		} else {
			request = new Request.Builder() //
					.addHeader(COOKIE, sessionID) //
					.url(httpUrl + url) //
					.post(builder.build()) //
					.build(); //
		}
		Response response = null;
		OkHttpClient okHttpClient = new OkHttpClient.Builder() //
				.connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
				.readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
				.build();
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				if (url.equals("/staff/getTokenEx.bx")) {
					Headers headers = response.headers();
					List<String> cookies = headers.values("Set-Cookie");
					String cookie = cookies.get(0);
					sessionID = cookie.substring(0, cookie.indexOf(";"));
				}
//				System.out.println("+++++++++++++++++++调用OKHttp成功，时间:" + new Date() + "," + response.body().string());
				return response.body().string();
			} else {
				System.out.println("status:" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("机器人Http请求失败");
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}
}