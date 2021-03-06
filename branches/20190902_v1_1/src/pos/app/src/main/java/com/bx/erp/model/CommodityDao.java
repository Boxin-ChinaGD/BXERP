package com.bx.erp.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "commodity".
*/
public class CommodityDao extends AbstractDao<Commodity, Long> {

    public static final String TABLENAME = "commodity";

    /**
     * Properties of entity Commodity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property Status = new Property(1, int.class, "status", false, "F_Status");
        public final static Property Name = new Property(2, String.class, "name", false, "F_Name");
        public final static Property ShortName = new Property(3, String.class, "shortName", false, "F_ShortName");
        public final static Property Specification = new Property(4, String.class, "specification", false, "F_Specification");
        public final static Property PackageUnitID = new Property(5, int.class, "packageUnitID", false, "F_PackageUnitID");
        public final static Property PurchasingUnit = new Property(6, String.class, "purchasingUnit", false, "F_PurchasingUnit");
        public final static Property BrandID = new Property(7, int.class, "brandID", false, "F_BrandID");
        public final static Property CategoryID = new Property(8, int.class, "categoryID", false, "F_CategoryID");
        public final static Property MnemonicCode = new Property(9, String.class, "mnemonicCode", false, "F_MnemonicCode");
        public final static Property PricingType = new Property(10, int.class, "pricingType", false, "F_PricingType");
        public final static Property LatestPricePurchase = new Property(11, double.class, "latestPricePurchase", false, "F_LatestPricePurchase");
        public final static Property PriceRetail = new Property(12, double.class, "priceRetail", false, "F_PriceRetail");
        public final static Property PriceVIP = new Property(13, double.class, "priceVIP", false, "F_PriceVIP");
        public final static Property PriceWholesale = new Property(14, double.class, "priceWholesale", false, "F_PriceWholesale");
        public final static Property CanChangePrice = new Property(15, int.class, "canChangePrice", false, "F_CanChangePrice");
        public final static Property RuleOfPoint = new Property(16, int.class, "ruleOfPoint", false, "F_RuleOfPoint");
        public final static Property Picture = new Property(17, String.class, "picture", false, "F_Picture");
        public final static Property ShelfLife = new Property(18, int.class, "shelfLife", false, "F_ShelfLife");
        public final static Property ReturnDays = new Property(19, int.class, "returnDays", false, "F_ReturnDays");
        public final static Property CreateDate = new Property(20, java.util.Date.class, "createDate", false, "F_CreateDate");
        public final static Property PurchaseFlag = new Property(21, int.class, "purchaseFlag", false, "F_PurchaseFlag");
        public final static Property RefCommodityID = new Property(22, int.class, "refCommodityID", false, "F_RefCommodityID");
        public final static Property RefCommodityMultiple = new Property(23, int.class, "refCommodityMultiple", false, "F_RefCommodityMultiple");
        public final static Property Tag = new Property(24, String.class, "tag", false, "F_Tag");
        public final static Property NO = new Property(25, int.class, "NO", false, "F_NO");
        public final static Property Type = new Property(26, int.class, "type", false, "F_Type");
        public final static Property NOStart = new Property(27, int.class, "nOStart", false, "F_NOStart");
        public final static Property PurchasingPriceStart = new Property(28, double.class, "purchasingPriceStart", false, "F_PurchasingPriceStart");
        public final static Property StartValueRemark = new Property(29, String.class, "startValueRemark", false, "F_StartValueRemark");
        public final static Property PropertyValue1 = new Property(30, String.class, "propertyValue1", false, "F_PropertyValue1");
        public final static Property PropertyValue2 = new Property(31, String.class, "propertyValue2", false, "F_PropertyValue2");
        public final static Property PropertyValue3 = new Property(32, String.class, "propertyValue3", false, "F_PropertyValue3");
        public final static Property PropertyValue4 = new Property(33, String.class, "propertyValue4", false, "F_PropertyValue4");
        public final static Property CreateDatetime = new Property(34, java.util.Date.class, "createDatetime", false, "F_CreateDatetime");
        public final static Property UpdateDatetime = new Property(35, java.util.Date.class, "updateDatetime", false, "F_UpdateDatetime");
        public final static Property SyncDatetime = new Property(36, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
        public final static Property SyncType = new Property(37, String.class, "syncType", false, "F_SyncType");
    }


    public CommodityDao(DaoConfig config) {
        super(config);
    }
    
    public CommodityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"commodity\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_Status\" INTEGER NOT NULL ," + // 1: status
                "\"F_Name\" TEXT NOT NULL ," + // 2: name
                "\"F_ShortName\" TEXT NOT NULL ," + // 3: shortName
                "\"F_Specification\" TEXT NOT NULL ," + // 4: specification
                "\"F_PackageUnitID\" INTEGER NOT NULL ," + // 5: packageUnitID
                "\"F_PurchasingUnit\" TEXT," + // 6: purchasingUnit
                "\"F_BrandID\" INTEGER NOT NULL ," + // 7: brandID
                "\"F_CategoryID\" INTEGER NOT NULL ," + // 8: categoryID
                "\"F_MnemonicCode\" TEXT NOT NULL ," + // 9: mnemonicCode
                "\"F_PricingType\" INTEGER NOT NULL ," + // 10: pricingType
                "\"F_LatestPricePurchase\" REAL NOT NULL ," + // 11: latestPricePurchase
                "\"F_PriceRetail\" REAL NOT NULL ," + // 12: priceRetail
                "\"F_PriceVIP\" REAL NOT NULL ," + // 13: priceVIP
                "\"F_PriceWholesale\" REAL NOT NULL ," + // 14: priceWholesale
                "\"F_CanChangePrice\" INTEGER NOT NULL ," + // 15: canChangePrice
                "\"F_RuleOfPoint\" INTEGER NOT NULL ," + // 16: ruleOfPoint
                "\"F_Picture\" TEXT NOT NULL ," + // 17: picture
                "\"F_ShelfLife\" INTEGER NOT NULL ," + // 18: shelfLife
                "\"F_ReturnDays\" INTEGER NOT NULL ," + // 19: returnDays
                "\"F_CreateDate\" INTEGER," + // 20: createDate
                "\"F_PurchaseFlag\" INTEGER NOT NULL ," + // 21: purchaseFlag
                "\"F_RefCommodityID\" INTEGER NOT NULL ," + // 22: refCommodityID
                "\"F_RefCommodityMultiple\" INTEGER NOT NULL ," + // 23: refCommodityMultiple
                "\"F_Tag\" TEXT NOT NULL ," + // 24: tag
                "\"F_NO\" INTEGER NOT NULL ," + // 25: NO
                "\"F_Type\" INTEGER NOT NULL ," + // 26: type
                "\"F_NOStart\" INTEGER NOT NULL ," + // 27: nOStart
                "\"F_PurchasingPriceStart\" REAL NOT NULL ," + // 28: purchasingPriceStart
                "\"F_StartValueRemark\" TEXT," + // 29: startValueRemark
                "\"F_PropertyValue1\" TEXT," + // 30: propertyValue1
                "\"F_PropertyValue2\" TEXT," + // 31: propertyValue2
                "\"F_PropertyValue3\" TEXT," + // 32: propertyValue3
                "\"F_PropertyValue4\" TEXT," + // 33: propertyValue4
                "\"F_CreateDatetime\" INTEGER NOT NULL ," + // 34: createDatetime
                "\"F_UpdateDatetime\" INTEGER NOT NULL ," + // 35: updateDatetime
                "\"F_SyncDatetime\" INTEGER," + // 36: syncDatetime
                "\"F_SyncType\" TEXT);"); // 37: syncType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"commodity\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Commodity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getShortName());
        stmt.bindString(5, entity.getSpecification());
        stmt.bindLong(6, entity.getPackageUnitID());
 
        String purchasingUnit = entity.getPurchasingUnit();
        if (purchasingUnit != null) {
            stmt.bindString(7, purchasingUnit);
        }
        stmt.bindLong(8, entity.getBrandID());
        stmt.bindLong(9, entity.getCategoryID());
        stmt.bindString(10, entity.getMnemonicCode());
        stmt.bindLong(11, entity.getPricingType());
        stmt.bindDouble(12, entity.getLatestPricePurchase());
        stmt.bindDouble(13, entity.getPriceRetail());
        stmt.bindDouble(14, entity.getPriceVIP());
        stmt.bindDouble(15, entity.getPriceWholesale());
        stmt.bindLong(16, entity.getCanChangePrice());
        stmt.bindLong(17, entity.getRuleOfPoint());
        stmt.bindString(18, entity.getPicture());
        stmt.bindLong(19, entity.getShelfLife());
        stmt.bindLong(20, entity.getReturnDays());
 
        java.util.Date createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindLong(21, createDate.getTime());
        }
        stmt.bindLong(22, entity.getPurchaseFlag());
        stmt.bindLong(23, entity.getRefCommodityID());
        stmt.bindLong(24, entity.getRefCommodityMultiple());
        stmt.bindString(25, entity.getTag());
        stmt.bindLong(26, entity.getNO());
        stmt.bindLong(27, entity.getType());
        stmt.bindLong(28, entity.getNOStart());
        stmt.bindDouble(29, entity.getPurchasingPriceStart());
 
        String startValueRemark = entity.getStartValueRemark();
        if (startValueRemark != null) {
            stmt.bindString(30, startValueRemark);
        }
 
        String propertyValue1 = entity.getPropertyValue1();
        if (propertyValue1 != null) {
            stmt.bindString(31, propertyValue1);
        }
 
        String propertyValue2 = entity.getPropertyValue2();
        if (propertyValue2 != null) {
            stmt.bindString(32, propertyValue2);
        }
 
        String propertyValue3 = entity.getPropertyValue3();
        if (propertyValue3 != null) {
            stmt.bindString(33, propertyValue3);
        }
 
        String propertyValue4 = entity.getPropertyValue4();
        if (propertyValue4 != null) {
            stmt.bindString(34, propertyValue4);
        }
        stmt.bindLong(35, entity.getCreateDatetime().getTime());
        stmt.bindLong(36, entity.getUpdateDatetime().getTime());
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(37, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(38, syncType);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Commodity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getShortName());
        stmt.bindString(5, entity.getSpecification());
        stmt.bindLong(6, entity.getPackageUnitID());
 
        String purchasingUnit = entity.getPurchasingUnit();
        if (purchasingUnit != null) {
            stmt.bindString(7, purchasingUnit);
        }
        stmt.bindLong(8, entity.getBrandID());
        stmt.bindLong(9, entity.getCategoryID());
        stmt.bindString(10, entity.getMnemonicCode());
        stmt.bindLong(11, entity.getPricingType());
        stmt.bindDouble(12, entity.getLatestPricePurchase());
        stmt.bindDouble(13, entity.getPriceRetail());
        stmt.bindDouble(14, entity.getPriceVIP());
        stmt.bindDouble(15, entity.getPriceWholesale());
        stmt.bindLong(16, entity.getCanChangePrice());
        stmt.bindLong(17, entity.getRuleOfPoint());
        stmt.bindString(18, entity.getPicture());
        stmt.bindLong(19, entity.getShelfLife());
        stmt.bindLong(20, entity.getReturnDays());
 
        java.util.Date createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindLong(21, createDate.getTime());
        }
        stmt.bindLong(22, entity.getPurchaseFlag());
        stmt.bindLong(23, entity.getRefCommodityID());
        stmt.bindLong(24, entity.getRefCommodityMultiple());
        stmt.bindString(25, entity.getTag());
        stmt.bindLong(26, entity.getNO());
        stmt.bindLong(27, entity.getType());
        stmt.bindLong(28, entity.getNOStart());
        stmt.bindDouble(29, entity.getPurchasingPriceStart());
 
        String startValueRemark = entity.getStartValueRemark();
        if (startValueRemark != null) {
            stmt.bindString(30, startValueRemark);
        }
 
        String propertyValue1 = entity.getPropertyValue1();
        if (propertyValue1 != null) {
            stmt.bindString(31, propertyValue1);
        }
 
        String propertyValue2 = entity.getPropertyValue2();
        if (propertyValue2 != null) {
            stmt.bindString(32, propertyValue2);
        }
 
        String propertyValue3 = entity.getPropertyValue3();
        if (propertyValue3 != null) {
            stmt.bindString(33, propertyValue3);
        }
 
        String propertyValue4 = entity.getPropertyValue4();
        if (propertyValue4 != null) {
            stmt.bindString(34, propertyValue4);
        }
        stmt.bindLong(35, entity.getCreateDatetime().getTime());
        stmt.bindLong(36, entity.getUpdateDatetime().getTime());
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(37, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(38, syncType);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Commodity readEntity(Cursor cursor, int offset) {
        Commodity entity = new Commodity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // status
            cursor.getString(offset + 2), // name
            cursor.getString(offset + 3), // shortName
            cursor.getString(offset + 4), // specification
            cursor.getInt(offset + 5), // packageUnitID
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // purchasingUnit
            cursor.getInt(offset + 7), // brandID
            cursor.getInt(offset + 8), // categoryID
            cursor.getString(offset + 9), // mnemonicCode
            cursor.getInt(offset + 10), // pricingType
            cursor.getDouble(offset + 11), // latestPricePurchase
            cursor.getDouble(offset + 12), // priceRetail
            cursor.getDouble(offset + 13), // priceVIP
            cursor.getDouble(offset + 14), // priceWholesale
            cursor.getInt(offset + 15), // canChangePrice
            cursor.getInt(offset + 16), // ruleOfPoint
            cursor.getString(offset + 17), // picture
            cursor.getInt(offset + 18), // shelfLife
            cursor.getInt(offset + 19), // returnDays
            cursor.isNull(offset + 20) ? null : new java.util.Date(cursor.getLong(offset + 20)), // createDate
            cursor.getInt(offset + 21), // purchaseFlag
            cursor.getInt(offset + 22), // refCommodityID
            cursor.getInt(offset + 23), // refCommodityMultiple
            cursor.getString(offset + 24), // tag
            cursor.getInt(offset + 25), // NO
            cursor.getInt(offset + 26), // type
            cursor.getInt(offset + 27), // nOStart
            cursor.getDouble(offset + 28), // purchasingPriceStart
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // startValueRemark
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // propertyValue1
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // propertyValue2
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // propertyValue3
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // propertyValue4
            new java.util.Date(cursor.getLong(offset + 34)), // createDatetime
            new java.util.Date(cursor.getLong(offset + 35)), // updateDatetime
            cursor.isNull(offset + 36) ? null : new java.util.Date(cursor.getLong(offset + 36)), // syncDatetime
            cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37) // syncType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Commodity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStatus(cursor.getInt(offset + 1));
        entity.setName(cursor.getString(offset + 2));
        entity.setShortName(cursor.getString(offset + 3));
        entity.setSpecification(cursor.getString(offset + 4));
        entity.setPackageUnitID(cursor.getInt(offset + 5));
        entity.setPurchasingUnit(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBrandID(cursor.getInt(offset + 7));
        entity.setCategoryID(cursor.getInt(offset + 8));
        entity.setMnemonicCode(cursor.getString(offset + 9));
        entity.setPricingType(cursor.getInt(offset + 10));
        entity.setLatestPricePurchase(cursor.getDouble(offset + 11));
        entity.setPriceRetail(cursor.getDouble(offset + 12));
        entity.setPriceVIP(cursor.getDouble(offset + 13));
        entity.setPriceWholesale(cursor.getDouble(offset + 14));
        entity.setCanChangePrice(cursor.getInt(offset + 15));
        entity.setRuleOfPoint(cursor.getInt(offset + 16));
        entity.setPicture(cursor.getString(offset + 17));
        entity.setShelfLife(cursor.getInt(offset + 18));
        entity.setReturnDays(cursor.getInt(offset + 19));
        entity.setCreateDate(cursor.isNull(offset + 20) ? null : new java.util.Date(cursor.getLong(offset + 20)));
        entity.setPurchaseFlag(cursor.getInt(offset + 21));
        entity.setRefCommodityID(cursor.getInt(offset + 22));
        entity.setRefCommodityMultiple(cursor.getInt(offset + 23));
        entity.setTag(cursor.getString(offset + 24));
        entity.setNO(cursor.getInt(offset + 25));
        entity.setType(cursor.getInt(offset + 26));
        entity.setNOStart(cursor.getInt(offset + 27));
        entity.setPurchasingPriceStart(cursor.getDouble(offset + 28));
        entity.setStartValueRemark(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setPropertyValue1(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setPropertyValue2(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setPropertyValue3(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setPropertyValue4(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setCreateDatetime(new java.util.Date(cursor.getLong(offset + 34)));
        entity.setUpdateDatetime(new java.util.Date(cursor.getLong(offset + 35)));
        entity.setSyncDatetime(cursor.isNull(offset + 36) ? null : new java.util.Date(cursor.getLong(offset + 36)));
        entity.setSyncType(cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Commodity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Commodity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Commodity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
