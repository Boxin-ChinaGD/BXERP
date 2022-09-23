package com.bx.erp.util;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;

public class listSortTest {

	@Test
	public void test() {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		BaseModel bm1 = new BaseModel();
		bm1.setID(3);
		bmList.add(bm1);

		BaseModel bm2 = new BaseModel();
		bm2.setID(1);
		bmList.add(bm2);

		BaseModel bm3 = new BaseModel();
		bm3.setID(2);
		bmList.add(bm3);

		BaseModel tmp = new BaseModel();
		tmp.setIsASC(EnumBoolean.EB_NO.getIndex());
		Collections.sort(bmList, tmp);
		assertTrue(bmList.get(0).getID() == 3 && bmList.get(1).getID() == 2 && bmList.get(2).getID() == 1);
	}
}
