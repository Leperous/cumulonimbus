package net.ollie.distributed.collections;

import java.io.IOException;
import static java.lang.Math.addExact;
import static java.lang.Math.toIntExact;
import java.util.Collection;
import java.util.HashSet;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import javax.annotation.Nonnull;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import net.ollie.distributed.functions.SerializableBiFunction;

/**
 * A merge of two distributed maps with the same key type but different value type.
 *
 * @author Ollie
 */
public class MergeValueMap<K, A, B, V>
        implements HazelcastMap<K, V>, DataSerializable {

    private HazelcastMap<K, A> left;
    private HazelcastMap<K, B> right;
    private SerializableBiFunction<? super A, ? super B, ? extends V> merge;

    @Deprecated
    MergeValueMap() {
    }

    public MergeValueMap(
            @Nonnull final HazelcastMap<K, A> left,
            @Nonnull final HazelcastMap<K, B> right,
            @Nonnull final SerializableBiFunction<? super A, ? super B, ? extends V> merge) {
        this.left = requireNonNull(left);
        this.right = requireNonNull(right);
        this.merge = requireNonNull(merge);
    }

    @Override
    public V get(final K key) {
        return merge.apply(left.get(key), right.get(key));
    }

    @Override
    public Set<K> copyKeys() {
        final Set<K> keys = new HashSet<>(toIntExact(addExact(left.size(), right.size())));
        keys.addAll(left.copyKeys());
        keys.addAll(right.copyKeys());
        return keys;
    }

    @Override
    public void evict(final Collection<K> keys) {
        left.evict(keys);
        right.evict(keys);
    }

    @Override
    public void close() {
        left.close();
        right.close();
    }

    @Override
    public String toString() {
        return "Merge values: [" + left + "] & [" + right + "]";
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeObject(left);
        out.writeObject(right);
        out.writeObject(merge);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        left = in.readObject();
        right = in.readObject();
        merge = in.readObject();
    }

}
