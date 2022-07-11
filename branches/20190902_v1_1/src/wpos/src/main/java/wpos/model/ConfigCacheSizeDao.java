//package wpos.model;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteStatement;
//
//import org.greenrobot.greendao.AbstractDao;
//import org.greenrobot.greendao.Property;
//import org.greenrobot.greendao.internal.DaoConfig;
//import org.greenrobot.greendao.database.Database;
//import org.greenrobot.greendao.database.DatabaseStatement;
//
//// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
///**
// * DAO for table "CONFIG_CACHE_SIZE".
//*/
//public class ConfigCacheSizeDao extends AbstractDao<ConfigCacheSize, Long> {
//
//    public static final String TABLENAME = "CONFIG_CACHE_SIZE";
//
//    /**
//     * Properties of entity ConfigCacheSize.<br/>
//     * Can be used for QueryBuilder and for referencing column names.
//     */
//    public static class Properties {
//        public final static Property ID = new Property(0, Long.class, "ID", true, "F_ID");
//        public final static Property Name = new Property(1, String.class, "name", false, "F_Name");
//        public final static Property Value = new Property(2, String.class, "value", false, "F_Value");
//    }
//
//
//    public ConfigCacheSizeDao(DaoConfig config) {
//        super(config);
//    }
//
//    public ConfigCacheSizeDao(DaoConfig config, DaoSession daoSession) {
//        super(config, daoSession);
//    }
//
//    /** Creates the underlying database table. */
//    public static void createTable(Database db, boolean ifNotExists) {
//        String constraint = ifNotExists? "IF NOT EXISTS ": "";
//        db.execSQL("CREATE TABLE " + constraint + "\"CONFIG_CACHE_SIZE\" (" + //
//                "\"F_ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
//                "\"F_Name\" TEXT NOT NULL ," + // 1: name
//                "\"F_Value\" TEXT NOT NULL );"); // 2: value
//    }
//
//    /** Drops the underlying database table. */
//    public static void dropTable(Database db, boolean ifExists) {
//        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONFIG_CACHE_SIZE\"";
//        db.execSQL(sql);
//    }
//
//    @Override
//    protected final void bindValues(DatabaseStatement stmt, ConfigCacheSize entity) {
//        stmt.clearBindings();
//
//        Long ID = entity.getID();
//        if (ID != null) {
//            stmt.bindLong(1, ID);
//        }
//        stmt.bindString(2, entity.getName());
//        stmt.bindString(3, entity.getValue());
//    }
//
//    @Override
//    protected final void bindValues(SQLiteStatement stmt, ConfigCacheSize entity) {
//        stmt.clearBindings();
//
//        Long ID = entity.getID();
//        if (ID != null) {
//            stmt.bindLong(1, ID);
//        }
//        stmt.bindString(2, entity.getName());
//        stmt.bindString(3, entity.getValue());
//    }
//
//    @Override
//    public Long readKey(Cursor cursor, int offset) {
//        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
//    }
//
//    @Override
//    public ConfigCacheSize readEntity(Cursor cursor, int offset) {
//        ConfigCacheSize entity = new ConfigCacheSize( //
//            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
//            cursor.getString(offset + 1), // name
//            cursor.getString(offset + 2) // value
//        );
//        return entity;
//    }
//
//    @Override
//    public void readEntity(Cursor cursor, ConfigCacheSize entity, int offset) {
//        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
//        entity.setName(cursor.getString(offset + 1));
//        entity.setValue(cursor.getString(offset + 2));
//     }
//
//    @Override
//    protected final Long updateKeyAfterInsert(ConfigCacheSize entity, long rowId) {
//        entity.setID(rowId);
//        return rowId;
//    }
//
//    @Override
//    public Long getKey(ConfigCacheSize entity) {
//        if(entity != null) {
//            return entity.getID();
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean hasKey(ConfigCacheSize entity) {
//        return entity.getID() != null;
//    }
//
//    @Override
//    protected final boolean isEntityUpdateable() {
//        return true;
//    }
//
//}
