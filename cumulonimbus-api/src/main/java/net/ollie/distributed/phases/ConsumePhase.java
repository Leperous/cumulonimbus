package net.ollie.distributed.phases;

/**
 *
 * @author Ollie
 */
public interface ConsumePhase<T> extends Phase<T, Object> {

    void consume(T object);

    @Override
    @Deprecated
    default Object transform(final T from) {
        this.consume(from);
        return CONSUMED;
    }

    Object CONSUMED = new Object();

}
