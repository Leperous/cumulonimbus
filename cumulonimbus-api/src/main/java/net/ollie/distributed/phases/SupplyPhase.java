package net.ollie.distributed.phases;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

import net.ollie.distributed.functions.NonnullSupplier;

/**
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
    default <R> SupplyPhase<R> andThen(@Nonnull final Phase<? super T, ? extends R> that) {
        return () -> that.transform(this.get());
    }

    @Override
    default <R> SupplyFuturePhase<R> andThen(@Nonnull final FuturePhase<? super T, R> that) {
        return () -> that.transform(this.get());
    }

    interface SupplyFuturePhase<T> extends SupplyPhase<CompletableFuture<T>>, FuturePhase<Object, T> {

        @Override
        default <X> SupplyFuturePhase<X> andLater(@Nonnull final Phase<? super T, ? extends X> that) {
            return () -> this.get().thenApply(that::transform);
        }

        @Override
        default <X> SupplyFuturePhase<X> andLater(final Phase<? super T, ? extends X> that, @Nonnull final Executor executor) {
            return () -> this.get().thenApplyAsync(that::transform, executor);
        }

    }

}
