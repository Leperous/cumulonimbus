package net.ollie.distributed.hazelcast.phases;

import com.hazelcast.core.IMap;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.hazelcast.collections.MergeValueMap;
import net.ollie.distributed.hazelcast.functions.SerializableBiFunction;
import net.ollie.distributed.phases.SupplyPhase;

/**
 * Supply a merge of two distributed maps.
 *
 * @author Ollie
 */
public class HazelcastMergedMapSupplyPhase<K, V1, V2, V>
        implements SupplyPhase<HazelcastMap<K, V>> {

    public static <K, V1, V2, V> HazelcastMergedMapSupplyPhase<K, V1, V2, V> create(
            final IMap<K, V1> left,
            final IMap<K, V2> right,
            final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        return new HazelcastMergedMapSupplyPhase<>(HazelcastMap.unmodifiable(left), HazelcastMap.unmodifiable(right), merge);
    }

    private final HazelcastMap<K, V1> left;
    private final HazelcastMap<K, V2> right;
    private final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge;

    public HazelcastMergedMapSupplyPhase(
            final HazelcastMap<K, V1> left,
            final HazelcastMap<K, V2> right,
            final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        this.left = left;
        this.right = right;
        this.merge = merge;
    }

    @Override
    public HazelcastMap<K, V> get() {
        return new MergeValueMap<>(left, right, merge);
    }

}
