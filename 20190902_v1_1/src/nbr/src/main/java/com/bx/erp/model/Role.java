package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class Role extends BaseModel {
	public static final RoleField field = new RoleField();

	private static final long serialVersionUID = 1L;
	public static final int MAX_LENGTH_Name = 20;
	public static final int Zero = 0;
	public static final String FEILD_ERROR_bForceDelete = "bForceDelete不能小于" + Zero;
	public static final String FIELD_ERROR_name = "角色名字为中文,英文或数字的组合且长度为[1, " + MAX_LENGTH_Name + "]";
	protected String name;

	protected int bForceDelete;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getbForceDelete() {
		return bForceDelete;
	}

	public void setbForceDelete(int bForceDelete) {
		this.bForceDelete = bForceDelete;
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", bForceDelete=" + bForceDelete + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Role r = (Role) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_Role_RetrieveAlsoStaff:
			params.put(field.getFIELD_NAME_ID(), r.getID());

			break;
		default:
			params.put(field.getFIELD_NAME_name(), r.getName() == null ? "" : r.getName());
			params.put(field.getFIELD_NAME_iPageIndex(), r.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), r.getPageSize());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Role r = (Role) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), r.getName() == null ? "" : r.getName());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Role r = (Role) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), r.getID());
		params.put(field.getFIELD_NAME_bForceDelete(), r.getbForceDelete());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Role r = (Role) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_ID(), r.getID());
			params.put(field.getFIELD_NAME_name(), r.getName() == null ? "" : r.getName());

			break;
		}
		return params;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Role obj = new Role();
		obj.setID(ID);
		obj.setName(name);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Role r = (Role) arg0;
		if ((ignoreIDInComparision == true ? true : (r.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& r.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		default:
			if (printCheckField("name", FIELD_ERROR_name, sbError) && (!FieldFormat.checkRoleName(name) || name.length() > MAX_LENGTH_Name)) { //
				return sbError.toString(); // ...把this.getName().trim().length() > 0 && this.getName().trim().length() <=
							// 12)并入checkRoleName()中
			}
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		default:
			
			if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			
			if (this.printCheckField("name", FIELD_ERROR_name, sbError) && (!FieldFormat.checkRoleName(name) || name.length() > MAX_LENGTH_Name)) { //
				return sbError.toString();// ...把this.getName().trim().length() > 0 && this.getName().trim().length() <=
							// 12)并入checkRoleName()中
			}
			return "";
		}

	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		switch (iUseCaseID) {
		case BaseBO.CASE_Role_RetrieveAlsoStaff:
			if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
		default:
			if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !StringUtils.isEmpty(name) && name.length() > MAX_LENGTH_Name) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError  = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_bForceDelete(), FEILD_ERROR_bForceDelete, sbError) && bForceDelete < Zero) { 
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}
	
	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			
			return this;
		} catch(Exception e) {
			return null;
		}
	}

	public enum EnumTypeRole {
		ETR_Cashier("Cashier", 1), // 员工，即收银员
		ETR_Boss("Boss", 2), // 老板
		ETR_PreSale("PreSale", 3), // 博昕售前
		ETR_ShopManager("ShopManager", 4); // 店长

		private String name;
		private int index;

		private EnumTypeRole(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumTypeRole r : EnumTypeRole.values()) {
				if (r.getIndex() == index) {
					return r.getName();
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
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
