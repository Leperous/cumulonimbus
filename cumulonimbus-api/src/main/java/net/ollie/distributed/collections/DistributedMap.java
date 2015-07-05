package net.ollie.distributed.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.ollie.distributed.serialization.MustDistribute;
import net.ollie.distributed.utils.Maps;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface DistributedMap<K, V>
        extends DistributedFunction<K, V> {

    /**
     *
     * @return a mutable copy of the keys in this map.
     */
    @Nonnull
    Set<K> copyKeys();

    /**
     *
     * @return a mutable copy of all the key/value pairs in this map.
     */
    @Nonnull
    default Map<K, V> copyMap() {
        return this.copyMap(this.copyKeys());
    }

    @Nonnull
    default <K2 extends K> Map<K2, V> copyMap(final Set<K2> keys) {
        final Map<K2, V> local = Maps.newHashMap(keys.size());
        keys.forEach(key -> local.put(key, this.get(key)));
        return local;
    }

    default boolean isEmpty() {
        return this.copyKeys().isEmpty();
    }

    default long size() {
        return this.copyKeys().size();
    }

    void evict(Collection<K> keys);

}
