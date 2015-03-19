package net.ollie.distributed.functions;

import java.io.Serializable;
import java.util.function.Predicate;

import com.hazelcast.mapreduce.KeyPredicate;

/**
 *
 * @author Ollie
 */
public interface SerializablePredicate<T> extends Predicate<T>, Serializable, KeyPredicate<T> {

    @Override
    default boolean evaluate(final T key) {
        return this.test(key);
    }

}
