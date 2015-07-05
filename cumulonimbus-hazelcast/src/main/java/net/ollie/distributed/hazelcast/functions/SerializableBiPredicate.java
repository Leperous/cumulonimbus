package net.ollie.distributed.hazelcast.functions;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.BiPredicate;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import net.ollie.distributed.serialization.MustDistribute;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface SerializableBiPredicate<A, B> extends BiPredicate<A, B>, Serializable {

    default SerializablePredicate<B> partialLeft(final A first) {
        return new LeftPartial<>(this, first);
    }

    class LeftPartial<A, B> implements SerializablePredicate<B>, DataSerializable {

        private static final long serialVersionUID = 1L;

        private SerializableBiPredicate<? super A, ? super B> outer;
        private A left;

        @Deprecated
        LeftPartial() {
        }

        public LeftPartial(final SerializableBiPredicate<? super A, ? super B> outer, final A left) {
            this.outer = outer;
            this.left = left;
        }

        @Override
        public boolean test(final B object) {
            return outer.test(left, object);
        }

        @Override
        public void writeData(final ObjectDataOutput out) throws IOException {
            out.writeObject(outer);
            out.writeObject(left);
        }

        @Override
        public void readData(final ObjectDataInput in) throws IOException {
            outer = in.readObject();
            left = in.readObject();
        }

    }

}
