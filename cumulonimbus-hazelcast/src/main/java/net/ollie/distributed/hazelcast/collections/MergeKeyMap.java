package net.ollie.distributed.hazelcast.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import net.ollie.distributed.hazelcast.collections.local.LocalMap;
import net.ollie.distributed.hazelcast.functions.SerializableBiFunction;
import net.ollie.distributed.utils.Lists;

/**
 *
 * @author Ollie
 */
public class MergeKeyMap<K, V, V1, K2, V2>
        implements HazelcastMap<K, V>, DataSerializable {

    private HazelcastMap<K, V1> map;
    private LocalMap<K2, V2> localMap;
    private SerializableBiFunction<? super K, ? super V1, ? extends K2> localKeyFunction;
    private SerializableBiFunction<? super V1, ? super V2, ? extends V> valueFunction;

    @Deprecated
    MergeKeyMap() {
    }

    public MergeKeyMap(
            final HazelcastMap<K, V1> map,
            final LocalMap<K2, V2> localMap,
            final SerializableBiFunction<? super K, ? super V1, ? extends K2> localKeyFunction,
            final SerializableBiFunction<? super V1, ? super V2, ? extends V> valueFunction) {
        this.map = map;
        this.localMap = localMap;
        this.localKeyFunction = localKeyFunction;
        this.valueFunction = valueFunction;
    }

    @Override
    public Set<K> copyKeys() {
        return map.copyKeys();
    }

    @Override
    public void evict(final Collection<K> keys) {
        map.evict(keys);
        final Collection<K2> localKeys = Lists.serialTransform(keys, this::localKey);
        localMap.evict(localKeys);
    }

    protected K2 localKey(final K key) {
        final V1 value = map.get(key);
        return localKeyFunction.apply(key, value);
    }

    @Override
    public V get(final K key) {
        final V1 value = map.get(key);
        final K2 localKey = localKeyFunction.apply(key, value);
        final V2 localValue = localKey == null ? null : localMap.get(localKey);
        return valueFunction.apply(value, localValue);
    }

    @Override
    public void close() {
        map.close();
        localMap.close();
    }

    @Override
    public String toString() {
        return "Merge keys: [" + map + "] & [" + localMap + "]";
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeObject(map);
        out.writeObject(localMap);
        out.writeObject(localKeyFunction);
        out.writeObject(valueFunction);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        map = in.readObject();
        localMap = in.readObject();
        localKeyFunction = in.readObject();
        valueFunction = in.readObject();
    }

}
