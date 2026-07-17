package www.unsa.java.error.error404.util;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class CrashHelper {
    /**
     * 根据异常名称抛出真实的 Java 异常，导致游戏崩溃并生成对应崩溃报告。
     */
    public static void crashJvm(String exceptionName) {
        // 调用前已确保在服务端/客户端均可，直接抛出对应异常
        switch (exceptionName) {
            case "NullPointerException": throw new NullPointerException("Simulated NPE from Java ERROR 404");
            case "ClassCastException": throw new ClassCastException("Simulated ClassCastException from Java ERROR 404");
            case "ArrayIndexOutOfBoundsException": throw new ArrayIndexOutOfBoundsException("Simulated ArrayIndexOutOfBoundsException from Java ERROR 404");
            case "StringIndexOutOfBoundsException": throw new StringIndexOutOfBoundsException("Simulated StringIndexOutOfBoundsException from Java ERROR 404");
            case "IllegalArgumentException": throw new IllegalArgumentException("Simulated IllegalArgumentException from Java ERROR 404");
            case "IllegalStateException": throw new IllegalStateException("Simulated IllegalStateException from Java ERROR 404");
            case "NumberFormatException": throw new NumberFormatException("Simulated NumberFormatException from Java ERROR 404");
            case "ArithmeticException": throw new ArithmeticException("Simulated ArithmeticException from Java ERROR 404");
            case "NegativeArraySizeException": throw new NegativeArraySizeException("Simulated NegativeArraySizeException from Java ERROR 404");
            case "ClassNotFoundException": throw new RuntimeException(new ClassNotFoundException("Simulated ClassNotFoundException from Java ERROR 404"));
            case "NoClassDefFoundError": throw new NoClassDefFoundError("Simulated NoClassDefFoundError from Java ERROR 404");
            case "NoSuchMethodError": throw new NoSuchMethodError("Simulated NoSuchMethodError from Java ERROR 404");
            case "NoSuchFieldError": throw new NoSuchFieldError("Simulated NoSuchFieldError from Java ERROR 404");
            case "OutOfMemoryError": throw new OutOfMemoryError("Simulated OutOfMemoryError from Java ERROR 404");
            case "StackOverflowError": throw new StackOverflowError("Simulated StackOverflowError from Java ERROR 404");
            case "UnsupportedOperationException": throw new UnsupportedOperationException("Simulated UnsupportedOperationException from Java ERROR 404");
            case "InterruptedException": throw new RuntimeException(new InterruptedException("Simulated InterruptedException from Java ERROR 404"));
            case "ExceptionInInitializerError": throw new ExceptionInInitializerError("Simulated ExceptionInInitializerError from Java ERROR 404");
            case "SecurityException": throw new SecurityException("Simulated SecurityException from Java ERROR 404");
            case "IllegalAccessException": throw new RuntimeException(new IllegalAccessException("Simulated IllegalAccessException from Java ERROR 404"));
            case "InstantiationException": throw new RuntimeException(new InstantiationException("Simulated InstantiationException from Java ERROR 404"));
            case "ConcurrentModificationException": throw new ConcurrentModificationException("Simulated ConcurrentModificationException from Java ERROR 404");
            case "NoSuchElementException": throw new NoSuchElementException("Simulated NoSuchElementException from Java ERROR 404");
            case "EmptyStackException": throw new RuntimeException(new EmptyStackException());
            case "MissingResourceException": throw new RuntimeException(new MissingResourceException("Missing resource", "", ""));
            case "InputMismatchException": throw new InputMismatchException("Simulated InputMismatchException from Java ERROR 404");
            case "IllegalFormatException": throw new IllegalFormatException("Simulated IllegalFormatException from Java ERROR 404");
            case "InvalidPropertiesFormatException": throw new RuntimeException(new InvalidPropertiesFormatException("Simulated error"));
            case "IOException": throw new RuntimeException(new IOException("Simulated IOException from Java ERROR 404"));
            case "FileNotFoundException": throw new RuntimeException(new FileNotFoundException("Simulated FileNotFoundException from Java ERROR 404"));
            case "EOFException": throw new RuntimeException(new EOFException("Simulated EOFException from Java ERROR 404"));
            case "UTFDataFormatException": throw new RuntimeException(new UTFDataFormatException("Simulated UTFDataFormatException from Java ERROR 404"));
            case "NotSerializableException": throw new RuntimeException(new NotSerializableException("Simulated NotSerializableException from Java ERROR 404"));
            case "StreamCorruptedException": throw new RuntimeException(new StreamCorruptedException("Simulated StreamCorruptedException from Java ERROR 404"));
            case "InterruptedIOException": throw new RuntimeException(new InterruptedIOException("Simulated InterruptedIOException from Java ERROR 404"));
            case "BufferOverflowException": throw new BufferOverflowException();
            case "BufferUnderflowException": throw new BufferUnderflowException();
            case "ReadOnlyBufferException": throw new ReadOnlyBufferException();
            case "FileSystemException": throw new RuntimeException(new FileSystemException("Simulated FileSystemException from Java ERROR 404"));
            case "ClosedChannelException": throw new RuntimeException(new ClosedChannelException());
            case "ClosedFileSystemException": throw new RuntimeException(new ClosedFileSystemException());
            case "SocketException": throw new RuntimeException(new SocketException("Simulated SocketException from Java ERROR 404"));
            case "UnknownHostException": throw new RuntimeException(new UnknownHostException("Simulated UnknownHostException from Java ERROR 404"));
            case "ConnectException": throw new RuntimeException(new ConnectException("Simulated ConnectException from Java ERROR 404"));
            case "BindException": throw new RuntimeException(new BindException("Simulated BindException from Java ERROR 404"));
            case "MalformedURLException": throw new RuntimeException(new MalformedURLException("Simulated MalformedURLException from Java ERROR 404"));
            default:
                throw new RuntimeException("Simulated " + exceptionName + " from Java ERROR 404");
        }
    }
}
