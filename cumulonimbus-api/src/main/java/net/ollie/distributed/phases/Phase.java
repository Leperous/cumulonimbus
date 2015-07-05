package net.ollie.distributed.phases;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import net.ollie.distributed.serialization.MustNotDistribute;

/**
 *
 * @author Ollie
 */
@MustNotDistribute
public interface Phase<F, T> {

    @Nonnull
    T transform(@Nonnull F from);

    @Nonnull
    @CheckReturnValue
    default <T2> Phase<F, T2> then(final Phase<? super T, ? extends T2> that) {
        return input -> that.transform(this.transform(input));
    }

    @Nonnull
    @CheckReturnValue
    default <T2> FuturePhase<F, T2> later(final FuturePhase<? super T, T2> that) {
        return input -> that.transform(this.transform(input));
    }

    static <T> Phase<T, T> identity() {
        return object -> object;
    }

}
