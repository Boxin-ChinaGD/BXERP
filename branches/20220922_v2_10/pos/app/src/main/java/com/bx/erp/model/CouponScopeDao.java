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
 * DAO for table "couponScope".
*/
public class CouponScopeDao extends AbstractDao<CouponScope, Long> {

    public static final String TABLENAME = "couponScope";

    /**
     * Properties of entity CouponScope.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property CouponID = new Property(1, int.class, "couponID", false, "F_CouponID");
        public final static Property CommodityID = new Property(2, int.class, "commodityID", false, "F_CommodityID");
        public final static Property CommodityName = new Property(3, String.class, "commodityName", false, "F_CommodityName");
    }


    public CouponScopeDao(DaoConfig config) {
        super(config);
    }
    
    public CouponScopeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"couponScope\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"F_CouponID\" INTEGER NOT NULL ," + // 1: couponID
                "\"F_CommodityID\" INTEGER NOT NULL ," + // 2: commodityID
                "\"F_CommodityName\" TEXT);"); // 3: commodityName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"couponScope\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CouponScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getCouponID());
        stmt.bindLong(3, entity.getCommodityID());
 
        String commodityName = entity.getCommodityName();
        if (commodityName != null) {
            stmt.bindString(4, commodityName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CouponScope entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getCouponID());
        stmt.bindLong(3, entity.getCommodityID());
 
        String commodityName = entity.getCommodityName();
        if (commodityName != null) {
            stmt.bindString(4, commodityName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CouponScope readEntity(Cursor cursor, int offset) {
        CouponScope entity = new CouponScope( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // couponID
            cursor.getInt(offset + 2), // commodityID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // commodityName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CouponScope entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCouponID(cursor.getInt(offset + 1));
        entity.setCommodityID(cursor.getInt(offset + 2));
        entity.setCommodityName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CouponScope entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CouponScope entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CouponScope entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
