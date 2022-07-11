package com.bx.erp.model.message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

public class Message extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_LENGTH_Parameter = 255;
	private static final int MIN_LENGTH_Parameter = 1;
	public static final String FIELD_ERROR_parameter = "消息内容的长度是[" + MIN_LENGTH_Parameter + ", " + MAX_LENGTH_Parameter + "]";
	public static final String FIELD_ERROR_status = "消息状态只能是0跟1";
	public static final String FIELD_ERROR_companyID = "公司ID必须大于0";
	public static final String FIELD_ERROR_categoryID = "分类ID必须大于0";
	public static final String FIELD_ERROR_isRead = "消息未读已读只能是0或1";
	public static final String FIELD_ERROR_senderID = "发送用户ID只能是正整数";
	public static final String FIELD_ERROR_receiverID = "接受用户ID必须大于0";
	
	public static final MessageField field = new MessageField();

	protected int categoryID;

	protected int companyID;

	protected int isRead;

	protected int status;

	protected String parameter;

	protected Date createDatetime;

	protected int senderID;

	protected int receiverID;

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(int receiverID) {
		this.receiverID = receiverID;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Message [categoryID=" + categoryID + ", companyID=" + companyID + ", isRead=" + isRead + ", status=" + status + ", parameter=" + parameter + ", createDatetime=" + createDatetime + ", senderID=" + senderID + ", receiverID="
				+ receiverID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}

		Message m = (Message) arg0;
		if ((ignoreIDInComparision == true ? true : (m.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& m.getCategoryID() == categoryID && printComparator(field.getFIELD_NAME_categoryID()) //
				&& m.getIsRead() == isRead && printComparator(field.getFIELD_NAME_isRead()) //
				&& m.getParameter().equals(parameter) && printComparator(field.getFIELD_NAME_categoryID()) //
				// && m.getCreateDate().equals(this.getCreateDate()) &&
				// printComparator("createDate") //
				&& m.getSenderID() == senderID && printComparator(field.getFIELD_NAME_senderID()) //
				&& m.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
				&& m.getReceiverID() == receiverID && printComparator(field.getFIELD_NAME_receiverID()) //
		) {

			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Message m = new Message();
		m.setID(ID);
		m.setCompanyID(companyID);
		m.setCategoryID(categoryID);
		m.setIsRead(isRead);
		m.setParameter(new String(parameter));
		m.setCreateDatetime((Date) createDatetime);
		m.setSenderID(senderID);
		m.setReceiverID(receiverID);
		m.setStatus(status);

		return m;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Message msg = (Message) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Message_RetrieveNForWx:
			params.put(field.getFIELD_NAME_status(), msg.getStatus());
			params.put(field.getFIELD_NAME_companyID(), msg.getCompanyID());
			break;
		default:
			params.put(field.getFIELD_NAME_iPageIndex(), msg.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), msg.getPageSize());
			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Message m = (Message) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Message_UpdateStatus:
			params.put(field.getFIELD_NAME_ID(), m.getID());
			break;
		default:
			params.put(field.getFIELD_NAME_ID(), m.getID());
			params.put(field.getFIELD_NAME_isRead(), m.getIsRead());
			params.put(field.getFIELD_NAME_status(), m.getStatus());
			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Message m = (Message) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_categoryID(), m.getCategoryID());
		params.put(field.getFIELD_NAME_companyID(), m.getCompanyID());
		params.put(field.getFIELD_NAME_isRead(), m.getIsRead());
		params.put(field.getFIELD_NAME_parameter(), m.getParameter() == null ? "" : m.getParameter());
		params.put(field.getFIELD_NAME_senderID(), m.getSenderID());
		params.put(field.getFIELD_NAME_receiverID(), m.getReceiverID());
		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && !FieldFormat.checkID(categoryID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_isRead(), FIELD_ERROR_isRead, sbError) && !(isRead == 0 || isRead == 1)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_parameter(), FIELD_ERROR_parameter, sbError) && StringUtils.isEmpty(parameter) || parameter.length() > MAX_LENGTH_Parameter) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_senderID(), FIELD_ERROR_senderID, sbError) && !FieldFormat.checkSenderID(String.valueOf(senderID))) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_receiverID(), FIELD_ERROR_receiverID, sbError) && !FieldFormat.checkID(receiverID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && !FieldFormat.checkID(companyID)) {
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		switch (iUseCaseID) {
		case BaseBO.CASE_Message_UpdateStatus:
			if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			break;
		default:
			if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			
			if(printCheckField(field.getFIELD_NAME_isRead(), FIELD_ERROR_isRead, sbError) && !(isRead == 0 || isRead == 1)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		switch (iUseCaseID) {
		case BaseBO.CASE_Message_RetrieveNForWx:
			if(printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && status != BaseAction.INVALID_STATUS && !(status == 0 || status == 1)) {
				return sbError.toString();
			}
			
			if(printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && companyID != BaseAction.INVALID_ID && !FieldFormat.checkID(companyID)) {
				return sbError.toString();
			}
			
			break;
		default:
			break;
		}
		return "";
	}
	
//	采购计划表待审核 EMC_ToApprovePurchasingTable
//	采购超时 EMC_PurchasingTimeout
//	入库价与采购订单上价格不符合	EMC_WarehousingPriceDifferent
//	商品临近保质期	EMC_ApproachingShelfLife
//	查看盘点差异报告	EMC_ViewInventoryReport
//	收银员登录上班	EMC_CashierLogin
//	交班、营业额	EMC_CashierLogout
//  商品滞销  EMC_UnsalableCommodity
	public enum EnumMessageCategory {
		EMC_ToApprovePurchasingTable("Not approvePurchasingTable",1),//
		EMC_PurchasingTimeout("PurchasingTimeout",2),//
		EMC_WarehousingPriceDifferent("WarehousingPriceDifferent",3),//
		EMC_ApproachingShelfLife("ApproachingShelfLife",4),//
		EMC_ViewInventoryReport("ViewInventoryReport",5),//
		EMC_CashierLogin("CashierLogin",6),//
		EMC_CashierLogout("CashierLogout",7),//
		EMC_UnsalableCommodity("UnsalableCommodity",8);
		
		private String name;
		private int index;

		private EnumMessageCategory(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumMessageCategory c : EnumMessageCategory.values()) {
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
}
