package net.ollie.distributed.phases;

import net.ollie.distributed.collections.history.DistributedHazelcastHistory;
import net.ollie.distributed.collections.HazelcastMap;

/**
 *
 * @author Ollie
 */
public class HazelcastHistoricMapReducePhase<T, K, V>
        implements Phase<DistributedHazelcastHistory<T, K, V>, HazelcastMap<K, V>> {

    private final T date;

    public HazelcastHistoricMapReducePhase(final T date) {
        this.date = date;
    }

    @Override
    public HazelcastMap<K, V> transform(final DistributedHazelcastHistory<T, K, V> from) {
        return from.on(date);
    }

}
