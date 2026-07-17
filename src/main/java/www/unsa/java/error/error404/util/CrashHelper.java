package www.unsa.java.error.error404.util;

public class CrashHelper {
    /**
     * 真实 JVM 崩溃：直接退出进程并打印异常名
     */
    public static void crashJvm(String exceptionName) {
        // 向标准错误流输出崩溃原因，模拟 JVM 崩溃日志
        System.err.println("FATAL ERROR: " + exceptionName);
        System.err.println("This is a simulated JVM crash from Java ERROR 404 mod.");
        // 强制退出，状态码 1 表示异常退出
        System.exit(1);
    }
}
