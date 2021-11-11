package com.bx.erp.util;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

public class GeneralUtilTest {

	@Test
	public void sortAndDeleteDuplicated() {
		List<Integer> listOut = null;
		// Case1: 输入参数为null
		Integer[] ia = null;
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 0);

		// Case2: 无元素
		ia = null;
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 0);

		// Case3: 有1个重复元素
		Integer[] ia2 = { 1, 2, 2, 3 };
		Integer[] ia3 = { 1, 2, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia2);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);

		// Case4: 有2个重复元素
		Integer[] ia4 = { 1, 2, 2, 2, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia4);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);

		// Case5: 数组里面全部元素重复
		Integer[] ia5 = { 1, 1, 2, 2, 3, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia5);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);

		// Case6 数组里面有4个重复
		Integer[] ia6 = { 1, 1, 2, 2, 2, 2, 3, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia6);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);

		// Case7 数组全部元素都重复多个
		Integer[] ia7 = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia7);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);

		// Case8: 数组里面无重复
		Integer[] ia8 = { 1, 2, 3 };
		listOut = GeneralUtil.sortAndDeleteDuplicated(ia8);
		Assert.assertTrue(listOut != null);
		Assert.assertTrue(listOut.size() == 3);
		assertEquals(listOut.toArray(), ia3);
	}

	@Test
	public static void testFormatToShow() {
		Shared.caseLog("case1:测试很大的数能否不显示科学计数法");
		System.out.println("999900000000.00-->" + GeneralUtil.formatToShow(999900000000d));
		Assert.assertTrue(GeneralUtil.formatToShow(999900000000d).equals("999900000000.00"), "计算错误");
		Shared.caseLog("case2:测试传0能显示为0.00");
		System.out.println("0.00-->" + GeneralUtil.formatToShow(0d));
		Assert.assertTrue(GeneralUtil.formatToShow(0d).equals("0.00"), "计算错误");
		
		// 格式化2位小数 四舍五入
		Assert.assertTrue(GeneralUtil.formatToShow(1.1251d).equals("1.13"), "格式化错误:" + GeneralUtil.formatToShow(1.1251d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.1351d).equals("1.14"), "格式化错误:" + GeneralUtil.formatToShow(1.1351d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.1451d).equals("1.15"), "格式化错误:" + GeneralUtil.formatToShow(1.1451d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.2250d).equals("1.23"), "格式化错误:" + GeneralUtil.formatToShow(1.2250d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.2350d).equals("1.24"), "格式化错误:" + GeneralUtil.formatToShow(1.2350d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.2450d).equals("1.25"), "格式化错误:" + GeneralUtil.formatToShow(1.2450d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.22501d).equals("1.23"), "格式化错误:" + GeneralUtil.formatToShow(1.22501d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.23505d).equals("1.24"), "格式化错误:" + GeneralUtil.formatToShow(1.23505d));
		Assert.assertTrue(GeneralUtil.formatToShow(1.24508d).equals("1.25"), "格式化错误:" + GeneralUtil.formatToShow(1.24508d));
		// 格式化6位小数 不足补0
		Assert.assertTrue(GeneralUtil.formatToCalculate(1.1d).equals("1.100000"), "格式化错误:" + GeneralUtil.formatToShow(1.1d));
	}

	@Test
	public static void testPrintArray(Object[] arr) {
		String out = null;

		System.out.println("\n---------------------------------------Case1: 输入Integer[]参数为null-----------------------------------------------");
		Integer[] ia = null;
		out = GeneralUtil.printArray(ia);
		Assert.assertTrue(out.equals(""));

		System.out.println("\n---------------------------------------Case2: 输入Integer[]参数为三个数组-----------------------------------------------");
		Integer[] ia1 = { 1, 2, 3 };
		out = GeneralUtil.printArray(ia1);
		Assert.assertTrue(out.equals("1 2 3 "));

		System.out.println("\n---------------------------------------Case3: 输入String[]参数为null-----------------------------------------------");
		String[] sa = null;
		out = GeneralUtil.printArray(sa);
		Assert.assertTrue(out.equals(""));

		System.out.println("\n---------------------------------------Case4: 输入String[]参数为两个数组-----------------------------------------------");
		String[] sa1 = { "大哥", "二哥" };
		out = GeneralUtil.printArray(sa1);
		Assert.assertTrue(out.equals("大哥 二哥 "));
	}

	@Test
	public void testGeneralUtil() {
		// 加法
		Assert.assertTrue(GeneralUtil.sum(1.1d, 2.2d) == 3.3d, "计算错误");
		Assert.assertTrue(GeneralUtil.sum(1.000001d, 2.2d) == 3.200001d, "计算错误");

		// 减法
		System.out.println();
		Assert.assertTrue(GeneralUtil.sub(3.6d, 2.2d) == 1.4d, "计算错误");
		Assert.assertTrue(GeneralUtil.sub(3.300001d, 2.2d) == 1.100001d, "计算错误");

		// 乘法
		Assert.assertTrue(GeneralUtil.mul(3.6d, 2.2d) == 7.92d, "计算错误");
		Assert.assertTrue(GeneralUtil.mul(3.30001d, 2.2d) == 7.260022d, "计算错误");

		// 除法
		Assert.assertTrue(GeneralUtil.div(6.3d, 2.1d, 6) == 3d, "计算错误");
		Assert.assertTrue(GeneralUtil.div(3.6666, 2d, 6) == 1.8333d, "计算错误");

		// 多个相加
		System.out.println(GeneralUtil.sumN(6.3d, 2.1d, 1.6d, 2.5d));
		Assert.assertTrue(GeneralUtil.sumN(6.3d, 2.1d, 1.6d, 2.5d) == 12.5d, "计算错误");
	}

	@Test
	public void testRound() {
		Shared.caseLog("case1:测试double精确到一位小数能否五入");
		System.out.println("501.05-->" + GeneralUtil.round(501.05d, 1));
		Assert.assertTrue(GeneralUtil.round(501.05d, 1) == 501.1d, "计算错误");

		Shared.caseLog("case2:测试double精确到两位小数能否五入");
		System.out.println("501.005-->" + GeneralUtil.round(501.005d, 2));
		Assert.assertTrue(GeneralUtil.round(501.005d, 2) == 501.01d, "计算错误");

		Shared.caseLog("case3:测试String精确到两位小数能否四舍");
		System.out.println("501.004-->" + GeneralUtil.round("501.004", 2));
		Assert.assertTrue(GeneralUtil.round("501.004", 2) == 501.00d, "计算错误");

		Shared.caseLog("case4:测试String精确到两位小数能否五入");
		System.out.println("501.005-->" + GeneralUtil.round("501.005", 2));
		Assert.assertTrue(GeneralUtil.round("501.005", 2) == 501.01d, "计算错误");

		Shared.caseLog("case5:测试String精确到六位小数能否五入");
		System.out.println("501.0050005-->" + GeneralUtil.round("501.0050005"));
		Assert.assertTrue(GeneralUtil.round("501.0050005") == 501.005001d, "计算错误");
	}

	@Test
	public void hasDuplicatedElementTest() {
		boolean bIa = false;

		Shared.caseLog("Case1: 输入参数为null");
		Integer[] ia = null;
		bIa = GeneralUtil.hasDuplicatedElement(ia);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case2: 有1个重复元素");
		Integer[] ia2 = { 1, 2, 2, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia2);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case3: 有2个重复元素");
		Integer[] ia3 = { 1, 2, 2, 2, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia3);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case4: 数组里面全部元素重复");
		Integer[] ia4 = { 1, 1, 2, 2, 3, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia4);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case5 数组里面有4个重复");
		Integer[] ia5 = { 1, 1, 2, 2, 2, 2, 3, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia5);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case6 数组全部元素都重复多个");
		Integer[] ia6 = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia6);
		Assert.assertTrue(bIa);

		Shared.caseLog("Case7: 数组里面无重复");
		Integer[] ia7 = { 1, 2, 3 };
		bIa = GeneralUtil.hasDuplicatedElement(ia7);
		Assert.assertTrue(!bIa);
	}
	
	@Test
	public void testGenerateMnemonicCode() {
		String name = "娃哈哈";
		Assert.assertTrue(GeneralUtil.generateMnemonicCode(name, "xx").equals("whh"));

		name = "娃哈哈3";
		Assert.assertTrue(GeneralUtil.generateMnemonicCode(name, "xx").equals("whh3"));

		name = "w哈3哈";
		Assert.assertTrue(GeneralUtil.generateMnemonicCode(name, "xx").equals("wh3h"));

		name = ")($";
		Assert.assertTrue(GeneralUtil.generateMnemonicCode(name, "xx").equals("xx"));
	}
	
	@Test
	public void testToIntDesc() {
		// 原数组为降序
		int[] ids = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		ids = GeneralUtil.toIntDesc(ids);
		for(int i = 0; i < ids.length - 1; i++) {
			Assert.assertTrue(ids[i + 1] >= ids[i], "降序排序方法有错误");
		}
		// 原数组为升序
		ids = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		ids = GeneralUtil.toIntDesc(ids);
		for(int i = 0; i < ids.length - 1; i++) {
			Assert.assertTrue(ids[i + 1] >= ids[i], "降序排序方法有错误");
		}
		// 原数组为乱序
		ids = new int[]{3, 2, 1, 6, 5, 4, 9, 8, 7, 10};
		ids = GeneralUtil.toIntDesc(ids);
		for(int i = 0; i < ids.length - 1; i++) {
			Assert.assertTrue(ids[i + 1] >= ids[i], "降序排序方法有错误");
		}
		// 原数组有多个元素相同
		ids = new int[]{3, 2, 2, 1, 6, 5, 5, 4, 9, 8, 8, 7, 10, 10};
		ids = GeneralUtil.toIntDesc(ids);
		for(int i = 0; i < ids.length - 1; i++) {
			Assert.assertTrue(ids[i + 1] >= ids[i], "降序排序方法有错误");
		}
		// 原数组有奇数个元素
		ids = new int[]{3, 2, 2, 1, 6, 5, 5, 4, 9, 8, 8, 7, 10};
		ids = GeneralUtil.toIntDesc(ids);
		for(int i = 0; i < ids.length - 1; i++) {
			Assert.assertTrue(ids[i + 1] >= ids[i], "降序排序方法有错误");
		}
	}
}
