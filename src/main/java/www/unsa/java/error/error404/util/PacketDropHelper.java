package www.unsa.java.error.error404.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class PacketDropHelper {
    private static final AtomicBoolean shouldDrop = new AtomicBoolean(false);

    public static void activateDrop() {
        shouldDrop.set(true);
    }

    public static boolean consumeDrop() {
        return shouldDrop.getAndSet(false);
    }
}
