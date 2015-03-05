package net.ollie.distributed.collections;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hazelcast.core.PartitionService;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.mapreduce.PartitionIdAware;
import com.hazelcast.mapreduce.impl.MapKeyValueSource;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.spi.NodeEngine;

/**
 *
 * @author Ollie
 * @see MapKeyValueSource
 */
public class DistributedKeyValueSource<K, V>
        extends KeyValueSource<K, V>
        implements PartitionIdAware, DataSerializable {

    private DistributedHazelcastMap<K, ? extends V> source;
    private transient int partitionId;
    private transient K nextKey;
    private transient Iterator<K> partitionKeys;
    private transient Map<Integer, List<K>> partitions;

    public DistributedKeyValueSource(final DistributedHazelcastMap<K, ? extends V> source) {
        this.source = source;
    }

    @Override
    public boolean open(final NodeEngine ne) {
        if (partitions == null) {
            partitions = partition(source.localKeys(), ne.getHazelcastInstance().getPartitionService());
        }
        partitionKeys = partitions.getOrDefault(partitionId, Collections.emptyList()).iterator();
        return true;
    }

    private static <K> Map<Integer, List<K>> partition(final Set<K> source, final PartitionService partitionService) {
        final Map<Integer, List<K>> partitions = new HashMap<>();
        source.forEach(key -> {
            final int partitionId = partitionService.getPartition(key).getPartitionId();
            partitions.computeIfAbsent(partitionId, p -> new ArrayList<>()).add(key);
        });
        return partitions;
    }

    @Override
    public boolean hasNext() {
        if (partitionKeys.hasNext()) {
            nextKey = partitionKeys.next();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public K key() {
        return nextKey;
    }

    @Override
    public Map.Entry<K, V> element() {
        return new AbstractMap.SimpleImmutableEntry<>(nextKey, source.get(nextKey));
    }

    @Override
    public boolean reset() {
        nextKey = null;
        partitionKeys = null;
        return true;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void setPartitionId(final int partitionId) {
        this.partitionId = partitionId;
    }

    @Override
    public boolean isAllKeysSupported() {
        return true;
    }

    @Override
    protected Collection<K> getAllKeys0() {
        return source.localKeys();
    }

    @Override
    public void writeData(final ObjectDataOutput out) throws IOException {
        out.writeObject(source);
    }

    @Override
    public void readData(final ObjectDataInput in) throws IOException {
        source = in.readObject();
    }

}
