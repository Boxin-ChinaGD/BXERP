package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.checkPoint.SmallSheetCP;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class BaseSmallSheetFrameTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setup();
	}
	
	@AfterClass
	public void tearDown() {
		super.tearDown();
	}
	
	
	public static class DataInput {
		private static SmallSheetFrame ssfInput = new SmallSheetFrame();

		public static final SmallSheetFrame getSmallSheetFrame() throws CloneNotSupportedException, InterruptedException {
			ssfInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			ssfInput.setCountOfBlankLineAtBottom(0);
			ssfInput.setCreateDatetime(new Date());
			Thread.sleep(1000);
			ssfInput.setDelimiterToRepeat("-");
			return (SmallSheetFrame) ssfInput.clone();
		}
	}
	
	public static SmallSheetFrame createViaMapper(SmallSheetFrame smallSheetFrame, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		String error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> params = smallSheetFrame.getCreateParam(BaseBO.INVALID_CASE_ID, smallSheetFrame);
		//
		DataSourceContextHolder.setDbName(dbName);
		SmallSheetFrame smallSheetFrameCreated = (SmallSheetFrame) smallSheetFrameMapper.create(params);
		Assert.assertTrue(smallSheetFrameCreated != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		smallSheetFrame.setIgnoreIDInComparision(true);
		if (smallSheetFrame.compareTo(smallSheetFrameCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		return smallSheetFrameCreated;
	}
	
	public static void deleteViaMapper(SmallSheetFrame smallSheetFrame, String dbName) {
		String error = smallSheetFrame.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), error);
		Map<String, Object> deleteParams = smallSheetFrame.getDeleteParam(BaseBO.INVALID_CASE_ID, smallSheetFrame);
		DataSourceContextHolder.setDbName(dbName);
		smallSheetFrameMapper.delete(deleteParams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	public static List<BaseModel> retrieveNViaMapper(SmallSheetFrame ssf, String dbName) {
		String error = ssf.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), error);
		Map<String, Object> params = ssf.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ssf);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> ssfList = smallSheetFrameMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
		return ssfList;
	}
	
	public static SmallSheetFrame retrieve1ViaMapper(SmallSheetFrame s, String dbName) {
		String error = s.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), error);
		// 查询
		Map<String, Object>params = s.getRetrieve1Param(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(dbName);
		SmallSheetFrame ssfRetrieve1 = (SmallSheetFrame) smallSheetFrameMapper.retrieve1(params);

		Assert.assertTrue(ssfRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
		return ssfRetrieve1;
	}
	
	public static SmallSheetFrame updateViaMapper(SmallSheetFrame s, String dbName) {
		String error = s.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), error);
		Map<String, Object> updateParam = s.getUpdateParam(BaseBO.INVALID_CASE_ID, s);

		DataSourceContextHolder.setDbName(dbName);
		SmallSheetFrame updateSSF = (SmallSheetFrame) smallSheetFrameMapper.update(updateParam);
		s.setIgnoreIDInComparision(true);
		if (s.compareTo(updateSSF) != 0) {
			Assert.assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		return updateSSF;
	}
	
	public static SmallSheetFrame createViaAction(MockMvc mvc, HttpSession sessionBoss, SmallSheetFrame ssf, String defaultTextList, String dbName, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/smallSheetFrame/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(SmallSheetFrame.field.getFIELD_NAME_logo(), ssf.getLogo()) //
						.param(SmallSheetFrame.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat()) //
						.param(SmallSheetFrame.field.getFIELD_NAME_createDatetime(), new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3).format(ssf.getCreateDatetime())) //
						.param(SmallSheetFrame.field.getFIELD_NAME_textList(), defaultTextList) //
						.param(SmallSheetFrame.field.getFIELD_NAME_countOfBlankLineAtBottom(), String.valueOf(ssf.getCountOfBlankLineAtBottom())) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		if(eec != EnumErrorCode.EC_NoError) {
			Shared.checkJSONErrorCode(mr, eec);
			return null;
		}
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		SmallSheetCP.verifyCreate(mr, ssf, smallSheetTextMapper, dbName);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		SmallSheetFrame ssfCreated = (SmallSheetFrame) new SmallSheetFrame().parse1(o.getString(BaseAction.KEY_Object));
		return ssfCreated;
	}
	
	public static void createViaAction(MockMvc mvc, HttpSession sessionBoss, SmallSheetFrame ssf, String defaultTextList, String dbName, EnumErrorCode eec, String errorMsg) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/smallSheetFrame/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(SmallSheetFrame.field.getFIELD_NAME_logo(), ssf.getLogo()) //
						.param(SmallSheetFrame.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat()) //
						.param(SmallSheetFrame.field.getFIELD_NAME_createDatetime(), new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3).format(ssf.getCreateDatetime())) //
						.param(SmallSheetFrame.field.getFIELD_NAME_textList(), defaultTextList) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONMsg(mr, errorMsg, "错误信息错误！");
	}
	
	public static void deleteViaAction(MockMvc mvc, HttpSession sessionBoss, SmallSheetFrame ssf, String dbName, EnumErrorCode eec) throws Exception {
		MvcResult mr1 = mvc.perform(get("/smallSheetFrame/deleteEx.bx?" + SmallSheetFrame.field.getFIELD_NAME_ID() + "=" + ssf.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		if(eec != EnumErrorCode.EC_NoError) {
			Shared.checkJSONErrorCode(mr1, eec);
			return;
		}
		Shared.checkJSONErrorCode(mr1);
		SmallSheetCP.verifyDelete(ssf, smallSheetFrameMapper, dbName);
	};
	
	public static List<BaseModel> retrieveNViaAction(MockMvc mvc, HttpSession sessionBoss) throws Exception {
		MvcResult mr = mvc.perform(get("/smallSheetFrame/retrieveNEx.bx") //
				.session((MockHttpSession) sessionBoss) //
				.contentType(MediaType.APPLICATION_JSON) //
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String object = o.getString(BaseAction.KEY_ObjectList);
		return new SmallSheetFrame().parseN(object);
	}
	
	public static BaseModel retrieve1ViaAction(MockMvc mvc, HttpSession sessionBoss, int smallSheetFrameID, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(get("/smallSheetFrame/retrieve1Ex.bx?" + SmallSheetFrame.field.getFIELD_NAME_ID() + "=" + smallSheetFrameID) //
				.session((MockHttpSession) sessionBoss) //
				.contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		if(eec != EnumErrorCode.EC_NoError) {
			Shared.checkJSONErrorCode(mr, eec);
			return null;
		}
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String object = o.getString(BaseAction.KEY_Object);
		return new SmallSheetFrame().parse1(object);
	}
	
	public static void updateViaAction(MockMvc mvc, HttpSession sessionBoss, SmallSheetFrame ssf, String textList, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(post("/smallSheetFrame/updateEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
				.param(SmallSheetFrame.field.getFIELD_NAME_ID(), ssf.getID() + "") //
				.param(SmallSheetFrame.field.getFIELD_NAME_logo(), ssf.getLogo()) //
				.param(SmallSheetFrame.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat()) //
				.param(SmallSheetFrame.field.getFIELD_NAME_countOfBlankLineAtBottom(), String.valueOf(ssf.getCountOfBlankLineAtBottom())) //
				.param(SmallSheetFrame.field.getFIELD_NAME_textList(), textList)) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		if(eec != EnumErrorCode.EC_NoError) {
			Shared.checkJSONErrorCode(mr, eec);
			return;
		}
		Shared.checkJSONErrorCode(mr);
		// 检查点
		SmallSheetCP.verifyUpdate(mr, ssf, Shared.DBName_Test, smallSheetTextMapper);
	}
}
