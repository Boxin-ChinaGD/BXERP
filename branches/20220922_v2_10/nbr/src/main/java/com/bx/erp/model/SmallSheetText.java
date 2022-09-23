package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class SmallSheetText extends BaseModel {
	private static final long serialVersionUID = -6920407583206480148L;
	public static final SmallSheetTextField field = new SmallSheetTextField();

	private static final int Zero = 0;
	private static final int MAX_LENGTH_Content = 100;
	public static final String FIELD_ERROR_content = "小票内容长度为(" + Zero + ", " + MAX_LENGTH_Content + "]";
	public static final String FIELD_ERROR_frameID = "小票格式ID必须大于" + Zero;
	public static final String FIELD_ERROR_size = "字体大小必须大于" + Zero;
	public static final String FIELD_ERROR_bold = "字体加粗只能是" + Zero + "或" + 1;
	public static final String FIELD_ERROR_gravity = "内容位置必须大于" + Zero;

	private String content;

	private double size;

	private int bold;

	private int gravity;

	private int frameId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public int getBold() {
		return bold;
	}

	public void setBold(int bold) {
		this.bold = bold;
	}

	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public int getFrameId() {
		return frameId;
	}

	public void setFrameId(int frameId) {
		this.frameId = frameId;
	}

	@Override
	public String toString() {
		return "SmallSheetText [content=" + content + ", size=" + size + ", bold=" + bold + ", gravity=" + gravity + ", frameId=" + frameId + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		SmallSheetText sst = (SmallSheetText) arg0;
		if ((ignoreIDInComparision == true ? true : (sst.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& sst.getContent().equals(content) && printComparator(field.getFIELD_NAME_content()) //
				&& Math.abs(GeneralUtil.sub(sst.getSize(), size)) < TOLERANCE && printComparator(field.getFIELD_NAME_size()) //
				&& sst.getBold() == bold && printComparator(field.getFIELD_NAME_bold()) //
				&& sst.getGravity() == gravity && printComparator(field.getFIELD_NAME_gravity())//
				&& sst.getFrameId() == frameId && printComparator(field.getFIELD_NAME_frameId())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		SmallSheetText obj = new SmallSheetText();
		obj.setID(ID);
		obj.setContent(content);
		obj.setSize(size);
		obj.setBold(bold);
		obj.setGravity(gravity);
		obj.setFrameId(frameId);

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		SmallSheetText sst = (SmallSheetText) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_content(), sst.getContent() == null ? "" : sst.getContent());
		params.put(field.getFIELD_NAME_size(), sst.getSize());
		params.put(field.getFIELD_NAME_bold(), sst.getBold());
		params.put(field.getFIELD_NAME_gravity(), sst.getGravity());
		params.put(field.getFIELD_NAME_frameId(), sst.getFrameId());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SmallSheetText sst = (SmallSheetText) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_frameId(), sst.getFrameId());
		params.put(field.getFIELD_NAME_iPageIndex(), sst.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), sst.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		SmallSheetText sst = (SmallSheetText) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_frameId(), sst.getFrameId());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		SmallSheetText sst = (SmallSheetText) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), sst.getID());
		params.put(field.getFIELD_NAME_content(), sst.getContent());
		params.put(field.getFIELD_NAME_size(), sst.getSize());
		params.put(field.getFIELD_NAME_bold(), sst.getBold());
		params.put(field.getFIELD_NAME_gravity(), sst.getGravity());
		params.put(field.getFIELD_NAME_frameId(), sst.getFrameId());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_content(), FIELD_ERROR_content, sbError) && content == null || content.length() > MAX_LENGTH_Content) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_size(), FIELD_ERROR_size, sbError) && !(size > Zero)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_bold(), FIELD_ERROR_bold, sbError) && !(bold == Zero || bold == 1)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_gravity(), FIELD_ERROR_gravity, sbError) && !(gravity > Zero)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
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

		if (printCheckField(field.getFIELD_NAME_content(), FIELD_ERROR_content, sbError) && content == null || content.length() > MAX_LENGTH_Content) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_size(), FIELD_ERROR_size, sbError) && !(size > Zero)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_bold(), FIELD_ERROR_bold, sbError) && !(bold == Zero || bold == 1)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_gravity(), FIELD_ERROR_gravity, sbError) && !(gravity > Zero)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
