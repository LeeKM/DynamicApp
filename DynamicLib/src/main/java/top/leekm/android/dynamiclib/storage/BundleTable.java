package top.leekm.android.dynamiclib.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import top.leekm.android.dynamiclib.DynamicBundle;
import top.leekm.android.dynamiclib.utils.FileUtils;

/**
 * Created by lkm on 2017/4/27.
 */
public class BundleTable extends Table {

    @Override
    protected void onTableOpen(SQLiteDatabase sharedDB) {
        sharedDB.execSQL("CREATE TABLE IF NOT EXISTS bundle_table(" +
                "name VARCHAR(32) UNIQUE NOT NULL," +
                "file_path VARCHAR(255) NOT NULL," +
                "secure TEXT);");
    }

    public void deleteBundleByName(String bundleName) {
        String query = "DELETE FROM bundle_table WHERE name=?;";
        sharedDB.execSQL(query, new Object[]{bundleName});
    }

    public boolean isBundleRegisted(DynamicBundle bundle) {
        String query = "SELECT COUNT(*) FROM bundle_table WHERE name=? AND file_path=? AND secure=?;";
        return queryExist(query, new String[]{bundle.bundleName, bundle.filePath, bundle.secure});
    }

    public void registBundle(DynamicBundle bundle) {
        String query = "INSERT INTO bundle_table(name, file_path, secure) VALUES(?,?,?);";
        sharedDB.execSQL(query, new Object[]{bundle.bundleName, bundle.filePath, bundle.secure});
    }

    public boolean checkBundleName(String bundleName) {
        String query = "SELECT COUNT(*) FROM bundle_table WHERE name=?;";
        return queryExist(query, new String[]{bundleName});
    }

    private boolean queryExist(String query, String[] args) {
        Cursor cursor = sharedDB.rawQuery(query, args);
        boolean exist = false;
        if (null != cursor && cursor.moveToNext()) {
            exist = cursor.getInt(cursor.getColumnIndex("COUNT(*)")) >= 1;
        }
        FileUtils.close(cursor);
        return exist;
    }
}
