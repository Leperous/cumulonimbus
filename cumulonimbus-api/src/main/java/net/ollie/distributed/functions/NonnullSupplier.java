package net.ollie.distributed.functions;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
@FunctionalInterface
public interface NonnullSupplier<T> extends Supplier<T> {

    @Nonnull
    @Override
    T get();

}
