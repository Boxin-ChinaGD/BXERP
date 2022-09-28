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
 * DAO for table "warehousing".
*/
public class WarehousingDao extends AbstractDao<Warehousing, Long> {

    public static final String TABLENAME = "warehousing";

    /**
     * Properties of entity Warehousing.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property ProviderID = new Property(1, Long.class, "providerID", false, "F_ProviderID");
        public final static Property Status = new Property(2, int.class, "status", false, "F_Status");
        public final static Property WarehouseID = new Property(3, int.class, "warehouseID", false, "F_WarehouseID");
        public final static Property StaffID = new Property(4, int.class, "staffID", false, "F_StaffID");
        public final static Property ApproverID = new Property(5, int.class, "approverID", false, "F_ApproverID");
        public final static Property CreateDatetime = new Property(6, java.util.Date.class, "createDatetime", false, "F_CreateDatetime");
        public final static Property PurchasingOrderID = new Property(7, int.class, "purchasingOrderID", false, "F_PurchasingOrderID");
        public final static Property Sn = new Property(8, String.class, "sn", false, "F_SN");
    }


    public WarehousingDao(DaoConfig config) {
        super(config);
    }
    
    public WarehousingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"warehousing\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"F_ProviderID\" INTEGER NOT NULL ," + // 1: providerID
                "\"F_Status\" INTEGER NOT NULL ," + // 2: status
                "\"F_WarehouseID\" INTEGER NOT NULL ," + // 3: warehouseID
                "\"F_StaffID\" INTEGER NOT NULL ," + // 4: staffID
                "\"F_ApproverID\" INTEGER NOT NULL ," + // 5: approverID
                "\"F_CreateDatetime\" INTEGER NOT NULL ," + // 6: createDatetime
                "\"F_PurchasingOrderID\" INTEGER NOT NULL ," + // 7: purchasingOrderID
                "\"F_SN\" TEXT NOT NULL );"); // 8: sn
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"warehousing\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Warehousing entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getProviderID());
        stmt.bindLong(3, entity.getStatus());
        stmt.bindLong(4, entity.getWarehouseID());
        stmt.bindLong(5, entity.getStaffID());
        stmt.bindLong(6, entity.getApproverID());
        stmt.bindLong(7, entity.getCreateDatetime().getTime());
        stmt.bindLong(8, entity.getPurchasingOrderID());
        stmt.bindString(9, entity.getSn());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Warehousing entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getProviderID());
        stmt.bindLong(3, entity.getStatus());
        stmt.bindLong(4, entity.getWarehouseID());
        stmt.bindLong(5, entity.getStaffID());
        stmt.bindLong(6, entity.getApproverID());
        stmt.bindLong(7, entity.getCreateDatetime().getTime());
        stmt.bindLong(8, entity.getPurchasingOrderID());
        stmt.bindString(9, entity.getSn());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Warehousing readEntity(Cursor cursor, int offset) {
        Warehousing entity = new Warehousing( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getLong(offset + 1), // providerID
            cursor.getInt(offset + 2), // status
            cursor.getInt(offset + 3), // warehouseID
            cursor.getInt(offset + 4), // staffID
            cursor.getInt(offset + 5), // approverID
            new java.util.Date(cursor.getLong(offset + 6)), // createDatetime
            cursor.getInt(offset + 7), // purchasingOrderID
            cursor.getString(offset + 8) // sn
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Warehousing entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProviderID(cursor.getLong(offset + 1));
        entity.setStatus(cursor.getInt(offset + 2));
        entity.setWarehouseID(cursor.getInt(offset + 3));
        entity.setStaffID(cursor.getInt(offset + 4));
        entity.setApproverID(cursor.getInt(offset + 5));
        entity.setCreateDatetime(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setPurchasingOrderID(cursor.getInt(offset + 7));
        entity.setSn(cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Warehousing entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Warehousing entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Warehousing entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}