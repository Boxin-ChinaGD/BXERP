package com.bx.erp.model.message;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MessageItem extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	public static final MessageItemField field = new MessageItemField();
	
	protected int messageID;
	
	protected int messageCategoryID;
	
	protected int commodityID;
	
	protected String commodityName;
	
	protected String commodityBarcode;
	
	protected Double commodityPriceRetail;
	
	protected Double commodityPriceSuggestion;

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getMessageCategoryID() {
		return messageCategoryID;
	}

	public void setMessageCategoryID(int messageCategoryID) {
		this.messageCategoryID = messageCategoryID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}
	
	

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityBarcode() {
		return commodityBarcode;
	}

	public void setCommodityBarcode(String commodityBarcode) {
		this.commodityBarcode = commodityBarcode;
	}

	public Double getCommodityPriceRetail() {
		return commodityPriceRetail;
	}

	public void setCommodityPriceRetail(Double commodityPriceRetail) {
		this.commodityPriceRetail = commodityPriceRetail;
	}

	public Double getCommodityPriceSuggestion() {
		return commodityPriceSuggestion;
	}

	public void setCommodityPriceSuggestion(Double commodityPriceSuggestion) {
		this.commodityPriceSuggestion = commodityPriceSuggestion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String checkCreate(int iUseCaseID) {
		
		return "";
	}
	
	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		
		return "";
	}
	
	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		MessageItem mi = new MessageItem();
		mi.setID(ID);
		mi.setMessageID(messageID);
		mi.setMessageCategoryID(messageCategoryID);
		mi.setCommodityID(commodityID);
		mi.setCreateDatetime(createDatetime);
		mi.setUpdateDatetime(updateDatetime);
		return mi;
	}
	
	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}

		MessageItem mi = (MessageItem) arg0;
		if ((ignoreIDInComparision == true ? true : (mi.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& mi.getMessageID() == messageID && printComparator(field.getFIELD_NAME_messageID()) //
				&& mi.getMessageCategoryID() == messageCategoryID && printComparator(field.getFIELD_NAME_messageCategoryID()) //
				&& mi.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
		) {

			return 0;
		}

		return -1;
	}
	
	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MessageItem mi = (MessageItem) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_messageID(), mi.getMessageID());
		params.put(field.getFIELD_NAME_messageCategoryID(), mi.getMessageCategoryID());
		params.put(field.getFIELD_NAME_commodityID(), mi.getCommodityID());
		return params;
	}
	
	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		MessageItem msgItem = (MessageItem) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_messageCategoryID(), msgItem.getMessageCategoryID());
			params.put(field.getFIELD_NAME_iPageIndex(), msgItem.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), msgItem.getPageSize());
			break;
		}

		return params;
	}
	
	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			messageID = jo.getInt(field.getFIELD_NAME_messageID());
			messageCategoryID = jo.getInt(field.getFIELD_NAME_messageCategoryID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}

			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			
		}catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return this;
	}
	
	@Override
	public List<BaseModel> parseN(String s){
		List<BaseModel> messageItemList = new ArrayList<>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				MessageItem messageItem = new MessageItem();
				messageItem = (MessageItem) messageItem.doParse1(json);
				messageItemList.add(messageItem);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return messageItemList;
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
	
}
