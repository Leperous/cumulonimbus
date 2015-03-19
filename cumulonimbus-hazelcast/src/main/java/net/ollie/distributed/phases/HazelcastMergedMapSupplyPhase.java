package net.ollie.distributed.phases;

import com.hazelcast.core.IMap;

import net.ollie.distributed.collections.DistributedHazelcastMap;
import net.ollie.distributed.collections.DistributedMergeValueMap;
import net.ollie.distributed.functions.SerializableBiFunction;

/**
 * Supply a merger of two distributed maps.
 *
 * @author Ollie
 */
public class HazelcastMergedMapSupplyPhase<K, V1, V2, V>
        implements SupplyPhase<DistributedHazelcastMap<K, V>> {

    public static <K, V1, V2, V> HazelcastMergedMapSupplyPhase<K, V1, V2, V> create(
            final IMap<K, V1> left,
            final IMap<K, V2> right,
            final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        return new HazelcastMergedMapSupplyPhase<>(DistributedHazelcastMap.unmodifiable(left), DistributedHazelcastMap.unmodifiable(right), merge);
    }

    private final DistributedHazelcastMap<K, V1> left;
    private final DistributedHazelcastMap<K, V2> right;
    private final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge;

    public HazelcastMergedMapSupplyPhase(
            final DistributedHazelcastMap<K, V1> left,
            final DistributedHazelcastMap<K, V2> right,
            final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        this.left = left;
        this.right = right;
        this.merge = merge;
    }

    @Override
    public DistributedHazelcastMap<K, V> get() {
        return new DistributedMergeValueMap<>(left, right, merge);
    }

}
