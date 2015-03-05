package net.ollie.distributed.collections.local;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;

/**
 * Maintains an (asynchronously) updated, read-only map in local memory.
 *
 * The main function of this class is to allow fast distributed map joins where keys would otherwise be on different
 * nodes. This is not only slow to use within a distributed execution, but also Hazelcast does not permit it.
 *
 * This class is not suitable for large, updating maps.
 *
 * @author Ollie
 */
public abstract class LocalAsyncReadMap<K, V>
        implements LocalMap<K, V> {

    private final ConcurrentMap<K, V> localMap;
    private final String id;

    protected LocalAsyncReadMap(@Nonnull final IMap<K, V> distributedMap) {
        this(distributedMap, new ConcurrentHashMap<>(distributedMap.size()));
    }

    protected LocalAsyncReadMap(@Nonnull final IMap<K, V> distributedMap, @Nonnull final ConcurrentMap<K, V> localMap) {
        this.id = distributedMap.getName();
        this.localMap = listenTo(distributedMap);
    }

    private static <K, V> ConcurrentMap<K, V> listenTo(final IMap<K, V> distributedMap) {
        final ConcurrentMap<K, V> map = new ConcurrentHashMap<>(distributedMap.size());
        distributedMap.addEntryListener(new UpdatingEntryListener<>(map), true);
        distributedMap.entrySet().forEach(entry -> map.putIfAbsent(entry.getKey(), entry.getValue()));
        return map;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public V get(final K key) {
        return localMap.get(key);
    }

    protected Map<K, V> delegate() {
        return localMap;
    }

    protected boolean containsKey(final K key) {
        return localMap.containsKey(key);
    }

    @Override
    public Map<K, V> localMap() {
        return new HashMap<>(localMap);
    }

    @Override
    public Set<K> localKeys() {
        return new HashSet<>(localMap.keySet());
    }

    @Override
    public long size() {
        return localMap.size();
    }

    private static final class UpdatingEntryListener<K, V> implements EntryListener<K, V> {

        private final Map<K, V> map;

        UpdatingEntryListener(final Map<K, V> delegate) {
            this.map = delegate;
        }

        @Override
        public void entryAdded(final EntryEvent<K, V> event) {
            map.put(event.getKey(), event.getValue());
        }

        @Override
        public void entryUpdated(final EntryEvent<K, V> event) {
            this.entryAdded(event);
        }

        @Override
        public void entryRemoved(final EntryEvent<K, V> event) {
            map.remove(event.getKey());
        }

        @Override
        public void entryEvicted(final EntryEvent<K, V> event) {
            this.entryRemoved(event);
        }

        @Override
        public void mapCleared(final MapEvent event) {
            map.clear();
        }

        @Override
        public void mapEvicted(final MapEvent event) {
            this.mapCleared(event);
        }

    }

}
