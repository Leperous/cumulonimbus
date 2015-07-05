package net.ollie.distributed.utils;

import java.util.List;
import java.util.function.Function;
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

}
