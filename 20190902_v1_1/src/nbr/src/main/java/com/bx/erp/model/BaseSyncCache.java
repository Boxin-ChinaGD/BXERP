package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.cache.SyncCache;
import com.bx.erp.util.FieldFormat;

public class BaseSyncCache extends BaseModel {
	private static final long serialVersionUID = 7912579662819526388L;
	public static final String FIELD_ERROR_syncData_ID = "syncData_ID必须大于0";
	public static final String FIELD_ERROR_syncType = "FIELD_ERROR_syncType必须是" + SyncCache.SYNC_Type_C + "、" + SyncCache.SYNC_Type_U + "、" + SyncCache.SYNC_Type_D + "中的一个";
	public static final String FIELD_ERROR_syncSequence = "syncDate_ID必须大于或等于0";
	public static final int MIX_syncSequence = 0;

	public static final BaseSyncCacheField field = new BaseSyncCacheField();

	public static final int MAX_SyncCacheSize = 100000000;

	protected BaseSyncCache getSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 从表信息。如果将来有第2个从表，则可以再定义listSlave2 */
	protected List<?> listSlave1;

	protected String syncType;

	protected int posID;
	
	public int getPosID() {
		return posID;
	}

	public void setPosID(int posID) {
		this.posID = posID;
	}

	protected int syncData_ID;

	public int getSyncData_ID() {
		return syncData_ID;
	}

	public void setSyncData_ID(int syncData_ID) {
		this.syncData_ID = syncData_ID;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public List<?> getListSlave1() {
		return listSlave1;
	}

	public void setListSlave1(List<?> listSlave1) {
		this.listSlave1 = listSlave1;
	}

	@Override
	public String toString() {
		return "BaseSyncCache [listSlave1=" + listSlave1 + ", syncType=" + syncType  + ", posID=" + posID + ", syncData_ID=" + syncData_ID + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		BaseSyncCache sc = (BaseSyncCache) arg0;
		if ((ignoreIDInComparision == true ? true : (sc.getSyncData_ID() == this.getSyncData_ID() && printComparator(field.getFIELD_NAME_syncData_ID())))//
				&& sc.getSyncType().equals(this.getSyncType()) && printComparator(field.getFIELD_NAME_syncType())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		BaseSyncCache sc = getSyncCache();
		sc.setID(this.getID());
		sc.setSyncData_ID(this.getSyncData_ID());
		sc.setSyncSequence(syncSequence);
		sc.setSyncType(this.getSyncType());
		sc.setPosID(this.getPosID());

		if (this.getListSlave1() != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : this.getListSlave1()) {
				BaseSyncCacheDispatcher syncCacheDispatcher = getSyncCacheDispatcher();
				syncCacheDispatcher = (BaseSyncCacheDispatcher) o;
				list.add(syncCacheDispatcher.clone());
			}
			sc.setListSlave1(list);
		}
		return sc;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		BaseSyncCache sc = (BaseSyncCache) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_syncData_ID(), sc.getSyncData_ID());
		params.put(field.getFIELD_NAME_syncSequence(), sc.getSyncSequence());
		params.put(field.getFIELD_NAME_syncType(), sc.getSyncType());
		params.put(field.getFIELD_NAME_posID(), sc.getPosID());

		return params;
	}

	@Override
	public Map<String, Object> getCreateParamEx(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		BaseSyncCache sc = (BaseSyncCache) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_syncData_ID(), sc.getSyncData_ID());
		params.put(field.getFIELD_NAME_syncSequence(), sc.getSyncSequence());
		params.put(field.getFIELD_NAME_syncType(), sc.getSyncType());
		params.put(field.getFIELD_NAME_posID(), sc.getPosID());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		BaseSyncCache sc = (BaseSyncCache) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_syncData_ID(), sc.getSyncData_ID());

		return params;
	}

	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		Map<String, Object> params = new HashMap<String, Object>();
		return params;
	}

//	public static String DEFINE_SET_SyncCacheType(String syncCacheType) {
//		return syncCacheType;
//	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_syncData_ID(), FIELD_ERROR_syncData_ID, sbError) && !(FieldFormat.checkID(syncData_ID)))//
		{
			return sbError.toString();
		}
		
		if (this.printCheckField(field.getFIELD_NAME_syncType(), FIELD_ERROR_syncType, sbError) //
				&& !(syncType.equals(SyncCache.SYNC_Type_C) || syncType.equals(SyncCache.SYNC_Type_U) || syncType.equals(SyncCache.SYNC_Type_D)))//
		{
			return sbError.toString();
		}
		
		if (this.printCheckField(field.getFIELD_NAME_syncSequence(), FIELD_ERROR_syncSequence, sbError) && !(syncSequence >= MIX_syncSequence))//
		{
			return sbError.toString();
		}
		
		return "";
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
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_syncData_ID(), FIELD_ERROR_syncData_ID, sbError) && !(FieldFormat.checkID(syncData_ID)))//
		{
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
