package net.ollie.distributed.hazelcast.collections.history;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.collections.DistributedHistory;
import net.ollie.distributed.serialization.MustNotDistribute;

/**
 *
 * @author Ollie
 */
@MustNotDistribute
public interface HazelcastHistory<T, K, V>
        extends DistributedHistory<T, K, V> {

    @Override
    HazelcastMap<K, V> at(T temporal);

}
