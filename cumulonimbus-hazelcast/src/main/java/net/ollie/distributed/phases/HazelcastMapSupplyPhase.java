package net.ollie.distributed.phases;

import static java.util.Objects.requireNonNull;

import com.hazelcast.core.IMap;

import net.ollie.distributed.collections.DistributedHazelcastMap;

/**
 *
 * @author Ollie
 */
public class HazelcastMapSupplyPhase<K, V>
        implements SupplyPhase<DistributedHazelcastMap<K, V>> {

    public static <K, V> HazelcastMapSupplyPhase<K, V> create(final IMap<K, V> map) {
        return new HazelcastMapSupplyPhase<>(DistributedHazelcastMap.unmodifiable(map));
    }

    private final DistributedHazelcastMap<K, V> map;

    public HazelcastMapSupplyPhase(final DistributedHazelcastMap<K, V> map) {
        this.map = requireNonNull(map);
    }

    @Override
    public DistributedHazelcastMap<K, V> get() {
        return map;
    }

}
