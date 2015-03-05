package net.ollie.distributed.utils;

import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import java.util.stream.StreamSupport;

/**
 *
 * @author Ollie
 */
public final class Lists {

    private Lists() {
    }

    public static <F, T> List<T> transform(final Iterable<? extends F> iterable, final Function<? super F, ? extends T> transform) {
        return StreamSupport.stream(iterable.spliterator(), false).map(transform).collect(toList());
    }

}
