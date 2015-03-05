package net.ollie.distributed.phases;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Transforms into a future.
 *
 * @author Ollie
 */
public interface FuturePhase<F, T> extends Phase<F, CompletableFuture<T>> {

    @Nonnull
    @CheckReturnValue
    default <X> Phase<F, CompletableFuture<X>> andLater(final Phase<? super T, ? extends X> that) {
        return input -> this.transform(input).thenApply(that::transform);
    }

    @Nonnull
    @CheckReturnValue
    default <X> Phase<F, CompletableFuture<X>> andLater(final Phase<? super T, ? extends X> that, @Nonnull final Executor executor) {
        return input -> this.transform(input).thenApplyAsync(that::transform, executor);
    }

    default CheckedPhase<F, T, Exception> andWait(@Nonnull final Duration timeout) {
        return this.andWait(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    default CheckedPhase<F, T, Exception> andWait(@Nonnegative final long timeout, final TimeUnit timeoutUnit) {
        return input -> this.transform(input).get(timeout, timeoutUnit);
    }

}
