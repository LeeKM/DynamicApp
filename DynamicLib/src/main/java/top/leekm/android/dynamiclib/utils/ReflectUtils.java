package top.leekm.android.dynamiclib.utils;

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
        return method.invoke(null, args);
    }

    public static Object invokeMethod(Object target, String name, Object... args)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = target.getClass().getDeclaredMethod(name, getArgsClass(args));
        return method.invoke(target, args);
    }

    public static Object getStatic(Class<?> clazz, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(null);
    }

    public static void setStatic(Class<?> clazz, String name, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(null, value);
    }

    public static Object getField(Object target, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    public static void setField(Object target, String name, Object value)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
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

}
