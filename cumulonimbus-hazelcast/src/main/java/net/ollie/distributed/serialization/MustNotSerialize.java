package net.ollie.distributed.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that Java or Hazelcast must not attempt to serialize the annotated class or any of its subclasses.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustNotSerialize {

}
