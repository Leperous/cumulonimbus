package net.ollie.distributed.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 * Wrapper around a Hazelcast {@link IMap}.
 *
 * @author Ollie
 */
public class DistributedIMap<K, V>
        implements DistributedHazelcastMap<K, V>, MutableDistributedMap<K, V>, HazelcastInstanceAware, DataSerializable {

    private String mapName;
    private transient volatile IMap<K, V> delegate;

    @Deprecated
    DistributedIMap() {
    }

    public DistributedIMap(@Nonnull final IMap<K, V> delegate) {
        this.mapName = delegate.getName();
        this.delegate = delegate;
    }

    @Override
    public V get(final K key) {
        return delegate.get(key);
    }

    @Override
    public Map<K, V> localMap() {
        return new HashMap<>(delegate);
    }

    @Override
    public Map<K, V> localMap(final Set<K> keys) {
        return delegate.getAll(keys);
    }

    @Override
    public Set<K> localKeys() {
        return delegate.keySet();
    }

    @Override
    public KeyValueSource<K, V> source() {
        return KeyValueSource.fromMap(delegate);
    }

    @Override
    public long size() {
        return delegate.size();
    }

    @Override
    public V put(final K key, V value) {
        return delegate.put(key, value);
    }

    @Override
    public void delete(final Object key) {
        delegate.delete(key);
    }

    @Override
    public void write(final K key, final V value) {
        delegate.set(key, value);
    }

    @Override
    public boolean replace(K key, V expected, V newValue) {
        return delegate.replace(key, expected, newValue);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public void close() {
        this.clear();
    }

    @Override
    public void evict(final Collection<K> keys) {
        keys.forEach(delegate::delete);
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeUTF(mapName);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        mapName = in.readUTF();
    }

    @Override
    public void setHazelcastInstance(final HazelcastInstance hazelcastInstance) {
        delegate = hazelcastInstance.getMap(mapName);
    }

}
