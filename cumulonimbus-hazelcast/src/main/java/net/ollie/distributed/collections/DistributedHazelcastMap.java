package net.ollie.distributed.collections;

import java.io.Closeable;

import javax.annotation.Nonnull;

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

    default DistributedHazelcastMap<K, V> filter(final KeyPredicate<K> predicate) {
        return new DistributedFilteredHazelcastMap<>(this, predicate);
    }

    default <V2, V3> DistributedHazelcastMap<K, V3> compose(final DistributedHazelcastMap<K, V2> that, final SerializableBiFunction<V, V2, V3> merge) {
        return new DistributedMergeValueMap<>(this, that, merge);
    }

    @Override
    void close();

}
