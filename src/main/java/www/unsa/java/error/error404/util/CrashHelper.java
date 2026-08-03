package www.unsa.java.error.error404.util;

import java.lang.reflect.Field;

public class CrashHelper {
    public static void crashJvm(Runnable crashTask) {
        Thread crashThread = new Thread(() -> {
            crashTask.run();
        }, "pool-1-thread-1");
        crashThread.setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
        });
        crashThread.start();

        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

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
