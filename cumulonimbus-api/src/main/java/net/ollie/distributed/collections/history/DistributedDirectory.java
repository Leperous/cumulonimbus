package net.ollie.distributed.collections.history;

import javax.annotation.Nonnull;

import net.ollie.distributed.collections.DistributedMap;

/**
 *
 * @author Ollie
 */
public interface DistributedDirectory<T, K, V> {

    @Nonnull
    DistributedMap<K, V> at(T key);

}
