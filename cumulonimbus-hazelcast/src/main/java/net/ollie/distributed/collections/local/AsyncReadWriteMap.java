package net.ollie.distributed.collections.local;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.CheckForNull;

import com.hazelcast.core.IMap;

/**
 * Asynchronously reads and writes from/to Hazelcast.
 *
 * @author Ollie
 */
public class AsyncReadWriteMap<K, V>
        extends AsyncReadMap<K, V> {

    private final Function<? super K, ? extends V> read;
    private final BiConsumer<? super K, ? super V> write;

    protected AsyncReadWriteMap(
            final IMap<? extends K, ? extends V> distributedMap,
            final Function<? super K, ? extends V> read,
            final BiConsumer<? super K, ? super V> write) {
        super(distributedMap);
        this.read = read;
        this.write = write;
    }

    @Override
    public V get(final K key) {
        final V got = super.get(key);
        return got == null
                ? this.load(key)
                : got;
    }

    @CheckForNull
    private V load(final K key) {
        final V loaded = read.apply(key);
        if (loaded != null) {
            this.delegate().putIfAbsent(key, loaded);
            write.accept(key, loaded);
        }
        return loaded;
    }

}
