package com.bx.erp.util;

import java.util.Map;

import com.bx.erp.model.commodity.Commodity;

public class AppUtil {
	public static String createCommodityViaOkHttp(String url, Commodity commodity) {
		// 将下面塞字段的代码放进Commodity中，统一管理Model的字段，避免增减字段时忘记修改本段代码。
		Map<String, String> params = commodity.putInMap();
		String response = OkHttpUtil.post(url, params); // TODO 要做失败处理。如果是名称重复，让导入继续。
		return response;
	}
}
