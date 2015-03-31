package net.ollie.distributed.phases;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collector;

import javax.annotation.Nonnull;

import net.ollie.distributed.utils.Lists;

/**
 *
 * @author Ollie
 */
public interface CollectPhase<F, T> extends Phase<Collection<? extends F>, T> {

    @Nonnull
    default Collection<T> flatMap(@Nonnull final Collection<? extends F> collection) {
        return Lists.serialTransform(collection, object -> this.transform(Collections.singleton(object)));
    }

    /**
     * Convert a {@link Collector} into a serial collect phase.
     */
    static <F, I, T> CollectPhase<F, T> serial(final Collector<? super F, I, ? extends T> collector) {
        return collection -> collection.stream().collect(collector);
    }

}
