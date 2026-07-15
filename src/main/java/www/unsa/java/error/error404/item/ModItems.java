package www.unsa.java.error.error404.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import www.unsa.java.error.error404.JavaError404;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JavaError404.MODID);

    public static final DeferredItem<Item> JAVA_NETWORK_PACKET = register("java_network_packet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SCISSORS = register("scissors", () -> new ScissorsItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> JAVA = register("java_item", () -> new JavaItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> NULL_POINTER = register("null_pointer_exception", () -> new ExceptionItem("NullPointerException", false));
    public static final DeferredItem<Item> CLASS_CAST = register("class_cast_exception", () -> new ExceptionItem("ClassCastException", false));
    public static final DeferredItem<Item> ARRAY_INDEX = register("array_index_out_of_bounds_exception", () -> new ExceptionItem("ArrayIndexOutOfBoundsException", false));
    public static final DeferredItem<Item> STRING_INDEX = register("string_index_out_of_bounds_exception", () -> new ExceptionItem("StringIndexOutOfBoundsException", false));
    public static final DeferredItem<Item> ILLEGAL_ARGUMENT = register("illegal_argument_exception", () -> new ExceptionItem("IllegalArgumentException", false));
    public static final DeferredItem<Item> ILLEGAL_STATE = register("illegal_state_exception", () -> new ExceptionItem("IllegalStateException", false));
    public static final DeferredItem<Item> NUMBER_FORMAT = register("number_format_exception", () -> new ExceptionItem("NumberFormatException", false));
    public static final DeferredItem<Item> ARITHMETIC = register("arithmetic_exception", () -> new ExceptionItem("ArithmeticException", false));
    public static final DeferredItem<Item> NEGATIVE_ARRAY_SIZE = register("negative_array_size_exception", () -> new ExceptionItem("NegativeArraySizeException", false));
    public static final DeferredItem<Item> CLASS_NOT_FOUND = register("class_not_found_exception", () -> new ExceptionItem("ClassNotFoundException", true));
    public static final DeferredItem<Item> NO_CLASS_DEF = register("no_class_def_found_error", () -> new ExceptionItem("NoClassDefFoundError", true));
    public static final DeferredItem<Item> NO_SUCH_METHOD = register("no_such_method_error", () -> new ExceptionItem("NoSuchMethodError", true));
    public static final DeferredItem<Item> NO_SUCH_FIELD = register("no_such_field_error", () -> new ExceptionItem("NoSuchFieldError", true));
    public static final DeferredItem<Item> OUT_OF_MEMORY = register("out_of_memory_error", () -> new ExceptionItem("OutOfMemoryError", true));
    public static final DeferredItem<Item> STACK_OVERFLOW = register("stack_overflow_error", () -> new ExceptionItem("StackOverflowError", true));
    public static final DeferredItem<Item> UNSUPPORTED_OP = register("unsupported_operation_exception", () -> new ExceptionItem("UnsupportedOperationException", false));
    public static final DeferredItem<Item> INTERRUPTED = register("interrupted_exception", () -> new ExceptionItem("InterruptedException", false));
    public static final DeferredItem<Item> EXCEPTION_IN_INIT = register("exception_in_initializer_error", () -> new ExceptionItem("ExceptionInInitializerError", true));
    public static final DeferredItem<Item> SECURITY = register("security_exception", () -> new ExceptionItem("SecurityException", false));
    public static final DeferredItem<Item> ILLEGAL_ACCESS = register("illegal_access_exception", () -> new ExceptionItem("IllegalAccessException", false));
    public static final DeferredItem<Item> INSTANTIATION = register("instantiation_exception", () -> new ExceptionItem("InstantiationException", false));
    public static final DeferredItem<Item> CONCURRENT_MOD = register("concurrent_modification_exception", () -> new ExceptionItem("ConcurrentModificationException", false));
    public static final DeferredItem<Item> NO_SUCH_ELEMENT = register("no_such_element_exception", () -> new ExceptionItem("NoSuchElementException", false));
    public static final DeferredItem<Item> EMPTY_STACK = register("empty_stack_exception", () -> new ExceptionItem("EmptyStackException", false));
    public static final DeferredItem<Item> TOO_MANY_LISTENERS = register("too_many_listeners_exception", () -> new ExceptionItem("TooManyListenersException", false));
    public static final DeferredItem<Item> MISSING_RESOURCE = register("missing_resource_exception", () -> new ExceptionItem("MissingResourceException", false));
    public static final DeferredItem<Item> INPUT_MISMATCH = register("input_mismatch_exception", () -> new ExceptionItem("InputMismatchException", false));
    public static final DeferredItem<Item> ILLEGAL_FORMAT = register("illegal_format_exception", () -> new ExceptionItem("IllegalFormatException", false));
    public static final DeferredItem<Item> INVALID_PROPERTIES = register("invalid_properties_format_exception", () -> new ExceptionItem("InvalidPropertiesFormatException", false));
    public static final DeferredItem<Item> IO_EXCEPTION = register("io_exception", () -> new ExceptionItem("IOException", false));
    public static final DeferredItem<Item> FILE_NOT_FOUND = register("file_not_found_exception", () -> new ExceptionItem("FileNotFoundException", false));
    public static final DeferredItem<Item> EOF_EXCEPTION = register("eof_exception", () -> new ExceptionItem("EOFException", false));
    public static final DeferredItem<Item> UTF_DATA = register("utf_data_format_exception", () -> new ExceptionItem("UTFDataFormatException", false));
    public static final DeferredItem<Item> NOT_SERIALIZABLE = register("not_serializable_exception", () -> new ExceptionItem("NotSerializableException", false));
    public static final DeferredItem<Item> STREAM_CORRUPTED = register("stream_corrupted_exception", () -> new ExceptionItem("StreamCorruptedException", false));
    public static final DeferredItem<Item> INTERRUPTED_IO = register("interrupted_io_exception", () -> new ExceptionItem("InterruptedIOException", false));
    public static final DeferredItem<Item> BUFFER_OVERFLOW = register("buffer_overflow_exception", () -> new ExceptionItem("BufferOverflowException", false));
    public static final DeferredItem<Item> BUFFER_UNDERFLOW = register("buffer_underflow_exception", () -> new ExceptionItem("BufferUnderflowException", false));
    public static final DeferredItem<Item> READ_ONLY_BUFFER = register("read_only_buffer_exception", () -> new ExceptionItem("ReadOnlyBufferException", false));
    public static final DeferredItem<Item> FILE_SYSTEM = register("file_system_exception", () -> new ExceptionItem("FileSystemException", false));
    public static final DeferredItem<Item> CLOSED_CHANNEL = register("closed_channel_exception", () -> new ExceptionItem("ClosedChannelException", false));
    public static final DeferredItem<Item> CLOSED_FILE_SYSTEM = register("closed_file_system_exception", () -> new ExceptionItem("ClosedFileSystemException", false));
    public static final DeferredItem<Item> SOCKET = register("socket_exception", () -> new ExceptionItem("SocketException", true));
    public static final DeferredItem<Item> UNKNOWN_HOST = register("unknown_host_exception", () -> new ExceptionItem("UnknownHostException", true));
    public static final DeferredItem<Item> CONNECT = register("connect_exception", () -> new ExceptionItem("ConnectException", true));
    public static final DeferredItem<Item> BIND = register("bind_exception", () -> new ExceptionItem("BindException", true));
    public static final DeferredItem<Item> MALFORMED_URL = register("malformed_url_exception", () -> new ExceptionItem("MalformedURLException", false));
    public static final DeferredItem<Item> HTTP_RETRY = register("http_retry_exception", () -> new ExceptionItem("HttpRetryException", false));

    private static DeferredItem<Item> register(String name, Supplier<Item> sup) {
        return ITEMS.register(name, sup);
    }
}
