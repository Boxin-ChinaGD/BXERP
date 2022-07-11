package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Pos extends BaseAuthenticationModel {
	private static final long serialVersionUID = 1L;
	public static final PosField field = new PosField();

	public static final int MIN_Status = EnumStatusPos.ESP_Active.getIndex();
	public static final int MAX_Status = EnumStatusPos.ESP_Inactive.getIndex();
	public static final int MAX_LengthSalt = 32;
	public static final int MAX_LengthPassWordInPos = 16;
	public static final int MAX_LengthPosSN = 32;

	public static final String FIELD_ERROR_ShopID = "门店ID不能小于0";
	public static final String FIELD_ERROR_Status = "pos的状态只能在" + MIN_Status + "到" + MAX_Status + "之间";
	public static final String FIELD_ERROR_Salt = "Salt不能为空，长度不能超过" + MAX_LengthSalt + "个字符";
	public static final String FIELD_ERROR_PassWordInPos = "pos机的密码不能为空，长度不能超过" + MAX_LengthPassWordInPos + "个字符";
	public static final String FIELD_ERROR_PosSN = "posSN码不能为空，长度不能超过" + MAX_LengthPosSN + "个字符";
	public static final String FIELD_ERROR_ShopID_RN = "RN时，ShopID只能等于-1或大于0";// -1代表查询所有
	public static final String FIELD_ERROR_Status_RN = "RN时，status只能为-1, " + MIN_Status + " ," + MAX_Status;// -1代表查询所有

	public static final int RESET_PASSWORD_IN_POS = 1;
	public static final int NOT_RESET_PASSWORD_IN_POS = 0;

	private String pos_SN;
	private int shopID;

	private int status;

	protected int resetPasswordInPos;
	/** int3为0代表是login操作，需要清空会话，1为其他操作，不需要清空会话 */
	protected int operationType;

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public int getResetPasswordInPos() {
		return resetPasswordInPos;
	}

	public void setResetPasswordInPos(int resetPasswordInPos) {
		this.resetPasswordInPos = resetPasswordInPos;
	}

	@Override
	public String getKey() {
		return String.valueOf(ID);
	}

	public String getPos_SN() {
		return pos_SN;
	}

	public void setPos_SN(String pos_SN) {
		this.pos_SN = pos_SN;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Pos [pos_SN=" + pos_SN + ", shopID=" + shopID + ", status=" + status + ", returnSalt=" + returnSalt + ", resetPasswordInPos=" + resetPasswordInPos + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			pos_SN = jo.getString(field.getFIELD_NAME_pos_SN());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			status = jo.getInt(field.getFIELD_NAME_status());
			pwdEncrypted = jo.getString(field.getFIELD_NAME_pwdEncrypted());
			salt = jo.getString(field.getFIELD_NAME_salt());
			passwordInPOS = jo.getString(field.getFIELD_NAME_passwordInPOS());
			ID = jo.getInt(field.getFIELD_NAME_ID());
			//
			String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			SimpleDateFormat sDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			if (!"".equals(tmp)) {
				createDatetime = sDateFormat.parse(tmp);
			}
			//
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = sDateFormat.parse(tmp);
			}
			return this;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> posList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Pos pos = new Pos();
				pos.doParse1(jsonObject);
				posList.add(pos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return posList;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Pos p = new Pos();
		p.setID(ID);
		p.setPos_SN(pos_SN);
		p.setShopID(shopID);
		p.setSalt(salt);
		p.setStatus(status);
		p.setPwdEncrypted(pwdEncrypted);
		p.setPasswordInPOS(passwordInPOS);
		p.setCreateDatetime(createDatetime == null ? null : (Date) createDatetime.clone());
		p.setUpdateDatetime(updateDatetime == null ? null : (Date) updateDatetime.clone());

		p.setCompanySN(companySN); // 特别注意：作为公司SN

		return p;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		Pos p = (Pos) arg0;
		if ((ignoreIDInComparision == true ? true : (p.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& p.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& p.getPos_SN().equals(pos_SN) && printComparator(field.getFIELD_NAME_pos_SN()) //
				&& p.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
		) {
			return 0;
		}

		return -1;
	}

	// public Map<String, Object> getRetrieve1Param(final BaseModel bm) {
	// return getSimpleParam(bm.getID());
	// }
	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Pos p = (Pos) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_pos_SN(), p.getPos_SN() == null ? "" : p.getPos_SN());
		params.put(field.getFIELD_NAME_shopID(), p.getShopID());// == 0 ? BaseAction.INVALID_ID : p.getShopID());
		params.put(field.getFIELD_NAME_status(), p.getStatus());// == 0 ? BaseAction.INVALID_STATUS : p.getStatus());
		params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());
		params.put(field.getFIELD_NAME_returnSalt(), p.getReturnSalt());

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Pos p = (Pos) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_pos_SN(), p.getPos_SN() == null ? "" : p.getPos_SN());
		params.put(field.getFIELD_NAME_shopID(), p.getShopID());
		params.put(field.getFIELD_NAME_salt(), p.getSalt() == null ? "" : p.getSalt());
		params.put(field.getFIELD_NAME_status(), p.getStatus());
		params.put(field.getFIELD_NAME_returnSalt(), p.getReturnSalt());
		params.put(field.getFIELD_NAME_passwordInPOS(), p.getPasswordInPOS());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Pos p = (Pos) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), p.getID());
		params.put(field.getFIELD_NAME_returnSalt(), p.getReturnSalt());
		switch (iUseCaseID) {
		case BaseBO.CASE_POS_Reset:
			break;
		case BaseBO.CASE_POS_RecycleApp:
			params.put(field.getFIELD_NAME_passwordInPOS(), p.getPasswordInPOS());
			params.put(field.getFIELD_NAME_salt(), p.getSalt());
			break;
		default:
			params.put(field.getFIELD_NAME_shopID(), p.getShopID());
			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		Pos p = (Pos) bm;
		params.put(field.getFIELD_NAME_returnSalt(), p.getReturnSalt());

		switch (iUseCaseID) {
		case BaseBO.CASE_Pos_Retrieve1BySN:
			params.put(field.getFIELD_NAME_pos_SN(), p.getPos_SN() == null ? "" : p.getPos_SN());
			break;
		default:
			params.put(field.getFIELD_NAME_ID(), p.getID());
			params.put(field.getFIELD_NAME_resetPasswordInPos(), p.getResetPasswordInPos());
			break;
		}
		return params;
	}

	// public Map<String, Object> getDeleteParam(final BaseModel bm) {
	// return getSimpleParam(bm.getID());
	// }

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_pos_SN(), FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(pos_SN) && pos_SN.length() <= MAX_LengthPosSN) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_ShopID, sbError) && !FieldFormat.checkID(shopID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_Salt, sbError) && !StringUtils.isEmpty(salt) && salt.length() <= MAX_LengthSalt) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_Status, sbError) && MIN_Status <= status && status <= MAX_Status) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_passwordInPOS(), FIELD_ERROR_PassWordInPos, sbError) && !StringUtils.isEmpty(passwordInPOS) && passwordInPOS.length() <= MAX_LengthPassWordInPos) {
		} else {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
		{
			return sbError.toString();
		}

		switch (iUseCaseID) {
		case BaseBO.CASE_POS_Reset:
			break;
		case BaseBO.CASE_POS_RecycleApp:
			if (this.printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_Salt, sbError) && (StringUtils.isEmpty(salt) || salt.length() > MAX_LengthSalt)) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_passwordInPOS(), FIELD_ERROR_PassWordInPos, sbError) && (StringUtils.isEmpty(passwordInPOS) || passwordInPOS.length() > MAX_LengthPassWordInPos)) {
				return sbError.toString();
			}
			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_ShopID, sbError) && !FieldFormat.checkID(shopID)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.CASE_Login:
			break;
		case BaseBO.CASE_Pos_Retrieve1BySN:
			StringBuilder sbError = new StringBuilder();

			if (this.printCheckField(field.getFIELD_NAME_pos_SN(), FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(pos_SN) && pos_SN.length() <= MAX_LengthPosSN) {
			} else {
				return sbError.toString();
			}

			break;
		default:
			return super.checkRetrieve1(iUseCaseID);
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(pos_SN)) {
			if (this.printCheckField(field.getFIELD_NAME_pos_SN(), FIELD_ERROR_PosSN, sbError) && pos_SN.length() > MAX_LengthPosSN) {
				return sbError.toString();
			}
		}

		if (this.printCheckField(field.getFIELD_NAME_shopID(), String.format(FIELD_ERROR_ShopID_RN, field.getFIELD_NAME_shopID()), sbError) && !(shopID == BaseAction.INVALID_ID || shopID > 0)) {// RN时shopID=-1代表查询所有
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_status(), String.format(FIELD_ERROR_Status_RN, field.getFIELD_NAME_status()), sbError) && !(status >= BaseAction.INVALID_STATUS && status <= MAX_Status)) {// RN时status=-1代表查询所有
			return sbError.toString();
		}
		return "";
	}

	public enum EnumStatusPos {
		ESP_Active("Active", 0), //
		ESP_Inactive("Inactive", 1);

		private String name;
		private int index;

		private EnumStatusPos(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusPos c : EnumStatusPos.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_POSCacheSize.getIndex();
	}

	/** 清除敏感信息。一般用来向请求者返回数据前设置 */
	public void clearSensitiveInfo() {
		salt = null;
	}

}
