package net.ollie.distributed.utils;

import java.util.HashMap;

/**
 *
 * @author Ollie
 */
public final class Maps {

    private Maps() {
    }

    public static <K, V> HashMap<K, V> newHashMap(final int size) {
        return new HashMap<>(capacity(size));
    }

    public static int capacity(final int size) {
        return size <= 4
                ? size
                : size * 4 / 3;
    }

}
