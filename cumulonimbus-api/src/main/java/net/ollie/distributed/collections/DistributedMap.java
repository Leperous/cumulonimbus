package net.ollie.distributed.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.ollie.distributed.utils.Maps;

/**
 *
 * @author Ollie
 */
public interface DistributedMap<K, V>
        extends DistributedFunction<K, V> {

    @Nonnull
    Set<K> copyKeys();

    /**
     *
     * @return a mutable copy of all the data in memory.
     */
    @Nonnull
    default Map<K, V> copyMap() {
        final Set<K> keys = this.copyKeys();
        final Map<K, V> map = Maps.newHashMap(keys.size());
        keys.forEach(key -> map.put(key, this.get(key)));
        return map;
    }

    @Nonnull
    default <K2 extends K> Map<K2, V> copyMap(final Set<K2> keys) {
        final Map<K2, V> local = Maps.newHashMap(keys.size());
        keys.forEach(key -> local.put(key, this.get(key)));
        return local;
    }

    default long size() {
        return this.copyKeys().size();
    }

    void evict(Collection<K> keys);

}
