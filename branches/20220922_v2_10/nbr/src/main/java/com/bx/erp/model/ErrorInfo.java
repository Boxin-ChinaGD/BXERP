package com.bx.erp.model;

import net.sf.json.JSONObject;

public class ErrorInfo extends BaseModel {
	private static final long serialVersionUID = 2033798065231436579L;

	public static final ErrorInfoField field = new ErrorInfoField();

	@Override
	public String toString() {
		return "错误码=" + errorCode + "\t错误信息=" + errorMessage;
	}

	protected EnumErrorCode errorCode;
	// protected String errorMsg;//BaseModel基类里已经有这个数据成员

	public ErrorInfo() {
		errorCode = EnumErrorCode.EC_OtherError;
	}

	public EnumErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(EnumErrorCode eec) {
		this.errorCode = eec;
	}

	public enum EnumErrorCode {
		EC_NoError("No error", 0), //
		EC_Duplicated("Duplicated", 1), //
		EC_NoSuchData("No such data", 2), //
		EC_OtherError("Other error", 3), //
		EC_Hack("Hack", 4), //
		EC_NotImplemented("Not implemented", 5), //
		EC_ObjectNotEqual("Object not equal", 6), //
		EC_BusinessLogicNotDefined("Business logic not defined", 7), //
		EC_WrongFormatForInputField("Wrong format for input field", 8), //
		EC_Timeout("Time out", 9), //
		EC_NoPermission("No permission", 10), //
		EC_OtherError2("Other error 2", 11),//
		EC_PartSuccess("Part success", 12),
		EC_Redirect("Redirect", 13),
		EC_DuplicatedSession("Duplicated session", 14), //你已经在其它地方登录
		EC_InvalidSession("Invalid session", 15), // 代表会话无效，一般是因为已经断网
		EC_SessionTimeout("Session time out", 16),// 会话过期
		EC_WechatServerError("Wechat Server Error", 17); 

		private String name;
		private int index;

		private EnumErrorCode(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumErrorCode c : EnumErrorCode.values()) {
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
	public BaseModel doParse1(JSONObject jo) {
		try {
			errorMessage = jo.getString(field.getFIELD_NAME_ErrorMessage());
			
			String ec = jo.getString(field.getFIELD_NAME_ErrorCode());
			errorCode = EnumErrorCode.valueOf(ec);

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}
}
