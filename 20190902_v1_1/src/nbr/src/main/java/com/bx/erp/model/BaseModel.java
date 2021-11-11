package com.bx.erp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BaseModel implements Serializable, Comparable<BaseModel>, Cloneable, Comparator<BaseModel> {
	private Log logger = LogFactory.getLog(BaseModel.class);
	public static final BaseModelField field = new BaseModelField();
	
	/**
	 * 为了打log时令人类更容易看到有重大错误
	 */
	public static final String ERROR_Tag = "~~~~~~~~~~~~~~XXXXXXXX~~~~~~~~~~~~~~XXXXXXXX~~~~~~~~~~~~~~XXXXXXXX~~~~~~~~~~~~~~XXXXXXXX";

	public static final String FIELD_ERROR_pageSize = "pageSize不能大于" + BaseAction.PAGE_SIZE_MAX;

	public static final int FIRST_PAGE_Default = 1;

	private static final long serialVersionUID = 1731935110676228223L;

	/** 从表信息。如果将来有第2个从表，则可以再定义listSlave2 */
	protected List<?> listSlave1;

	public List<?> getListSlave1() {
		return listSlave1;
	}

	public void setListSlave1(List<?> listSlave1) {
		this.listSlave1 = listSlave1;
	}

	/** 用于一些特殊用途，需要存储从表外的List时候使用 */
	protected List<?> listSlave2;

	public List<?> getListSlave2() {
		return listSlave2;
	}

	public void setListSlave2(List<?> listSlave2) {
		this.listSlave2 = listSlave2;
	}
	
	protected List<?> listSlave3;

	public List<?> getListSlave3() {
		return listSlave3;
	}

	public void setListSlave3(List<?> listSlave3) {
		this.listSlave3 = listSlave3;
	}

	@JSONField(name = "ID") // 使fastjson解析时使用该注解命名的字段
	protected int ID;

	protected Date createDatetime;

	public boolean isIgnoreIDInComparision() {
		return ignoreIDInComparision;
	}

	protected Date updateDatetime;

	protected Date syncDatetime;

	public Date getSyncDatetime() {
		return syncDatetime;
	}

	public void setSyncDatetime(Date syncDatetime) {
		this.syncDatetime = syncDatetime;
	}

	/** POS CUD对象，首先插入SQLite，然后上传到服务器，最后还有其它处理。在上传到服务器这一步失败后，下次还需要再次上传。<br />
	 * 下次上传需要知道call哪个syncAction，本字段可以作为判断依据。<br />
	 * 本字段只在POS端有用，在nbr端无意义 */
	protected String syncType;

	protected int iOrderBy;

	protected int isASC;

	public int getiOrderBy() {
		return iOrderBy;
	}

	public void setiOrderBy(int iOrderBy) {
		this.iOrderBy = iOrderBy;
	}

	public int getIsASC() {
		return isASC;
	}

	public void setIsASC(int isASC) {
		this.isASC = isASC;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	protected String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public final static double TOLERANCE = 0.000001d;

	protected boolean ignoreIDInComparision;

	public void setIgnoreIDInComparision(boolean ignoreIDInComparision) {
		this.ignoreIDInComparision = ignoreIDInComparision;
	}

	protected boolean ignoreSlaveListInComparision;

	public void setIgnoreSlaveListInComparision(boolean ignoreSlaveListInComparision) {
		this.ignoreSlaveListInComparision = ignoreSlaveListInComparision;
	}

	public boolean isIgnoreSlaveListInComparision() {
		return ignoreSlaveListInComparision;
	}

	protected int pageIndex;

	protected int pageSize;
	/** 搜索关键字 */
	protected String queryKeyword;

	/** 代表是否返回对象 */
	protected int returnObject;

	/** 检查字段是否唯一 */
	protected int fieldToCheckUnique;

	/** 代表要搜索的员工的ID */
	protected int operatorStaffID;

	protected int syncSequence;

	protected int returnSalt;

	/** 表示该商品同步块是否完全同步 1为同步 */
	protected int isSync;

	public int getIsSync() {
		return isSync;
	}

	public void setIsSync(int isSync) {
		this.isSync = isSync;
	}

	public int getReturnSalt() {
		return returnSalt;
	}

	public void setReturnSalt(int returnSalt) {
		this.returnSalt = returnSalt;
	}

	public int getSyncSequence() {
		return syncSequence;
	}

	public void setSyncSequence(int iSyncSequence) {
		this.syncSequence = iSyncSequence;
	}

	public int getOperatorStaffID() {
		return operatorStaffID;
	}

	public void setOperatorStaffID(int operatorStaffID) {
		this.operatorStaffID = operatorStaffID;
	}

	public String getQueryKeyword() {
		return queryKeyword;
	}

	public void setQueryKeyword(String queryKeyword) {
		this.queryKeyword = queryKeyword;
	}

	public int getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(int returnObject) {
		this.returnObject = returnObject;
	}

	public int getFieldToCheckUnique() {
		return fieldToCheckUnique;
	}

	public void setFieldToCheckUnique(int fieldToCheckUnique) {
		this.fieldToCheckUnique = fieldToCheckUnique;
	}

	protected int posID;

	public int getPosID() {
		return posID;
	}

	public void setPosID(int posID) {
		this.posID = posID;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	protected Date date1;

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	protected Date date2;

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	// 用于唯一字段
	protected String uniqueField;

	public String getUniqueField() {
		return uniqueField;
	}

	public void setUniqueField(String uniqueField) {
		this.uniqueField = uniqueField;
	}

	public BaseModel() {
		pageIndex = FIRST_PAGE_Default;
		pageSize = BaseAction.PAGE_SIZE;
	}

	protected boolean printComparator(String fieldName) {
		logger.info("	Field Equal: " + fieldName);
		return true;
	}

	/** 1、打印当前要检查的字段 2、重置err的值为suggestion */
	protected boolean printCheckField(String fieldNameInCode, String suggestion, StringBuilder err) {
		logger.info("￥￥￥准备检查以下字段的格式: " + fieldNameInCode);
		err.setLength(0);
		err.append(suggestion);

		return true;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		throw new RuntimeException("尚未实现compareTo()方法！");
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("尚未实现clone()方法！");
	}

	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getRetriveNParam()方法！");
	}

	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getRetriveNParamEx()方法！");
	}

	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getCreateParam()方法！");
	}

	public Map<String, Object> getCreateParamEx(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getCreateParam()方法！");
	}

	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getUpdateParam()方法！");
	}

	public Map<String, Object> getUpdateParamEx(int iUseCaseID, final BaseModel bm) {
		throw new RuntimeException("尚未实现getUpdateParam()方法！");
	}

	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	public Map<String, Object> getDeleteParamEx(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	public Map<String, Object> getDeleteParam(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	public Map<String, Object> getDeleteParamEx(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	protected void checkParameterInput(BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}
	}

	protected Map<String, Object> getSimpleParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.INVALID_CASE_ID:
			BaseModelField bmf = new BaseModelField();
			params.put(bmf.getFIELD_NAME_ID(), bm.getID());
			break;
		default:
			throw new RuntimeException("未定义的CASE！");
		}

		return params;
	}

	protected Map<String, Object> getSimpleParam(int iUseCaseID, final int objID) {
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.SYSTEM:
		case BaseBO.CASE_Normal:
		case BaseBO.INVALID_CASE_ID:
			BaseModelField bmf = new BaseModelField();
			params.put(bmf.getFIELD_NAME_ID(), objID);
			break;
		default:
			throw new RuntimeException("未定义的CASE！");
		}

		return params;
	}

	protected BaseModel JSONToObject(String JSon) {
		throw new RuntimeException("该函数尚未实现！");
	}

	protected List<BaseModel> JSONToObjects(String JSon) {
		throw new RuntimeException("该函数尚未实现！");
	}

	public String checkCreate(int iUseCaseID) {
		throw new RuntimeException("该函数尚未实现！");
	}

	public String checkUpdate(int iUseCaseID) {
		throw new RuntimeException("该函数尚未实现！");
	}

	public String checkRetrieve1(int iUseCaseID) {
		// throw new RuntimeException("该函数尚未实现！");

		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)) //
		{
			return "";
		}

		return sbError.toString();
	}

	public String checkRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_iPageIndex(), FieldFormat.FIELD_ERROR_Paging, sbError) && this.printCheckField(field.getFIELD_NAME_iPageSize(), FieldFormat.FIELD_ERROR_Paging, sbError)//
				&& !FieldFormat.checkPaging(pageIndex, pageSize))//
		{
			return sbError.toString();
		}

		return doCheckRetrieveN(iUseCaseID);
	}

	protected String doCheckRetrieveN(int iUseCaseID) {
		throw new RuntimeException("尚未实现doCheckRetrieveN(int iUseCaseID)！");
	}

	public String checkDelete(int iUseCaseID) {
		// throw new RuntimeException("该函数尚未实现！");

		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)) //
		{
			return "";
		}

		return sbError.toString();
	}

	public List<?> parseN(String s) {
		throw new RuntimeException("该函数尚未实现！");
	}

	public List<?> parseN(JSONArray jsonArray) {
		List<BaseModel> bmList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (this.parse1(jsonObject.toString()) == null) {
					return null;
				}
				bmList.add(this.clone());
			}
			return bmList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<?> parseN(com.alibaba.fastjson.JSONArray jsonArray) {
		List<BaseModel> bmList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (this.parse1(jsonObject.toString()) == null) {
					return null;
				}
				bmList.add(this.clone());
			}
			return bmList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BaseModel parse1(String s) {
		try {
			return doParse1(JSONObject.fromObject(s));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public BaseModel parse1(JSONObject jo) {
		try {
			return doParse1(jo);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public BaseModel parse1(com.alibaba.fastjson.JSONObject jo) {
		try {
			return doParse1(jo);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected BaseModel doParse1(JSONObject jo) {
		throw new RuntimeException("尚未实现doParse1(JSONObject jo)方法！");
	}

	protected BaseModel doParse1(com.alibaba.fastjson.JSONObject jo) {
		throw new RuntimeException("尚未实现doParse1(com.alibaba.fastjson.JSONObject jo)方法！");
	}

	public String toJson(BaseModel bm) {
		throw new RuntimeException("尚未实现toJson()方法！");
	}

	public String toJson(List<BaseModel> bmList) {
		throw new RuntimeException("尚未实现toJson()方法！");
	}

	public boolean setDefaultValueToCreate(int iUseCaseID) {
		return true;
	}

	public String checkRetrieveNEx(int iUseCaseID) {
		throw new RuntimeException("尚未实现checkRetrieveNEx()方法！");
	}

	public enum EnumBoolean {
		EB_NO("NO", 0), //
		EB_Yes("Yes", 1);

		private String name;
		private int index;

		private EnumBoolean(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumBoolean c : EnumBoolean.values()) {
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
	public int compare(BaseModel bm1, BaseModel bm2) {
		if (isASC == EnumBoolean.EB_Yes.getIndex()) {
			return bm1.getID() - bm2.getID();
		} else {
			return bm2.getID() - bm1.getID();
		}
	}

	/** 在加载缓存时，有的对象要加载的数目被配置在t_configcachesize中。这个数目可能大于BaseAction.PAGE_SIZE_MAX，导致加载失败。
	 * 为了令其加载成功，加载时判断pageSize的值如果与t_configcachesize中配置的值相等，则让其加载。 */
	public int getCacheSizeID() {
		return 0;// 并非所有对象都需要在项目启动时加载到缓存中，所以有的对象是没有缓存的。返回0，表明此种对象是没有缓存的
	}

	public boolean checkPageSize(BaseModel bm) {
		if (bm.getPageSize() > BaseAction.PAGE_SIZE_MAX) {
			System.out.println("当前线程的DB name=" + DataSourceContextHolder.getDbName());
			// 虽然超过PageSize最大值，但是，有的情况下并非用来显示在页面上，而是用来加载到缓存中。这时，应该接受一个较大的缓存值。这个值通常配置在t_configcachesize中
			if (getCacheSizeID() > 0) {// 节省加锁解锁的消耗
				ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(DataSourceContextHolder.getDbName(), EnumCacheType.ECT_ConfigCacheSize).read1(getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(),
						DataSourceContextHolder.getDbName());
				if (ccs != null && bm.getPageSize() == Integer.parseInt(ccs.getValue())) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
