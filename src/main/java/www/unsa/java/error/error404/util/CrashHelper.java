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
     * 抛出 Error 导致 JVM 致命崩溃，生成崩溃报告。
     */
    public static void crashJvm(String exceptionName) {
        // 每个异常都包装成 Error 抛出，确保进程终止
        switch (exceptionName) {
            case "NullPointerException": throw new Error(new NullPointerException("Simulated NPE from Java ERROR 404"));
            case "ClassCastException": throw new Error(new ClassCastException("Simulated ClassCastException from Java ERROR 404"));
            case "ArrayIndexOutOfBoundsException": throw new Error(new ArrayIndexOutOfBoundsException("Simulated ArrayIndexOutOfBoundsException from Java ERROR 404"));
            case "StringIndexOutOfBoundsException": throw new Error(new StringIndexOutOfBoundsException("Simulated StringIndexOutOfBoundsException from Java ERROR 404"));
            case "IllegalArgumentException": throw new Error(new IllegalArgumentException("Simulated IllegalArgumentException from Java ERROR 404"));
            case "IllegalStateException": throw new Error(new IllegalStateException("Simulated IllegalStateException from Java ERROR 404"));
            case "NumberFormatException": throw new Error(new NumberFormatException("Simulated NumberFormatException from Java ERROR 404"));
            case "ArithmeticException": throw new Error(new ArithmeticException("Simulated ArithmeticException from Java ERROR 404"));
            case "NegativeArraySizeException": throw new Error(new NegativeArraySizeException("Simulated NegativeArraySizeException from Java ERROR 404"));
            case "ClassNotFoundException": throw new Error(new ClassNotFoundException("Simulated ClassNotFoundException from Java ERROR 404"));
            case "NoClassDefFoundError": throw new Error(new NoClassDefFoundError("Simulated NoClassDefFoundError from Java ERROR 404"));
            case "NoSuchMethodError": throw new Error(new NoSuchMethodError("Simulated NoSuchMethodError from Java ERROR 404"));
            case "NoSuchFieldError": throw new Error(new NoSuchFieldError("Simulated NoSuchFieldError from Java ERROR 404"));
            case "OutOfMemoryError": throw new OutOfMemoryError("Simulated OutOfMemoryError from Java ERROR 404");
            case "StackOverflowError": throw new StackOverflowError("Simulated StackOverflowError from Java ERROR 404");
            case "UnsupportedOperationException": throw new Error(new UnsupportedOperationException("Simulated UnsupportedOperationException from Java ERROR 404"));
            case "InterruptedException": throw new Error(new InterruptedException("Simulated InterruptedException from Java ERROR 404"));
            case "ExceptionInInitializerError": throw new ExceptionInInitializerError("Simulated ExceptionInInitializerError from Java ERROR 404");
            case "SecurityException": throw new Error(new SecurityException("Simulated SecurityException from Java ERROR 404"));
            case "IllegalAccessException": throw new Error(new IllegalAccessException("Simulated IllegalAccessException from Java ERROR 404"));
            case "InstantiationException": throw new Error(new InstantiationException("Simulated InstantiationException from Java ERROR 404"));
            case "ConcurrentModificationException": throw new Error(new ConcurrentModificationException("Simulated ConcurrentModificationException from Java ERROR 404"));
            case "NoSuchElementException": throw new Error(new NoSuchElementException("Simulated NoSuchElementException from Java ERROR 404"));
            case "EmptyStackException": throw new Error(new EmptyStackException());
            case "MissingResourceException": throw new Error(new MissingResourceException("Missing resource", "", ""));
            case "InputMismatchException": throw new Error(new InputMismatchException("Simulated InputMismatchException from Java ERROR 404"));
            case "IllegalFormatException": throw new Error(new RuntimeException("IllegalFormatException: Simulated from Java ERROR 404"));
            case "InvalidPropertiesFormatException": throw new Error(new InvalidPropertiesFormatException("Simulated error"));
            case "IOException": throw new Error(new IOException("Simulated IOException from Java ERROR 404"));
            case "FileNotFoundException": throw new Error(new FileNotFoundException("Simulated FileNotFoundException from Java ERROR 404"));
            case "EOFException": throw new Error(new EOFException("Simulated EOFException from Java ERROR 404"));
            case "UTFDataFormatException": throw new Error(new UTFDataFormatException("Simulated UTFDataFormatException from Java ERROR 404"));
            case "NotSerializableException": throw new Error(new NotSerializableException("Simulated NotSerializableException from Java ERROR 404"));
            case "StreamCorruptedException": throw new Error(new StreamCorruptedException("Simulated StreamCorruptedException from Java ERROR 404"));
            case "InterruptedIOException": throw new Error(new InterruptedIOException("Simulated InterruptedIOException from Java ERROR 404"));
            case "BufferOverflowException": throw new Error(new BufferOverflowException());
            case "BufferUnderflowException": throw new Error(new BufferUnderflowException());
            case "ReadOnlyBufferException": throw new Error(new ReadOnlyBufferException());
            case "FileSystemException": throw new Error(new FileSystemException("Simulated FileSystemException from Java ERROR 404"));
            case "ClosedChannelException": throw new Error(new ClosedChannelException());
            case "ClosedFileSystemException": throw new Error(new ClosedFileSystemException());
            case "SocketException": throw new Error(new SocketException("Simulated SocketException from Java ERROR 404"));
            case "UnknownHostException": throw new Error(new UnknownHostException("Simulated UnknownHostException from Java ERROR 404"));
            case "ConnectException": throw new Error(new ConnectException("Simulated ConnectException from Java ERROR 404"));
            case "BindException": throw new Error(new BindException("Simulated BindException from Java ERROR 404"));
            case "MalformedURLException": throw new Error(new MalformedURLException("Simulated MalformedURLException from Java ERROR 404"));
            default:
                throw new Error("Simulated " + exceptionName + " from Java ERROR 404");
        }
    }
}
