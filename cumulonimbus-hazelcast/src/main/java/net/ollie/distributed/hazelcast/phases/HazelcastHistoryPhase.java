package net.ollie.distributed.hazelcast.phases;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.hazelcast.collections.history.HazelcastHistory;
import net.ollie.distributed.phases.Phase;

/**
 * A phase that extracts a point in history.
 *
 * @author Ollie
 */
public class HazelcastHistoryPhase<T, K, V>
        implements Phase<HazelcastHistory<T, K, V>, HazelcastMap<K, V>> {

    private final T temporal;

    public HazelcastHistoryPhase(final T temporal) {
        this.temporal = temporal;
    }

    @Override
    public HazelcastMap<K, V> transform(final HazelcastHistory<T, K, V> from) {
        return from.at(temporal);
    }

}
