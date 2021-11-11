package com.bx.erp.util;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

public class ChineseToENTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void ChineseToPY() {
		Shared.printTestMethodStartInfo();

		ChineseToEN cte = new ChineseToEN();

		System.out.println("食品：" + cte.getAllFirstLetter("食品"));
		System.out.println("可口可乐：" + cte.getAllFirstLetter("可口可乐"));
		System.out.println("可比克薯片：" + cte.getAllFirstLetter("可比克薯片"));
		System.out.println("维他柠檬茶：" + cte.getAllFirstLetter("维他柠檬茶"));
		System.out.println("安慕希：" + cte.getAllFirstLetter("安慕希"));
		System.out.println("润泽玻尿酸面膜：" + cte.getAllFirstLetter("润泽玻尿酸面膜"));
		System.out.println("长生：" + cte.getAllFirstLetter("长生"));
		System.out.println("生长：" + cte.getAllFirstLetter("生长"));
		System.out.println("q2qs1111：" + cte.getAllFirstLetter("q2qs1111"));
		assertTrue(cte.getAllFirstLetter("食品").equals("sp") && cte.getAllFirstLetter("可口可乐").equals("kkkl"));
		//
		assertTrue(cte.getAllFirstLetter("婷()*&^%%$$##!@").equals("t()*&^%%$$##!@"));
		System.out.println("婷：" + cte.getAllFirstLetter("婷()*&^%%$$##!@"));
		assertTrue(cte.getAllFirstLetter("婷").equals("t"));
		// 生僻字
		System.out.println("生僻字:" + cte.getAllFirstLetter("茕茕孑立a沆瀣一气a踽踽独行d醍醐灌顶d绵绵瓜瓞f奉为圭臬f龙行龘龘e犄角旮旯w娉婷袅娜r涕泗滂沱3呶呶不休?不稂不莠"));
		
		assertTrue(cte.getAllFirstLetter("茕茕孑立a沆瀣一气a踽踽独行d醍醐灌顶d绵绵瓜瓞f奉为圭臬f龙行龘龘e犄角旮旯w娉婷袅娜r涕泗滂沱3呶呶不休?不稂不莠").equals("qqjlahxyqajjdxdthgddmmgdffwgnflxddejjglwptnnrtspt3nnbx?blby"));
		// 繁体字
		System.out.println("繁体字:" + cte.getAllFirstLetter("茕茕孑立 沆瀣一氣踽踽獨行 醍醐灌頂綿綿瓜瓞 奉爲圭臬龍行龘龘 犄角旮旯娉婷袅娜 涕泗滂沱呶呶不休 不稂不莠"));
		assertTrue(cte.getAllFirstLetter("茕茕孑立 沆瀣一氣踽踽獨行 醍醐灌頂綿綿瓜瓞 奉爲圭臬龍行龘龘 犄角旮旯娉婷袅娜 涕泗滂沱呶呶不休 不稂不莠").equals("qqjl hxyqjjdx thgdmmgd fwgnlxdd jjglptnn tsptnnbx blby"));
	}
}
