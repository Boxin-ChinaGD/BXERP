package com.bx.erp.model;

public class TaskType  extends BaseModel {
	private static final long serialVersionUID = -3448807081968029026L;

	public enum EnumTaskType {
		ETT_PurchasingTimeout("PurchasingTimeout", 0), //
		ETT_ShelfLifeTaskThread("ShelfLifeTaskThread", 1), //
		ETT_UnsalableCommodity("UnsalableCommodity", 2), //
		ETT_RetailTradeDailyReportSummaryTaskThread("RetailTradeDailyReportSummaryTaskThread",3),
		ETT_RetailTradeMonthlyReportSummaryTaskThread("RetailTradeMonthlyReportSummaryTaskThread",4),
		ETT_RetailTradeDailyReportByCategoryParentTaskThread("RetailTradeDailyReportByCategoryParentTaskThread",5),
		ETT_RetailTradeDailyReportByStaffTaskThread("RetailTradeDailyReportByStaffTaskThread",6),
		ETT_BonusTaskThread("BonusTaskThread",7);
		
		private String name;
		private int index;

		private EnumTaskType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumTaskType c : EnumTaskType.values()) {
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
