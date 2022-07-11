package com.test.bx.app.robot;

public class Config {
    public static final int PORT = 3618;

    public static final int HEADER_Length = 32;
    public static final int MAX_BodyLength = 1024 * 100;

    public static final String SERVER_Address = "192.168.1.166";
    //
    public static final int SERVER_ReadBufferSize = 1024;
    public static final int SERVER_IdleTime = 300;

    public static final String KEY_NAME_MyMeal = "pos3机器餐";

    public static final String MyPosName = "pos3";

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

    public enum EnumXlsRetailTradeMasterTableColumnName {
        ERTMTCN_ID("ID", 0);

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
        ERTCTCN_tradeID("tradeID", 1);

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
}
