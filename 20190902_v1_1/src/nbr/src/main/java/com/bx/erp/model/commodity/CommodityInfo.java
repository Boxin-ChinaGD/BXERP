package com.bx.erp.model.commodity;

import java.util.List;

import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.purchasing.Provider;

/**
 * 该类的作用是为了方便返回数据给前端
 * */
public class CommodityInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "CommodiryInfo [commodity=" + commodity + ", listMultiPackageCommodity=" + listMultiPackageCommodity + "]";
	}

	protected Commodity commodity;
	protected List<Commodity> listMultiPackageCommodity;
	protected List<Provider> listProvider;
	protected List<Barcodes> listBarcodes;
	protected CommodityProperty commodityProperty;

	public CommodityProperty getCommodityProperty() {
		return commodityProperty;
	}

	public void setCommodityProperty(CommodityProperty commodityProperty) {
		this.commodityProperty = commodityProperty;
	}

	public List<Provider> getListProvider() {
		return listProvider;
	}

	public void setListProvider(List<Provider> listProvider) {
		this.listProvider = listProvider;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public List<Commodity> getListMultiPackageCommodity() {
		return listMultiPackageCommodity;
	}

	public void setListMultiPackageCommodity(List<Commodity> listMultiPackageCommodity) {
		this.listMultiPackageCommodity = listMultiPackageCommodity;
	}

	public List<Barcodes> getListBarcodes() {
		return listBarcodes;
	}

	public void setListBarcodes(List<Barcodes> listBarcodes) {
		this.listBarcodes = listBarcodes;
	}

}
