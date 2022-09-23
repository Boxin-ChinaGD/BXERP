package com.bx.erp.model;

public class CacheType extends BaseModel {
	private static final long serialVersionUID = 2033798065231436579L;

	private static int enumIndex = 0;
	public enum EnumCacheType {
		ECT_Commodity("Commodity", enumIndex++), //
		ECT_Promotion("Promotion", enumIndex++), //
		ECT_Category("Category", enumIndex++), //
		ECT_Brand("Brand", enumIndex++), //
		ECT_Staff("Staff", enumIndex++), //
		ECT_Vip("Vip", enumIndex++), //
		ECT_POS("Pos", enumIndex++), //
		ECT_ConfigGeneral("ConfigGeneral", enumIndex++), //
		ECT_ConfigCacheSize("ConfigCacheSize", enumIndex++), //
		ECT_VipCategory("VipCategory", enumIndex++), //
		ECT_InventorySheet("InventorySheet", enumIndex++), //
		ECT_Provider("Provider", enumIndex++), //
		ECT_ProviderDistrict("ProviderDistrict", enumIndex++), //
		ECT_PurchasingOrder("PurchasingOrder", enumIndex++), //
		ECT_Warehouse("Warehouse", enumIndex++), //
		ECT_Warehousing("Warehousing", enumIndex++), //
		ECT_SmallSheet("SmallSheet", enumIndex++), //
		ECT_StaffPermission("StaffPermission", enumIndex++), //
		ECT_CategoryParent("CategoryParent", enumIndex++), //
		ECT_Barcodes("Barcodes", enumIndex++), //
		ECT_RetailTradePromoting("RetailTradePromoting", enumIndex++), //
		ECT_Company("Company", enumIndex++), //
		ECT_Shop("Shop", enumIndex++), //
		ECT_BxStaff("BxStaff", enumIndex++), //
		ECT_BXConfigGeneral("ECT_BXConfigGeneral", enumIndex++), //
		ECT_StaffBelonging("ECT_StaffBelonging", enumIndex++),//
		ECT_VipCard("VipCard", enumIndex++);//

		private String name;
		private int index;

		private EnumCacheType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCacheType c : EnumCacheType.values()) {
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
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
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
}
