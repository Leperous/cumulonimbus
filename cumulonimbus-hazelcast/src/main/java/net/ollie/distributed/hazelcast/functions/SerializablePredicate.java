package net.ollie.distributed.hazelcast.functions;

import java.io.Serializable;
import java.util.function.Predicate;

import com.hazelcast.mapreduce.KeyPredicate;

import net.ollie.distributed.serialization.MustDistribute;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface SerializablePredicate<T> extends Predicate<T>, KeyPredicate<T>, Serializable {

    @Override
    default boolean evaluate(final T key) {
        return this.test(key);
    }

}
