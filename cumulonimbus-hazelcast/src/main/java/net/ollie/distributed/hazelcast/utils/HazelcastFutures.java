package net.ollie.distributed.hazelcast.utils;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.ICompletableFuture;

/**
 *
 * @author Ollie
 */
public interface HazelcastFutures {

    @Nonnull
    default <T> CompletableFuture<T> convertFuture(@Nonnull final ICompletableFuture<T> input) {
        final CompletableFuture<T> output = new CompletableFuture<>();
        input.andThen(new ExecutionCallback<T>() {

            @Override
            public void onResponse(final T response) {
                output.complete(response);
            }

            @Override
            public void onFailure(final Throwable t) {
                output.completeExceptionally(t);
            }

        });
        return output;
    }

}
