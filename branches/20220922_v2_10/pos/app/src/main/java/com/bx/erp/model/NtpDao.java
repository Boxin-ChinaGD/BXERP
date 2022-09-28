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
 * DAO for table "NTP".
*/
public class NtpDao extends AbstractDao<Ntp, Void> {

    public static final String TABLENAME = "NTP";

    /**
     * Properties of entity Ntp.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property T1 = new Property(0, long.class, "t1", false, "T1");
        public final static Property T2 = new Property(1, long.class, "t2", false, "T2");
        public final static Property T3 = new Property(2, long.class, "t3", false, "T3");
        public final static Property T4 = new Property(3, long.class, "t4", false, "T4");
    }


    public NtpDao(DaoConfig config) {
        super(config);
    }
    
    public NtpDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NTP\" (" + //
                "\"T1\" INTEGER NOT NULL ," + // 0: t1
                "\"T2\" INTEGER NOT NULL ," + // 1: t2
                "\"T3\" INTEGER NOT NULL ," + // 2: t3
                "\"T4\" INTEGER NOT NULL );"); // 3: t4
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NTP\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Ntp entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getT1());
        stmt.bindLong(2, entity.getT2());
        stmt.bindLong(3, entity.getT3());
        stmt.bindLong(4, entity.getT4());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Ntp entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getT1());
        stmt.bindLong(2, entity.getT2());
        stmt.bindLong(3, entity.getT3());
        stmt.bindLong(4, entity.getT4());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Ntp readEntity(Cursor cursor, int offset) {
        Ntp entity = new Ntp( //
            cursor.getLong(offset + 0), // t1
            cursor.getLong(offset + 1), // t2
            cursor.getLong(offset + 2), // t3
            cursor.getLong(offset + 3) // t4
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Ntp entity, int offset) {
        entity.setT1(cursor.getLong(offset + 0));
        entity.setT2(cursor.getLong(offset + 1));
        entity.setT3(cursor.getLong(offset + 2));
        entity.setT4(cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Ntp entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Ntp entity) {
        return null;
    }

    @Override
    public boolean hasKey(Ntp entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}