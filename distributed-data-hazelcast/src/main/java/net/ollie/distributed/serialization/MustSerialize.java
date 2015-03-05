package net.ollie.distributed.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that Hazelcast must be able to serialize any and all concrete
 * subclasses of this class.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustSerialize {

}
