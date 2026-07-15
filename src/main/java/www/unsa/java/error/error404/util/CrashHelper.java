// Fixed
package www.unsa.java.error.error404.util;

public class CrashHelper {
    public static void crashJvm(String reason) {
        try {
            // 反射获取 Unsafe 并触发 SIGSEGV
            java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
            unsafe.getAddress(0L); // 立即崩溃
        } catch (Exception e) {
            // 降级：抛出严重错误
            throw new RuntimeException("JVM Crash: " + reason, e);
        }
    }

    // 为 Java 1.1 版本错误使用
    public static void crashJvmVersionError() {
        throw new UnsupportedClassVersionError("Java version 8 required but found 17");
    }
}
