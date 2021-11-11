package com.bx.erp.model;

public class SyncCacheType extends BaseModel {
	private static final long serialVersionUID = 2033798065231436579L;

	private static int enumIndex = 0;
	public enum EnumSyncCacheType {
		ESCT_PromotionActivity("PromotionActivity", enumIndex++), //
		ESCT_Staff("Staff", enumIndex++), //
		ESCT_CommoditySyncInfo("CommoditySyncInfo", enumIndex++), //
		ESCT_VipCategorySyncInfo("VipCategorySyncInfo", enumIndex++), //
		ESCT_BrandSyncInfo("BrandSyncInfo", enumIndex++), //
		ESCT_PosSyncInfo("PosSyncInfo", enumIndex++), //
		ESCT_CategorySyncInfo("CategorySyncInfo", enumIndex++), //
		ESCT_BarcodesSyncInfo("BarcodesSyncInfo", enumIndex++), 
		ESCT_PromotionSyncInfo("PromotionSyncInfo", enumIndex++), //
		ESCT_RetailTradePromotingSyncInfo("RetailTradePromotingSyncInfo", enumIndex++);//
		// ESCT_Commodity("Commodity", 0), //
		// ESCT_Category("Category", 2), //
		// ESCT_Brand("Brand", 3), //
		// ESCT_Vip("Vip", 5),
		// ESCT_StaffSyncInfo("StaffSyncInfo", 6),
//		ESCT_VipSyncInfo("VipSyncInfo", 7), //
//		ESCT_RetailTradeSyncInfo("RetailTradeSyncInfo", 11), //
//		ESCT_ConfigGeneralSyncInfo("ConfigGeneralSyncInfo", 16), ////

		private String name;
		private int index;

		private EnumSyncCacheType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumSyncCacheType c : EnumSyncCacheType.values()) {
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
