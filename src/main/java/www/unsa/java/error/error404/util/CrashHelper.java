package www.unsa.java.error.error404.util;

import java.lang.reflect.Field;

public class CrashHelper {
    public static void crashJvm(String exceptionName) {
        // 先向控制台打印错误信息，模拟崩溃日志
        System.err.println("FATAL ERROR: " + exceptionName);
        System.err.println("This is a simulated JVM crash from Java ERROR 404 mod.");

        // 使用 Unsafe 触发段错误 (SIGSEGV)，必定导致 JVM 崩溃，无法被捕获
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
            // 访问非法地址，立即崩溃
            unsafe.getAddress(0L);
        } catch (Throwable t) {
            // 如果 Unsafe 不可用（例如 Android 环境），降级为强制退出进程
            Runtime.getRuntime().halt(1);
        }
    }
}
