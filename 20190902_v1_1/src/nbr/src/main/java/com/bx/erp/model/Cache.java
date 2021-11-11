package com.bx.erp.model;

public class Cache extends BaseModel {
	public static final CacheField field = new CacheField();
	
	private static final long serialVersionUID = 7568150636337210693L;
	
	protected String dbName;
	protected int cacheType;
	
	public String dbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public int getCacheType() {
		return cacheType;
	}

	public void setCacheType(int cacheType) {
		this.cacheType = cacheType;
	}

	@Override
	public String toString() {
		return "Cache [dbName=" + dbName + ", cacheType=" + cacheType + "]";
	}
	
}
