package net.ollie.distributed.phases;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * A phase that can inspect objects, returning them afterwards
 *
 * @author Ollie
 */
public interface PeekPhase<T> extends Phase<T, T> {

    void peek(T object);

    @Override
    default T transform(final T from) {
        this.peek(from);
        return from;
    }

    static <T> PeekPhase<T> async(final Executor executor, final Consumer<? super T> consumer) {
        return object -> executor.execute(() -> consumer.accept(object));
    }

}
