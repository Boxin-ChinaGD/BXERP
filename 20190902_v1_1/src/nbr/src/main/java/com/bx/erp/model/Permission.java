package com.bx.erp.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.util.FieldFormat;

public class Permission extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final PermissionField field = new PermissionField();

	public static final int Max_LengthName = 20;
	public static final int Max_LengthDomainth = 16;
	public static final int Max_LengthSP = 80;
	public static final int Max_LengthRemark = 32;

	public static final String FIELD_ERROR_name = "操作名称长度不超过" + Max_LengthName + "个字符";
	public static final String FIELD_ERROR_domain = "领域名称长度不能超过" + Max_LengthDomainth + "个字符";
	public static final String FIELD_ERROR_SP = "SP长度不能超过" + Max_LengthSP + "个字符";
	public static final String FIELD_ERROR_remark = "备注长度不能超过" + Max_LengthRemark + "个字符";

	protected String SP;

	protected String name;

	protected String domain;

	protected String remark;
	
	protected int forceDelete;

	public int getForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(int forceDelete) {
		this.forceDelete = forceDelete;
	}

	public String getSP() {
		return SP;
	}

	public void setSP(String sP) {
		SP = sP;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Permission [SP=" + SP + ", name=" + name + ", domain=" + domain + ", remark=" + remark + ", forceDelete=" + forceDelete + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Permission p = (Permission) arg0;
		if ((ignoreIDInComparision == true ? true : (p.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))) //
				&& p.getSP().equals(this.getSP()) && printComparator(field.getFIELD_NAME_SP()) //
				&& p.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
				&& p.getDomain().equals(this.getDomain()) && printComparator(field.getFIELD_NAME_domain()) //
				&& p.getRemark().equals(this.getRemark()) && printComparator(field.getFIELD_NAME_remark()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Permission obj = new Permission();
		obj.setID(ID);
		obj.setSP(SP);
		obj.setName(name);
		obj.setDomain(domain);
		obj.setRemark(remark);
		obj.setCreateDatetime((Date) createDatetime);

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Permission p = (Permission) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_SP(), p.getSP());
		params.put(field.getFIELD_NAME_name(), p.getName());
		params.put(field.getFIELD_NAME_domain(), p.getDomain() == null ? "" : p.getDomain());
		params.put(field.getFIELD_NAME_remark(), p.getRemark() == null ? "" : p.getRemark());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Permission p = (Permission) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), p.getID());
		params.put(field.getFIELD_NAME_forceDelete(), p.getForceDelete()); // 判断是否强制删除，强制删除传1

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Permission p = (Permission) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_Permission_RetrieveAlsoRoleStaff:
			params.put(field.getFIELD_NAME_ID(), p.getID());

			break;
		default:
			params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
			params.put(field.getFIELD_NAME_domain(), p.getDomain() == null ? "" : p.getDomain());
			params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(Permission.field.FIELD_NAME_SP, FIELD_ERROR_SP, sbError) && !StringUtils.isEmpty(name) && SP.length() <= Max_LengthSP) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(Permission.field.FIELD_NAME_name, FIELD_ERROR_name, sbError) && !StringUtils.isEmpty(name) && name.length() <= Max_LengthName) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(Permission.field.FIELD_NAME_domain, FIELD_ERROR_domain, sbError) && !StringUtils.isEmpty(domain) && domain.length() <= Max_LengthDomainth) {
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(Permission.field.FIELD_NAME_remark, FIELD_ERROR_remark, sbError) && !StringUtils.isEmpty(remark) && remark.length() <= Max_LengthRemark) {
		} else {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_Permission_RetrieveAlsoRoleStaff:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
			{
				return sbError.toString();
			}

			return "";
		default:
			if (!StringUtils.isEmpty(name)) {
				if (this.printCheckField(Permission.field.FIELD_NAME_name, FIELD_ERROR_name, sbError) && name.length() > Max_LengthName) {
					return sbError.toString();
				}
			}

			if (!StringUtils.isEmpty(domain)) {
				if (this.printCheckField(Permission.field.FIELD_NAME_domain, FIELD_ERROR_domain, sbError) && domain.length() > Max_LengthDomainth) {
					return sbError.toString();
				}
			}
			return "";
		}
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true; // 因为前端和功能代码没有使用到这个permission/retrieveN或permission/retrieveNEx接口，所以这里不怕调用者（通常是测试代码）传递极大的pageSize过来
	}
}
