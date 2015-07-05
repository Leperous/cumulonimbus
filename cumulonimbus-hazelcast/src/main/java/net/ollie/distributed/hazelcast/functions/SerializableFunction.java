package net.ollie.distributed.hazelcast.functions;

import java.io.Serializable;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import net.ollie.distributed.serialization.MustDistribute;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface SerializableFunction<F, T> extends Function<F, T>, Serializable {

    @Override
    @CheckForNull
    T apply(@Nonnull F t);

}
