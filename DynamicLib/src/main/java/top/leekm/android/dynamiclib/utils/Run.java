package top.leekm.android.dynamiclib.utils;

/**
 * Created by lkm on 2017/4/22.
 */
public abstract class Run implements Runnable {

    @Override
    public void run() {
        try {
            todo();
        } catch (Throwable throwable) {
            onFailed(throwable);
        }
    }

    protected abstract void todo() throws Throwable;

    protected void onFailed(Throwable throwable) {
        throwable.printStackTrace();
    }
}
