package com.bx.erp.model;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

public class MyOpenHelper extends DaoMaster.DevOpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    /**
     * 升级APP（原先POS已经有了旧的版本）时，SQLite中的数据不会变化，只有程序会被更新。
     * 如果新版本引入了新的表字段，新程序并不会自动创建这个字段，这样会导致出现SQLite操作异常。
     * 为避免此问题，需要在用户升级新版本时，帮用户修改表结构
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            switch (newVersion) {
                case 2:
                    String sql = "alter table Staff add F_roleID INTEGER";
                    db.execSQL(sql);
                    break;
                default:
                    break;
            }
        }
        //db.execSQL后等于是oldVersion升一级
        oldVersion++;
        //升级后还是比最新版低级的话，继续升级
        if (oldVersion < newVersion) {
            //递归
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}