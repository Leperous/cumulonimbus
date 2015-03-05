package net.ollie.distributed.functions;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 *
 * @author Ollie
 */
public interface SerializableBiFunction<A, B, T> extends BiFunction<A, B, T>, Serializable {

}
