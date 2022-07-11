//package com.bx.erp.test.wx.wxopen;
//
//import java.io.IOException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bx.erp.test.BaseActionTest;
//
//@WebAppConfiguration
//public class WXOpenActionTest extends BaseActionTest {
//
//	@BeforeClass
//	public void setup() {
//		super.setUp();
//	}
//
//	@AfterClass
//	public void tearDown() {
//		super.tearDown();
//	}
//
//	@Test
//	public void testGetVerifyTicket() throws Exception {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("timestamp", "");
//		jsonObject.put("nonce", "");
//		jsonObject.put("msg_signature", "");
//		// jsonObject.put("", "");
//
//		String resp = doPost("http://wx.bxit.vip/wxOpen/getComponentVerifyTicket.bx", jsonObject.toString());
//		System.out.println("response: " + resp);
//	}
//
//	@Test
//	public void testComponentGetAccessToken() throws Exception {
//		// String url_component_access_token =
//		// "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
//		// String response = doPost("http://wx.bxit.vip/wxOpen/getPreAuthCode.bx","");
//		String response = doGet("http://wx.bxit.vip/wxOpen/getPreAuthCode.bx");
//
//		System.out.println("response: " + response);
//	}
//
//	public String doPost(String url, String json) {
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpEntity entity = null;
//		HttpEntity Rentity = null;
//		String resp = "";
//		CloseableHttpResponse response = null;
//		try {
//			entity = new StringEntity(json, "UTF-8");
//			HttpPost hp = new HttpPost(url);
//			hp.addHeader("Content-Type", "application/json;charset=UTF-8");
//			hp.setEntity(entity);
//			System.out.println("body:" + entity);
//			response = httpclient.execute(hp);
//			Rentity = response.getEntity();
//			if (Rentity != null) {
//				resp = EntityUtils.toString(Rentity, "UTF-8");
//			}
//		} catch (Exception e) {
//			System.out.println("POST请求异常：" + e.getMessage());
//		} finally {
//			try {
//				if (response != null)
//					response.close();
//				if (httpclient != null)
//					httpclient.close();
//			} catch (IOException e) {
//			}
//		}
//		return resp;
//	}
//
//	public String doGet(String url) {
//		CloseableHttpResponse response = null;
//		String resp = "";
//		CloseableHttpClient httpCilent = HttpClients.createDefault();// Creates CloseableHttpClient instance with default configuration.
//		HttpGet httpGet = new HttpGet("http://www.baidu.com");
//		try {
//			response = httpCilent.execute(httpGet);
//			HttpEntity Rentity = response.getEntity();
//			if (Rentity != null) {
//				resp = EntityUtils.toString(Rentity, "UTF-8");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				httpCilent.close();// 释放资源
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return resp;
//	}
//}
