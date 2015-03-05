package net.ollie.distributed.functions;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
@FunctionalInterface
public interface NonnullSupplier<T> {

    @Nonnull
    T get();

}
