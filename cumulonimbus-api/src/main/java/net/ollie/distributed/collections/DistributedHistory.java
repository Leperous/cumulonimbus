package net.ollie.distributed.collections;

import javax.annotation.Nonnull;

/**
 * A history (or directory) of distributed maps.
 *
 * @author Ollie
 */
public interface DistributedHistory<T, K, V> {

    @Nonnull
    DistributedMap<K, V> at(T key);

}
