package net.ollie.distributed.phases;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import net.ollie.distributed.serialization.MustNotDistribute;

/**
 * A phase that transforms into a future result.
 *
 * @author Ollie
 * @param <F> from type
 * @param <T> to type
 */
@MustNotDistribute
public interface FuturePhase<F, T> extends Phase<F, CompletableFuture<T>> {

    @CheckReturnValue
    default <X> FuturePhase<F, X> later(@Nonnull final Phase<? super T, ? extends X> that) {
        return input -> this.transform(input).thenApply(that::transform);
    }

    @CheckReturnValue
    default <X> FuturePhase<F, X> later(@Nonnull final Phase<? super T, ? extends X> that, @Nonnull final Executor executor) {
        return input -> this.transform(input).thenApplyAsync(that::transform, executor);
    }

    @CheckReturnValue
    default ExceptionalPhase<F, T, AsyncException> wait(@Nonnull final Duration timeout) {
        return this.wait(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    @CheckReturnValue
    default ExceptionalPhase<F, T, AsyncException> wait(@Nonnegative final long timeout, @Nonnull final TimeUnit timeoutUnit) {
        return input -> {
            try {
                return this.transform(input).get(timeout, timeoutUnit);
            } catch (final ExecutionException | InterruptedException | TimeoutException tex) {
                throw new AsyncException(tex);
            }
        };
    }

    class AsyncException extends Exception {

        private static final long serialVersionUID = 1L;

        protected AsyncException(final Exception cause) {
            super(cause);
        }

    }

}
