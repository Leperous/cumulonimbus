package net.ollie.distributed.hazelcast.functions;

import java.io.Serializable;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Ollie
 */
public interface SerializableFunction<F, T> extends Function<F, T>, Serializable {

    @Override
    @CheckForNull
    T apply(@Nonnull F t);

}
