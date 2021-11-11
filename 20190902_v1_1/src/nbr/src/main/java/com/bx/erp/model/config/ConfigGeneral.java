package com.bx.erp.model.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.BaseModel;
import net.sf.json.JSONObject;

public class ConfigGeneral extends Config {
	private static final long serialVersionUID = 1L;
	public static final ConfigGeneralField field = new ConfigGeneralField();

	public static final String FIELD_ERROR_value1 = "非法时间格式！正确的时间格式应为：" + BaseAction.TIME_FORMAT_ConfigGeneral;
	public static final String FIELD_ERROR_value2 = "非法正整数！";

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
		return "ConfigGeneral [name=" + name + ", value=" + value + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ConfigGeneral c = new ConfigGeneral();
		c.setID(ID);
		c.setName(name);
		c.setValue(value);

		return c;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		ConfigGeneral c = (ConfigGeneral) arg0;
		if ((ignoreIDInComparision == true ? true : (c.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()))) //
				&& c.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ConfigGeneral c = (ConfigGeneral) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());
		params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ConfigGeneral c = (ConfigGeneral) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_value(), c.getValue() == null ? "" : c.getValue());

		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		switch (ID) {
		case BaseCache.PurchasingTimeoutTaskScanStartTime:
		case BaseCache.PurchasingTimeoutTaskScanEndTime:
		case BaseCache.UsalableCommodityTaskScanStartTime:
		case BaseCache.UsalableCommodityTaskScanEndTime:
		case BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime:
		case BaseCache.RetailTradeDailyReportSummaryTaskScanEndTime:
		case BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime:
		case BaseCache.RetailTradeMonthlyReportSummaryTaskScanEndTime:
		case BaseCache.RetailTradeDailyReportByCategoryParentTaskScanStartTime:
		case BaseCache.RetailTradeDailyReportByCategoryParentTaskScanEndTime:
		case BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime:
		case BaseCache.RetailTradeDailyReportByStaffTaskScanEndTime:
		case BaseCache.ShelfLifeTaskScanStartTime:
		case BaseCache.ShelfLifeTaskScanEndTime:
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
			try {
				simpleDateFormat.parse(value);
			} catch (Exception e) {
				return FIELD_ERROR_value1;
			}
			break;
		case BaseCache.PAGESIZE:
		case BaseCache.ACTIVE_SMALLSHEET_ID:
		case BaseCache.PurchasingTimeoutTaskFlag:
		case BaseCache.UsalableCommodityTaskFlag:
		case BaseCache.ShelfLifeTaskFlag:
		case BaseCache.MaxVicePackageUnit:
		case BaseCache.MaxProviderNOPerCommodity:
		case BaseCache.MaxPurchasingOrderCommodityNO:
		case BaseCache.CommodityLogoVolumeMax:
		case BaseCache.MaxWarehousingCommodityNO:
			try {
				int v = Integer.parseInt(value);
				if (v <= 0) {
					return FIELD_ERROR_value2;
				}
			} catch (Exception e) {
				return FIELD_ERROR_value2;
			}
			break;
		default:
			throw new RuntimeException("T_ConfigGeneral里不存在的配置项！值：" + ID);
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
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
