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
 * DAO for table "T_BXConfigGeneral".
*/
public class BXConfigGeneralDao extends AbstractDao<BXConfigGeneral, Long> {

    public static final String TABLENAME = "T_BXConfigGeneral";

    /**
     * Properties of entity BXConfigGeneral.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "F_Name");
        public final static Property Value = new Property(2, String.class, "value", false, "F_Value");
        public final static Property SyncDatetime = new Property(3, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
    }


    public BXConfigGeneralDao(DaoConfig config) {
        super(config);
    }
    
    public BXConfigGeneralDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"T_BXConfigGeneral\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_Name\" TEXT NOT NULL ," + // 1: name
                "\"F_Value\" TEXT NOT NULL ," + // 2: value
                "\"F_SyncDatetime\" INTEGER NOT NULL );"); // 3: syncDatetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"T_BXConfigGeneral\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BXConfigGeneral entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getValue());
        stmt.bindLong(4, entity.getSyncDatetime().getTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BXConfigGeneral entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getValue());
        stmt.bindLong(4, entity.getSyncDatetime().getTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BXConfigGeneral readEntity(Cursor cursor, int offset) {
        BXConfigGeneral entity = new BXConfigGeneral( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // value
            new java.util.Date(cursor.getLong(offset + 3)) // syncDatetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BXConfigGeneral entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setValue(cursor.getString(offset + 2));
        entity.setSyncDatetime(new java.util.Date(cursor.getLong(offset + 3)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BXConfigGeneral entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BXConfigGeneral entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BXConfigGeneral entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}