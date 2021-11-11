package com.bx.erp.test.robot.program;

public abstract class ProgramEx {

	private static int INDEX_EnumProgramType = 0;

	public enum EnumProgramType {
		EPT_CreateCommodity("Create Commodity", INDEX_EnumProgramType++), //

		EPT_CreatePurchasingOrder("Create Purchasing Order", INDEX_EnumProgramType++), //

		EPT_CreateWarehousing("Create Warehousing", INDEX_EnumProgramType++), //

		EPT_ProgramPromotion("Create Return Commodity Sheet", INDEX_EnumProgramType++);

		private String name;
		private int index;

		private EnumProgramType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumProgramType c : EnumProgramType.values()) {
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

	public abstract boolean run() throws CloneNotSupportedException, InterruptedException, Exception;

}
