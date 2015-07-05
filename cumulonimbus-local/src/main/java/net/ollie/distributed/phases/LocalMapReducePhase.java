package net.ollie.distributed.phases;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.ollie.distributed.utils.Lists;
import net.ollie.distributed.utils.Maps;

/**
 *
 * @author Ollie
 */
public class LocalMapReducePhase<K1, V1, K2, V2> implements FuturePhase<Map<K1, V1>, Map<K2, V2>> {

    private final boolean parallel;
    private final Function<? super K1, ? extends K2> mapper;
    private final Function<? super Collection<? extends V1>, ? extends V2> reducer;

    public LocalMapReducePhase(
            final boolean parallel,
            final Function<? super K1, ? extends K2> mapper,
            final Function<? super Collection<? extends V1>, ? extends V2> reducer) {
        this.parallel = parallel;
        this.mapper = mapper;
        this.reducer = reducer;
    }

    @Override
    public CompletableFuture<Map<K2, V2>> transform(final Map<K1, V1> from) {
        return CompletableFuture.supplyAsync(() -> this.mapReduce(from));
    }

    protected Map<K2, V2> mapReduce(final Map<K1, V1> from) {
        final Map<K2, List<Map.Entry<K1, V1>>> mapped = this.map(from);
        return this.reduce(mapped);
    }

    private Map<K2, List<Map.Entry<K1, V1>>> map(final Map<K1, V1> from) {
        return this.stream(from.entrySet()).collect(this.mapperCollector());
    }

    private Collector<Map.Entry<K1, V1>, ?, Map<K2, List<Map.Entry<K1, V1>>>> mapperCollector() {
        return Collectors.groupingBy(this::mapped);
    }

    private K2 mapped(final Map.Entry<K1, V1> entry) {
        return mapper.apply(entry.getKey());
    }

    private Map<K2, V2> reduce(final Map<K2, List<Map.Entry<K1, V1>>> mapped) {
        return this.stream(mapped.entrySet())
                .map(this::reduce)
                .collect(Maps.collectEntries());
    }

    private Map.Entry<K2, V2> reduce(final Map.Entry<K2, List<Map.Entry<K1, V1>>> entry) {
        final List<V1> values = Lists.serialTransform(entry.getValue(), Map.Entry::getValue);
        final V2 reduced = reducer.apply(values);
        return new SimpleImmutableEntry<>(entry.getKey(), reduced);
    }

    private <T> Stream<T> stream(final Collection<T> collection) {
        return parallel ? collection.parallelStream() : collection.stream();
    }

}
