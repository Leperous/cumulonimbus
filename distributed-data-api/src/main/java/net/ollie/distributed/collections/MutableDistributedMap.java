package net.ollie.distributed.collections;

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public interface MutableDistributedMap<K, V> extends DistributedMap<K, V> {

    @CheckForNull
    V put(K key, V value);

    void write(K key, V value);

    default void writeAll(@Nonnull final Map<? extends K, ? extends V> values) {
        values.entrySet().forEach(entry -> this.write(entry.getKey(), entry.getValue()));
    }

    boolean replace(K key, V expected, V newValue);

    void delete(Object key);

    void clear();

}
