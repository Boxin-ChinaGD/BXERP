package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VipSource extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final VipSourceField field = new VipSourceField();

	protected int vipID;

	protected int sourceCode;

	protected String ID1;

	protected String ID2;

	protected String ID3;

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public int getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(int sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getID1() {
		return ID1;
	}

	public void setID1(String iD1) {
		ID1 = iD1;
	}

	public String getID2() {
		return ID2;
	}

	public void setID2(String iD2) {
		ID2 = iD2;
	}

	public String getID3() {
		return ID3;
	}

	public void setID3(String iD3) {
		ID3 = iD3;
	}

	@Override
	public String toString() {
		return "VipSource [vipID=" + vipID + ", sourceCode=" + sourceCode + ", ID1=" + ID1 + ", ID2=" + ID2 + ", ID3=" + ID3 + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		VipSource vipSource = new VipSource();
		vipSource.setID(ID);
		vipSource.setVipID(vipID);
		vipSource.setSourceCode(sourceCode);
		vipSource.setID1(ID1);
		vipSource.setID2(ID2);
		vipSource.setID3(ID3);

		return vipSource;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		VipSource vipSource = (VipSource) arg0;
		if ((ignoreIDInComparision == true ? true : vipSource.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& vipSource.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID()) //
				&& vipSource.getSourceCode() == sourceCode && printComparator(field.getFIELD_NAME_sourceCode()) //
				&& vipSource.getID1().equals(ID1) && printComparator(field.getFIELD_NAME_ID1()) //
				&& vipSource.getID2().equals(ID2) && printComparator(field.getFIELD_NAME_ID2()) //
				&& vipSource.getID3().equals(ID3) && printComparator(field.getFIELD_NAME_ID3()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			sourceCode = jo.getInt(field.getFIELD_NAME_sourceCode());
			ID1 = jo.getString(field.getFIELD_NAME_ID1());
			ID2 = jo.getString(field.getFIELD_NAME_ID2());
			ID3 = jo.getString(field.getFIELD_NAME_ID3());

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> vipSourceList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				VipSource vipSource = new VipSource();
				vipSource.doParse1(jsonObject1);
				vipSourceList.add(vipSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return vipSourceList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipSource vipSource = (VipSource) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), vipSource.getVipID());
		params.put(field.getFIELD_NAME_sourceCode(), vipSource.getSourceCode());
		params.put(field.getFIELD_NAME_ID1(), vipSource.getID1() == null ? "" : vipSource.getID1());
		params.put(field.getFIELD_NAME_ID2(), vipSource.getID2() == null ? "" : vipSource.getID2());
		params.put(field.getFIELD_NAME_ID3(), vipSource.getID3() == null ? "" : vipSource.getID3());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipSource vipSource = (VipSource) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), vipSource.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), vipSource.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkCreate(int iUseCaseID) {
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

	public enum EnumVipSourceCode {
		EVSC_WX("WX", 0), //
		EVSC_ALIPAY("ALIPAY", 1), //
		EVSC_MEITUAN("MEITUAN", 2);

		private String name;
		private int index;

		private EnumVipSourceCode(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumVipSourceCode c : EnumVipSourceCode.values()) {
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

}
