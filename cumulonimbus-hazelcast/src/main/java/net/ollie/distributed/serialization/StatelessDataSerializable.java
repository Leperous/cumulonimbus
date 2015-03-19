package net.ollie.distributed.serialization;

import java.io.IOException;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public interface StatelessDataSerializable extends DataSerializable {

    @Override
    default void writeData(final ObjectDataOutput out) throws IOException {
    }

    @Override
    default void readData(final ObjectDataInput in) throws IOException {
    }

}
