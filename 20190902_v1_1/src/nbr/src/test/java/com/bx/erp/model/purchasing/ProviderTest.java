package com.bx.erp.model.purchasing;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ProviderTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkUpdate() { // ...命名？？？
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		// 模拟web前端表单中的字段的数据绑定。Action接收到的MODEL中的String型字段不会是null。
		p.setAddress("");
		p.setName("a");
		p.setContactName("");

		String err = "";
		p.setID(-99);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);

		// 供应商地址长度不超过50个字符
		p.setAddress(" #hiowqA12w#@!测试 aDD ressTest123#123#@!abc123abc123abc123abc123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress("abc1vabc123abc123abc123abc123abc123abc123abc123abc12323abc1vabc");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress(" a");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress("666");

		p.setDistrictID(-99);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(err, Provider.FIELD_ERROR_districtID);
		p.setDistrictID(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		p.setDistrictID(1);

		p.setAddress("#hiowqA12w#@!");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("#123#@!");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("abc123abc1abc123abc1abc123abc1abc123abc1abc123abc1");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("aDD ress");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("Test123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商联系人名字必须是中英文或数字，中间可以有空格，但首尾不能有空格且长度不超过20个字符
		p.setContactName("abc1vabc123abc123abc123abc123abc1");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("qew！*（）io#q");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName(" asd123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("asd123 ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);

		p.setContactName("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("Abc 123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("abcd1234ababcd1234ab");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商联系人电话为数字，长度不超过11个字符
		p.setMobile("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("-1123 ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("11231 ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("123 ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("qweqwe");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("！@ ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("2311111111111111111111111");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);

		p.setMobile("");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setMobile("12345678901");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商名字不允许有空格出现且长度不超过32个字符
		p.setName("123456789012345678901234567890aaa");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("ewqoi#123^&*(");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName(" qwejo213");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("qwe321 123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("qwe321123 ");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);

		p.setName("abc123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setName("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setName("abc123abc123abc123qw");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 全过返回""
		p.setAddress("测试");
		p.setContactName("测试");
		p.setMobile("12345678901");
		p.setName("abc123abc123abc123qw");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("#123#@!");
		p.setContactName("abcd1234ababcd1234ab");
		p.setMobile("12345678903");
		p.setName("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 根据需求首尾不能为空
		p.setAddress("aDD ress");
		p.setContactName("Abc 123");
		p.setMobile("12345678923");
		p.setName("abc123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("");
		p.setContactName("");
		p.setMobile("");
		p.setName("测试123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

	}

	@Test
	public void checkCreate() { // ...命名？？？
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		// 模拟web前端表单中的字段的数据绑定。Action接收到的MODEL中的String型字段不会是null。
		p.setAddress("");
		p.setName("a");
		p.setContactName("");

		String err = "";

		p.setDistrictID(0);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_districtID);
		p.setDistrictID(-99);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_districtID);
		p.setDistrictID(1);

		// 供应商地址长度不超过50个字符
		p.setAddress("abc1vabc123abc123abc123abc123abc123abc123abc123abc12323abc1vabc");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress(" a");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);

		p.setAddress("#hiowqA12w#@!");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("测试");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("#123#@!");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("abc123abc1abc123abc1abc123abc1abc123abc1abc123abc1");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("aDD ress");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("Test123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商联系人名字必须是中英文或数字，中间可以有空格，但首尾不能有空格且长度不超过20个字符
		p.setContactName("abc1vabc123abc123abc123abc123abc1");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("qew！*（）io#q");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName(" asd123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("asd123 ");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);

		p.setContactName("测试");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("Abc 123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("abcd1234ababcd1234ab");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//供应商联系人电话不限制输入字符，长度为7到24个字符;
		p.setMobile("测试");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("123123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile(" 31*&");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("12312 ");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("qweqwe");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("！@#￥");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("abc1vabc123abc123abc123abc123abc1");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);

		p.setMobile("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setMobile("12345678901");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商名字不允许有空格出现且长度不超过32个字符
		p.setName("012345678901234567890123456789abc");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("ewqoi#123^&*(");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName(" qwejo213");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("qwe321 123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("qwe321123 ");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);
		p.setName("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_name);

		p.setName("abc123");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setName("测试");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setName("abc123abc123abc123qw");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 全过返回""
		p.setID(1);
		p.setAddress("测试");
		p.setContactName("测试");
		p.setMobile("12345678901");
		p.setName("abc123abc123abc123qw");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("#123#@!");
		p.setContactName("abcd1234ababcd1234ab");
		p.setMobile("12345678902");
		p.setName("测试");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("aDD ress");
		p.setContactName("Abc 123");
		p.setMobile("12345678900");
		p.setName("abc123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("");
		p.setContactName("");
		p.setMobile("");
		p.setName("测试123");
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

	}

	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		// 模拟web前端表单中的字段的数据绑定。Action接收到的MODEL中的String型字段不会是null。
		p.setAddress("");
		p.setName("a");
		p.setContactName("");

		String err = "";
		p.setID(-99);
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_ID);
		p.setID(0);

		// 供应商地址长度不超过50个字符
		p.setAddress(" #hiowqA12w#@!测试 aDD ressTest123#123#@!abc123abc123abc123abc123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress("abc1vabc123abc123abc123abc123abc123abc123abc123abc12323abc1vabc");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress(" a");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_address);
		p.setAddress("666");

		p.setAddress("#hiowqA12w#@!");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("测试");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("#123#@!");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("abc123abc1abc123abc1abc123abc1abc123abc1abc123abc1");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("aDD ress");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("Test123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setAddress("");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商联系人名字必须是中英文或数字，中间可以有空格，但首尾不能有空格且长度不超过20个字符
		p.setContactName("abc1vabc123abc123abc123abc123abc1");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("qew！*（）io#q");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName(" asd123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);
		p.setContactName("asd123 ");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_contactName);

		p.setContactName("测试");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("Abc 123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setContactName("abcd1234ababcd1234ab");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 供应商联系人电话为数字，长度不超过11个字符
		p.setMobile("测试");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("12312");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile(" 31as");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("123 ");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("qweqwe");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("！@#");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);
		p.setMobile("abc1vabc123abc123abc123abc123abc1");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Provider.FIELD_ERROR_mobile);

		p.setMobile("");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setMobile("12345678901");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 全过返回""
		p.setAddress("测试");
		p.setContactName("测试");
		p.setMobile("12345678901");
		p.setName("abc123abc123abc123qw");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("#123#@!");
		p.setContactName("abcd1234ababcd1234ab");
		p.setMobile("12345678903");
		p.setName("测试");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 根据需求首尾不能为空
		p.setAddress("aDD ress");
		p.setContactName("Abc 123");
		p.setMobile("12345678923");
		p.setName("abc123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setAddress("");
		p.setContactName("");
		p.setMobile("");
		p.setName("测试123");
		err = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		String err = "";
		p.setID(-99);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		String err = "";
		p.setID(-99);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkRetrieveNByFields() {
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		String err = "";
		p.setPageIndex(-99);
		err = p.checkRetrieveN(BaseBO.CASE_Provider_RetrieveNByFields);
		assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(0);
		err = p.checkRetrieveN(BaseBO.CASE_Provider_RetrieveNByFields);
		assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(1);

		p.setQueryKeyword("afjkaslhksafdjklfsadjklajfkljaskldfkljf");
		err = p.checkRetrieveN(BaseBO.CASE_Provider_RetrieveNByFields);
		assertEquals(err, Provider.FIELD_ERROR_queryKeyword);
		//
		p.setQueryKeyword("");
		err = p.checkRetrieveN(BaseBO.CASE_Provider_RetrieveNByFields);
		assertEquals(err, "");
		p.setQueryKeyword("11");
		err = p.checkRetrieveN(BaseBO.CASE_Provider_RetrieveNByFields);
		assertEquals(err, "");
	}

	@Test
	public void CheckUniqueField() {
		Shared.printTestMethodStartInfo();

		Provider p = new Provider();
		String err = "";
		p.setFieldToCheckUnique(1);
		p.setUniqueField("131354465456651325134654563511351");
		err = p.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		assertEquals(err, Provider.FIELD_ERROR_name);
		//
		p.setUniqueField("1111");
		err = p.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		assertEquals(err, "");
		
		p.setFieldToCheckUnique(2);
		p.setUniqueField("131354465456651145465161351653511111");
		err = p.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		assertEquals(err, Provider.FIELD_ERROR_mobile);
		//
		p.setUniqueField("1111");
		err = p.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		assertEquals(err, Provider.FIELD_ERROR_mobile);
		
		p.setFieldToCheckUnique(3);
		err = p.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		assertEquals(err, Provider.FIELD_ERROR_checkUniqueField);
	}
	
}
