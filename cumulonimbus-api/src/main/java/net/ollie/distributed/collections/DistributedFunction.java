package net.ollie.distributed.collections;

import static java.util.Objects.requireNonNull;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import net.ollie.distributed.functions.NonnullSupplier;

/**
 *
 * @author Ollie
 */
public interface DistributedFunction<K, V> {

    @CheckForNull
    V get(@Nonnull K key);

    @Nonnull
    default V getOrDefault(final K key, @Nonnull final V value) {
        return this.getOrElse(key, () -> requireNonNull(value));
    }

    @Nonnull
    default V getOrElse(final K key, final NonnullSupplier<? extends V> supplier) {
        final V got = this.get(key);
        return got == null ? supplier.get() : got;
    }

}
