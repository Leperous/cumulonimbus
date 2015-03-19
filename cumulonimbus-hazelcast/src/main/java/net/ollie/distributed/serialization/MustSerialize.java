package net.ollie.distributed.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that Hazelcast must be able to serialize the annotated class and any/all of its subclasses.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustSerialize {

}
