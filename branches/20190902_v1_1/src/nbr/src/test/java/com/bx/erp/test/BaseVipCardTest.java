package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseVipCardTest extends BaseMapperTest {

	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static VipCard vipCardInput = null;

		public static final VipCard getVipCard() throws CloneNotSupportedException {
			vipCardInput = new VipCard();
			vipCardInput.setTitle("会员卡");
			vipCardInput.setBackgroundColor("255,255,255;255,255,255");
			vipCardInput.setClearBonusDay(3650);
			vipCardInput.setClearBonusDatetime(new Date());

			return (VipCard) vipCardInput.clone();
		}

	}

	public static VipCard createViaMapper(VipCard vipCard) {
		Map<String, Object> params = vipCard.getCreateParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCard vipCardCreate = (VipCard) vipCardMapper.create(params);
		Assert.assertTrue(vipCardCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败。param=" + params + "\n\r vipCard=" + vipCard);
		//
		vipCard.setIgnoreIDInComparision(true);
		if (vipCard.compareTo(vipCardCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = vipCardCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return vipCardCreate;
	}

	public static VipCard updateViaMapper(VipCard vipCard) {
		Map<String, Object> params = vipCard.getUpdateParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCard vipCardUpdate = (VipCard) vipCardMapper.update(params);
		Assert.assertTrue(vipCardUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败。param=" + params + "\n\r vipCard=" + vipCard);
		//
		vipCard.setIgnoreIDInComparision(true);
		if (vipCard.compareTo(vipCardUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = vipCardUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return vipCardUpdate;
	}

	public static void deleteViaMapper(VipCard vipCard) {
		Map<String, Object> paramsDelete = vipCard.getDeleteParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCardMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel vipCardRetrieve1 = retrieve1ViaMapper(vipCard);
		Assert.assertTrue(vipCardRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(VipCard vipCard) {
		Map<String, Object> params = vipCard.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel vipCardRetrieve1 = vipCardMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (vipCardRetrieve1 != null) {
			String err = vipCardRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return vipCardRetrieve1;
	}

	public static void updateViaAction(MockMvc mvc, HttpSession session, VipCard vipCard, EnumErrorCode errorCode) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/vipCard/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(VipCard.field.getFIELD_NAME_ID(), String.valueOf(vipCard.getID())) //
						.param(VipCard.field.getFIELD_NAME_title(), vipCard.getTitle()) //
						.param(VipCard.field.getFIELD_NAME_backgroundColor(), vipCard.getBackgroundColor()) //
						.param(VipCard.field.getFIELD_NAME_clearBonusDay(), String.valueOf(vipCard.getClearBonusDay())) //
						.param(VipCard.field.getFIELD_NAME_clearBonusDatetime(), vipCard.getClearBonusDatetime() == null ? null : simpleDateFormat.format(vipCard.getClearBonusDatetime())) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, errorCode);

	}
}
