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
 * DAO for table "coupon".
*/
public class CouponDao extends AbstractDao<Coupon, Long> {

    public static final String TABLENAME = "coupon";

    /**
     * Properties of entity Coupon.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property Status = new Property(1, int.class, "status", false, "F_Status");
        public final static Property CardType = new Property(2, int.class, "cardType", false, "F_CardType");
        public final static Property Bonus = new Property(3, int.class, "bonus", false, "F_Bonus");
        public final static Property LeastAmount = new Property(4, double.class, "leastAmount", false, "F_LeastAmount");
        public final static Property ReduceAmount = new Property(5, double.class, "reduceAmount", false, "F_ReduceAmount");
        public final static Property Discount = new Property(6, double.class, "discount", false, "F_Discount");
        public final static Property Title = new Property(7, String.class, "title", false, "F_Title");
        public final static Property Color = new Property(8, String.class, "color", false, "F_Color");
        public final static Property Description = new Property(9, String.class, "description", false, "F_Description");
        public final static Property PersonalLimit = new Property(10, int.class, "personalLimit", false, "F_PersonalLimit");
        public final static Property Type = new Property(11, int.class, "type", false, "F_Type");
        public final static Property BeginTime = new Property(12, String.class, "beginTime", false, "F_BeginTime");
        public final static Property EndTime = new Property(13, String.class, "endTime", false, "F_EndTime");
        public final static Property BeginDateTime = new Property(14, java.util.Date.class, "beginDateTime", false, "F_BeginDateTime");
        public final static Property EndDateTime = new Property(15, java.util.Date.class, "endDateTime", false, "F_EndDateTime");
        public final static Property Quantity = new Property(16, int.class, "quantity", false, "F_Quantity");
        public final static Property RemainingQuantity = new Property(17, int.class, "remainingQuantity", false, "F_RemainingQuantity");
        public final static Property Scope = new Property(18, int.class, "scope", false, "F_Scope");
    }


    public CouponDao(DaoConfig config) {
        super(config);
    }
    
    public CouponDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"coupon\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"F_Status\" INTEGER NOT NULL ," + // 1: status
                "\"F_CardType\" INTEGER NOT NULL ," + // 2: cardType
                "\"F_Bonus\" INTEGER NOT NULL ," + // 3: bonus
                "\"F_LeastAmount\" REAL NOT NULL ," + // 4: leastAmount
                "\"F_ReduceAmount\" REAL NOT NULL ," + // 5: reduceAmount
                "\"F_Discount\" REAL NOT NULL ," + // 6: discount
                "\"F_Title\" TEXT NOT NULL ," + // 7: title
                "\"F_Color\" TEXT NOT NULL ," + // 8: color
                "\"F_Description\" TEXT NOT NULL ," + // 9: description
                "\"F_PersonalLimit\" INTEGER NOT NULL ," + // 10: personalLimit
                "\"F_Type\" INTEGER NOT NULL ," + // 11: type
                "\"F_BeginTime\" TEXT NOT NULL ," + // 12: beginTime
                "\"F_EndTime\" TEXT NOT NULL ," + // 13: endTime
                "\"F_BeginDateTime\" INTEGER NOT NULL ," + // 14: beginDateTime
                "\"F_EndDateTime\" INTEGER NOT NULL ," + // 15: endDateTime
                "\"F_Quantity\" INTEGER NOT NULL ," + // 16: quantity
                "\"F_RemainingQuantity\" INTEGER NOT NULL ," + // 17: remainingQuantity
                "\"F_Scope\" INTEGER NOT NULL );"); // 18: scope
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"coupon\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Coupon entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindLong(3, entity.getCardType());
        stmt.bindLong(4, entity.getBonus());
        stmt.bindDouble(5, entity.getLeastAmount());
        stmt.bindDouble(6, entity.getReduceAmount());
        stmt.bindDouble(7, entity.getDiscount());
        stmt.bindString(8, entity.getTitle());
        stmt.bindString(9, entity.getColor());
        stmt.bindString(10, entity.getDescription());
        stmt.bindLong(11, entity.getPersonalLimit());
        stmt.bindLong(12, entity.getType());
        stmt.bindString(13, entity.getBeginTime());
        stmt.bindString(14, entity.getEndTime());
        stmt.bindLong(15, entity.getBeginDateTime().getTime());
        stmt.bindLong(16, entity.getEndDateTime().getTime());
        stmt.bindLong(17, entity.getQuantity());
        stmt.bindLong(18, entity.getRemainingQuantity());
        stmt.bindLong(19, entity.getScope());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Coupon entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getStatus());
        stmt.bindLong(3, entity.getCardType());
        stmt.bindLong(4, entity.getBonus());
        stmt.bindDouble(5, entity.getLeastAmount());
        stmt.bindDouble(6, entity.getReduceAmount());
        stmt.bindDouble(7, entity.getDiscount());
        stmt.bindString(8, entity.getTitle());
        stmt.bindString(9, entity.getColor());
        stmt.bindString(10, entity.getDescription());
        stmt.bindLong(11, entity.getPersonalLimit());
        stmt.bindLong(12, entity.getType());
        stmt.bindString(13, entity.getBeginTime());
        stmt.bindString(14, entity.getEndTime());
        stmt.bindLong(15, entity.getBeginDateTime().getTime());
        stmt.bindLong(16, entity.getEndDateTime().getTime());
        stmt.bindLong(17, entity.getQuantity());
        stmt.bindLong(18, entity.getRemainingQuantity());
        stmt.bindLong(19, entity.getScope());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Coupon readEntity(Cursor cursor, int offset) {
        Coupon entity = new Coupon( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // status
            cursor.getInt(offset + 2), // cardType
            cursor.getInt(offset + 3), // bonus
            cursor.getDouble(offset + 4), // leastAmount
            cursor.getDouble(offset + 5), // reduceAmount
            cursor.getDouble(offset + 6), // discount
            cursor.getString(offset + 7), // title
            cursor.getString(offset + 8), // color
            cursor.getString(offset + 9), // description
            cursor.getInt(offset + 10), // personalLimit
            cursor.getInt(offset + 11), // type
            cursor.getString(offset + 12), // beginTime
            cursor.getString(offset + 13), // endTime
            new java.util.Date(cursor.getLong(offset + 14)), // beginDateTime
            new java.util.Date(cursor.getLong(offset + 15)), // endDateTime
            cursor.getInt(offset + 16), // quantity
            cursor.getInt(offset + 17), // remainingQuantity
            cursor.getInt(offset + 18) // scope
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Coupon entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStatus(cursor.getInt(offset + 1));
        entity.setCardType(cursor.getInt(offset + 2));
        entity.setBonus(cursor.getInt(offset + 3));
        entity.setLeastAmount(cursor.getDouble(offset + 4));
        entity.setReduceAmount(cursor.getDouble(offset + 5));
        entity.setDiscount(cursor.getDouble(offset + 6));
        entity.setTitle(cursor.getString(offset + 7));
        entity.setColor(cursor.getString(offset + 8));
        entity.setDescription(cursor.getString(offset + 9));
        entity.setPersonalLimit(cursor.getInt(offset + 10));
        entity.setType(cursor.getInt(offset + 11));
        entity.setBeginTime(cursor.getString(offset + 12));
        entity.setEndTime(cursor.getString(offset + 13));
        entity.setBeginDateTime(new java.util.Date(cursor.getLong(offset + 14)));
        entity.setEndDateTime(new java.util.Date(cursor.getLong(offset + 15)));
        entity.setQuantity(cursor.getInt(offset + 16));
        entity.setRemainingQuantity(cursor.getInt(offset + 17));
        entity.setScope(cursor.getInt(offset + 18));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Coupon entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Coupon entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Coupon entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
