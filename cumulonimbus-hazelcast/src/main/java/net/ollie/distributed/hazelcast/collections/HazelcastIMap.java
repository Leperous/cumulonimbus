package net.ollie.distributed.hazelcast.collections;

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

import net.ollie.distributed.collections.MutableDistributedMap;

/**
 * Wrapper around a Hazelcast {@link IMap}.
 *
 * @author Ollie
 */
public class HazelcastIMap<K, V>
        implements HazelcastMap<K, V>, MutableDistributedMap<K, V>, HazelcastInstanceAware, DataSerializable {

    private String mapName;
    private transient volatile IMap<K, V> delegate;

    @Deprecated
    HazelcastIMap() {
    }

    public HazelcastIMap(@Nonnull final IMap<K, V> delegate) {
        this.mapName = delegate.getName();
        this.delegate = delegate;
    }

    @Override
    public V get(final K key) {
        return delegate.get(key);
    }

    @Override
    public Map<K, V> copyMap() {
        return new HashMap<>(delegate);
    }

    @Override
    public <K2 extends K> Map<K2, V> copyMap(final Set<K2> keys) {
        return delegate.getAll((Set) keys);
    }

    @Override
    public Set<K> copyKeys() {
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
    public void writeAll(final Map<? extends K, ? extends V> values) {
        delegate.putAll(values);
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
