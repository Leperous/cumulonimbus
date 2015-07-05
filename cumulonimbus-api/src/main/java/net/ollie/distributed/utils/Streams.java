package net.ollie.distributed.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public final class Streams {

    private Streams() {
    }

    public static <T> Stream<T> serialStream(@Nonnull final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> parallelStream(@Nonnull final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

}
