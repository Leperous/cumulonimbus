package net.ollie.distributed.phases;

import java.util.concurrent.CompletableFuture;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public interface Phase<F, T> {

    @Nonnull
    T transform(@Nonnull F from);

    @Nonnull
    @CheckReturnValue
    default <X> Phase<F, X> andThen(final Phase<? super T, ? extends X> that) {
        return input -> that.transform(this.transform(input));
    }

    @Nonnull
    @CheckReturnValue
    default <X> FuturePhase<F, X> andAfter(final Phase<? super T, ? extends CompletableFuture<X>> that) {
        return input -> that.transform(this.transform(input));
    }

    static <T> Phase<T, T> identity() {
        return object -> object;
    }

}
