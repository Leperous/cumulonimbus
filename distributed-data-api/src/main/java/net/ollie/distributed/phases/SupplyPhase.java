package net.ollie.distributed.phases;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public interface SupplyPhase<T> extends Phase<Object, T> {

    @Nonnull
    T supply();

    @Override
    @Deprecated
    default T transform(final Object from) {
        return this.supply();
    }

}
