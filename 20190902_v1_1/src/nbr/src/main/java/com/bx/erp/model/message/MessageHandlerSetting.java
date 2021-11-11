package com.bx.erp.model.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

public class MessageHandlerSetting extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_LENGTH_Template = 128;
	private static final int MAX_LENGTH_Link = 255;
	public static final String FIELD_ERROR_template = "类别模板的长度为[1, " + MAX_LENGTH_Template + "]";
	public static final String FIELD_ERROR_link = "链接的长度为[1, " + MAX_LENGTH_Link + "]"; 
	public static final String FIELD_ERROR_categoryID = "消息类别ID必须大于0";
	
	public static final MessageHandlerSettingField field = new MessageHandlerSettingField();

	protected int categoryID;

	protected String template;

	protected String link;

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "MessageHandlerSetting [categoryID=" + categoryID + ", template=" + template + ", link=" + link + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}

		MessageHandlerSetting m = (MessageHandlerSetting) arg0;
		if ((ignoreIDInComparision == true ? true : (m.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& m.getCategoryID() == categoryID && printComparator(field.getFIELD_NAME_categoryID()) //
				&& m.getTemplate().equals(template) && printComparator(field.getFIELD_NAME_template()) //
				&& m.getLink().equals(link) && printComparator(field.getFIELD_NAME_link()) //
		) {

			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		MessageHandlerSetting m = new MessageHandlerSetting();
		m.setID(ID);
		m.setCategoryID(categoryID);
		m.setTemplate(new String(template));
		m.setLink(new String(link));

		return m;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MessageHandlerSetting m = (MessageHandlerSetting) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_categoryID(), m.getCategoryID());
		params.put(field.getFIELD_NAME_template(), m.getTemplate() == null ? "" : m.getTemplate());
		params.put(field.getFIELD_NAME_link(), m.getLink() == null ? "" : m.getLink());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MessageHandlerSetting m = (MessageHandlerSetting) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), m.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), m.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MessageHandlerSetting m = (MessageHandlerSetting) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), m.getID());
		params.put(field.getFIELD_NAME_categoryID(), m.getCategoryID());
		params.put(field.getFIELD_NAME_template(), m.getTemplate() == null ? "" : m.getTemplate());
		params.put(field.getFIELD_NAME_link(), m.getLink() == null ? "" : m.getLink());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && !FieldFormat.checkID(categoryID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_template(), FIELD_ERROR_template, sbError) && StringUtils.isEmpty(template) || template.length() > MAX_LENGTH_Template) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_link(), FIELD_ERROR_link, sbError) && StringUtils.isEmpty(link) || link.length() > MAX_LENGTH_Link) {
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && !FieldFormat.checkID(categoryID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_template(), FIELD_ERROR_template, sbError) && StringUtils.isEmpty(template) || template.length() > MAX_LENGTH_Template) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_link(), FIELD_ERROR_link, sbError) && StringUtils.isEmpty(link) || link.length() > MAX_LENGTH_Link) {
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

}
