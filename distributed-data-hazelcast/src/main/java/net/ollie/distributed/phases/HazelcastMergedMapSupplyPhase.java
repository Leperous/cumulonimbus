package net.ollie.distributed.phases;

import net.ollie.distributed.collections.DistributedHazelcastMap;
import net.ollie.distributed.collections.DistributedMergeMap;
import net.ollie.distributed.functions.SerializableBiFunction;

/**
 * Supply a merger of two distributed maps.
 *
 * @author Ollie
 */
public class HazelcastMergedMapSupplyPhase<K, V1, V2, V>
        implements SupplyPhase<DistributedHazelcastMap<K, V>> {

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
    public DistributedHazelcastMap<K, V> supply() {
        return new DistributedMergeMap<>(left, right, merge);
    }

}
