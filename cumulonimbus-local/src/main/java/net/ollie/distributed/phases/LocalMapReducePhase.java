package net.ollie.distributed.phases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

import net.ollie.distributed.collections.DistributedMap;
import net.ollie.distributed.collections.DistributedFunction;

/**
 * @author Ollie
 */
public class LocalMapReducePhase<K1, V1, K2, V2> implements FuturePhase<DistributedMap<K1, V1>, Map<K2, V2>> {

    private static final CompletableFuture[] FUTURES = new CompletableFuture[0];
    private final Executor executor;
    private final BiFunction<K1, V1, ? extends Collection<Map.Entry<? extends K2, ? extends Collection<V1>>>> map;
    private final Function<? super Collection<V1>, V2> reduce;

    public LocalMapReducePhase(
            final Executor executor,
            final BiFunction<K1, V1, ? extends Collection<Map.Entry<? extends K2, ? extends Collection<V1>>>> map,
            final Function<? super Collection<V1>, V2> reduce) {
        this.executor = executor;
        this.map = map;
        this.reduce = reduce;
    }

    @Override
    public CompletableFuture<Map<K2, V2>> transform(final DistributedMap<K1, V1> input) {
        return this.mapAll(input).thenComposeAsync(this::reduceAll, executor);
    }

    private CompletableFuture<Map<K2, Collection<V1>>> mapAll(final DistributedMap<K1, V1> source) {
        final Map<K2, Collection<V1>> multimap = new ConcurrentHashMap<>();
        final List<CompletableFuture<?>> futures = source.copyKeys()
                .stream()
                .map(key -> this.map(key, source, multimap)) //Create a future for each key
                .collect(toList());
        return CompletableFuture.allOf(futures.toArray(FUTURES)).thenApply(v -> multimap);
    }

    private CompletableFuture<?> map(final K1 key, final DistributedFunction<K1, V1> source, final Map<K2, Collection<V1>> multimap) {
        return CompletableFuture.runAsync(() -> {
            final Map<K2, Collection<V1>> mapped = this.map(key, source.get(key));
            mapped.entrySet().forEach(entry -> this.write(entry, multimap));
        }, executor);
    }

    private Map<K2, Collection<V1>> map(final K1 key, final V1 value) {
        final Map<K2, Collection<V1>> output = new HashMap<>();
        map.apply(key, value).forEach(entry -> output.put(entry.getKey(), entry.getValue()));
        return output;
    }

    private void write(final Map.Entry<K2, Collection<V1>> entry, final Map<K2, Collection<V1>> multimap) {
        multimap.computeIfAbsent(entry.getKey(), this::newBucket).addAll(entry.getValue());
    }

    protected <T> Collection<T> newBucket(final K2 key) {
        return Collections.synchronizedList(new ArrayList<>()); //FIXME
    }

    private CompletableFuture<Map<K2, V2>> reduceAll(final Map<K2, ? extends Collection<V1>> multimap) {
        final Map<K2, V2> reductions = new ConcurrentHashMap<>();
        final List<CompletableFuture<?>> futures = multimap.entrySet()
                .stream()
                .map(entry -> this.reduce(entry, reductions))
                .collect(toList());
        return CompletableFuture.allOf(futures.toArray(FUTURES)).thenApply(v -> reductions);
    }

    private CompletableFuture<?> reduce(final Map.Entry<K2, ? extends Collection<V1>> entry, final Map<K2, V2> reductions) {
        return CompletableFuture.runAsync(() -> {
            final V2 reduced = this.doReduce(entry.getValue());
            reductions.put(entry.getKey(), reduced);
        });
    }

    private V2 doReduce(final Collection<V1> values) {
        return reduce.apply(values);
    }

}
