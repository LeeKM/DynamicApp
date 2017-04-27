package top.leekm.android.dynamiclib.utils;

/**
 * Created by lkm on 2017/4/27.
 */

public abstract class RunOrThrow extends Run {
    @Override
    protected void onFailed(Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    public static void wrapper(Run task) {
        task.run();
    }
}
