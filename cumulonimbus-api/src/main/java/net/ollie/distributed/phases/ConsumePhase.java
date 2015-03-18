package net.ollie.distributed.phases;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 *
 * @author Ollie
 */
public interface ConsumePhase<T> extends Phase<T, T> {

    void consume(T object);

    @Override
    default T transform(final T from) {
        this.consume(from);
        return from;
    }

    static <T> ConsumePhase<T> async(final Consumer<? super T> consumer, final Executor executor) {
        return object -> executor.execute(() -> consumer.accept(object));
    }

}
