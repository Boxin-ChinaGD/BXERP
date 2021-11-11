package com.bx.erp.model.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class BxConfigGeneral extends Config {
	private static final long serialVersionUID = 1L;

	public static final BxConfigGeneralField field = new BxConfigGeneralField();

	public static final String FIELD_ERROR_value1 = "非法浮点数！";
	public static final String FIELD_ERROR_value2 = "非法整数！";
	public static final String FIELD_ERROR_value4 = "非法正整数！";
	public static final String FIELD_ERROR_value3 = "非法路径！";

	public static final int MIN_BxConfigGeneralID = BaseCache.CompanyBusinessLicensePictureDir;
	public static final int MAX_BxConfigGeneralID = BaseCache.EXTRA_DISK_SPACE_SIZE;
	public static final String FIELD_ERROR_BxConfigGeneralID = "公共配置表的ID只能在" + MIN_BxConfigGeneralID + "到" + MAX_BxConfigGeneralID + "之间";

	protected String name;

	protected String value;
	
	protected int isRequestFromPos;

	public int getIsRequestFromPos() {
		return isRequestFromPos;
	}

	public void setIsRequestFromPos(int isRequestFromPos) {
		this.isRequestFromPos = isRequestFromPos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BXConfigGeneral [name=" + name + ", value=" + value + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		BxConfigGeneral c = new BxConfigGeneral();
		c.setID(ID);
		c.setName(new String(name));
		c.setValue(new String(value));

		return c;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		BxConfigGeneral c = (BxConfigGeneral) arg0;
		if ((ignoreIDInComparision == true ? true : (c.getName() == this.getName() && printComparator(field.getFIELD_NAME_name()))) //
				&& c.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BxConfigGeneral c = (BxConfigGeneral) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());
		params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BxConfigGeneral c = (BxConfigGeneral) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_value(), c.getValue() == null ? "" : c.getValue());

		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		// switch (ID) {
		// case BaseCache.CompanyBusinessLicensePictureDir:
		// case BaseCache.CommodityLogoDir:
		// case BaseCache.CompanyAPICertDir:
		// try {
		// File dir = new File(value);
		// if (!dir.isDirectory()) {
		// return FIELD_ERROR_value3;
		// }
		// } catch (Exception ex) {
		// return FIELD_ERROR_value3;
		// }
		// break;
		// case BaseCache.CommodityPurchasingPriceStart:
		// try {
		// Float.parseFloat(value);
		// } catch (Exception e) {
		// return FIELD_ERROR_value1;
		// }
		// break;
		// case BaseCache.CompanyBusinessLicensePictureVolumeMax:
		// case BaseCache.CompanyAPICertVolumeMax:
		// try {
		// int v = Integer.parseInt(value);
		// if (v <= 0) {
		// return FIELD_ERROR_value4;
		// }
		// } catch (Exception e) {
		// return FIELD_ERROR_value4;
		// }
		// break;
		// case BaseCache.CommodityNOStart:
		// try {
		// Integer.parseInt(value);
		// } catch (Exception e) {
		// return FIELD_ERROR_value2;
		// }
		// break;
		// default:
		// throw new RuntimeException("T_BxConfigGeneral里不存在的配置项！值：" + ID);
		// }
		// return "";
		throw new RuntimeException("暂不支持公共配置表修改功能 !");
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_BxConfigGeneralID, sbError) && FieldFormat.checkID(ID) && MAX_BxConfigGeneralID >= ID) //
		{
			return "";
		}

		return sbError.toString();
	}

	// SP中没有传入参数，所以不用检查
	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);

			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			value = jo.getString(field.getFIELD_NAME_value());
			//
			String tmp3 = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp3)) {
				createDatetime = sdf.parse(tmp3);
			}
			//
			String tmp4 = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp4)) {
				updateDatetime = sdf.parse(tmp4);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
