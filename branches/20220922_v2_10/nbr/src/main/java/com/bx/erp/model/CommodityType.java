package com.bx.erp.model;

public class CommodityType {
	protected EnumCommodityType commodityType;

	public EnumCommodityType getType() {
		return commodityType;
	}

	public void setType(EnumCommodityType ect) {
		this.commodityType = ect;
	}
	
	public enum EnumCommodityType {
		ECT_Normal("Normal Commodity", 0), //平常商品
		ECT_Combination("Combination  Commodity", 1), //组合商品
		ECT_MultiPackaging("MultiPackaging Commodity", 2),//多包装
		ECT_Service("Service Commodity", 3);//服务商品

		private String name;
		private int index;

		private EnumCommodityType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCommodityType c : EnumCommodityType.values()) {
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
