package com.bx.erp.test.paySIT.dataProvider;

import java.util.List;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BasePackageUnitTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.commodity.BaseCategoryParentTest;
import com.bx.erp.test.purchasing.BaseProviderTest;

public class DBDataLoader {
	private static List<BaseModel> queryAllBrand() {
		Brand brand = new Brand();
		brand.setPageIndex(BaseAction.PAGE_StartIndex);
		brand.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return BaseBrandTest.retrieveNViaMapper(brand, Shared.DBName_Test);
	}

	private static List<BaseModel> queryAllPackageUnit() {
		PackageUnit packageUnit = new PackageUnit();
		packageUnit.setPageIndex(BaseAction.PAGE_StartIndex);
		packageUnit.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return BasePackageUnitTest.retrieveNViaMapper(packageUnit, Shared.DBName_Test);
	}

	private static List<BaseModel> queryAllCategory() {
		Category category = new Category();
		category.setPageIndex(BaseAction.PAGE_StartIndex);
		category.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return BaseCategoryParentTest.queryCategoryViaMapper(category, Shared.DBName_Test);
	}

	private static List<BaseModel> queryAllProvider() {
		Provider provider = new Provider();
		provider.setPageIndex(BaseAction.PAGE_StartIndex);
		provider.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return BaseProviderTest.queryViaMapper(provider, Shared.DBName_Test);
	}

	private static List<BaseModel> queryNomalCommodity() {
		Commodity commodity = new Commodity();
		commodity.setPageIndex(BaseAction.PAGE_StartIndex);
		commodity.setType(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodity.setPageSize(BaseAction.PAGE_SIZE_MAX + new Random().nextInt(BaseAction.PAGE_SIZE_MAX));
		List<BaseModel> commList = BaseCommodityTest.retrieveNViaMapper(commodity, Shared.DBName_Test);
		// 找到商品的条形码
		for (BaseModel baseModel : commList) {
			Commodity comm = (Commodity) baseModel;
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
			comm.setBarcodeID(barcodes.getID());
			comm.setBarcodes(barcodes.getBarcode());
		}
		//
		return commList;
	}

	private static List<BaseModel> queryAllPromotion() {
		Promotion promotion = new Promotion();
		promotion.setPageIndex(BaseAction.PAGE_StartIndex);
		promotion.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return BasePromotionTest.retrieveNPromotionViaMapper(promotion);
	}

	public static List<BaseModel> queryAllData(int type) {
		List<BaseModel> list = null;
		switch (EnumQueryDataName.values()[type]) {
		case EQDN_PackageUnit:
			list = queryAllPackageUnit();
			break;
		case EQDN_Brand:
			list = queryAllBrand();
			break;
		case EQDN_Category:
			list = queryAllCategory();
			break;
		case EQDN_Provider:
			list = queryAllProvider();
			break;
		case EQDN_Commodity:
			list = queryNomalCommodity();
			break;
		case EQDN_Promotion:
			list = queryAllPromotion();
			break;
		default:
			break;
		}
		return list;
	}

	public enum EnumQueryDataName {
		EQDN_PackageUnit("EQDN_PackageUnit", 0), //
		EQDN_Brand("EQDN_Brand", 1), //
		EQDN_Category("EQDN_Category", 2), //
		EQDN_Provider("EQDN_Provider", 3), //
		EQDN_Commodity("EQDN_Commodity", 4), //
		EQDN_Promotion("EQDN_Promotion", 5);//

		private String name;
		private int index;

		private EnumQueryDataName(String name, int index) {
			this.name = name;
			this.index = index;
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
