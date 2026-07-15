package www.unsa.java.error.error404.util;

public class CrashHelper {
    public static void crashJvm(String reason) {
        try {
            java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
            unsafe.getAddress(0L);
        } catch (Exception e) {
            throw new RuntimeException("JVM Crash: " + reason, e);
        }
    }
}
