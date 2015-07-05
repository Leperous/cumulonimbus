package net.ollie.distributed.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that instances of the annotated class and any/all of its subclasses are distributed, and therefore must be
 * distributable by some scheme.
 *
 * In practice this normally means that instances should be serializable.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustDistribute {

}
