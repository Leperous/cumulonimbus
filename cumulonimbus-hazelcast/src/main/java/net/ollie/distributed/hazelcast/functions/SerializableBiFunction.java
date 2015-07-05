package net.ollie.distributed.hazelcast.functions;

import java.io.Serializable;
import java.util.function.BiFunction;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import net.ollie.distributed.serialization.MustDistribute;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface SerializableBiFunction<A, B, T> extends BiFunction<A, B, T>, Serializable {

    @Override
    @CheckForNull
    T apply(@Nullable A left, @Nullable B right);

}
