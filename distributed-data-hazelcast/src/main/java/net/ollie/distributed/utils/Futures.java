package net.ollie.distributed.utils;

import java.util.concurrent.CompletableFuture;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.ICompletableFuture;

/**
 *
 * @author Ollie
 */
public final class Futures {

    private Futures() {
    }

    public static <T> CompletableFuture<T> convert(final ICompletableFuture<T> input) {
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
