package wpos.model;

import wpos.http.HttpRequestUnit;

class StageMessageType {
    public enum EnumStageMessageType {
        ERT_xxxxxxxx("xxxxxxxxxxxx", 5), //
        ERT_xxxxxxxxx("xxxxxxxxxxxxxx", 1);

        private String name;
        private int index;

        private EnumStageMessageType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStageMessageType c : EnumStageMessageType.values()) {
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
