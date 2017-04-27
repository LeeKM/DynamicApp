package top.leekm.android.dynamiclib.storage;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lkm on 2017/4/27.
 */

public abstract class Table {

    protected SQLiteDatabase sharedDB;

    public final void attatchToDatabase(SQLiteDatabase db) {
        this.sharedDB = db;
        this.onTableOpen(sharedDB);
    }

    protected abstract void onTableOpen(SQLiteDatabase sharedDB);

}
