package net.ollie.distributed.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Ollie
 */
public final class Streams {

    private Streams() {
    }

    public static <T> Stream<T> serialStream(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
