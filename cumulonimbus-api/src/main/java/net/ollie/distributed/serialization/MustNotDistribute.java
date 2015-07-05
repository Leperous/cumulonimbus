package net.ollie.distributed.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that instances of annotated classes must not be distributed; that is, they only live on one host.
 *
 * In practice this normally means that instances should not be serializable.
 *
 * @author Ollie
 */
@Target(ElementType.TYPE)
public @interface MustNotDistribute {

}
