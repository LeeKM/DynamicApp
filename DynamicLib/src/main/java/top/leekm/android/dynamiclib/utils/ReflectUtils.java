package top.leekm.android.dynamiclib.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lkm on 2017/4/8.
 */
public class ReflectUtils {

    public static Class<?> loadClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    public static Object invokeStatic(Class<?> clazz, String name, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(name, getArgsClass(args));
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    public static Object invokeMethod(Object target, String name, Object... args)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = target.getClass().getDeclaredMethod(name, getArgsClass(args));
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    public static Object getStatic(Class<?> clazz, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, name);
        return field.get(null);
    }

    public static void setStatic(Class<?> clazz, String name, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, name);
        field.set(null, value);
    }

    public static Object getField(Object target, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(target.getClass(), name);
        return field.get(target);
    }

    public static void setField(Object target, String name, Object value)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = getField(target.getClass(), name);
        field.set(target, value);
    }

    private static Class<?>[] getArgsClass(Object... args) {
        Class<?>[] argsClazz = new Class[null == args ? 0 : args.length];
        if (null != args) {
            for (int i = 0; i < args.length; ++i) {
                argsClazz[i] = args[i].getClass();
            }
        }
        return argsClazz;
    }

    private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        while (clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name + " Not Found in class");
    }

    private static Application sApplication;
    private final static Handler mainThreadHelper = new Handler(Looper.getMainLooper());
    public static Application getApplication() {
        if (null == sApplication) {
            synchronized (mainThreadHelper) {
                try {
                    mainThreadHelper.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sApplication;
    }

    static {
        mainThreadHelper.post(new Run() {
            @Override
            protected void todo() throws Throwable {
                sApplication = (Application) invokeStatic(loadClass("android.app.ActivityThread"),
                        "currentApplication");
                synchronized (mainThreadHelper) {
                    mainThreadHelper.notifyAll();
                }
            }

            @Override
            protected void onFailed(Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}
