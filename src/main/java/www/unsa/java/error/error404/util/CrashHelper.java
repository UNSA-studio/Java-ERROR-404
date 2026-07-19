package www.unsa.java.error.error404.util;

public class CrashHelper {
    public static void crashJvm(String exceptionName) {
        // 真正执行会导致对应异常的操作，使 JVM 崩溃
        switch (exceptionName) {
            case "NullPointerException":
                Object o = null;
                o.toString(); // 必定 NPE
                break;
            case "ClassCastException":
                Object obj = "string";
                Integer i = (Integer) obj; // ClassCastException
                break;
            case "ArrayIndexOutOfBoundsException":
                int[] arr = new int[0];
                int x = arr[1]; // 越界
                break;
            case "StringIndexOutOfBoundsException":
                "".charAt(0); // 越界
                break;
            case "IllegalArgumentException":
                throw new IllegalArgumentException("Simulated");
            case "IllegalStateException":
                throw new IllegalStateException("Simulated");
            case "NumberFormatException":
                Integer.parseInt("not a number"); // NumberFormatException
                break;
            case "ArithmeticException":
                int y = 1 / 0; // 除以零
                break;
            case "NegativeArraySizeException":
                int[] neg = new int[-1]; // 负大小数组
                break;
            case "ClassNotFoundException":
                Class.forName("non.existent.Class"); // 类找不到
                break;
            case "NoClassDefFoundError":
                // 模拟类存在但运行时定义丢失（较难，使用错误替代）
                throw new NoClassDefFoundError("Simulated");
            case "NoSuchMethodError":
                throw new NoSuchMethodError("Simulated");
            case "NoSuchFieldError":
                throw new NoSuchFieldError("Simulated");
            case "OutOfMemoryError":
                // 尝试分配巨大数组触发 OOM（可能真 OOM）
                int[] big = new int[Integer.MAX_VALUE];
                break;
            case "StackOverflowError":
                recursive(); // 无限递归
                break;
            case "UnsupportedOperationException":
                throw new UnsupportedOperationException("Simulated");
            case "InterruptedException":
                Thread.currentThread().interrupt();
                throw new RuntimeException(new InterruptedException("Simulated"));
            case "ExceptionInInitializerError":
                throw new ExceptionInInitializerError("Simulated");
            case "SecurityException":
                throw new SecurityException("Simulated");
            case "IllegalAccessException":
                // 反射访问私有成员
                try {
                    var f = String.class.getDeclaredField("value");
                    f.setAccessible(true); // 可能被禁止
                    f.get("test");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "InstantiationException":
                try {
                    Class.forName("java.lang.Integer").newInstance(); // 无默认构造
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "ConcurrentModificationException":
                java.util.List<String> list = new java.util.ArrayList<>();
                list.add("a");
                for (String s : list) {
                    list.remove(s); // 并发修改
                }
                break;
            case "NoSuchElementException":
                new java.util.ArrayList<>().iterator().next(); // 无元素
                break;
            case "EmptyStackException":
                new java.util.Stack<>().pop(); // 空栈
                break;
            case "MissingResourceException":
                throw new RuntimeException(new java.util.MissingResourceException("Simulated", "", ""));
            case "InputMismatchException":
                throw new java.util.InputMismatchException("Simulated");
            case "IllegalFormatException":
                // 使用错误的格式化
                String.format("%d", "string");
                break;
            case "InvalidPropertiesFormatException":
                throw new RuntimeException(new java.util.InvalidPropertiesFormatException("Simulated"));
            case "IOException":
                throw new RuntimeException(new java.io.IOException("Simulated"));
            case "FileNotFoundException":
                throw new RuntimeException(new java.io.FileNotFoundException("Simulated"));
            case "EOFException":
                throw new RuntimeException(new java.io.EOFException("Simulated"));
            case "UTFDataFormatException":
                throw new RuntimeException(new java.io.UTFDataFormatException("Simulated"));
            case "NotSerializableException":
                throw new RuntimeException(new java.io.NotSerializableException("Simulated"));
            case "StreamCorruptedException":
                throw new RuntimeException(new java.io.StreamCorruptedException("Simulated"));
            case "InterruptedIOException":
                throw new RuntimeException(new java.io.InterruptedIOException("Simulated"));
            case "BufferOverflowException":
                throw new java.nio.BufferOverflowException();
            case "BufferUnderflowException":
                throw new java.nio.BufferUnderflowException();
            case "ReadOnlyBufferException":
                throw new java.nio.ReadOnlyBufferException();
            case "FileSystemException":
                throw new RuntimeException(new java.nio.file.FileSystemException("Simulated"));
            case "ClosedChannelException":
                throw new RuntimeException(new java.nio.channels.ClosedChannelException());
            case "ClosedFileSystemException":
                throw new RuntimeException(new java.nio.file.ClosedFileSystemException());
            case "SocketException":
                throw new RuntimeException(new java.net.SocketException("Simulated"));
            case "UnknownHostException":
                throw new RuntimeException(new java.net.UnknownHostException("Simulated"));
            case "ConnectException":
                throw new RuntimeException(new java.net.ConnectException("Simulated"));
            case "BindException":
                throw new RuntimeException(new java.net.BindException("Simulated"));
            case "MalformedURLException":
                throw new RuntimeException(new java.net.MalformedURLException("Simulated"));
            default:
                throw new RuntimeException("Simulated " + exceptionName);
        }
    }

    private static void recursive() {
        recursive();
    }
}
