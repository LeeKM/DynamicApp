package top.leekm.android.dynamiclib;

/**
 * Created by lkm on 2017/4/8.
 */

public class DynamicBundle {

    // bundle的名字
    public String bundleName;

    // bundle文件路径，若空则按照默认规则查找
    public String filePath;

    // bundle安全校验序列
    public String secure;
}
