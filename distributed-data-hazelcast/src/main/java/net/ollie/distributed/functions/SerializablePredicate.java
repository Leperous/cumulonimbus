package net.ollie.distributed.functions;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 *
 * @author Ollie
 */
public interface SerializablePredicate<T> extends Predicate<T>, Serializable {

}
