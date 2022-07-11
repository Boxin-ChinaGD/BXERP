package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.util.FieldFormat;

public class BaseSyncCacheDispatcher extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String FIELD_NAME_pos_ID = "posID必须大于0";
	public static final String FIELD_NAME_syncCacheID = "syncCacheID必须大于0";

	BaseSyncCacheDispatcherField field = new BaseSyncCacheDispatcherField();

	private int syncCacheID;

	private int pos_ID;

	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		throw new RuntimeException("Not yet implemented!");
	}

	public int getSyncCacheID() {
		return syncCacheID;
	}

	public void setSyncCacheID(int syncCacheID) {
		this.syncCacheID = syncCacheID;
	}

	public int getPos_ID() {
		return pos_ID;
	}

	public void setPos_ID(int pos_ID) {
		this.pos_ID = pos_ID;
	}

	@Override
	public String toString() {
		return "BaseSyncCacheDispatcher [syncCacheID=" + syncCacheID + ", pos_ID=" + pos_ID + ", ID=" + ID + ", createDatetime=" + createDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		BaseSyncCacheDispatcher scd = (BaseSyncCacheDispatcher) arg0;
		if ((ignoreIDInComparision == true ? true : (scd.getSyncCacheID() == this.getSyncCacheID() && printComparator(field.getFIELD_NAME_syncCacheID())))//
				&& scd.getPos_ID() == this.getPos_ID() && printComparator(field.getFIELD_NAME_pos_ID())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		BaseSyncCacheDispatcher scd = getSyncCacheDispatcher();
		scd.setID(this.getID());
		scd.setSyncCacheID(this.getSyncCacheID());
		scd.setPos_ID(this.getPos_ID());
		return scd;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		BaseSyncCacheDispatcher scd = (BaseSyncCacheDispatcher) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_syncCacheID(), scd.getSyncCacheID());
		params.put(field.getFIELD_NAME_pos_ID(), scd.getPos_ID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		Map<String, Object> params = new HashMap<String, Object>();
		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_pos_ID(), FIELD_NAME_pos_ID, sbError) && !FieldFormat.checkID(pos_ID)) //
		{
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_syncCacheID(), FIELD_NAME_syncCacheID, sbError) && !FieldFormat.checkID(syncCacheID)) //
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
		return "";
	}

}
