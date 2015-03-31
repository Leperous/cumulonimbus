package net.ollie.distributed.phases;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import net.ollie.distributed.utils.Lists;

/**
 *
 * @author Ollie
 */
public interface BarrierPhase<F, T> extends FuturePhase<Collection<? extends Future<F>>, T> {

    @Nonnull
    default Collection<CompletableFuture<T>> flatMap(@Nonnull final Collection<? extends Future<F>> collection) {
        return Lists.serialTransform(collection, object -> this.transform(Collections.singleton(object)));
    }

}
