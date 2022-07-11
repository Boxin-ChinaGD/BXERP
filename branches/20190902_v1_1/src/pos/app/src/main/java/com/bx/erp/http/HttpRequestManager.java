package com.bx.erp.http;

import java.util.HashMap;

public class HttpRequestManager {
    public enum EnumDomainType {
        EDT_Communication("EDT_Communication", 0), //
        EDT_Authentication("EDT_Authentication", 1);

        private String name;
        private int index;

        private EnumDomainType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumDomainType c : EnumDomainType.values()) {
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

        public void sleep(int i){
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static HashMap<EnumDomainType, HttpRequestThread> list = new HashMap<EnumDomainType, HttpRequestThread>();

    public static void register(EnumDomainType ect, HttpRequestThread bc) {
        list.put(ect, bc);
    }

    public static HttpRequestThread getCache(EnumDomainType ect) {
        return list.get(ect);
    }
}
