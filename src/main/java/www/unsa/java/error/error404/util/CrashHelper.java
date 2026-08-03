package www.unsa.java.error.error404.util;

import java.lang.reflect.Field;

public class CrashHelper {
    /**
     * 在新线程中执行真实的异常代码（绕过 Minecraft 全局异常处理），
     * 然后通过 Unsafe 触发 SIGSEGV 确保进程必定终止。
     */
    public static void crashJvm(Runnable crashTask) {
        Thread crashThread = new Thread(() -> {
            crashTask.run();
        }, "Java-ERROR-404-Crash");
        crashThread.setUncaughtExceptionHandler((t, e) -> {
            System.err.println("===== Java ERROR 404: CLIENT CRASH =====");
            e.printStackTrace();
        });
        crashThread.start();

        // 等待异常线程执行
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        // Unsafe SIGSEGV 兜底，确保进程必定终止
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
