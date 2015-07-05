package net.ollie.distributed.utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public final class Lists {

    private Lists() {
    }

    public static <F, T> List<T> serialTransform(
            @Nonnull final Iterable<? extends F> iterable,
            @Nonnull final Function<? super F, ? extends T> transform) {
        return Streams.serialStream(iterable).map(transform).collect(toList());
    }

    public static <F, T> List<T> parallelTransform(
            @Nonnull final Iterable<? extends F> iterable,
            @Nonnull final Function<? super F, ? extends T> transform) {
        return Streams.parallelStream(iterable).map(transform).collect(toList());
    }

    public static <T> List<T> filter(final Iterable<? extends T> collection, final Predicate<? super T> predicate) {
        return Streams.serialStream(collection).filter(predicate).map(o -> (T) o).collect(toList());
    }

}
