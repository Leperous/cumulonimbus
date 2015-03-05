package net.ollie.distributed.collections.history;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.ollie.distributed.collections.DistributedHazelcastMap;
import net.ollie.distributed.collections.DistributedIMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ollie.distributed.functions.KeyPredicateWrapper;
import net.ollie.distributed.functions.SerializableBiPredicate;

/**
 *
 * @author Ollie
 */
public class DistributedLruHistory<T, K, V>
        implements DistributedHazelcastHistory<T, K, V> {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLruHistory.class);
    private final LinkedHashMap<T, DistributedHazelcastMap<K, V>> maps;
    private final DistributedIMap<K, V> target;
    private final SerializableBiPredicate<? super T, ? super K> datePredicate;
    private final Function<? super T, Map<? extends K, ? extends V>> load;

    public DistributedLruHistory(final int maxSize,
            final DistributedIMap<K, V> target,
            final SerializableBiPredicate<? super T, ? super K> datePredicate,
            final Function<? super T, Map<? extends K, ? extends V>> load) {
        this.maps = new LruMap(maxSize);
        this.target = target;
        this.datePredicate = datePredicate;
        this.load = load;
    }

    @Override
    public synchronized DistributedHazelcastMap<K, V> on(final T date) {
        return maps.computeIfAbsent(date, this::load);
    }

    protected DistributedHazelcastMap<K, V> load(final T date) {
        logger.info("Loading data for [{}] ...", date);
        final Map<? extends K, ? extends V> loaded = load.apply(date);
        target.writeAll(loaded);
        return target.filter(new KeyPredicateWrapper<>(datePredicate.partialLeft(date)));
    }

    protected void unload(final T date, final DistributedHazelcastMap<K, V> map) {
        logger.info("Unloading data for [{}] ...", date);
        map.close();
    }

    private final class LruMap extends LinkedHashMap<T, DistributedHazelcastMap<K, V>> {

        private static final long serialVersionUID = 1L;
        private final int maxCapacity;

        LruMap(final int maxCapacity) {
            super(maxCapacity, 0.75f, true);
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<T, DistributedHazelcastMap<K, V>> eldest) {
            if (this.size() >= maxCapacity) {
                DistributedLruHistory.this.unload(eldest.getKey(), eldest.getValue());
                return true;
            }
            return false;
        }

    }

}
