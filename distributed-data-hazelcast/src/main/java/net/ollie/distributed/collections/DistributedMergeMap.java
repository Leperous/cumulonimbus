package net.ollie.distributed.collections;

import java.io.IOException;
import static java.lang.Math.addExact;
import static java.lang.Math.toIntExact;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import net.ollie.distributed.functions.SerializableBiFunction;

/**
 *
 * @author Ollie
 */
public class DistributedMergeMap<K, A, B, V>
        implements DistributedHazelcastMap<K, V>, DataSerializable {

    private DistributedHazelcastMap<K, A> left;
    private DistributedHazelcastMap<K, B> right;
    private SerializableBiFunction<? super A, ? super B, ? extends V> merge;

    public DistributedMergeMap(
            final DistributedHazelcastMap<K, A> left,
            final DistributedHazelcastMap<K, B> right,
            final SerializableBiFunction<? super A, ? super B, ? extends V> merge) {
        this.left = left;
        this.right = right;
        this.merge = merge;
    }

    @Override
    public V get(final K key) {
        return merge.apply(left.get(key), right.get(key));
    }

    @Override
    public Map<K, V> localMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> localKeys() {
        final Set<K> keys = new HashSet<>(toIntExact(addExact(left.size(), right.size())));
        keys.addAll(left.localKeys());
        keys.addAll(right.localKeys());
        return keys;
    }

    @Override
    public KeyValueSource<K, V> source() {
        throw new UnsupportedOperationException();
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
