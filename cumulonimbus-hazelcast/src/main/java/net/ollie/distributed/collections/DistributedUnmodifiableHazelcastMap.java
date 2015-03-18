package net.ollie.distributed.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public class DistributedUnmodifiableHazelcastMap<K, V>
        implements DistributedHazelcastMap<K, V>, DataSerializable {

    private DistributedHazelcastMap<K, ? extends V> delegate;

    @Deprecated
    DistributedUnmodifiableHazelcastMap() {
    }

    public DistributedUnmodifiableHazelcastMap(final DistributedHazelcastMap<K, ? extends V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Set<K> copyKeys() {
        return Collections.unmodifiableSet(delegate.copyKeys());
    }

    @Override
    public V get(K key) {
        return delegate.get(key);
    }

    @Override
    public void evict(final Collection<K> keys) {
        delegate.evict(keys);
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeObject(delegate);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        delegate = in.readObject();
    }

}
