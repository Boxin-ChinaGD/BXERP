package com.bx.erp.util;

import java.util.Date;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.test.BaseTestNGSpringContextTest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@WebAppConfiguration
public class JsonUtilTest extends BaseTestNGSpringContextTest {

	@Test
	public void testInit() {
		Brand b = new Brand();
		b.setName("Brand A");
		b.setCreateDatetime(new Date());
		b.setDate1(new Date());
		// b.setDate2(new Date());
		b.setSyncDatetime(new Date());
		b.setUpdateDatetime(new Date());

		String s = JSONObject.fromObject(b, JsonUtil.jsonConfig).toString();
		System.out.println("应用jsonConfig后，string=" + s);

		// JSONObject joRT = new JSONObject(s);

		// JSONObject joRT = JSONObject.fromObject(s, JsonUtil.jsonConfig);

		JSONObject json = JSONObject.fromObject(s, JsonUtil.jsonConfig);
		System.out.println("Json=" + json);

		// json.getString(key)

		//
		// Object o = JSONObject.toBean(json, Brand.class, JsonUtil.jsonConfig);
		// Brand brand = (Brand) o;
		// System.out.println("toBean后，Brand=" + brand);
	}

	@Test
	public void testJsonValueProcessorOfDouble() {
		String str = "[{'amount': 1455993913.34}]";
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Float.class, new JsonValueProcessorOfDouble());
		JSONArray jsonArray = JSONArray.fromObject(str, jsonConfig);
		System.out.println("JsonObject:" + jsonArray.toString());

		com.alibaba.fastjson.JSONArray jsonArray2 = (com.alibaba.fastjson.JSONArray) JSON.parse(str);
		System.out.println("com.alibaba.fastjson :" + jsonArray2.toString());

		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setAmount(1455993913.34);
		retailTrade.setAmountCash(1.45599391334E9);
		String reString = JSON.toJSONString(retailTrade);
		System.out.println("com.alibaba.fastjson :" + reString);
		RetailTrade jsonObject = JSON.parseObject(reString, RetailTrade.class);
		System.out.println("com.alibaba.fastjson :" + jsonObject.getAmount());
	}
}
