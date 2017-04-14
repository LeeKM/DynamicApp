package top.leekm.android.dynamiclib;

/**
 * Created by lkm on 2017/4/3.
 */

public class DynamicSDK {

    private static DynamicSyncCenter defaultCenter = new DynamicSyncCenter();

    public static DynamicSyncCenter defaultSyncCenter() {
        return defaultCenter;
    }

}
