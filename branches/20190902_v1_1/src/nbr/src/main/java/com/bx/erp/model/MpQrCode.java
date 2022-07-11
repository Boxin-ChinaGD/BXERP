package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

public class MpQrCode extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final MpQrCodeField field = new MpQrCodeField();

	protected String scene;

	protected String page;

	protected int width;

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "MpQrCode [scene=" + scene + ", page=" + page + ", width=" + width + "]";
	}

	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		MpQrCode mpQrCode = (MpQrCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_scene(), "commpanySN=" + mpQrCode.getScene());
		params.put(field.getFIELD_NAME_width(), mpQrCode.getWidth());

		return params;
	}

}
