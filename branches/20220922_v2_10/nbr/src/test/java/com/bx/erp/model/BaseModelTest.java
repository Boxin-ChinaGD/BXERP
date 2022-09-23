package com.bx.erp.model;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.testng.Assert;

import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.test.Shared;

public class BaseModelTest {

	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 修改对象的任一可比较的字段，令对象比较时不要相等 */
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		throw new RuntimeException("Not yet implemented!");
	}

	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 修改对象的任一可比较的字段，令对象比较时不要相等 */
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		throw new RuntimeException("Not yet implemented!");
	}

	public void testCompareTo_Case1() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case1: 俩个对象数据一样 ");
		
		BaseModel master = getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");
	}

	public void testCompareTo_Case2() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case2: 俩个对象数据不一样 ");
		
		BaseModel master = getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = updateMasterTableObject(master);
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");

	}

	public void testCompareTo_Case3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3: 俩个对象的主表信息相同，从表A和B都为null");
		
		BaseModel master = getMasterTableObject();
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");

	}

	public void testCompareTo_Case4() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case4: 俩个对象的主表信息相同，从表A为null，B不为null，B的size为0，返回0。");

		BaseModel master = getMasterTableObject();
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		List<BaseModel> slave = new ArrayList<BaseModel>();
		master2.setListSlave1(slave);
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");

	}

	public void testCompareTo_Case5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5: 俩个对象的主表信息相同，从表A为null，B不为null，B的size大于0，返回-1。");

		BaseModel master = getMasterTableObject();
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		List<BaseModel> slave = getSlaveTableObject();
		master2.setListSlave1(slave);
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");

	}

	public void testCompareTo_Case6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: 俩个对象的主表信息相同，从表A不为null，B为null，A的size为0，返回0。");

		BaseModel master = getMasterTableObject();
		//
		BaseModel master1 = master.clone();
		List<BaseModel> slave = new ArrayList<BaseModel>();
		master1.setListSlave1(slave);
		BaseModel master2 = master.clone();
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");
	}

	public void testCompareTo_Case7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: 俩个对象的主表信息相同，从表A不为null，B为null，A的size大于0，返回-1。");

		BaseModel master = getMasterTableObject();
		//
		BaseModel master1 = master.clone();
		List<BaseModel> slave = getSlaveTableObject();
		master1.setListSlave1(slave);
		BaseModel master2 = master.clone();
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");
	}

	public void testCompareTo_Case8() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case8: 俩个对象的主表信息相同，从表A和B都不为null，A和B的size相等，从表循环compare通过，返回0。");

		BaseModel master = getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");
	}

	public void testCompareTo_Case9() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case9: 俩个对象的主表信息相同，从表A和B都不为null，A和B的size相等，从表循环compare不通过，返回-1。");

		BaseModel master = getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		master2.setListSlave1(updateSlaveTableObject(slave));
		Assert.assertTrue(master1.compareTo(master2) == -1, "比较失败");
	}

	public void testCompareTo_Case10() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case10: 俩个对象的主表信息相同，从表信息不相同 但设置为不比较从表信息");

		BaseModel master = getMasterTableObject();
		List<BaseModel> slave = getSlaveTableObject();
		master.setListSlave1(slave);
		//
		BaseModel master1 = master.clone();
		BaseModel master2 = master.clone();
		master2.setListSlave1(updateSlaveTableObject(slave));
		//
		master1.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(master1.compareTo(master2) == 0, "比较失败");
	}

	private Random random = new Random();

	@Test
	public void testCompareBaseModel() throws NumberFormatException, InterruptedException {
		List<BaseModel> list = new ArrayList<BaseModel>();
		for (int i = 1; i <= 5; i++) {
			BaseModel bmTest = new BaseModel();
			bmTest.setID(random.nextInt(10) + 1);
			list.add(bmTest);
		}

		BaseModel bModel = new BaseModel();
		bModel.setIsASC(EnumBoolean.EB_Yes.getIndex());
		for (int i = 0; i < list.size(); i++) {
			System.out.println("进行升序排序前：" + list.get(i).getID());
		}
		Collections.sort(list, bModel);
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行升序排序后：" + list.get(i).getID());
			assertTrue(list.get(i - 1).getID() <= list.get(i).getID(), "排序异常！！！");
		}
		//
		bModel.setIsASC(EnumBoolean.EB_NO.getIndex());
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行降序排序前：" + list.get(i).getID());
		}
		Collections.sort(list, bModel);
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行降序排序后：" + list.get(i).getID());
			assertTrue(list.get(i - 1).getID() >= list.get(i).getID(), "排序异常！！！");
		}
	}

	@Test
	public void testCompareBrand() {
		List<Brand> list = new ArrayList<Brand>();
		for (int i = 1; i <= 5; i++) {
			Brand bTest = new Brand();
			bTest.setID(random.nextInt(10) + 1);
			list.add(bTest);
		}

		Brand brand = new Brand();
		brand.setIsASC(EnumBoolean.EB_Yes.getIndex());
		for (int i = 0; i < list.size(); i++) {
			System.out.println("进行升序排序前：" + list.get(i).getID());
		}
		Collections.sort(list, brand);
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行升序排序后：" + list.get(i).getID());
			assertTrue(list.get(i - 1).getID() <= list.get(i).getID(), "排序异常！！！");
		}
		//
		brand.setIsASC(EnumBoolean.EB_NO.getIndex());
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行降序排序前：" + list.get(i).getID());
		}
		Collections.sort(list, brand);
		for (int i = 1; i < list.size(); i++) {
			System.out.println("进行降序排序后：" + list.get(i).getID());
			assertTrue(list.get(i - 1).getID() >= list.get(i).getID(), "排序异常！！！");
		}
	}
}
