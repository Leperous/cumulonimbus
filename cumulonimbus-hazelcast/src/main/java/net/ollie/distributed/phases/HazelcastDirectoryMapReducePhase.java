package net.ollie.distributed.phases;

import net.ollie.distributed.collections.HazelcastMap;
import net.ollie.distributed.collections.history.HazelcastDirectory;

/**
 *
 * @author Ollie
 */
public class HazelcastDirectoryMapReducePhase<T, K, V>
        implements Phase<HazelcastDirectory<T, K, V>, HazelcastMap<K, V>> {

    private final T temporal;

    public HazelcastDirectoryMapReducePhase(final T temporal) {
        this.temporal = temporal;
    }

    @Override
    public HazelcastMap<K, V> transform(final HazelcastDirectory<T, K, V> from) {
        return from.at(temporal);
    }

}
