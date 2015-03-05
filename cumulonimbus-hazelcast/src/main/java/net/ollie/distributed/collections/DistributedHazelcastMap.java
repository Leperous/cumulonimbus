package net.ollie.distributed.collections;

import java.io.Closeable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyPredicate;
import com.hazelcast.mapreduce.KeyValueSource;

import net.ollie.distributed.functions.SerializableBiFunction;
import net.ollie.distributed.serialization.MustSerialize;

/**
 * A distributed map that can additionally construct a {@link KeyValueSource}.
 *
 * @author Ollie
 */
@MustSerialize
public interface DistributedHazelcastMap<K, V>
        extends DistributedMap<K, V>, Closeable {

    @Nonnull
    default KeyValueSource<K, V> source() {
        return new DistributedKeyValueSource<>(this);
    }

    @CheckReturnValue
    default DistributedHazelcastMap<K, V> filter(final KeyPredicate<K> predicate) {
        return new DistributedFilteredHazelcastMap<>(this, predicate);
    }

    @CheckReturnValue
    default <V2, V3> DistributedHazelcastMap<K, V3> compose(final DistributedHazelcastMap<K, V2> that, final SerializableBiFunction<? super V, ? super V2, ? extends V3> merge) {
        return new DistributedMergeValueMap<>(this, that, merge);
    }

    @Override
    void close();

    static <K, V> DistributedHazelcastMap<K, V> unmodifiable(final IMap<K, V> map) {
        return new DistributedUnmodifiableHazelcastMap<>(mutable(map));
    }

    static <K, V> DistributedIMap<K, V> mutable(final IMap<K, V> map) {
        return new DistributedIMap<>(map);
    }

    static <K, V1, V2, V> DistributedHazelcastMap<K, V> of(final IMap<K, V1> left, final IMap<K, V2> right, final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        return unmodifiable(left).compose(unmodifiable(right), merge);
    }

}
