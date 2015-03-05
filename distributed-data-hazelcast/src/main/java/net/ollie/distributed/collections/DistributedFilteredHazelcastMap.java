package net.ollie.distributed.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;

import com.hazelcast.mapreduce.KeyPredicate;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public class DistributedFilteredHazelcastMap<K, V>
        implements DistributedHazelcastMap<K, V>, DataSerializable {

    private DistributedHazelcastMap<K, V> delegate;
    private KeyPredicate<? super K> predicate;

    public DistributedFilteredHazelcastMap(final DistributedHazelcastMap<K, V> delegate, final KeyPredicate<? super K> predicate) {
        this.delegate = delegate;
        this.predicate = predicate;
    }

    @Override
    public V get(final K key) {
        return predicate.evaluate(key)
                ? delegate.get(key)
                : null;
    }

    @Override
    public Map<K, V> localMap() {
        final Map<K, V> local = delegate.localMap();
        local.keySet().removeIf(predicate::evaluate);
        return local;
    }

    @Override
    public Set<K> localKeys() {
        final Set<K> keys = delegate.localKeys();
        keys.removeIf(predicate::evaluate);
        return keys;
    }

    @Override
    public void evict(final Collection<K> keys) {
        final Collection<K> filtered = keys.stream().filter(predicate::evaluate).collect(toList());
        delegate.evict(filtered);
    }

    @Override
    public void close() {
        this.evict(this.localKeys());
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeObject(delegate);
        out.writeObject(predicate);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        delegate = in.readObject();
        predicate = in.readObject();
    }

}
