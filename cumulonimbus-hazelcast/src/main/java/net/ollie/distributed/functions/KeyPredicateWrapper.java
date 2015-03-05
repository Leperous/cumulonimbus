package net.ollie.distributed.functions;

import com.hazelcast.mapreduce.KeyPredicate;

/**
 *
 * @author Ollie
 */
public class KeyPredicateWrapper<T> implements KeyPredicate<T> {

    private static final long serialVersionUID = 1L;

    private final SerializablePredicate<? super T> delegate;

    public KeyPredicateWrapper(final SerializablePredicate<? super T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean evaluate(final T key) {
        return delegate.test(key);
    }

}
