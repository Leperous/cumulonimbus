package net.ollie.distributed.phases;

/**
 * A phase that will consume and object, and return null.
 *
 * @author Ollie
 */
public interface ConsumePhase<F> extends Phase<F, Void> {

    void consume(F from);

    @Override
    public default Void transform(final F from) {
        this.consume(from);
        return null;
    }

}
