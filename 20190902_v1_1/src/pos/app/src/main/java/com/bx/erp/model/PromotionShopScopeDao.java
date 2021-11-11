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
 * DAO for table "PROMOTION_SHOP_SCOPE".
*/
public class PromotionShopScopeDao extends AbstractDao<PromotionShopScope, Long> {

    public static final String TABLENAME = "PROMOTION_SHOP_SCOPE";

    /**
     * Properties of entity PromotionShopScope.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property PromotionID = new Property(1, int.class, "promotionID", false, "F_PromotionID");
        public final static Property ShopID = new Property(2, int.class, "shopID", false, "F_ShopID");
        public final static Property ShopName = new Property(3, String.class, "shopName", false, "F_ShopName");
        public final static Property SyncType = new Property(4, String.class, "syncType", false, "F_SyncType");
        public final static Property SyncDatetime = new Property(5, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
    }


    public PromotionShopScopeDao(DaoConfig config) {
        super(config);
    }
    
    public PromotionShopScopeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROMOTION_SHOP_SCOPE\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_PromotionID\" INTEGER NOT NULL ," + // 1: promotionID
                "\"F_ShopID\" INTEGER NOT NULL ," + // 2: shopID
                "\"F_ShopName\" TEXT," + // 3: shopName
                "\"F_SyncType\" TEXT," + // 4: syncType
                "\"F_SyncDatetime\" INTEGER);"); // 5: syncDatetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROMOTION_SHOP_SCOPE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PromotionShopScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPromotionID());
        stmt.bindLong(3, entity.getShopID());
 
        String shopName = entity.getShopName();
        if (shopName != null) {
            stmt.bindString(4, shopName);
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(5, syncType);
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(6, syncDatetime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PromotionShopScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPromotionID());
        stmt.bindLong(3, entity.getShopID());
 
        String shopName = entity.getShopName();
        if (shopName != null) {
            stmt.bindString(4, shopName);
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(5, syncType);
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(6, syncDatetime.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PromotionShopScope readEntity(Cursor cursor, int offset) {
        PromotionShopScope entity = new PromotionShopScope( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // promotionID
            cursor.getInt(offset + 2), // shopID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shopName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // syncType
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // syncDatetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PromotionShopScope entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPromotionID(cursor.getInt(offset + 1));
        entity.setShopID(cursor.getInt(offset + 2));
        entity.setShopName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSyncType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSyncDatetime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PromotionShopScope entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PromotionShopScope entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PromotionShopScope entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
