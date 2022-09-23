package com.test.bx.app.robot.program;

import java.text.ParseException;

public abstract class ProgramEx {

    private static int INDEX = 0;

    protected long lTimeout;

    public enum EnumProgramType {
        EPT_RetailTrade("EPT_RetailTrade", INDEX++),
        EPT_PosLogin("EPT_PosLogin", INDEX++), //
        EPT_SyncData("EPT_SyncData", INDEX++), //
        EPT_QueryRetailTrade("EPT_QueryRetailTrade", INDEX++), //
        EPT_RetailTradeAggregation("EPT_RetailTradeAggregation", INDEX++); //

        private String name;
        private int index;

        private EnumProgramType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumProgramType ept : EnumProgramType.values()) {
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

    public ProgramEx() {}

    public abstract boolean run(StringBuilder errorInfo) throws InterruptedException, ParseException;
}
