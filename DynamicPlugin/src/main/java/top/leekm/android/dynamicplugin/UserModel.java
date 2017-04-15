package top.leekm.android.dynamicplugin;

import top.leekm.android.dynamiclib.ActivitySupportImpl;

/**
 * Created by lkm on 2017/4/15.
 */
public class UserModel extends ActivitySupportImpl {

    public String name = defaultName;
    public String password = defaultPassword;

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";
    }

    private static String defaultName = "";
    private static String defaultPassword = "";

    static {
        defaultName = "leekm";
        defaultPassword = "yjcsxdl";
    }
}