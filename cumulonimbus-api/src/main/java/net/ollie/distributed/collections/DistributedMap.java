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
    Set<K> localKeys();

    /**
     *
     * @return a mutable copy of all the data in memory.
     */
    @Nonnull
    default Map<K, V> localMap() {
        final Set<K> keys = this.localKeys();
        final Map<K, V> map = Maps.newHashMap(keys.size());
        keys.forEach(key -> map.put(key, this.get(key)));
        return map;
    }

    @Nonnull
    default Map<K, V> localMap(final Set<K> keys) {
        final Map<K, V> local = Maps.newHashMap(keys.size());
        keys.forEach(key -> local.put(key, this.get(key)));
        return local;
    }

    default long size() {
        return this.localKeys().size();
    }

    void evict(Collection<K> keys);

}
