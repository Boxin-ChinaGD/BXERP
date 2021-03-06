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
 * DAO for table "PROMOTION_SCOPE".
*/
public class PromotionScopeDao extends AbstractDao<PromotionScope, Long> {

    public static final String TABLENAME = "PROMOTION_SCOPE";

    /**
     * Properties of entity PromotionScope.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property PromotionID = new Property(1, int.class, "promotionID", false, "F_PromotionID");
        public final static Property CommodityID = new Property(2, int.class, "commodityID", false, "F_CommodityID");
        public final static Property CommodityName = new Property(3, String.class, "commodityName", false, "F_CommodityName");
        public final static Property SyncType = new Property(4, String.class, "syncType", false, "F_SyncType");
        public final static Property SyncDatetime = new Property(5, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
    }


    public PromotionScopeDao(DaoConfig config) {
        super(config);
    }
    
    public PromotionScopeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROMOTION_SCOPE\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_PromotionID\" INTEGER NOT NULL ," + // 1: promotionID
                "\"F_CommodityID\" INTEGER NOT NULL ," + // 2: commodityID
                "\"F_CommodityName\" TEXT," + // 3: commodityName
                "\"F_SyncType\" TEXT," + // 4: syncType
                "\"F_SyncDatetime\" INTEGER);"); // 5: syncDatetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROMOTION_SCOPE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PromotionScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPromotionID());
        stmt.bindLong(3, entity.getCommodityID());
 
        String commodityName = entity.getCommodityName();
        if (commodityName != null) {
            stmt.bindString(4, commodityName);
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
    protected final void bindValues(SQLiteStatement stmt, PromotionScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPromotionID());
        stmt.bindLong(3, entity.getCommodityID());
 
        String commodityName = entity.getCommodityName();
        if (commodityName != null) {
            stmt.bindString(4, commodityName);
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
    public PromotionScope readEntity(Cursor cursor, int offset) {
        PromotionScope entity = new PromotionScope( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // promotionID
            cursor.getInt(offset + 2), // commodityID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // commodityName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // syncType
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // syncDatetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PromotionScope entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPromotionID(cursor.getInt(offset + 1));
        entity.setCommodityID(cursor.getInt(offset + 2));
        entity.setCommodityName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSyncType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSyncDatetime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PromotionScope entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PromotionScope entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PromotionScope entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
