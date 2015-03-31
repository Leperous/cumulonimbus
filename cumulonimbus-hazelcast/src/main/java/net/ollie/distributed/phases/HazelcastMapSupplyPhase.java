package net.ollie.distributed.phases;

import static java.util.Objects.requireNonNull;

import com.hazelcast.core.IMap;

import net.ollie.distributed.collections.HazelcastMap;

/**
 *
 * @author Ollie
 */
public class HazelcastMapSupplyPhase<K, V>
        implements SupplyPhase<HazelcastMap<K, V>> {

    public static <K, V> HazelcastMapSupplyPhase<K, V> create(final IMap<K, V> map) {
        return new HazelcastMapSupplyPhase<>(HazelcastMap.unmodifiable(map));
    }

    private final HazelcastMap<K, V> map;

    public HazelcastMapSupplyPhase(final HazelcastMap<K, V> map) {
        this.map = requireNonNull(map);
    }

    @Override
    public HazelcastMap<K, V> get() {
        return map;
    }

}
