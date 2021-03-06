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
 * DAO for table "purchasingOrderCommodity".
*/
public class PurchasingOrderCommodityDao extends AbstractDao<PurchasingOrderCommodity, Long> {

    public static final String TABLENAME = "purchasingOrderCommodity";

    /**
     * Properties of entity PurchasingOrderCommodity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
        public final static Property PurchasingOrderID = new Property(1, int.class, "purchasingOrderID", false, "F_PurchasingOrderID");
        public final static Property PriceSuggestion = new Property(2, double.class, "priceSuggestion", false, "F_PriceSuggestion");
        public final static Property CommodityID = new Property(3, int.class, "commodityID", false, "F_CommodityID");
        public final static Property CommodityNO = new Property(4, int.class, "commodityNO", false, "F_CommodityNO");
        public final static Property CommodityName = new Property(5, String.class, "commodityName", false, "F_CommodityName");
        public final static Property BarcodeID = new Property(6, int.class, "barcodeID", false, "F_BarcodeID");
        public final static Property PackageUnitID = new Property(7, int.class, "packageUnitID", false, "F_PackageUnitID");
    }


    public PurchasingOrderCommodityDao(DaoConfig config) {
        super(config);
    }
    
    public PurchasingOrderCommodityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"purchasingOrderCommodity\" (" + //
                "\"F_ID\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"F_PurchasingOrderID\" INTEGER NOT NULL ," + // 1: purchasingOrderID
                "\"F_PriceSuggestion\" REAL NOT NULL ," + // 2: priceSuggestion
                "\"F_CommodityID\" INTEGER NOT NULL ," + // 3: commodityID
                "\"F_CommodityNO\" INTEGER NOT NULL ," + // 4: commodityNO
                "\"F_CommodityName\" TEXT NOT NULL ," + // 5: commodityName
                "\"F_BarcodeID\" INTEGER NOT NULL ," + // 6: barcodeID
                "\"F_PackageUnitID\" INTEGER NOT NULL );"); // 7: packageUnitID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"purchasingOrderCommodity\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PurchasingOrderCommodity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPurchasingOrderID());
        stmt.bindDouble(3, entity.getPriceSuggestion());
        stmt.bindLong(4, entity.getCommodityID());
        stmt.bindLong(5, entity.getCommodityNO());
        stmt.bindString(6, entity.getCommodityName());
        stmt.bindLong(7, entity.getBarcodeID());
        stmt.bindLong(8, entity.getPackageUnitID());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PurchasingOrderCommodity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
        stmt.bindLong(2, entity.getPurchasingOrderID());
        stmt.bindDouble(3, entity.getPriceSuggestion());
        stmt.bindLong(4, entity.getCommodityID());
        stmt.bindLong(5, entity.getCommodityNO());
        stmt.bindString(6, entity.getCommodityName());
        stmt.bindLong(7, entity.getBarcodeID());
        stmt.bindLong(8, entity.getPackageUnitID());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PurchasingOrderCommodity readEntity(Cursor cursor, int offset) {
        PurchasingOrderCommodity entity = new PurchasingOrderCommodity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.getInt(offset + 1), // purchasingOrderID
            cursor.getDouble(offset + 2), // priceSuggestion
            cursor.getInt(offset + 3), // commodityID
            cursor.getInt(offset + 4), // commodityNO
            cursor.getString(offset + 5), // commodityName
            cursor.getInt(offset + 6), // barcodeID
            cursor.getInt(offset + 7) // packageUnitID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PurchasingOrderCommodity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPurchasingOrderID(cursor.getInt(offset + 1));
        entity.setPriceSuggestion(cursor.getDouble(offset + 2));
        entity.setCommodityID(cursor.getInt(offset + 3));
        entity.setCommodityNO(cursor.getInt(offset + 4));
        entity.setCommodityName(cursor.getString(offset + 5));
        entity.setBarcodeID(cursor.getInt(offset + 6));
        entity.setPackageUnitID(cursor.getInt(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PurchasingOrderCommodity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PurchasingOrderCommodity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PurchasingOrderCommodity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
