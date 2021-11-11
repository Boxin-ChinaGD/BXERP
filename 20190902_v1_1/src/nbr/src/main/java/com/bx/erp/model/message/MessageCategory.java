package com.bx.erp.model.message;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;

public class MessageCategory extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final MessageCategoryField field = new MessageCategoryField();

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MessageCategory [name=" + name + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}

		MessageCategory m = (MessageCategory) arg0;
		if ((ignoreIDInComparision == true ? true : (m.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& m.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
		) {

			return 0;
		}

		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MessageCategory m = (MessageCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), m.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), m.getPageSize());

		return params;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		MessageCategory m = new MessageCategory();
		m.setID(ID);
		m.setName(new String(name));

		return m;
	}
	
	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}
	
	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}
}
