package net.ollie.distributed.collections;

import static java.util.Objects.requireNonNull;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import net.ollie.distributed.functions.NonnullSupplier;
import net.ollie.distributed.serialization.MustDistribute;

/**
 *
 * @author Ollie
 */
@MustDistribute
public interface DistributedFunction<K, V> {

    @CheckForNull
    V get(@Nonnull K key);

    @Nonnull
    default Optional<V> getOptionally(final K key) {
        return Optional.ofNullable(this.get(key));
    }

    @Nonnull
    default V getOrRequire(final K key, @Nonnull final V value) {
        requireNonNull(value);
        return this.getOrSupply(key, (NonnullSupplier<V>) () -> value);
    }

    @Nonnull
    default V getOrSupply(final K key, final NonnullSupplier<? extends V> supplier) {
        final V got = this.get(key);
        return got == null ? supplier.get() : got;
    }

}
