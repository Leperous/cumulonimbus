package net.ollie.distributed.phases;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

import net.ollie.distributed.functions.NonnullSupplier;

/**
 * A phase that can supply objects without requiring input.
 *
 * @author Ollie
 */
public interface SupplyPhase<T> extends Phase<Object, T>, NonnullSupplier<T> {

    @Nonnull
    @Override
    T get();

    @Override
    @Deprecated
    default T transform(final Object from) {
        return this.get();
    }

    @Override
    default <R> SupplyPhase<R> then(@Nonnull final Phase<? super T, ? extends R> that) {
        return () -> that.transform(this.get());
    }

    @Override
    default <R> SupplyFuturePhase<R> later(@Nonnull final FuturePhase<? super T, R> that) {
        return () -> that.transform(this.get());
    }

    /**
     * Supplies an object at some point in the future.
     *
     * @param <T>
     */
    interface SupplyFuturePhase<T> extends SupplyPhase<CompletableFuture<T>>, FuturePhase<Object, T> {

        @Override
        default <X> SupplyFuturePhase<X> later(@Nonnull final Phase<? super T, ? extends X> that) {
            return () -> this.get().thenApply(that::transform);
        }

        @Override
        default <X> SupplyFuturePhase<X> later(final Phase<? super T, ? extends X> that, @Nonnull final Executor executor) {
            return () -> this.get().thenApplyAsync(that::transform, executor);
        }

    }

    interface ExceptionalSupplyPhase<T, X extends Exception> extends SupplyPhase<T>, ExceptionalPhase<Object, T, X> {

        T getExceptionally() throws X;

        @Override
        default T get() {
            return this.transform(null);
        }

        @Override
        @Deprecated
        default T transform(final Object object) {
            return ExceptionalPhase.super.transform(object);
        }

        @Override
        default T transformChecked(final Object from) throws X {
            return this.getExceptionally();
        }

    }

}
