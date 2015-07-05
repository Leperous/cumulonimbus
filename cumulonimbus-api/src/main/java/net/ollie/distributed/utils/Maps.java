package net.ollie.distributed.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> collectEntries() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

}
