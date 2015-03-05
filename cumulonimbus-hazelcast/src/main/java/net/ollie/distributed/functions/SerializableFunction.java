package net.ollie.distributed.functions;

import java.io.Serializable;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public interface SerializableFunction<F, T> extends Function<F, T>, Serializable {

    @Override
    @CheckForNull
    T apply(@Nonnull F t);

}
