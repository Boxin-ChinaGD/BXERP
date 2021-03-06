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
 * DAO for table "retailTradeAggregation".
*/
public class RetailTradeAggregationDao extends AbstractDao<RetailTradeAggregation, Long> {

    public static final String TABLENAME = "retailTradeAggregation";

    /**
     * Properties of entity RetailTradeAggregation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property StaffID = new Property(1, int.class, "staffID", false, "F_StaffID");
        public final static Property PosID = new Property(2, int.class, "posID", false, "F_PosID");
        public final static Property WorkTimeStart = new Property(3, java.util.Date.class, "workTimeStart", false, "F_WorkTimeStart");
        public final static Property WorkTimeEnd = new Property(4, java.util.Date.class, "workTimeEnd", false, "F_WorkTimeEnd");
        public final static Property TradeNO = new Property(5, int.class, "tradeNO", false, "F_TradeNO");
        public final static Property Amount = new Property(6, double.class, "amount", false, "F_Amount");
        public final static Property ReserveAmount = new Property(7, double.class, "reserveAmount", false, "F_ReserveAmount");
        public final static Property CashAmount = new Property(8, double.class, "cashAmount", false, "F_CashAmount");
        public final static Property WechatAmount = new Property(9, double.class, "wechatAmount", false, "F_WechatAmount");
        public final static Property AlipayAmount = new Property(10, double.class, "alipayAmount", false, "F_AlipayAmount");
        public final static Property Amount1 = new Property(11, double.class, "amount1", false, "F_Amount1");
        public final static Property Amount2 = new Property(12, double.class, "amount2", false, "F_Amount2");
        public final static Property Amount3 = new Property(13, double.class, "amount3", false, "F_Amount3");
        public final static Property Amount4 = new Property(14, double.class, "amount4", false, "F_Amount4");
        public final static Property Amount5 = new Property(15, double.class, "amount5", false, "F_Amount5");
        public final static Property UploadDateTime = new Property(16, java.util.Date.class, "uploadDateTime", false, "F_UploadDateTime");
        public final static Property SyncDatetime = new Property(17, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
        public final static Property SyncType = new Property(18, String.class, "syncType", false, "F_SyncType");
    }


    public RetailTradeAggregationDao(DaoConfig config) {
        super(config);
    }
    
    public RetailTradeAggregationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"retailTradeAggregation\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_StaffID\" INTEGER NOT NULL ," + // 1: staffID
                "\"F_PosID\" INTEGER NOT NULL ," + // 2: posID
                "\"F_WorkTimeStart\" INTEGER NOT NULL ," + // 3: workTimeStart
                "\"F_WorkTimeEnd\" INTEGER," + // 4: workTimeEnd
                "\"F_TradeNO\" INTEGER NOT NULL ," + // 5: tradeNO
                "\"F_Amount\" REAL NOT NULL ," + // 6: amount
                "\"F_ReserveAmount\" REAL NOT NULL ," + // 7: reserveAmount
                "\"F_CashAmount\" REAL NOT NULL ," + // 8: cashAmount
                "\"F_WechatAmount\" REAL NOT NULL ," + // 9: wechatAmount
                "\"F_AlipayAmount\" REAL NOT NULL ," + // 10: alipayAmount
                "\"F_Amount1\" REAL NOT NULL ," + // 11: amount1
                "\"F_Amount2\" REAL NOT NULL ," + // 12: amount2
                "\"F_Amount3\" REAL NOT NULL ," + // 13: amount3
                "\"F_Amount4\" REAL NOT NULL ," + // 14: amount4
                "\"F_Amount5\" REAL NOT NULL ," + // 15: amount5
                "\"F_UploadDateTime\" INTEGER," + // 16: uploadDateTime
                "\"F_SyncDatetime\" INTEGER," + // 17: syncDatetime
                "\"F_SyncType\" TEXT);"); // 18: syncType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"retailTradeAggregation\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RetailTradeAggregation entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStaffID());
        stmt.bindLong(3, entity.getPosID());
        stmt.bindLong(4, entity.getWorkTimeStart().getTime());
 
        java.util.Date workTimeEnd = entity.getWorkTimeEnd();
        if (workTimeEnd != null) {
            stmt.bindLong(5, workTimeEnd.getTime());
        }
        stmt.bindLong(6, entity.getTradeNO());
        stmt.bindDouble(7, entity.getAmount());
        stmt.bindDouble(8, entity.getReserveAmount());
        stmt.bindDouble(9, entity.getCashAmount());
        stmt.bindDouble(10, entity.getWechatAmount());
        stmt.bindDouble(11, entity.getAlipayAmount());
        stmt.bindDouble(12, entity.getAmount1());
        stmt.bindDouble(13, entity.getAmount2());
        stmt.bindDouble(14, entity.getAmount3());
        stmt.bindDouble(15, entity.getAmount4());
        stmt.bindDouble(16, entity.getAmount5());
 
        java.util.Date uploadDateTime = entity.getUploadDateTime();
        if (uploadDateTime != null) {
            stmt.bindLong(17, uploadDateTime.getTime());
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(18, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(19, syncType);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RetailTradeAggregation entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStaffID());
        stmt.bindLong(3, entity.getPosID());
        stmt.bindLong(4, entity.getWorkTimeStart().getTime());
 
        java.util.Date workTimeEnd = entity.getWorkTimeEnd();
        if (workTimeEnd != null) {
            stmt.bindLong(5, workTimeEnd.getTime());
        }
        stmt.bindLong(6, entity.getTradeNO());
        stmt.bindDouble(7, entity.getAmount());
        stmt.bindDouble(8, entity.getReserveAmount());
        stmt.bindDouble(9, entity.getCashAmount());
        stmt.bindDouble(10, entity.getWechatAmount());
        stmt.bindDouble(11, entity.getAlipayAmount());
        stmt.bindDouble(12, entity.getAmount1());
        stmt.bindDouble(13, entity.getAmount2());
        stmt.bindDouble(14, entity.getAmount3());
        stmt.bindDouble(15, entity.getAmount4());
        stmt.bindDouble(16, entity.getAmount5());
 
        java.util.Date uploadDateTime = entity.getUploadDateTime();
        if (uploadDateTime != null) {
            stmt.bindLong(17, uploadDateTime.getTime());
        }
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(18, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(19, syncType);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RetailTradeAggregation readEntity(Cursor cursor, int offset) {
        RetailTradeAggregation entity = new RetailTradeAggregation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // staffID
            cursor.getInt(offset + 2), // posID
            new java.util.Date(cursor.getLong(offset + 3)), // workTimeStart
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // workTimeEnd
            cursor.getInt(offset + 5), // tradeNO
            cursor.getDouble(offset + 6), // amount
            cursor.getDouble(offset + 7), // reserveAmount
            cursor.getDouble(offset + 8), // cashAmount
            cursor.getDouble(offset + 9), // wechatAmount
            cursor.getDouble(offset + 10), // alipayAmount
            cursor.getDouble(offset + 11), // amount1
            cursor.getDouble(offset + 12), // amount2
            cursor.getDouble(offset + 13), // amount3
            cursor.getDouble(offset + 14), // amount4
            cursor.getDouble(offset + 15), // amount5
            cursor.isNull(offset + 16) ? null : new java.util.Date(cursor.getLong(offset + 16)), // uploadDateTime
            cursor.isNull(offset + 17) ? null : new java.util.Date(cursor.getLong(offset + 17)), // syncDatetime
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18) // syncType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RetailTradeAggregation entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStaffID(cursor.getInt(offset + 1));
        entity.setPosID(cursor.getInt(offset + 2));
        entity.setWorkTimeStart(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setWorkTimeEnd(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setTradeNO(cursor.getInt(offset + 5));
        entity.setAmount(cursor.getDouble(offset + 6));
        entity.setReserveAmount(cursor.getDouble(offset + 7));
        entity.setCashAmount(cursor.getDouble(offset + 8));
        entity.setWechatAmount(cursor.getDouble(offset + 9));
        entity.setAlipayAmount(cursor.getDouble(offset + 10));
        entity.setAmount1(cursor.getDouble(offset + 11));
        entity.setAmount2(cursor.getDouble(offset + 12));
        entity.setAmount3(cursor.getDouble(offset + 13));
        entity.setAmount4(cursor.getDouble(offset + 14));
        entity.setAmount5(cursor.getDouble(offset + 15));
        entity.setUploadDateTime(cursor.isNull(offset + 16) ? null : new java.util.Date(cursor.getLong(offset + 16)));
        entity.setSyncDatetime(cursor.isNull(offset + 17) ? null : new java.util.Date(cursor.getLong(offset + 17)));
        entity.setSyncType(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RetailTradeAggregation entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RetailTradeAggregation entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RetailTradeAggregation entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
