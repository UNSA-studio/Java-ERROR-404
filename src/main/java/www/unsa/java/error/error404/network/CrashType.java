package www.unsa.java.error.error404.network;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public enum CrashType {
    NULL_POINTER,
    CLASS_CAST,
    ARRAY_INDEX_OUT_OF_BOUNDS,
    STRING_INDEX_OUT_OF_BOUNDS,
    ILLEGAL_ARGUMENT,
    ILLEGAL_STATE,
    NUMBER_FORMAT,
    ARITHMETIC,
    NEGATIVE_ARRAY_SIZE,
    CLASS_NOT_FOUND,
    NO_CLASS_DEF_FOUND,
    NO_SUCH_METHOD,
    NO_SUCH_FIELD,
    OUT_OF_MEMORY,
    STACK_OVERFLOW,
    UNSUPPORTED_OPERATION,
    INTERRUPTED,
    EXCEPTION_IN_INITIALIZER,
    SECURITY,
    ILLEGAL_ACCESS,
    INSTANTIATION,
    CONCURRENT_MODIFICATION,
    NO_SUCH_ELEMENT,
    EMPTY_STACK,
    TOO_MANY_LISTENERS,
    MISSING_RESOURCE,
    INPUT_MISMATCH,
    ILLEGAL_FORMAT,
    INVALID_PROPERTIES_FORMAT,
    IO,
    FILE_NOT_FOUND,
    EOF,
    UTF_DATA_FORMAT,
    NOT_SERIALIZABLE,
    STREAM_CORRUPTED,
    INTERRUPTED_IO,
    BUFFER_OVERFLOW,
    BUFFER_UNDERFLOW,
    READ_ONLY_BUFFER,
    FILE_SYSTEM,
    CLOSED_CHANNEL,
    CLOSED_FILE_SYSTEM,
    SOCKET,
    UNKNOWN_HOST,
    CONNECT,
    BIND,
    MALFORMED_URL,
    HTTP_RETRY;

    /**
     * Throws the declared checked exception without wrapping it in RuntimeException,
     * so crash reports show the real exception type (e.g. java.io.IOException).
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable t) throws E {
        throw (E) t;
    }

    public void execute() {
        switch (this) {
            case NULL_POINTER -> { String s = null; s.length(); }
            case CLASS_CAST -> { Object o = Integer.valueOf(1); String s = (String) o; }
            case ARRAY_INDEX_OUT_OF_BOUNDS -> { int[] arr = new int[1]; int x = arr[10]; }
            case STRING_INDEX_OUT_OF_BOUNDS -> { "abc".charAt(10); }
            case ILLEGAL_ARGUMENT -> { try { Thread.sleep(-1); } catch (InterruptedException e) {} }
            case ILLEGAL_STATE -> { throw new IllegalStateException(); }
            case NUMBER_FORMAT -> { Integer.parseInt("abc"); }
            case ARITHMETIC -> { int x = 1 / 0; }
            case NEGATIVE_ARRAY_SIZE -> { byte[] b = new byte[-1]; }
            case CLASS_NOT_FOUND -> {
                try { Class.forName("non.existent.Cls"); }
                catch (ClassNotFoundException e) { sneakyThrow(e); }
            }
            case NO_CLASS_DEF_FOUND -> { throw new NoClassDefFoundError("no.such.Class.DefFound"); }
            case NO_SUCH_METHOD -> {
                try { String.class.getMethod("nonExistentMethod"); }
                catch (NoSuchMethodException e) { throw new NoSuchMethodError(e.getMessage()); }
            }
            case NO_SUCH_FIELD -> {
                try { String.class.getField("nonExistentField"); }
                catch (NoSuchFieldException e) { throw new NoSuchFieldError(e.getMessage()); }
            }
            case OUT_OF_MEMORY -> { List<byte[]> leak = new ArrayList<>(); while(true) leak.add(new byte[1024 * 1024]); }
            case STACK_OVERFLOW -> { Runnable r = new Runnable() { public void run() { this.run(); } }; r.run(); }
            case UNSUPPORTED_OPERATION -> { List.of(1,2).add(3); }
            case INTERRUPTED -> {
                Thread.currentThread().interrupt();
                try { Thread.sleep(1000); }
                catch (InterruptedException e) { sneakyThrow(e); }
            }
            case EXCEPTION_IN_INITIALIZER -> { class Bad { static { int x = 1/0; } } new Bad(); }
            case SECURITY -> { throw new SecurityException(); }
            case ILLEGAL_ACCESS -> {
                try { Runtime.class.getDeclaredConstructor().newInstance(); }
                catch (Exception e) { sneakyThrow(e); }
            }
            case INSTANTIATION -> {
                try { List.class.newInstance(); }
                catch (Exception e) { sneakyThrow(e); }
            }
            case CONCURRENT_MODIFICATION -> { List<String> list = new ArrayList<>(List.of("a","b","c")); for(String s : list) list.remove(s); }
            case NO_SUCH_ELEMENT -> { new ArrayList<>().iterator().next(); }
            case EMPTY_STACK -> { new Stack<>().pop(); }
            case TOO_MANY_LISTENERS -> { sneakyThrow(new java.util.TooManyListenersException()); }
            case MISSING_RESOURCE -> { ResourceBundle.getBundle("non.existent.Bundle"); }
            case INPUT_MISMATCH -> { new Scanner("abc").nextInt(); }
            case ILLEGAL_FORMAT -> { new Formatter().format("%d", "string"); }
            case INVALID_PROPERTIES_FORMAT -> {
                try { new Properties().loadFromXML(new ByteArrayInputStream("<broken".getBytes(StandardCharsets.UTF_8))); }
                catch (IOException e) { sneakyThrow(e); }
            }
            case IO -> { sneakyThrow(new IOException("Simulated I/O failure")); }
            case FILE_NOT_FOUND -> {
                try { new FileInputStream("non_existent_j404_file"); }
                catch (FileNotFoundException e) { sneakyThrow(e); }
            }
            case EOF -> {
                try { new DataInputStream(new ByteArrayInputStream(new byte[0])).readUTF(); }
                catch (IOException e) { sneakyThrow(e); }
            }
            case UTF_DATA_FORMAT -> {
                // length=2, data: 'A' + lone continuation byte 0x80 — invalid modified UTF-8, readUTF must throw
                try {
                    new DataInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x02, 0x41, (byte) 0x80})).readUTF();
                } catch (IOException e) { sneakyThrow(e); }
            }
            case NOT_SERIALIZABLE -> {
                try { new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(new Object()); }
                catch (IOException e) { sneakyThrow(e); }
            }
            case STREAM_CORRUPTED -> { sneakyThrow(new StreamCorruptedException()); }
            case INTERRUPTED_IO -> { sneakyThrow(new InterruptedIOException()); }
            case BUFFER_OVERFLOW -> { ByteBuffer.allocate(1).put(new byte[10]); }
            case BUFFER_UNDERFLOW -> { ByteBuffer.allocate(1).getInt(); }
            case READ_ONLY_BUFFER -> { ByteBuffer.allocate(1).asReadOnlyBuffer().put((byte)1); }
            case FILE_SYSTEM -> {
                try {
                    Path.of("/non_existent_j404_dir", "missing.bin").toRealPath();
                } catch (IOException e) {
                    sneakyThrow(e);
                }
                sneakyThrow(new FileSystemException("non_existent_j404_dir/missing.bin"));
            }
            case CLOSED_CHANNEL -> {
                try {
                    SocketChannel ch = SocketChannel.open();
                    ch.close();
                    ch.read(ByteBuffer.allocate(1));
                } catch (IOException e) { sneakyThrow(e); }
            }
            case CLOSED_FILE_SYSTEM -> { sneakyThrow(new ClosedFileSystemException()); }
            case SOCKET -> {
                try { Socket s = new Socket(); s.getOutputStream().write(1); }
                catch (IOException e) { sneakyThrow(e); }
            }
            case UNKNOWN_HOST -> {
                try { InetAddress.getByName("unknown.domain"); }
                catch (UnknownHostException e) { sneakyThrow(e); }
                sneakyThrow(new UnknownHostException("unknown.domain"));
            }
            case CONNECT -> {
                try { new Socket("127.0.0.1", 12345); }
                catch (IOException e) { sneakyThrow(e); }
                sneakyThrow(new ConnectException("Connection refused: connect to 127.0.0.1:12345"));
            }
            case BIND -> {
                try {
                    ServerSocket ss = new ServerSocket(0);
                    new ServerSocket(ss.getLocalPort());
                } catch (IOException e) { sneakyThrow(e); }
            }
            case MALFORMED_URL -> {
                try { new URL("http://"); }
                catch (MalformedURLException e) { sneakyThrow(e); }
                sneakyThrow(new MalformedURLException("Malformed URL: http://"));
            }
            case HTTP_RETRY -> { sneakyThrow(new HttpRetryException("", 503)); }
        }
    }

    public String javaException() {
        return switch (this) {
            case NULL_POINTER -> "java.lang.NullPointerException";
            case CLASS_CAST -> "java.lang.ClassCastException";
            case ARRAY_INDEX_OUT_OF_BOUNDS -> "java.lang.ArrayIndexOutOfBoundsException";
            case STRING_INDEX_OUT_OF_BOUNDS -> "java.lang.StringIndexOutOfBoundsException";
            case ILLEGAL_ARGUMENT -> "java.lang.IllegalArgumentException";
            case ILLEGAL_STATE -> "java.lang.IllegalStateException";
            case NUMBER_FORMAT -> "java.lang.NumberFormatException";
            case ARITHMETIC -> "java.lang.ArithmeticException";
            case NEGATIVE_ARRAY_SIZE -> "java.lang.NegativeArraySizeException";
            case CLASS_NOT_FOUND -> "java.lang.ClassNotFoundException";
            case NO_CLASS_DEF_FOUND -> "java.lang.NoClassDefFoundError";
            case NO_SUCH_METHOD -> "java.lang.NoSuchMethodError";
            case NO_SUCH_FIELD -> "java.lang.NoSuchFieldError";
            case OUT_OF_MEMORY -> "java.lang.OutOfMemoryError";
            case STACK_OVERFLOW -> "java.lang.StackOverflowError";
            case UNSUPPORTED_OPERATION -> "java.lang.UnsupportedOperationException";
            case INTERRUPTED -> "java.lang.InterruptedException";
            case EXCEPTION_IN_INITIALIZER -> "java.lang.ExceptionInInitializerError";
            case SECURITY -> "java.lang.SecurityException";
            case ILLEGAL_ACCESS -> "java.lang.IllegalAccessException";
            case INSTANTIATION -> "java.lang.InstantiationException";
            case CONCURRENT_MODIFICATION -> "java.util.ConcurrentModificationException";
            case NO_SUCH_ELEMENT -> "java.util.NoSuchElementException";
            case EMPTY_STACK -> "java.util.EmptyStackException";
            case TOO_MANY_LISTENERS -> "java.util.TooManyListenersException";
            case MISSING_RESOURCE -> "java.util.MissingResourceException";
            case INPUT_MISMATCH -> "java.util.InputMismatchException";
            case ILLEGAL_FORMAT -> "java.util.IllegalFormatException";
            case INVALID_PROPERTIES_FORMAT -> "java.util.InvalidPropertiesFormatException";
            case IO -> "java.io.IOException";
            case FILE_NOT_FOUND -> "java.io.FileNotFoundException";
            case EOF -> "java.io.EOFException";
            case UTF_DATA_FORMAT -> "java.io.UTFDataFormatException";
            case NOT_SERIALIZABLE -> "java.io.NotSerializableException";
            case STREAM_CORRUPTED -> "java.io.StreamCorruptedException";
            case INTERRUPTED_IO -> "java.io.InterruptedIOException";
            case BUFFER_OVERFLOW -> "java.nio.BufferOverflowException";
            case BUFFER_UNDERFLOW -> "java.nio.BufferUnderflowException";
            case READ_ONLY_BUFFER -> "java.nio.ReadOnlyBufferException";
            case FILE_SYSTEM -> "java.nio.file.FileSystemException";
            case CLOSED_CHANNEL -> "java.nio.channels.ClosedChannelException";
            case CLOSED_FILE_SYSTEM -> "java.nio.file.ClosedFileSystemException";
            case SOCKET -> "java.net.SocketException";
            case UNKNOWN_HOST -> "java.net.UnknownHostException";
            case CONNECT -> "java.net.ConnectException";
            case BIND -> "java.net.BindException";
            case MALFORMED_URL -> "java.net.MalformedURLException";
            case HTTP_RETRY -> "java.net.HttpRetryException";
        };
    }
}
