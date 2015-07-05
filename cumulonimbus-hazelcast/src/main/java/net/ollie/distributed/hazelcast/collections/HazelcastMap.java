package net.ollie.distributed.hazelcast.collections;

import java.io.Closeable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyPredicate;
import com.hazelcast.mapreduce.KeyValueSource;

import net.ollie.distributed.collections.DistributedMap;
import net.ollie.distributed.hazelcast.functions.SerializableBiFunction;
import net.ollie.distributed.serialization.MustDistribute;

/**
 * A distributed Hazelcast map that can additionally construct a {@link KeyValueSource}.
 *
 * This will ultimately hang of one or more {@link IMap} constructs.
 *
 * @author Ollie
 */
@MustDistribute
public interface HazelcastMap<K, V>
        extends DistributedMap<K, V>, Closeable {

    @Nonnull
    default KeyValueSource<K, V> source() {
        return new DistributedKeyValueSource<>(this);
    }

    @CheckReturnValue
    default HazelcastMap<K, V> filter(final KeyPredicate<? super K> predicate) {
        return new FilteredHazelcastMap<>(this, predicate);
    }

    @CheckReturnValue
    default <V2, V3> HazelcastMap<K, V3> compose(final HazelcastMap<K, V2> that, final SerializableBiFunction<? super V, ? super V2, ? extends V3> merge) {
        return new MergeValueMap<>(this, that, merge);
    }

    @Override
    void close();

    static <K, V> HazelcastMap<K, V> unmodifiable(final IMap<K, V> map) {
        return new UnmodifiableHazelcastMap<>(mutable(map));
    }

    static <K, V> HazelcastIMap<K, V> mutable(final IMap<K, V> map) {
        return new HazelcastIMap<>(map);
    }

    static <K, V1, V2, V> HazelcastMap<K, V> of(final IMap<K, V1> left, final IMap<K, V2> right, final SerializableBiFunction<? super V1, ? super V2, ? extends V> merge) {
        return unmodifiable(left).compose(unmodifiable(right), merge);
    }

}
