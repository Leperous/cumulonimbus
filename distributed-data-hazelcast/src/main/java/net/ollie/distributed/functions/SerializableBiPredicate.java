package net.ollie.distributed.functions;

import java.io.Serializable;
import java.util.function.BiPredicate;

/**
 *
 * @author Ollie
 */
public interface SerializableBiPredicate<A, B> extends BiPredicate<A, B>, Serializable {

    default SerializablePredicate<B> partialLeft(final A first) {
        return new LeftPartial<>(this, first);
    }

    class LeftPartial<A, B> implements SerializablePredicate<B> {

        private static final long serialVersionUID = 1L;

        private final SerializableBiPredicate<? super A, ? super B> outer;
        private final A left;

        public LeftPartial(final SerializableBiPredicate<? super A, ? super B> outer, final A left) {
            this.outer = outer;
            this.left = left;
        }

        @Override
        public boolean test(final B object) {
            return outer.test(left, object);
        }

    }

}
