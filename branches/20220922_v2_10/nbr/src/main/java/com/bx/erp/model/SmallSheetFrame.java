package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SmallSheetFrame extends BaseModel {
	private static final long serialVersionUID = -1035884433617222977L;
	public static final SmallSheetFrameField field = new SmallSheetFrameField();

	public static final int NO_SmallSheetTextItem = 20; // 小票格式从表总数
	public static final String FIELD_ERROR_logoSize = "Logo图片太大!";
	public static final String FIELD_ERROR_NO = "创建的小票格式的总数已经到达上限，不允许再创建新的小票格式！";
	public static final int MIN_LENGTH_CountOfBlankLineAtBottom = 0;
	public static final int MAX_LENGTH_CountOfBlankLineAtBottom = 5;
	public static final String FIELD_ERROR_countOfBlankLineAtBottom = "小票底部空行数的范围[" + MIN_LENGTH_CountOfBlankLineAtBottom + ", " + MAX_LENGTH_CountOfBlankLineAtBottom + "]";
	public static final String FIELD_ERROR_delimiterToRepeat = "小票分隔符不能为null";
	// private List<SmallSheetText> smallSheetTextList;

	// private List<RetailTradeCommodity> rtcList;

	protected String logo;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	protected int countOfBlankLineAtBottom;

	public int getCountOfBlankLineAtBottom() {
		return countOfBlankLineAtBottom;
	}

	public void setCountOfBlankLineAtBottom(int countOfBlankLineAtBottom) {
		this.countOfBlankLineAtBottom = countOfBlankLineAtBottom;
	}

	protected String delimiterToRepeat;

	public String getDelimiterToRepeat() {
		return delimiterToRepeat;
	}

	public void setDelimiterToRepeat(String delimiterToRepeat) {
		this.delimiterToRepeat = delimiterToRepeat;
	}

	@Override
	public String toString() {
		return "SmallSheetFrame [listSlave1=" + listSlave1 + ", logo=" + logo + ",countOfBlankLineAtBottom=" + countOfBlankLineAtBottom + ",delimiterToRepeat=" + delimiterToRepeat + ", ID=" + ID + ",createDatetime=" + createDatetime
				+ ",  updateDatetime=" + updateDatetime + ", errorMessage=" + errorMessage + ",  ignoreIDInComparision=" + ignoreIDInComparision + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", date1=" + date1 + ", date2="
				+ date2 + "]";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);

			// smallSheetTextList
			// rtcList
			logo = jo.getString(field.getFIELD_NAME_logo());
			ID = jo.getInt(field.getFIELD_NAME_ID());
			countOfBlankLineAtBottom = jo.getInt(field.getFIELD_NAME_countOfBlankLineAtBottom());
			delimiterToRepeat = jo.getString(field.getFIELD_NAME_delimiterToRepeat());
			//
			String tmp1 = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp1)) {
				createDatetime = sdf.parse(tmp1);
			}
			//
			String tmp2 = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp2)) {
				updateDatetime = sdf.parse(tmp2);
			}
			//
			errorMessage = jo.getString(field.getFIELD_NAME_ErrorMessage());
			// ignoreIDInComparision
			pageIndex = jo.getInt(field.getFIELD_NAME_pageIndex());
			pageSize = jo.getInt(field.getFIELD_NAME_pageSize());
			// int1 = jo.getInt(field.getFIELD_NAME_int1());
			// float2 = (float) jo.getDouble(field.getFIELD_NAME_float2());
			queryKeyword = jo.getString(field.getFIELD_NAME_queryKeyword());
			// string2 = jo.getString(field.getFIELD_NAME_string2());
			// string3 = jo.getString(field.getFIELD_NAME_string3());
			//
			String tmp3 = jo.getString(field.getFIELD_NAME_date1());
			if (!"".equals(tmp3)) {
				date1 = sdf.parse(tmp3);
			}
			//
			String tmp4 = jo.getString(field.getFIELD_NAME_date2());
			if (!"".equals(tmp4)) {
				date2 = sdf.parse(tmp4);
			}
			return this;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// public List<SmallSheetText> getTextList() {
	// return smallSheetTextList;
	// }
	//
	// public void setTextList(List<SmallSheetText> textList) {
	// this.smallSheetTextList = textList;
	// }
	//
	// public List<RetailTradeCommodity> getRtcList() {
	// return rtcList;
	// }
	//
	// public void setRtcList(List<RetailTradeCommodity> rtcList) {
	// this.rtcList = rtcList;
	// }

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		SmallSheetFrame ssf = (SmallSheetFrame) arg0;
		if ((ignoreIDInComparision == true ? true : (ssf.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& ssf.getLogo().equals(logo) && printComparator(field.getFIELD_NAME_logo())//
				&& ssf.getCountOfBlankLineAtBottom() == countOfBlankLineAtBottom && printComparator(field.getFIELD_NAME_countOfBlankLineAtBottom()) //
				&& DatetimeUtil.compareDate(ssf.getCreateDatetime(), createDatetime) && printComparator(field.getFIELD_NAME_createDatetime())//
				&& ssf.getDelimiterToRepeat().equals(delimiterToRepeat) && printComparator(field.getFIELD_NAME_delimiterToRepeat()) //
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && ssf.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && ssf.getListSlave1() != null) {
					if (ssf.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && ssf.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && ssf.getListSlave1() != null) {
					if (listSlave1.size() != ssf.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						SmallSheetText sst1 = (SmallSheetText) listSlave1.get(i);
						sst1.setIgnoreIDInComparision(ignoreIDInComparision);
						Boolean exist = false;
						for (int j = 0; j < ssf.getListSlave1().size(); j++) {
							SmallSheetText sst2 = (SmallSheetText) ssf.getListSlave1().get(j);
							if (sst1.compareTo(sst2) == 0) {
								exist = true;
								break;
							}
						}
						if (!exist) {
							return -1;
						}
					}
				}
			}
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		SmallSheetFrame obj = new SmallSheetFrame();
		obj.setID(ID);
		obj.setLogo(logo);
		obj.setCountOfBlankLineAtBottom(countOfBlankLineAtBottom);
		obj.setDelimiterToRepeat(delimiterToRepeat);
		obj.setCreateDatetime(createDatetime);
		if (listSlave1 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave1) {
				SmallSheetText sst = (SmallSheetText) o;
				list.add(sst.clone());
			}
			obj.setListSlave1(list);
		}

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		SmallSheetFrame ssf = (SmallSheetFrame) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_logo(), ssf.getLogo() == null ? "" : ssf.getLogo());
		params.put(field.getFIELD_NAME_countOfBlankLineAtBottom(), ssf.getCountOfBlankLineAtBottom());
		params.put(field.getFIELD_NAME_createDatetime(), ssf.getCreateDatetime() == null ? new Date() : ssf.getCreateDatetime());
		params.put(field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SmallSheetFrame ssf = (SmallSheetFrame) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), ssf.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), ssf.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		SmallSheetFrame ssf = (SmallSheetFrame) bm;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), ssf.getID());
		params.put(field.getFIELD_NAME_logo(), ssf.getLogo() == null ? "" : ssf.getLogo());
		params.put(field.getFIELD_NAME_countOfBlankLineAtBottom(), ssf.getCountOfBlankLineAtBottom());
		params.put(field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		ErrorInfo ecOut = new ErrorInfo();
		StringBuilder sbError = new StringBuilder();
		BxConfigGeneral smallSheetLogoVolume = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MAX_SmallSheetLogoVolume, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		if (logo != null) {
			if (logo.length() > Integer.parseInt(smallSheetLogoVolume.getValue())) {
				return FIELD_ERROR_logoSize;
			}
		}

		BxConfigGeneral smallSheetNumber = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MAX_SmallSheetNumber, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		List<BaseModel> smallSheetList = (List<BaseModel>) CacheManager.getCache(DataSourceContextHolder.getDbName(), EnumCacheType.ECT_SmallSheet).readN(false, false);
		if (smallSheetList.size() >= Integer.parseInt(smallSheetNumber.getValue())) {
			return FIELD_ERROR_NO;
		}

		if (printCheckField(field.getFIELD_NAME_countOfBlankLineAtBottom(), FIELD_ERROR_countOfBlankLineAtBottom, sbError) && !(FieldFormat.checkCountOfBlankLineAtBottom(countOfBlankLineAtBottom))) {//
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_delimiterToRepeat(), FIELD_ERROR_delimiterToRepeat, sbError) && delimiterToRepeat == null) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_countOfBlankLineAtBottom(), FIELD_ERROR_countOfBlankLineAtBottom, sbError) && !(FieldFormat.checkCountOfBlankLineAtBottom(countOfBlankLineAtBottom))) {//
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_delimiterToRepeat(), FIELD_ERROR_delimiterToRepeat, sbError) && delimiterToRepeat == null) {
			return sbError.toString();
		}

		ErrorInfo ecOut = new ErrorInfo();
		BxConfigGeneral smallSheetLogoVolume = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
				.read1(BaseCache.MAX_SmallSheetLogoVolume, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		if (logo != null) {
			if (logo.length() > Integer.parseInt(smallSheetLogoVolume.getValue())) {// TODO pos端传来的是图片的String格式还是图片的名称？
				return FIELD_ERROR_logoSize;
			}
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

	public enum EnumStatusSmallSheetFrame {
		ESSF_Normal("Normal", 0), //
		ESSF_Deleted("Deleted", 1);

		private String name;
		private int index;

		private EnumStatusSmallSheetFrame(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusSmallSheetFrame c : EnumStatusSmallSheetFrame.values()) {
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
	public List<BaseModel> parseN(String s) {
		List<BaseModel> smallSheetFrameList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
				smallSheetFrame.doParse1(jsonObject);
				smallSheetFrameList.add(smallSheetFrame);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return smallSheetFrameList;
	}

	@Override
	public boolean checkPageSize(BaseModel bm) { // 由于小票格式已经在配置文件中限定只能有10个，所以不会有问题。
		return true;
	}
}
