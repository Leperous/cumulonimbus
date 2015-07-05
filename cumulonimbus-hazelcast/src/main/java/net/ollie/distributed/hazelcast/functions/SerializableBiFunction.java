package net.ollie.distributed.hazelcast.functions;

import java.io.Serializable;
import java.util.function.BiFunction;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public interface SerializableBiFunction<A, B, T> extends BiFunction<A, B, T>, Serializable {

    @Override
    @CheckForNull
    T apply(@Nullable A left, @Nullable B right);

}
