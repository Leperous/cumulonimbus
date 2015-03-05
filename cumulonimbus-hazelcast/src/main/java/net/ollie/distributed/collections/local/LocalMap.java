package net.ollie.distributed.collections.local;

import javax.annotation.Nonnull;

import net.ollie.distributed.collections.DistributedHazelcastMap;
import net.ollie.distributed.serialization.LocalMapStreamSerializer;
import net.ollie.distributed.serialization.MustBeLocallyAvailable;

/**
 *
 * @author Ollie
 * @see LocalMapStreamSerializer A special stream serializer must be registered against this instance.
 */
@MustBeLocallyAvailable
public interface LocalMap<K, V> extends DistributedHazelcastMap<K, V> {

    @Nonnull
    String id();

}
