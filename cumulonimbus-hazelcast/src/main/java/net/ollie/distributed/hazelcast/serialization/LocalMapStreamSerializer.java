package net.ollie.distributed.hazelcast.serialization;

import java.io.IOException;
import java.util.Map;
import static java.util.Objects.requireNonNull;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

import net.ollie.distributed.hazelcast.collections.local.LocalMap;

/**
 *
 * @author Ollie
 * @see LocalMap
 */
public class LocalMapStreamSerializer implements StreamSerializer<LocalMap<?, ?>> {

    private final int typeId;
    private final Map<String, ? extends LocalMap<?, ?>> maps;

    public LocalMapStreamSerializer(final int typeId, final Map<String, ? extends LocalMap<?, ?>> maps) {
        this.typeId = typeId;
        this.maps = maps;
    }

    @Override
    public void write(final ObjectDataOutput out, final LocalMap<?, ?> object) throws IOException {
        final String id = object == null ? null : object.id();
        out.writeUTF(id);
    }

    @Override
    public LocalMap<?, ?> read(final ObjectDataInput in) throws IOException {
        final String id = in.readUTF();
        return id == null
                ? null
                : requireNonNull(maps.get(id), () -> "No local map named [" + id + "] is available on this node!");
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void destroy() {
    }

}
