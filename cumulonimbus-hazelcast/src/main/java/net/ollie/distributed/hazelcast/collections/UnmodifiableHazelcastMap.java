package net.ollie.distributed.hazelcast.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import javax.annotation.Nonnull;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public class UnmodifiableHazelcastMap<K, V>
        implements HazelcastMap<K, V>, DataSerializable {

    private HazelcastMap<K, ? extends V> delegate;

    @Deprecated
    UnmodifiableHazelcastMap() {
    }

    public UnmodifiableHazelcastMap(@Nonnull final HazelcastMap<K, ? extends V> delegate) {
        this.delegate = requireNonNull(delegate);
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
    public V get(final K key) {
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
