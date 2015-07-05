package net.ollie.distributed.hazelcast.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that instances of the annotated class do not themselves serialize, but are instead made available on all
 * nodes, perhaps via some dependency injection mechanism.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustBeLocallyAvailable {

}
