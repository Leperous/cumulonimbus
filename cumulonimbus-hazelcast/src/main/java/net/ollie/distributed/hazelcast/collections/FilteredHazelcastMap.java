package net.ollie.distributed.hazelcast.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.hazelcast.mapreduce.KeyPredicate;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import net.ollie.distributed.utils.Lists;

/**
 *
 * @author Ollie
 */
public class FilteredHazelcastMap<K, V>
        implements HazelcastMap<K, V>, DataSerializable {

    private HazelcastMap<K, V> delegate;
    private KeyPredicate<? super K> predicate;

    public FilteredHazelcastMap(final HazelcastMap<K, V> delegate, final KeyPredicate<? super K> predicate) {
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
    public Map<K, V> copyMap() {
        final Map<K, V> local = delegate.copyMap();
        local.keySet().removeIf(predicate::evaluate);
        return local;
    }

    @Override
    public Set<K> copyKeys() {
        final Set<K> keys = delegate.copyKeys();
        keys.removeIf(predicate::evaluate);
        return keys;
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty() || HazelcastMap.super.isEmpty();
    }

    @Override
    public void evict(final Collection<K> keys) {
        final Collection<K> filtered = Lists.filter(keys, predicate::evaluate);
        delegate.evict(filtered);
    }

    @Override
    public void close() {
        this.evict(this.copyKeys());
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
