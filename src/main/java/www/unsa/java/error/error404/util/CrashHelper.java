package www.unsa.java.error.error404.util;

import java.lang.reflect.Field;

public class CrashHelper {
    public static void crashJvm(String exceptionName) {
        // 在新线程中实际抛出对应异常，绕过 Minecraft 全局异常处理
        Thread crashThread = new Thread(() -> {
            throw new RuntimeException("FATAL ERROR: " + exceptionName + " [Java ERROR 404]");
        }, "Java-ERROR-404-Crash");
        crashThread.setUncaughtExceptionHandler((t, e) -> {
            System.err.println("===== JAVA ERROR 404: CLIENT CRASH DETECTED =====");
            System.err.println("Exception: " + exceptionName);
            System.err.println("This client has been terminated by the Java ERROR 404 mod.");
            e.printStackTrace();
        });
        crashThread.start();

        // 等待异常线程执行
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        // 确保进程必定终止：Unsafe SIGSEGV 兜底
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
            unsafe.getAddress(0L);
        } catch (Throwable t) {
            Runtime.getRuntime().halt(1);
        }
    }
}
