package net.ollie.distributed;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ollie.distributed.collections.DistributedFunction;
import net.ollie.distributed.phases.FuturePhase;
import net.ollie.distributed.phases.Phase;
import net.ollie.distributed.phases.SupplyPhase;

/**
 * Map/reduce helper.
 *
 * @author Ollie
 */
public final class MapReduce {

    private MapReduce() {
    }

    /**
     * Map/reduce a map into a future map.
     *
     * @param <M1> input map type.
     * @param <M2> output map type.
     * @param map input map.
     * @param mapReducer phase that will transform the input map.
     * @return
     */
    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>> CompletableFuture<M2> mapReduce(
            final M1 map,
            final FuturePhase<M1, M2> mapReducer) {
        return mapReducer.transform(map);
    }

    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>> CompletableFuture<M2> mapReduce(
            final SupplyPhase<M1> collector,
            final FuturePhase<M1, M2> mapReducer) {
        return mapReduceAugment(collector, mapReducer, Phase.identity());
    }

    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>, M3 extends Map<?, ?>> CompletableFuture<M3> mapReduceAugment(
            final SupplyPhase<M1> supplier,
            final FuturePhase<M1, M2> mapReducer,
            final Phase<M2, M3> augmenter) {
        return supplier.later(mapReducer).later(augmenter).get();
    }

}
