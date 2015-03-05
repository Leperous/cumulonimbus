package net.ollie.distributed.collections.history;

import net.ollie.distributed.collections.DistributedHazelcastMap;
import net.ollie.distributed.serialization.MustNotSerialize;

/**
 *
 * @author Ollie
 */
@MustNotSerialize
public interface DistributedHazelcastHistory<T, K, V>
        extends DistributedHistory<T, K, V> {

    @Override
    DistributedHazelcastMap<K, V> on(T date);

}
