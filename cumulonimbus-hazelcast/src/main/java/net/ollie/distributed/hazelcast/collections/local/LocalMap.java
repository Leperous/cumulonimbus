package net.ollie.distributed.hazelcast.collections.local;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.hazelcast.core.IMap;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.hazelcast.serialization.LocalMapStreamSerializer;
import net.ollie.distributed.hazelcast.serialization.MustBeLocallyAvailable;

/**
 *
 * @author Ollie
 * @see LocalMapStreamSerializer A special stream serializer must be registered against this instance.
 */
@MustBeLocallyAvailable
public interface LocalMap<K, V> extends HazelcastMap<K, V> {

    @Nonnull
    String id();

    static <K, V> LocalMap<K, V> asyncReadOnly(final IMap<? extends K, ? extends V> map) {
        return new AsyncReadMap<>(map);
    }

    static <K, V> LocalMap<K, V> asyncReadWrite(
            final IMap<? extends K, ? extends V> distributedMap,
            final Function<? super K, ? extends V> read,
            final BiConsumer<? super K, ? super V> write) {
        return new AsyncReadWriteMap<>(distributedMap, read, write);
    }

}
