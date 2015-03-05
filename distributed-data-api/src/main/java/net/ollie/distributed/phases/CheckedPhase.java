package net.ollie.distributed.phases;

import javax.annotation.Nonnull;

/**
 *
 * @author Ollie
 */
public interface CheckedPhase<F, T, X extends Exception> extends Phase<F, T> {

    @Nonnull
    T transformChecked(@Nonnull F from) throws X;

    @Override
    @Deprecated
    default T transform(final F from) {
        try {
            return this.transformChecked(from);
        } catch (final RuntimeException rex) {
            throw rex;
        } catch (final Exception cex) {
            throw new RuntimeException(cex);
        }
    }

}
