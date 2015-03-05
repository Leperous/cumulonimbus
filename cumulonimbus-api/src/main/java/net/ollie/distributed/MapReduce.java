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

    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>, M3 extends Map<?, ?>> CompletableFuture<M3> process(
            final SupplyPhase<M1> collector,
            final FuturePhase<M1, M2> mapReducer,
            final Phase<M2, M3> augmenter) {
        return mapReducer.andLater(augmenter).transform(collector.supply());
    }

    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>> CompletableFuture<M2> process(
            final M1 map,
            final FuturePhase<M1, M2> mapReducer) {
        return mapReducer.transform(map);
    }

    public static <M1 extends DistributedFunction<?, ?>, M2 extends Map<?, ?>> CompletableFuture<M2> process(
            final SupplyPhase<M1> collector,
            final FuturePhase<M1, M2> mapReducer) {
        return process(collector, mapReducer, Phase.identity());
    }

}
