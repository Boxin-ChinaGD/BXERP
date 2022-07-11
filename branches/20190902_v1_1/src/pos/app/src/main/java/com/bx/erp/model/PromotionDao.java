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
 * DAO for table "Promotion".
*/
public class PromotionDao extends AbstractDao<Promotion, Long> {

    public static final String TABLENAME = "Promotion";

    /**
     * Properties of entity Promotion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property Type = new Property(1, int.class, "type", false, "F_Type");
        public final static Property Name = new Property(2, String.class, "name", false, "F_Name");
        public final static Property Status = new Property(3, int.class, "status", false, "F_Status");
        public final static Property CreateDatetime = new Property(4, java.util.Date.class, "createDatetime", false, "F_CreateDatetime");
        public final static Property DatetimeStart = new Property(5, java.util.Date.class, "datetimeStart", false, "F_DatetimeStart");
        public final static Property DatetimeEnd = new Property(6, java.util.Date.class, "datetimeEnd", false, "F_DatetimeEnd");
        public final static Property Sn = new Property(7, String.class, "sn", false, "F_SN");
        public final static Property ExcecutionDiscount = new Property(8, double.class, "excecutionDiscount", false, "F_ExcecutionDiscount");
        public final static Property ExcecutionThreshold = new Property(9, double.class, "excecutionThreshold", false, "F_ExcecutionThreshold");
        public final static Property ExcecutionAmount = new Property(10, Double.class, "excecutionAmount", false, "F_ExcecutionAmount");
        public final static Property Scope = new Property(11, int.class, "scope", false, "F_Scope");
        public final static Property Staff = new Property(12, int.class, "staff", false, "F_Staff");
        public final static Property SyncType = new Property(13, String.class, "syncType", false, "F_SyncType");
        public final static Property SyncDatetime = new Property(14, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
    }


    public PromotionDao(DaoConfig config) {
        super(config);
    }
    
    public PromotionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"Promotion\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"F_Type\" INTEGER NOT NULL ," + // 1: type
                "\"F_Name\" TEXT NOT NULL ," + // 2: name
                "\"F_Status\" INTEGER NOT NULL ," + // 3: status
                "\"F_CreateDatetime\" INTEGER," + // 4: createDatetime
                "\"F_DatetimeStart\" INTEGER NOT NULL ," + // 5: datetimeStart
                "\"F_DatetimeEnd\" INTEGER NOT NULL ," + // 6: datetimeEnd
                "\"F_SN\" TEXT NOT NULL ," + // 7: sn
                "\"F_ExcecutionDiscount\" REAL NOT NULL ," + // 8: excecutionDiscount
                "\"F_ExcecutionThreshold\" REAL NOT NULL ," + // 9: excecutionThreshold
                "\"F_ExcecutionAmount\" REAL NOT NULL ," + // 10: excecutionAmount
                "\"F_Scope\" INTEGER NOT NULL ," + // 11: scope
                "\"F_Staff\" INTEGER NOT NULL ," + // 12: staff
                "\"F_SyncType\" TEXT," + // 13: syncType
                "\"F_SyncDatetime\" INTEGER);"); // 14: syncDatetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"Promotion\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Promotion entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getType());
        stmt.bindString(3, entity.getName());
        stmt.bindLong(4, entity.getStatus());
 
        java.util.Date createDatetime = entity.getCreateDatetime();
        if (createDatetime != null) {
            stmt.bindLong(5, createDatetime.getTime());
        }
        stmt.bindLong(6, entity.getDatetimeStart().getTime());
        stmt.bindLong(7, entity.getDatetimeEnd().getTime());
        stmt.bindString(8, entity.getSn());
        stmt.bindDouble(9, entity.getExcecutionDiscount());
        stmt.bindDouble(10, entity.getExcecutionThreshold());
        stmt.bindDouble(11, entity.getExcecutionAmount());
        stmt.bindLong(12, entity.getScope());
        stmt.bindLong(13, entity.getStaff());
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(14, syncType);
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(15, syncDatetime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Promotion entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getType());
        stmt.bindString(3, entity.getName());
        stmt.bindLong(4, entity.getStatus());
 
        java.util.Date createDatetime = entity.getCreateDatetime();
        if (createDatetime != null) {
            stmt.bindLong(5, createDatetime.getTime());
        }
        stmt.bindLong(6, entity.getDatetimeStart().getTime());
        stmt.bindLong(7, entity.getDatetimeEnd().getTime());
        stmt.bindString(8, entity.getSn());
        stmt.bindDouble(9, entity.getExcecutionDiscount());
        stmt.bindDouble(10, entity.getExcecutionThreshold());
        stmt.bindDouble(11, entity.getExcecutionAmount());
        stmt.bindLong(12, entity.getScope());
        stmt.bindLong(13, entity.getStaff());
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(14, syncType);
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(15, syncDatetime.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Promotion readEntity(Cursor cursor, int offset) {
        Promotion entity = new Promotion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // type
            cursor.getString(offset + 2), // name
            cursor.getInt(offset + 3), // status
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // createDatetime
            new java.util.Date(cursor.getLong(offset + 5)), // datetimeStart
            new java.util.Date(cursor.getLong(offset + 6)), // datetimeEnd
            cursor.getString(offset + 7), // sn
            cursor.getDouble(offset + 8), // excecutionDiscount
            cursor.getDouble(offset + 9), // excecutionThreshold
            cursor.getDouble(offset + 10), // excecutionAmount
            cursor.getInt(offset + 11), // scope
            cursor.getInt(offset + 12), // staff
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // syncType
            cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)) // syncDatetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Promotion entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.getInt(offset + 1));
        entity.setName(cursor.getString(offset + 2));
        entity.setStatus(cursor.getInt(offset + 3));
        entity.setCreateDatetime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setDatetimeStart(new java.util.Date(cursor.getLong(offset + 5)));
        entity.setDatetimeEnd(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setSn(cursor.getString(offset + 7));
        entity.setExcecutionDiscount(cursor.getDouble(offset + 8));
        entity.setExcecutionThreshold(cursor.getDouble(offset + 9));
        entity.setExcecutionAmount(cursor.getDouble(offset + 10));
        entity.setScope(cursor.getInt(offset + 11));
        entity.setStaff(cursor.getInt(offset + 12));
        entity.setSyncType(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSyncDatetime(cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Promotion entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Promotion entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Promotion entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
