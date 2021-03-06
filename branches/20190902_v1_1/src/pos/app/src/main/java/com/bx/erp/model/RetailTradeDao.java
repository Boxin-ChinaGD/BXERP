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
 * DAO for table "retailTrade".
*/
public class RetailTradeDao extends AbstractDao<RetailTrade, Long> {

    public static final String TABLENAME = "retailTrade";

    /**
     * Properties of entity RetailTrade.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property VipID = new Property(1, long.class, "vipID", false, "F_VipID");
        public final static Property Sn = new Property(2, String.class, "sn", false, "F_SN");
        public final static Property LocalSN = new Property(3, int.class, "localSN", false, "F_LocalSN");
        public final static Property Pos_ID = new Property(4, int.class, "pos_ID", false, "F_POS_ID");
        public final static Property Logo = new Property(5, String.class, "logo", false, "F_Logo");
        public final static Property SaleDatetime = new Property(6, java.util.Date.class, "saleDatetime", false, "F_SaleDatetime");
        public final static Property StaffID = new Property(7, int.class, "staffID", false, "F_StaffID");
        public final static Property PaymentType = new Property(8, int.class, "paymentType", false, "F_PaymentType");
        public final static Property PaymentAccount = new Property(9, String.class, "paymentAccount", false, "F_PaymentAccount");
        public final static Property Status = new Property(10, int.class, "status", false, "F_Status");
        public final static Property Remark = new Property(11, String.class, "remark", false, "F_Remark");
        public final static Property SourceID = new Property(12, int.class, "sourceID", false, "F_SourceID");
        public final static Property SyncDatetime = new Property(13, java.util.Date.class, "syncDatetime", false, "F_SyncDatetime");
        public final static Property SyncType = new Property(14, String.class, "syncType", false, "F_SyncType");
        public final static Property Amount = new Property(15, double.class, "amount", false, "F_Amount");
        public final static Property AmountCash = new Property(16, double.class, "amountCash", false, "F_AmountCash");
        public final static Property AmountAlipay = new Property(17, double.class, "amountAlipay", false, "F_AmountAlipay");
        public final static Property AmountWeChat = new Property(18, double.class, "amountWeChat", false, "F_AmountWeChat");
        public final static Property Amount1 = new Property(19, double.class, "amount1", false, "F_Amount1");
        public final static Property Amount2 = new Property(20, double.class, "amount2", false, "F_Amount2");
        public final static Property Amount3 = new Property(21, double.class, "amount3", false, "F_Amount3");
        public final static Property Amount4 = new Property(22, double.class, "amount4", false, "F_Amount4");
        public final static Property Amount5 = new Property(23, double.class, "amount5", false, "F_Amount5");
        public final static Property SmallSheetID = new Property(24, int.class, "smallSheetID", false, "F_SmallSheetID");
        public final static Property AliPayOrderSN = new Property(25, String.class, "aliPayOrderSN", false, "F_AliPayOrderSN");
        public final static Property WxOrderSN = new Property(26, String.class, "wxOrderSN", false, "F_WxOrderSN");
        public final static Property WxTradeNO = new Property(27, String.class, "wxTradeNO", false, "F_WxTradeNO");
        public final static Property WxRefundNO = new Property(28, String.class, "wxRefundNO", false, "F_WxRefundNO");
        public final static Property WxRefundDesc = new Property(29, String.class, "wxRefundDesc", false, "F_WxRefundDesc");
        public final static Property WxRefundSubMchID = new Property(30, String.class, "wxRefundSubMchID", false, "F_WxRefundSubMchID");
        public final static Property CouponAmount = new Property(31, double.class, "couponAmount", false, "F_CouponAmount");
        public final static Property ConsumerOpenID = new Property(32, String.class, "consumerOpenID", false, "F_ConsumerOpenID");
    }


    public RetailTradeDao(DaoConfig config) {
        super(config);
    }
    
    public RetailTradeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"retailTrade\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_VipID\" INTEGER NOT NULL ," + // 1: vipID
                "\"F_SN\" TEXT," + // 2: sn
                "\"F_LocalSN\" INTEGER NOT NULL ," + // 3: localSN
                "\"F_POS_ID\" INTEGER NOT NULL ," + // 4: pos_ID
                "\"F_Logo\" TEXT NOT NULL ," + // 5: logo
                "\"F_SaleDatetime\" INTEGER NOT NULL ," + // 6: saleDatetime
                "\"F_StaffID\" INTEGER NOT NULL ," + // 7: staffID
                "\"F_PaymentType\" INTEGER NOT NULL ," + // 8: paymentType
                "\"F_PaymentAccount\" TEXT NOT NULL ," + // 9: paymentAccount
                "\"F_Status\" INTEGER NOT NULL ," + // 10: status
                "\"F_Remark\" TEXT NOT NULL ," + // 11: remark
                "\"F_SourceID\" INTEGER NOT NULL ," + // 12: sourceID
                "\"F_SyncDatetime\" INTEGER," + // 13: syncDatetime
                "\"F_SyncType\" TEXT," + // 14: syncType
                "\"F_Amount\" REAL NOT NULL ," + // 15: amount
                "\"F_AmountCash\" REAL NOT NULL ," + // 16: amountCash
                "\"F_AmountAlipay\" REAL NOT NULL ," + // 17: amountAlipay
                "\"F_AmountWeChat\" REAL NOT NULL ," + // 18: amountWeChat
                "\"F_Amount1\" REAL NOT NULL ," + // 19: amount1
                "\"F_Amount2\" REAL NOT NULL ," + // 20: amount2
                "\"F_Amount3\" REAL NOT NULL ," + // 21: amount3
                "\"F_Amount4\" REAL NOT NULL ," + // 22: amount4
                "\"F_Amount5\" REAL NOT NULL ," + // 23: amount5
                "\"F_SmallSheetID\" INTEGER NOT NULL ," + // 24: smallSheetID
                "\"F_AliPayOrderSN\" TEXT," + // 25: aliPayOrderSN
                "\"F_WxOrderSN\" TEXT," + // 26: wxOrderSN
                "\"F_WxTradeNO\" TEXT," + // 27: wxTradeNO
                "\"F_WxRefundNO\" TEXT," + // 28: wxRefundNO
                "\"F_WxRefundDesc\" TEXT," + // 29: wxRefundDesc
                "\"F_WxRefundSubMchID\" TEXT," + // 30: wxRefundSubMchID
                "\"F_CouponAmount\" REAL NOT NULL ," + // 31: couponAmount
                "\"F_ConsumerOpenID\" TEXT);"); // 32: consumerOpenID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"retailTrade\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RetailTrade entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getVipID());
 
        String sn = entity.getSn();
        if (sn != null) {
            stmt.bindString(3, sn);
        }
        stmt.bindLong(4, entity.getLocalSN());
        stmt.bindLong(5, entity.getPos_ID());
        stmt.bindString(6, entity.getLogo());
        stmt.bindLong(7, entity.getSaleDatetime().getTime());
        stmt.bindLong(8, entity.getStaffID());
        stmt.bindLong(9, entity.getPaymentType());
        stmt.bindString(10, entity.getPaymentAccount());
        stmt.bindLong(11, entity.getStatus());
        stmt.bindString(12, entity.getRemark());
        stmt.bindLong(13, entity.getSourceID());
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(14, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(15, syncType);
        }
        stmt.bindDouble(16, entity.getAmount());
        stmt.bindDouble(17, entity.getAmountCash());
        stmt.bindDouble(18, entity.getAmountAlipay());
        stmt.bindDouble(19, entity.getAmountWeChat());
        stmt.bindDouble(20, entity.getAmount1());
        stmt.bindDouble(21, entity.getAmount2());
        stmt.bindDouble(22, entity.getAmount3());
        stmt.bindDouble(23, entity.getAmount4());
        stmt.bindDouble(24, entity.getAmount5());
        stmt.bindLong(25, entity.getSmallSheetID());
 
        String aliPayOrderSN = entity.getAliPayOrderSN();
        if (aliPayOrderSN != null) {
            stmt.bindString(26, aliPayOrderSN);
        }
 
        String wxOrderSN = entity.getWxOrderSN();
        if (wxOrderSN != null) {
            stmt.bindString(27, wxOrderSN);
        }
 
        String wxTradeNO = entity.getWxTradeNO();
        if (wxTradeNO != null) {
            stmt.bindString(28, wxTradeNO);
        }
 
        String wxRefundNO = entity.getWxRefundNO();
        if (wxRefundNO != null) {
            stmt.bindString(29, wxRefundNO);
        }
 
        String wxRefundDesc = entity.getWxRefundDesc();
        if (wxRefundDesc != null) {
            stmt.bindString(30, wxRefundDesc);
        }
 
        String wxRefundSubMchID = entity.getWxRefundSubMchID();
        if (wxRefundSubMchID != null) {
            stmt.bindString(31, wxRefundSubMchID);
        }
        stmt.bindDouble(32, entity.getCouponAmount());
 
        String consumerOpenID = entity.getConsumerOpenID();
        if (consumerOpenID != null) {
            stmt.bindString(33, consumerOpenID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RetailTrade entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getVipID());
 
        String sn = entity.getSn();
        if (sn != null) {
            stmt.bindString(3, sn);
        }
        stmt.bindLong(4, entity.getLocalSN());
        stmt.bindLong(5, entity.getPos_ID());
        stmt.bindString(6, entity.getLogo());
        stmt.bindLong(7, entity.getSaleDatetime().getTime());
        stmt.bindLong(8, entity.getStaffID());
        stmt.bindLong(9, entity.getPaymentType());
        stmt.bindString(10, entity.getPaymentAccount());
        stmt.bindLong(11, entity.getStatus());
        stmt.bindString(12, entity.getRemark());
        stmt.bindLong(13, entity.getSourceID());
 
        java.util.Date syncDatetime = entity.getSyncDatetime();
        if (syncDatetime != null) {
            stmt.bindLong(14, syncDatetime.getTime());
        }
 
        String syncType = entity.getSyncType();
        if (syncType != null) {
            stmt.bindString(15, syncType);
        }
        stmt.bindDouble(16, entity.getAmount());
        stmt.bindDouble(17, entity.getAmountCash());
        stmt.bindDouble(18, entity.getAmountAlipay());
        stmt.bindDouble(19, entity.getAmountWeChat());
        stmt.bindDouble(20, entity.getAmount1());
        stmt.bindDouble(21, entity.getAmount2());
        stmt.bindDouble(22, entity.getAmount3());
        stmt.bindDouble(23, entity.getAmount4());
        stmt.bindDouble(24, entity.getAmount5());
        stmt.bindLong(25, entity.getSmallSheetID());
 
        String aliPayOrderSN = entity.getAliPayOrderSN();
        if (aliPayOrderSN != null) {
            stmt.bindString(26, aliPayOrderSN);
        }
 
        String wxOrderSN = entity.getWxOrderSN();
        if (wxOrderSN != null) {
            stmt.bindString(27, wxOrderSN);
        }
 
        String wxTradeNO = entity.getWxTradeNO();
        if (wxTradeNO != null) {
            stmt.bindString(28, wxTradeNO);
        }
 
        String wxRefundNO = entity.getWxRefundNO();
        if (wxRefundNO != null) {
            stmt.bindString(29, wxRefundNO);
        }
 
        String wxRefundDesc = entity.getWxRefundDesc();
        if (wxRefundDesc != null) {
            stmt.bindString(30, wxRefundDesc);
        }
 
        String wxRefundSubMchID = entity.getWxRefundSubMchID();
        if (wxRefundSubMchID != null) {
            stmt.bindString(31, wxRefundSubMchID);
        }
        stmt.bindDouble(32, entity.getCouponAmount());
 
        String consumerOpenID = entity.getConsumerOpenID();
        if (consumerOpenID != null) {
            stmt.bindString(33, consumerOpenID);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RetailTrade readEntity(Cursor cursor, int offset) {
        RetailTrade entity = new RetailTrade( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getLong(offset + 1), // vipID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sn
            cursor.getInt(offset + 3), // localSN
            cursor.getInt(offset + 4), // pos_ID
            cursor.getString(offset + 5), // logo
            new java.util.Date(cursor.getLong(offset + 6)), // saleDatetime
            cursor.getInt(offset + 7), // staffID
            cursor.getInt(offset + 8), // paymentType
            cursor.getString(offset + 9), // paymentAccount
            cursor.getInt(offset + 10), // status
            cursor.getString(offset + 11), // remark
            cursor.getInt(offset + 12), // sourceID
            cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)), // syncDatetime
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // syncType
            cursor.getDouble(offset + 15), // amount
            cursor.getDouble(offset + 16), // amountCash
            cursor.getDouble(offset + 17), // amountAlipay
            cursor.getDouble(offset + 18), // amountWeChat
            cursor.getDouble(offset + 19), // amount1
            cursor.getDouble(offset + 20), // amount2
            cursor.getDouble(offset + 21), // amount3
            cursor.getDouble(offset + 22), // amount4
            cursor.getDouble(offset + 23), // amount5
            cursor.getInt(offset + 24), // smallSheetID
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // aliPayOrderSN
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // wxOrderSN
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // wxTradeNO
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // wxRefundNO
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // wxRefundDesc
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // wxRefundSubMchID
            cursor.getDouble(offset + 31), // couponAmount
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32) // consumerOpenID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RetailTrade entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVipID(cursor.getLong(offset + 1));
        entity.setSn(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLocalSN(cursor.getInt(offset + 3));
        entity.setPos_ID(cursor.getInt(offset + 4));
        entity.setLogo(cursor.getString(offset + 5));
        entity.setSaleDatetime(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setStaffID(cursor.getInt(offset + 7));
        entity.setPaymentType(cursor.getInt(offset + 8));
        entity.setPaymentAccount(cursor.getString(offset + 9));
        entity.setStatus(cursor.getInt(offset + 10));
        entity.setRemark(cursor.getString(offset + 11));
        entity.setSourceID(cursor.getInt(offset + 12));
        entity.setSyncDatetime(cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)));
        entity.setSyncType(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setAmount(cursor.getDouble(offset + 15));
        entity.setAmountCash(cursor.getDouble(offset + 16));
        entity.setAmountAlipay(cursor.getDouble(offset + 17));
        entity.setAmountWeChat(cursor.getDouble(offset + 18));
        entity.setAmount1(cursor.getDouble(offset + 19));
        entity.setAmount2(cursor.getDouble(offset + 20));
        entity.setAmount3(cursor.getDouble(offset + 21));
        entity.setAmount4(cursor.getDouble(offset + 22));
        entity.setAmount5(cursor.getDouble(offset + 23));
        entity.setSmallSheetID(cursor.getInt(offset + 24));
        entity.setAliPayOrderSN(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setWxOrderSN(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setWxTradeNO(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setWxRefundNO(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setWxRefundDesc(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setWxRefundSubMchID(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setCouponAmount(cursor.getDouble(offset + 31));
        entity.setConsumerOpenID(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RetailTrade entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RetailTrade entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RetailTrade entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
