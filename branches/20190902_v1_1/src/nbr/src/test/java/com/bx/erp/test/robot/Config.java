package com.bx.erp.test.robot;

public class Config {
	public static final int PORT = 3618;

	public static final int HEADER_Length = 32;
	public static final int MAX_BodyLength = 1024 * 100;
	
	public static final String SERVER_Address = "localhost"; 
	//
	public static final int SERVER_ReadBufferSize = 1024;
	public static final int SERVER_IdleTime = 300;
	//
//	public static final int TOTAL_CLIENT_Count = 2;
	public static final int TOTAL_CLIENT_Count = 1;
	
	public static final String[] CLIENT_Name = {"pos1", "pos2", "pos3"};
	
	public static final String SessionKEY_PosNAME = "posName";
	
	/*nbr机器人活动*/
	public static final String[] Nbr_Activity = {"商品信息", "采购订单主表", "入库单主表"};
	
	/*pos机器人活动*/
	public static final String[] Pos_Activity = {"登录信息", "同步", "零售单主表"};
	
	public static final String[] Machine_Meal = {"nbr机器餐", "pos1机器餐", "pos2机器餐", "pos3机器餐"};
	
	public static final String[] Acitivity_NO = {"活动1", "活动2"};

	public enum EnumXlsMealColumn {
		EB_ProgramUnitNO("活动单元no", 0), //
		EB_SheetTableName("工作表名", 1), //
		EB_MasterTable("主表", 2), //
		EB_OperationType("操作类型代码operationType", 3), //
		EB_OperationTypeName("操作类型名称", 4), //
		EB_ProgramName("活动名称", 5);

		private String name;
		private int index;

		private EnumXlsMealColumn(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsMealColumn c : EnumXlsMealColumn.values()) {
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

	/** 跟APP端的RetailTrade.EnumOperationType一一对应。若改这里，需要改那里 */
	public enum EnumRetailTradeOperationType {
        ERTOT_CreateRetailTrade("ERTOT_CreateRetailTrade", 0),
        ERTOT_CreateReturnRetailTrade("ERTOT_CreateReturnRetailTrade", 1), //
        ERTOT_QueryRetailTrade("EOT_QueryRetailTrade", 2); //

        private String name;
        private int index;

        private EnumRetailTradeOperationType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumRetailTradeOperationType ept : EnumRetailTradeOperationType.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
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
    }

	public enum EnumXlsRetailTradeMasterTableColumnName {
		ERTMTCN_sourceID("sourceID", 11);

        private String name;
        private int index;

        private EnumXlsRetailTradeMasterTableColumnName(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumXlsRetailTradeMasterTableColumnName ept : EnumXlsRetailTradeMasterTableColumnName.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
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
	}
	
	public enum EnumXlsRetailTradeCommodityTableColumnName {
		ERTCTCN_tradeID("tradeID", 1),
		ERTCTCN_commodityID("commodityID", 2);

        private String name;
        private int index;

        private EnumXlsRetailTradeCommodityTableColumnName(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumXlsRetailTradeCommodityTableColumnName ept : EnumXlsRetailTradeCommodityTableColumnName.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
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
	}
	
	// .xls活动表
	public enum EnumXlsProgramColumnName {
		EXPCN_ProgramUnitNO("活动单元no", 0), //
		EXPCN_NbrMachineMeal("nbr机器餐", 1), //
		EXPCN_Pos1MachineMeal("pos1机器餐", 2), //
		EXPCN_Pos2MachineMeal("pos2机器餐", 3), //
		EXPCN_Pos3MachineMeal("pos3机器餐", 4); //

		private String name;
		private int index;

		private EnumXlsProgramColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsProgramColumnName c : EnumXlsProgramColumnName.values()) {
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
	
	// .xls入库单主表
	public enum EnumXlsWarehousingColumnName {
		EXCN_ID("ID", 0); //


		private String name;
		private int index;

		private EnumXlsWarehousingColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsWarehousingColumnName c : EnumXlsWarehousingColumnName.values()) {
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
	
	// .xls入库单从表
	public enum EnumXlsWarehousingCommodityColumnName {
		EXCCN_warehousingID("warehousingID", 1); //

		private String name;
		private int index;

		private EnumXlsWarehousingCommodityColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsWarehousingCommodityColumnName c : EnumXlsWarehousingCommodityColumnName.values()) {
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
	
	// .xls采购单主表
	public enum EnumXlsPurchasingOrderColumnName {
		EXPCN_ID("ID", 0); //


		private String name;
		private int index;

		private EnumXlsPurchasingOrderColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsPurchasingOrderColumnName c : EnumXlsPurchasingOrderColumnName.values()) {
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
	
	
	// .xls采购单从表
	public enum EnumXlsPurchasingOrderCommodityColumnName {
		EXPCCN_purchasingOrderID("purchasingOrderID", 1); //


		private String name;
		private int index;

		private EnumXlsPurchasingOrderCommodityColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsPurchasingOrderCommodityColumnName c : EnumXlsPurchasingOrderCommodityColumnName.values()) {
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
	
	
	// .xls商品信息表
	public enum EnumXlsCommodityColumnName {
		ECCN_ID("ID", 0); //


		private String name;
		private int index;

		private EnumXlsCommodityColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsCommodityColumnName c : EnumXlsCommodityColumnName.values()) {
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
	
	// .xls条形码表
	public enum EnumXlsBarcodesColumnName {
		EBCN_commodityID("commodityID", 1), //
		EBCN_barcode("barcode", 2);

		private String name;
		private int index;

		private EnumXlsBarcodesColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsBarcodesColumnName c : EnumXlsBarcodesColumnName.values()) {
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
	
	// .xls组合商品表
	public enum EnumXlsCombinationCommodityColumnName {
		ECCCN_commodityID("commodityID", 1); //

		private String name;
		private int index;

		private EnumXlsCombinationCommodityColumnName(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumXlsCombinationCommodityColumnName c : EnumXlsCombinationCommodityColumnName.values()) {
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
