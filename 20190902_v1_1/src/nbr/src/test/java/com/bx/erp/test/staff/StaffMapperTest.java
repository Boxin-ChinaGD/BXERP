package com.bx.erp.test.staff;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.MD5Util;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;

public class StaffMapperTest extends BaseMapperTest {

	/** 检查staff手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFFPHONE = 1;

	/** 检查staff身份证号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFFICID = 2;

	/** 检查staff微信唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFFWECHAT = 3;

	/** 查找的对象包含离职的 */
	public static final int RETRIEVE1_INVOLVEDRESIGNED = 1;

	public static final int RETRIEVN_INVOLVED_RESIGNED = 2;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1 首次添加该用户 ");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2 重复添加该用户（业务上不允许的） 已有的身份证进行注册 ");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		// 首次添加
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 重复添加
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setICID(staffCreate.getICID());
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:WeChat重复");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setWeChat(staffCreate.getWeChat());
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:Phone重复");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setPhone(staffCreate.getPhone());
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:name重复");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setName(staffCreate.getName());
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6 正常添加,RoleID为4的时候，该角色是公司老板，需要设置对应的shopID为1，DepartmentID为1");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setShopID(1);
		staff.setDepartmentID(1);
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);

	}

	@Test
	public void createTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7 传入的DepartmentID不存在");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setDepartmentID(Shared.BIG_ID);
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);
	}

	// @Test //目前POS机上不能创建staff
	public void createTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8 IDInPOS = -1");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		// staff.setIDInPOS(1);
		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_OtherError, staffMapper);
	}

	@Test
	public void createTest_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9 传入正确的RoleID");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10 传入错误的RoleID");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(Shared.BIG_ID);
		String err = staff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
	}

	@Test
	public void createTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: 使用状态为离职（1）的员工手机、微信、身份证创建在职员工");
		// 创建离职的员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(1);
		Staff staffQuit = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 使用离职的员工的手机号，微信号，身份证创建新的员工
		Staff staffCreate = BaseStaffTest.DataInput.getStaff();
		staffCreate.setPhone(staffQuit.getPhone());
		staffCreate.setWeChat(staffQuit.getWeChat());
		staffCreate.setICID(staffQuit.getICID());
		Staff staffCreate1 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staffCreate, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate1, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void createTest_CASE12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: 传入的shopID不存在");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(Shared.BIG_ID);

		BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:查找在职");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setInvolvedResigned(EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		BaseStaffTest.retrieve1Staff(staffCreate, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:传递int1为1查找的对象不管是离职还是在职");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setInvolvedResigned(RETRIEVE1_INVOLVEDRESIGNED);
		//
		BaseStaffTest.retrieve1Staff(staffCreate, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:通过ID查找所有门店人员");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setID(staffCreate.getID());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:通过手机号查找所有门店人员");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setPhone(staffCreate.getPhone());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:通过ID和手机号查找所有门店人员");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setID(staffCreate.getID());
		staff2.setPhone(staffCreate.getPhone());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:通过ID和状态码查找所有门店人员");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setID(staffCreate.getID());
		staff2.setStatus(staffCreate.getStatus());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:通过手机号和状态码查找");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setPhone(staffCreate.getPhone());
		staff2.setStatus(staffCreate.getStatus());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:通过ID,手机号和状态码查找");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = new Staff();
		staff2.setID(staffCreate.getID());
		staff2.setPhone(staffCreate.getPhone());
		staff2.setStatus(staffCreate.getStatus());
		//
		BaseStaffTest.retrieve1Staff(staff2, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:没有条件，全部使用默认参数");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		BaseStaffTest.retrieve1Staff(staffCreate, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:传入一个不存在的ID进行查询 ");

		Staff staff = new Staff();
		staff.setID(9999999);
		//
		BaseStaffTest.retrieve1Staff(staff, staffMapper);
	}

	@Test
	public void retrieve1Test_CASE11() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:传入一个不存在的电话号码进行查询");

		Staff staff = new Staff();

		staff.setPhone("3333333333333333");
		//
		BaseStaffTest.retrieve1Staff(staff, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:iStatus为0的时候查到所有在职的员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		List<BaseModel> list = BaseStaffTest.retrieveNStaff(staffCreate, staffMapper);
		for (BaseModel bm : list) {
			String name = ((Staff) bm).getName();
			if (name.contains(staffCreate.getName())) {
				Assert.assertTrue(true);
			}
		}

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:iStatus传0的时候用name值查询，可以查到在职的相应员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setQueryKeyword(staffCreate.getName());
		//
		List<BaseModel> list = BaseStaffTest.retrieveNStaff(staffCreate, staffMapper);
		for (BaseModel bm : list) {
			String name = ((Staff) bm).getName();
			if (name.contains(staffCreate.getName())) {
				Assert.assertTrue(true);
			}
		}

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE:iStatus传-1的时候用name值查询,可以查到相应员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setStatus(BaseAction.INVALID_STATUS);
		staffCreate.setQueryKeyword(staffCreate.getName());
		//
		List<BaseModel> list = BaseStaffTest.retrieveNStaff(staffCreate, staffMapper);
		for (BaseModel bm : list) {
			String name = ((Staff) bm).getName();
			if (name.contains(staffCreate.getName())) {
				Assert.assertTrue(true);
			}
		}

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:iStatus传-1的时候可以查到所有的员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setStatus(BaseAction.INVALID_STATUS);
		//
		List<BaseModel> list = BaseStaffTest.retrieveNStaff(staffCreate, staffMapper);
		for (BaseModel bm : list) {
			String name = ((Staff) bm).getName();
			if (name.contains(staffCreate.getName())) {
				Assert.assertTrue(true);
			}
		}

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:iStatus传递1的时候只可以查询到离职的员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		BaseStaffTest.retrieveNStaff(staff, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:iStatus传1的时候用name值查询，可以查到所有的离职员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		staff.setQueryKeyword("店员");
		BaseStaffTest.retrieveNStaff(staff, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE7() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:用不存在的名称查询");

		Staff staff = new Staff();
		staff.setQueryKeyword("神秘的店员XXX");
		BaseStaffTest.retrieveNStaff(staff, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8：iStatus传0的时候用phone值查询，可以查到所有的相应员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);

		Staff staff1 = new Staff();
		staff1.setPhone(staffCreate.getPhone());
		staff1.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		BaseStaffTest.retrieveNStaff(staff1, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE9() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:iStatus传0的时候用不存在在Phone进行查询");

		Staff staff = new Staff();
		staff.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff.setQueryKeyword("88888888");
		BaseStaffTest.retrieveNStaff(staff, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10：iStatus传0的时候用6位数的phone值查询，可以查到所有的相应员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);

		Staff staff1 = new Staff();
		staff1.setPhone(staffCreate.getPhone().substring(0, 6));
		staff1.setStatus(Staff.EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		BaseStaffTest.retrieveNStaff(staff1, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);

	}

	@Test
	public void retrieveNTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11：查看默认返回是否降序");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		//
		List<BaseModel> bmList = BaseStaffTest.retrieveNStaff(staff, staffMapper);
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void retrieveNTest_CASE12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12：iStatus传-1，iOperator为0的时候，可以查到除售前账号外的所有员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(BaseAction.INVALID_STATUS);
		staff.setOperator(EnumBoolean.EB_NO.getIndex());
		staff.setPageSize(Shared.PAGE_SIZE_MAX);
		//
		List<BaseModel> bmList = BaseStaffTest.retrieveNStaff(staff, staffMapper);
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
	}

	@Test
	public void retrieveNTest_CASE13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13：售前为未删除时，iStatus传0，iOperator为0的时候，可以查到除售前账号外的所有未删除的员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff.setOperator(EnumBoolean.EB_NO.getIndex());
		staff.setPageSize(Shared.PAGE_SIZE_MAX);
		//
		List<BaseModel> bmList = BaseStaffTest.retrieveNStaff(staff, staffMapper);
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
	}

	@Test
	public void retrieveNTest_CASE14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14：售前为删除时，iStatus传1，iOperator为0的时候，可以查到除售前外账号的所有删除的员工");
		// 删除售前
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaff(staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		staff2.setOperator(EnumBoolean.EB_NO.getIndex());
		staff2.setPageSize(Shared.PAGE_SIZE_MAX);
		List<BaseModel> bmList = BaseStaffTest.retrieveNStaff(staff2, staffMapper);
		//
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
		// 恢复售前
		Staff staff3 = new Staff();
		staff3.setID(Shared.PreSaleID);
		staff3.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		Staff staffR1 = BaseStaffTest.retrieve1Staff(staff3, staffMapper);
		staffR1.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffR1.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		BaseStaffTest.updateStaff(BaseBO.INVALID_CASE_ID, staffR1, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15：售前为未删除时，iStatus传0，iOperator为1的时候，可以查到所有未删除的员工");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff.setOperator(EnumBoolean.EB_Yes.getIndex());
		staff.setPageSize(Shared.PAGE_SIZE_MAX);
		//
		List<BaseModel> bmList = BaseStaffTest.retrieveNStaff(staff, staffMapper);
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		boolean existPreSale = false;
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN Staff错误，应该查询出售前账号。");
	}

	// ------------------- 查询多个门店用户end --------------------

	@Test
	public void updateTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:正常修改");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staffUpdate = BaseStaffTest.updateStaff(BaseBO.INVALID_CASE_ID, staffCreate, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffUpdate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:主外键关系出错(门店ID)");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffCreate.setShopID(Shared.BIG_ID);
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:主外键关系出错(部门ID)");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffCreate.setDepartmentID(Shared.BIG_ID);
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:主外键关系出错(角色ID)");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setRoleID(BaseAction.INVALID_ID);
		String err = staff.checkUpdate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertEquals(err, "ID必须>0");
		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:修改为已有的信息（名字）");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setName(staffCreate.getName());
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staffUpdate = BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffUpdate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:修改为已有的信息（微信）");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate2 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffCreate.setWeChat(staffCreate2.getWeChat());// 把staffCreate2的微信修改成staffCreate的微信
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:修改为已有的信息（手机号）");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		String Phone = staffCreate.getPhone();
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate2 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setPhone(staffCreate2.getPhone());// 把staffCreate2的手机号修改成staffCreate的手机号
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		staffCreate.setPhone(Phone);
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate2, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:修改为已有的信息（ICID）");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate2 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff2, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setICID(staffCreate2.getICID());// 把staffCreate2的ICID修改成staffCreate的ICID
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:修改部分信息");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setID(staffCreate.getID());
		staff2.setICID(Shared.getValidICID());
		// staff2.setA1("T1e1s1t1");
		staff2.setRoleID(staff.getRoleID());
		Staff staffUpdate = BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staff2, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffUpdate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:修改员工状态为离职，错误码7");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staffCreate, EnumErrorCode.EC_BusinessLogicNotDefined, staffMapper);
		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建一条员工数据
		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 把员工改为离职
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);

		Shared.caseLog("CASE11:修改员工状态，离职转在职");
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setID(staffCreate.getID());
		staff2.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staff2, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateTest_CASE12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12:新增员工时，可以填写离职员工的手机号、微信、身份证，新增成功后，再修改这个员工的信息时，也能修改成功 ");
		// 创建离职员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());// 离职
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 创建一个在职的staff，使用上面创建的离职staff的手机号、微信、身份证
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setPhone(staffCreate.getPhone());
		staff2.setWeChat(staffCreate.getWeChat());
		staff2.setICID(staffCreate.getICID());
		Staff staffCreate2 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 修改在职员工信息
		Staff staff3 = BaseStaffTest.DataInput.getStaff();
		staff3.setID(staffCreate2.getID());
		staff3.setName("店员" + Shared.generateCompanyName(6));
		staff3.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staffUpdate = BaseStaffTest.updateStaff(BaseBO.CASE_SpecialResultVerification, staff3, EnumErrorCode.EC_NoError, staffMapper);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffUpdate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void deleteTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常删除");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void deleteTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:staff为售前账号，正常删除");
		// 创建员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		staffCreate.setRoleID(1);
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void deleteTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:staff为售前账号，有依赖。正常删除");
		// 创建员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 创建促销，绑定创建的员工
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setStaff(staffCreate.getID());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		//
		staffCreate.setRoleID(1);
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void resetPasswordTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: 正常重新设置密码");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		staffCreate.setSalt(MD5Util.MD5(String.valueOf(System.currentTimeMillis()).substring(6) + BaseAction.SHADOW));
		// CASE_ResetMyPassword 和
		// CASE_ResetOtherPassword的getUpdateParam是一样的，不同的只有checkUpdate的时候不一样，所以这里两个都可以
		String err = staffCreate.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> updateParam = staff.getUpdateParam(BaseBO.CASE_ResetOtherPassword, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff resetPasswordStaff = (Staff) staffMapper.resetPassword(updateParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(resetPasswordStaff != null && resetPasswordStaff.getRoleID() == EnumTypeRole.ETR_Cashier.getIndex());
		//
		err = resetPasswordStaff.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertEquals(err, "");
		//
		if (resetPasswordStaff.compareTo(staffCreate) != 0) {
			Assert.assertTrue(false, "修改的对象字段和DB读出的不相等");
		}
		err = staffCreate.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void resetPasswordTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2:Salt为空 ");
		//
		Staff staff1 = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate1 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff1, EnumErrorCode.EC_NoError, staffMapper);
		//
		staffCreate1.setSalt("");
		staffCreate1.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		String err = staffCreate1.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> updateParam1 = staffCreate1.getUpdateParam(BaseBO.CASE_ResetOtherPassword, staffCreate1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff updateStaff = staffMapper.resetPassword(updateParam1);
		//
		Assert.assertTrue(updateStaff != null && EnumErrorCode.values()[Integer.parseInt(updateParam1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParam1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = updateStaff.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertEquals(err, "");

		if (updateStaff.compareTo(staffCreate1) != 0) {
			Assert.assertTrue(false, "修改的对象的字段与DB读出的不相等");
		}
		err = staffCreate1.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		updateStaff.setRoleID(1);
		BaseStaffTest.deleteStaff(updateStaff, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void resetPasswordTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: 已离职员工不能修改密码");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
		//
		staffCreate.setSalt(MD5Util.MD5(String.valueOf(System.currentTimeMillis()).substring(6) + BaseAction.SHADOW));
		staffCreate.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		String err = staffCreate.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> updateParam = staff.getUpdateParam(BaseBO.CASE_ResetOtherPassword, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff resetPasswordStaff = (Staff) staffMapper.resetPassword(updateParam);
		//
		Assert.assertTrue(resetPasswordStaff == null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void resetPasswordTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4: 创建一个和离职员工相同手机的员工，修改该员工密码");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
		//
		Staff staffCreate2 = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate2.setSalt(MD5Util.MD5(String.valueOf(System.currentTimeMillis()).substring(6) + BaseAction.SHADOW));
		staffCreate2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		String err = staffCreate2.checkUpdate(BaseBO.CASE_ResetOtherPassword);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> updateParam = staff.getUpdateParam(BaseBO.CASE_ResetOtherPassword, staffCreate2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff resetPasswordStaff = (Staff) staffMapper.resetPassword(updateParam);
		//
		Assert.assertTrue(resetPasswordStaff != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		BaseStaffTest.deleteStaff(staffCreate2, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void resetPasswordTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5: 重置自己的密码");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		staffCreate.setSalt(MD5Util.MD5(String.valueOf(System.currentTimeMillis()).substring(6) + BaseAction.SHADOW));
		staffCreate.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex());
		String err = staffCreate.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> updateParam = staff.getUpdateParam(BaseBO.CASE_ResetMyPassword, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff resetPasswordStaff = (Staff) staffMapper.resetPassword(updateParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(resetPasswordStaff != null && resetPasswordStaff.getRoleID() == EnumTypeRole.ETR_Cashier.getIndex());
		//
		err = resetPasswordStaff.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertEquals(err, "");
		//
		if (resetPasswordStaff.compareTo(staffCreate) != 0) {
			Assert.assertTrue(false, "修改的对象字段和DB读出的不相等");
		}
		err = staffCreate.checkUpdate(BaseBO.CASE_ResetMyPassword);
		Assert.assertEquals(err, "");
		//
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateOpenidANDUnionidTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常更新");
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setOpenid("123sdfasrqxzf6t31fsdr12es" + Shared.generateStringByTime(8));
		staffCreate.setUnionid("1246wqedasf657qwer");
		//
		Map<String, Object> updateParams1 = staffCreate.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU = staffMapper.updateOpenidAndUnionid(updateParams1);
		//
		Assert.assertTrue(staffU != null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新成功：" + staffU);

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateOpenidANDUnionidTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:phone不存在");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		String Phone = staff.getPhone();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setOpenid("123sdfasrqxzf6t31fsdr12esghggfj68");
		staffCreate.setUnionid("1246wqedasf657qwer");
		staffCreate.setPhone("12345678910");
		//
		Map<String, Object> updateParams1 = staffCreate.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU = staffMapper.updateOpenidAndUnionid(updateParams1);
		//
		Assert.assertTrue(staffU == null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新失败：" + updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg));

		// 把测试数据改为离职，以免随机数重复
		staffCreate.setPhone(Phone);
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateOpenidANDUnionidTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:phone为空");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setOpenid("123sdfasrqxzf6t31fsdr12esghggfj68");
		staffCreate.setUnionid("1246wqedasf657qwer");
		staffCreate.setPhone("");
		//
		Map<String, Object> updateParams1 = staffCreate.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU = staffMapper.updateOpenidAndUnionid(updateParams1);
		//
		Assert.assertTrue(staffU == null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新失败：" + updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg));

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void updateOpenidANDUnionidTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:openid为空");

		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		staffCreate.setOpenid("");
		staffCreate.setUnionid("1246wqedasf657qwer");
		//
		Map<String, Object> updateParams1 = staffCreate.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU = staffMapper.updateOpenidAndUnionid(updateParams1);
		//
		Assert.assertTrue(staffU == null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新失败：" + updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg));

		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreate, EnumErrorCode.EC_NoError, staffMapper);
	}
	
	@Test
	public void updateOpenidANDUnionidTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:openid已存在");
		//创建staff
		Staff staffA = BaseStaffTest.DataInput.getStaff();
		Staff staffCreateA = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staffA, EnumErrorCode.EC_NoError, staffMapper);
		
		//修改staff的openID
		Staff staffUpdateA = (Staff) staffCreateA.clone();
		staffUpdateA.setOpenid("123456777776" + Shared.generateStringByTime(6));
		Map<String, Object> updateParamA = staffUpdateA.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffUpdateA);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU_A = staffMapper.updateOpenidAndUnionid(updateParamA);
		//
		Assert.assertTrue(staffU_A != null && EnumErrorCode.values()[Integer.parseInt(updateParamA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParamA.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if(staffUpdateA.compareTo(staffU_A) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		
		//创建StaffB
		Staff staffB = BaseStaffTest.DataInput.getStaff();
		Staff staffCreateB = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staffB, EnumErrorCode.EC_NoError, staffMapper);
		
		//修改staffB的openID为staffA的openID
		Staff staffUpdateB = (Staff) staffCreateB.clone();
		staffUpdateB.setOpenid(staffU_A.getOpenid());
		Map<String, Object> updateParamB = staffUpdateB.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffUpdateB);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffU_B = staffMapper.updateOpenidAndUnionid(updateParamB);
		//
		Assert.assertTrue(staffU_B == null && EnumErrorCode.values()[Integer.parseInt(updateParamB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateParamB.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		
		//删除测试产生的数据
		BaseStaffTest.deleteStaff(staffCreateA, EnumErrorCode.EC_NoError, staffMapper);
		BaseStaffTest.deleteStaff(staffCreateB, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void checkUniqueFieldTest_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询一个不存在的员工的手机号码");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFPHONE);
		staff.setUniqueField("12345678910");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 查询一个已存在的员工的手机号码");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFPHONE);
		staff.setUniqueField("13144496272");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 查询一个已存在的离职员工的手机号码");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFPHONE);
		staff.setUniqueField("13196721886");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params3 = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 查询一个不存在的员工的身份证号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField("540883198412111666");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE5() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5: 查询一个已存在的员工的身份证号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField("440883198412111666");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE6() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6: 查询一个已存在的离职员工的身份证号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField("341522198412111666");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE7() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7: 查询一个不存在的员工的微信号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("fffff2f");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE8() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8: 查询一个已存在的员工的微信号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("a326dsd");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE9() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: 查询一个已存在的离职员工的微信号");

		Staff staff = new Staff();
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("d2sasb4");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE10() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: 查询一个已存在的员工的手机号码,但传入的ID与已存在的员工的手机号码对应的员工ID相同");

		Staff staff = new Staff();
		staff.setID(2);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFPHONE);
		staff.setUniqueField("13144496272");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE11() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: 查询一个已存在的员工的身份证号,但传入的ID与已存在的员工的身份证号对应的员工ID相同");

		Staff staff = new Staff();
		staff.setID(2);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField("440883198412111666");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE12() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: 查询一个已存在的员工的微信号,但传入的ID与已存在的员工的微信号对应的员工ID相同");

		Staff staff = new Staff();
		staff.setID(2);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("a326dsd");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE13() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: 查询一个已存在的员工的微信号,传入的ID与已存在的员工的微信号对应的员工ID不相同");

		Staff staff = new Staff();
		staff.setID(3);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("a326dsd");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE14() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: 唯一性检查时，员工的身份证号为空串");

		Staff staff = new Staff();
		staff.setID(3);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField("");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE15() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: 唯一性检查时，员工的身份证号为null");

		Staff staff = new Staff();
		staff.setID(3);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFICID);
		staff.setUniqueField(null);
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE16() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: 唯一性检查时，员工的微信号为空串");

		Staff staff = new Staff();
		staff.setID(3);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField("");
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE17() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: 唯一性检查时，员工的微信号为null");

		Staff staff = new Staff();
		staff.setID(14);
		staff.setFieldToCheckUnique(CASE_CHECK_UNIQUE_STAFFWECHAT);
		staff.setUniqueField(null);
		String err = staff.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.checkUniqueField(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	@Test
	public void updateUnsubscribe_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常更新");
		//
		Staff staffGet = BaseStaffTest.DataInput.getStaff();
		Staff staffCreated = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staffGet, EnumErrorCode.EC_NoError, staffMapper);
		staffCreated.setOpenid("123sdfasrqxzf6t31fsdr12es" + Shared.generateStringByTime(8));
		//
		Map<String, Object> updateParams1 = staffCreated.getUpdateParam(BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffUpdated1 = staffMapper.updateOpenidAndUnionid(updateParams1);
		//
		Assert.assertTrue(staffUpdated1 != null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新成功：" + staffUpdated1);
		Assert.assertTrue(staffUpdated1.getOpenid() != null, "用户的openID不应该为null");
		// 将openID设为null
		Map<String, Object> updateParams2 = staffCreated.getUpdateParam(BaseBO.CASE_Staff_Update_Unsubscribe, staffCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffUpdated2 = staffMapper.updateUnsubscribe(updateParams2);
		//
		Assert.assertTrue(staffUpdated2 != null && EnumErrorCode.values()[Integer.parseInt(updateParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("测试成功，更新成功：" + staffUpdated2);
		Assert.assertTrue(staffUpdated2.getOpenid() == null, "用户的openID应该为null");
		// 把测试数据改为离职，以免随机数重复
		BaseStaffTest.deleteStaff(staffCreated, EnumErrorCode.EC_NoError, staffMapper);
	}
	
	@Test
	public void updateUnsubscribe_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:传入不存在的staffID");
		//
		Staff staffGet = BaseStaffTest.DataInput.getStaff();
		staffGet.setID(BaseAction.INVALID_ID);
		// 将openID设为null
		Map<String, Object> updateParams2 = staffGet.getUpdateParam(BaseBO.CASE_Staff_Update_Unsubscribe, staffGet);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffUpdated2 = staffMapper.updateUnsubscribe(updateParams2);
		//
		Assert.assertTrue(staffUpdated2 == null && EnumErrorCode.values()[Integer.parseInt(updateParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				updateParams2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
}
