package com.bx.erp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PoiUtilsTest {
	@Test
	public void readExcelTest() throws IOException {
		String file = "D:\\BXERP\\trunk\\doc\\手动测试文档\\DEV\\元粒度测试（单元测试）_nbr\\retailtrade.xlsx";
		Map<String, Object> params = new HashMap<String, Object>();
		PoiUtils poiXls = new PoiUtils(file);
		List<String> listRow = poiXls.readExcelRow(file, 1, 0);
		List<String> listRow1 = poiXls.readExcelRow(file, 1, 1);
		Assert.assertTrue(listRow != null && listRow1 != null, "读取xls文件失败！");
		for (int i = 0; i < listRow.size(); i++) {
			params.put(listRow.get(i), listRow1.get(i));
		}
		System.out.println(params);
	}

}
