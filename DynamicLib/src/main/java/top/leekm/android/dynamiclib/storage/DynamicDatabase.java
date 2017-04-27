package top.leekm.android.dynamiclib.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import top.leekm.android.dynamiclib.utils.FileUtils;
import top.leekm.android.dynamiclib.utils.RunOrThrow;

/**
 * Created by lkm on 2017/4/26.
 */

public class DynamicDatabase {

    private SQLiteDatabase mDatabase;

    private static DynamicDatabase gSharedDB;

    private DynamicDatabase(Context context) {
        mDatabase = context.openOrCreateDatabase("dynamic_bundle", Context.MODE_PRIVATE, null);
    }

    public synchronized static DynamicDatabase sharedInstance(Context context) {
        if (null == gSharedDB) {
            gSharedDB = new DynamicDatabase(context.getApplicationContext());
        }
        return gSharedDB;
    }

    public <T extends Table> T openTable(final Class<? extends Table> tableClazz) {
        final AtomicReference<Table> ref = new AtomicReference<>();
        RunOrThrow.wrapper(new RunOrThrow() {
            @Override
            protected void todo() throws Throwable {
                Table table = tableClazz.newInstance();
                table.attatchToDatabase(mDatabase);
                ref.set(table);
            }
        });
        return (T) ref.get();
    }

    public static synchronized void close() throws IOException {
        FileUtils.close(gSharedDB.mDatabase);
        gSharedDB = null;
    }
}
