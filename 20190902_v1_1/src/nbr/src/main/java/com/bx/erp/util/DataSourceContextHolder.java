package com.bx.erp.util;

public class DataSourceContextHolder{
	public static final String DATASOURCE_jdbcDataSource_nbr_bx = "jdbcDataSource_nbr_bx";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    
    public static void setDbName(String dbName) {
        contextHolder.set(dbName);
    }
    public static String getDbName() {
        return ((String) contextHolder.get());
    }
    public static void clearDbName() {
        contextHolder.remove();
    }
  
}
