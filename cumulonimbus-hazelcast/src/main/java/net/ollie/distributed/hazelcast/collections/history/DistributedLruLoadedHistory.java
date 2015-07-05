package net.ollie.distributed.hazelcast.collections.history;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.hazelcast.collections.HazelcastIMap;
import net.ollie.distributed.hazelcast.functions.SerializableBiPredicate;

/**
 * Loads data on demand for some temporal key.
 *
 * @param <T> temporal type.
 * @author Ollie
 */
public class DistributedLruLoadedHistory<T, K, V>
        implements HazelcastHistory<T, K, V> {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLruLoadedHistory.class);
    private final LinkedHashMap<T, HazelcastMap<K, V>> maps;
    private final HazelcastIMap<K, V> target;
    private final SerializableBiPredicate<? super T, ? super K> datePredicate;
    private final Function<? super T, Map<? extends K, ? extends V>> load;

    public DistributedLruLoadedHistory(final int maxSize,
            final HazelcastIMap<K, V> target,
            final SerializableBiPredicate<? super T, ? super K> datePredicate,
            final Function<? super T, Map<? extends K, ? extends V>> load) {
        this.maps = new LruMap(maxSize);
        this.target = target;
        this.datePredicate = datePredicate;
        this.load = load;
    }

    @Override
    public synchronized HazelcastMap<K, V> at(final T temporal) {
        return maps.computeIfAbsent(temporal, this::load);
    }

    protected HazelcastMap<K, V> load(final T date) {
        logger.info("Loading data for [{}] ...", date);
        final Map<? extends K, ? extends V> loaded = load.apply(date);
        target.writeAll(loaded);
        return target.filter(datePredicate.partialLeft(date));
    }

    protected void unload(final T date, final HazelcastMap<K, V> map) {
        logger.info("Unloading data for [{}] ...", date);
        map.close();
    }

    private final class LruMap extends LinkedHashMap<T, HazelcastMap<K, V>> {

        private static final long serialVersionUID = 1L;
        private final int maxCapacity;

        LruMap(final int maxCapacity) {
            super(maxCapacity, 0.75f, true);
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<T, HazelcastMap<K, V>> eldest) {
            if (this.size() >= maxCapacity) {
                DistributedLruLoadedHistory.this.unload(eldest.getKey(), eldest.getValue());
                return true;
            }
            return false;
        }

    }

}
