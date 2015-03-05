package net.ollie.distributed.serialization;

import java.io.IOException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

/**
 *
 * @author Ollie
 */
public class IMapSerializer implements StreamSerializer<IMap<?, ?>> {

    private final int typeId;
    private final HazelcastInstance hazelcastInstance;

    public IMapSerializer(final int typeId, final HazelcastInstance hazelcastInstance) {
        this.typeId = typeId;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void write(final ObjectDataOutput out, final IMap<?, ?> object) throws IOException {
        if (object == null) {
            out.writeUTF(null);
        } else {
            out.writeUTF(object.getName());
        }
    }

    @Override
    public IMap<?, ?> read(final ObjectDataInput in) throws IOException {
        final String name = in.readUTF();
        return name == null ? null : hazelcastInstance.getMap(name);
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void destroy() {
    }

}
