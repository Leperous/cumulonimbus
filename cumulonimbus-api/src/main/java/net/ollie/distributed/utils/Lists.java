package net.ollie.distributed.utils;

import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Ollie
 */
public final class Lists {

    private Lists() {
    }

    public static <F, T> List<T> serialTransform(final Iterable<? extends F> iterable, final Function<? super F, ? extends T> transform) {
        return Streams.serialStream(iterable).map(transform).collect(toList());
    }

}
