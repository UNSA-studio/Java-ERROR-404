package www.unsa.java.error.error404.util;

import java.util.*;

public class CrashHelper {
    public static void crashJvm(String exceptionName) {
        switch (exceptionName) {
            case "NullPointerException":
                Object o = null;
                o.toString();
                break;
            case "ClassCastException":
                Object obj = "string";
                Integer i = (Integer) obj;
                break;
            case "ArrayIndexOutOfBoundsException":
                int[] arr = new int[0];
                int x = arr[1];
                break;
            case "StringIndexOutOfBoundsException":
                "".charAt(0);
                break;
            case "IllegalArgumentException":
                throw new IllegalArgumentException("Simulated");
            case "IllegalStateException":
                throw new IllegalStateException("Simulated");
            case "NumberFormatException":
                Integer.parseInt("not a number");
                break;
            case "ArithmeticException":
                int y = 1 / 0;
                break;
            case "NegativeArraySizeException":
                int[] neg = new int[-1];
                break;
            case "ClassNotFoundException":
                try { Class.forName("non.existent.Class"); }
                catch (ClassNotFoundException e) { throw new RuntimeException(e); }
                break;
            case "NoClassDefFoundError":
                throw new NoClassDefFoundError("Simulated");
            case "NoSuchMethodError":
                throw new NoSuchMethodError("Simulated");
            case "NoSuchFieldError":
                throw new NoSuchFieldError("Simulated");
            case "OutOfMemoryError":
                try { int[] big = new int[Integer.MAX_VALUE]; }
                catch (Throwable t) { throw new OutOfMemoryError("Simulated"); }
                break;
            case "StackOverflowError":
                recursive();
                break;
            case "UnsupportedOperationException":
                throw new UnsupportedOperationException("Simulated");
            case "InterruptedException":
                throw new RuntimeException(new InterruptedException("Simulated"));
            case "ExceptionInInitializerError":
                throw new ExceptionInInitializerError("Simulated");
            case "SecurityException":
                throw new SecurityException("Simulated");
            case "IllegalAccessException":
                try {
                    var f = String.class.getDeclaredField("value");
                    f.setAccessible(true);
                    f.get("test");
                } catch (Exception e) { throw new RuntimeException(e); }
                break;
            case "InstantiationException":
                try { Class.forName("java.lang.Integer").newInstance(); }
                catch (Exception e) { throw new RuntimeException(e); }
                break;
            case "ConcurrentModificationException":
                List<String> list = new ArrayList<>();
                list.add("a");
                for (String s : list) { list.remove(s); }
                break;
            case "NoSuchElementException":
                new ArrayList<>().iterator().next();
                break;
            case "EmptyStackException":
                new Stack<>().pop();
                break;
            case "MissingResourceException":
                throw new RuntimeException(new MissingResourceException("Simulated", "", ""));
            case "InputMismatchException":
                throw new InputMismatchException("Simulated");
            case "IllegalFormatException":
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

    private static void recursive() { recursive(); }
}
