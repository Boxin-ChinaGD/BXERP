package com.bx.erp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WxSku extends BaseWxModel {
	private static final long serialVersionUID = 1L;

	public static final WxSkuField field = new WxSkuField();

	private int quantity;

	@Override
	public Long getID() {
		return ID;
	}

	@Override
	public void setID(Long ID) {
		this.ID = ID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "WxSku [quantity=" + quantity + ", ID=" + ID + "]";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
//		ID = jo.getInt(field.getFIELD_NAME_ID());
		try {
			quantity = jo.getInt(field.getFIELD_NAME_quantity());
			return this;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> wxSkuList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = new  JSONArray(s);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				WxSku wxSku = new WxSku();
				wxSku.doParse1(jsonObject);
				wxSkuList.add(wxSku);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return wxSkuList;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		WxSku wxSku = (WxSku) arg0;
		if ((ignoreIDInComparision == true ? true : (wxSku.getID() == ID && printComparator(getFIELD_NAME_ID())))//
				&& wxSku.getQuantity() == quantity && printComparator(field.getFIELD_NAME_quantity())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		WxSku wxSku = new WxSku();
		wxSku.setID(ID);
		wxSku.setQuantity(quantity);

		return wxSku;
	}
}
