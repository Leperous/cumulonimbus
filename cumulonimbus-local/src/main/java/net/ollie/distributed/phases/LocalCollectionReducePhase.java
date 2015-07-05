package net.ollie.distributed.phases;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Vanilla map/reduce from a collection to a single value.
 *
 * Optionally parallel, using the default {@link ForkJoinPool fork-join pool} if so.
 *
 * @author Ollie
 */
public class LocalCollectionReducePhase<T, R> implements FuturePhase<Collection<T>, R> {

    private final boolean parallel;
    private final Function<? super T, ? extends R> mapper;
    private final Supplier<? extends R> initial;
    private final BinaryOperator<R> reducer;

    public LocalCollectionReducePhase(
            final boolean parallel,
            final Function<? super T, ? extends R> mapper,
            final Supplier<? extends R> initial,
            final BinaryOperator<R> reducer) {
        this.parallel = parallel;
        this.mapper = mapper;
        this.initial = initial;
        this.reducer = reducer;
    }

    @Override
    public CompletableFuture<R> transform(final Collection<T> input) {
        return CompletableFuture.supplyAsync(() -> {
            return this.stream(input).map(mapper).reduce(initial.get(), reducer);
        });
    }

    private <T> Stream<T> stream(final Collection<T> input) {
        return parallel ? input.parallelStream() : input.stream();
    }

}
