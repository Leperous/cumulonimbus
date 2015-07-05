package net.ollie.distributed.hazelcast.phases;

import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.CompletableFuture;

import com.hazelcast.mapreduce.CombinerFactory;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.Mapper;
import com.hazelcast.mapreduce.ReducerFactory;
import com.hazelcast.mapreduce.ReducingSubmittableJob;

import net.ollie.distributed.hazelcast.collections.HazelcastMap;
import net.ollie.distributed.phases.FuturePhase;
import net.ollie.distributed.hazelcast.utils.HazelcastFutures;
import net.ollie.distributed.serialization.MustNotDistribute;

/**
 * Executes map/reduce phase on Hazelcast, given a job tracker, mapper, combiner and reducer.
 *
 * @author Ollie
 */
@MustNotDistribute
public abstract class HazelcastMapReducePhase<K1, V1, K2, V2>
        implements FuturePhase<HazelcastMap<K1, V1>, Map<K2, V2>>, HazelcastFutures {

    public static <K1, V1, K2, V2> FuturePhase<HazelcastMap<K1, V1>, Map<K2, V2>> mapReduce(
            final JobTracker jobTracker,
            final Mapper<K1, V1, K2, V1> mapper,
            final ReducerFactory<K2, V1, V2> reducer) {
        return new MapReduce<>(jobTracker, mapper, reducer);
    }

    public static <K1, V1, C1, C2, K2, V2> FuturePhase<HazelcastMap<K1, V1>, Map<K2, V2>> mapCombineReduce(
            final JobTracker jobTracker,
            final Mapper<K1, V1, K2, C1> mapper,
            final CombinerFactory<K2, C1, C2> combiner,
            final ReducerFactory<K2, C2, V2> reducer) {
        return new MapCombineReduce<>(jobTracker, mapper, combiner, reducer);
    }

    private final JobTracker tracker;

    protected HazelcastMapReducePhase(final JobTracker tracker) {
        this.tracker = requireNonNull(tracker);
    }

    @Override
    public CompletableFuture<Map<K2, V2>> transform(final HazelcastMap<K1, V1> from) {
        final Job<K1, V1> trackingJob = this.trackingJob(from);
        final ReducingSubmittableJob<K1, K2, V2> mapReduceJob = this.mapReduceJob(trackingJob);
        return this.submit(mapReduceJob);
    }

    protected abstract ReducingSubmittableJob<K1, K2, V2> mapReduceJob(final Job<K1, V1> job);

    protected Job<K1, V1> trackingJob(final HazelcastMap<K1, V1> map) {
        return tracker.newJob(map.source());
    }

    protected CompletableFuture<Map<K2, V2>> submit(final ReducingSubmittableJob<K1, K2, V2> job) {
        return this.convertFuture(job.submit());
    }

    static final class MapReduce<K1, V1, VI, K2, V2>
            extends HazelcastMapReducePhase<K1, V1, K2, V2> {

        private final Mapper<K1, V1, K2, VI> mapper;
        private final ReducerFactory<K2, VI, V2> reducer;

        MapReduce(
                final JobTracker tracker,
                final Mapper<K1, V1, K2, VI> mapper,
                final ReducerFactory<K2, VI, V2> reducer) {
            super(tracker);
            this.mapper = mapper;
            this.reducer = reducer;
        }

        @Override
        protected ReducingSubmittableJob<K1, K2, V2> mapReduceJob(Job<K1, V1> job) {
            return job.mapper(mapper).reducer(reducer);
        }

    }

    static final class MapCombineReduce<K1, V1, C1, C2, K2, V2>
            extends HazelcastMapReducePhase<K1, V1, K2, V2> {

        private final Mapper<K1, V1, K2, C1> mapper;
        private final CombinerFactory<K2, C1, C2> combiner;
        private final ReducerFactory<K2, C2, V2> reducer;

        MapCombineReduce(
                final JobTracker tracker,
                final Mapper<K1, V1, K2, C1> mapper,
                final CombinerFactory<K2, C1, C2> combiner,
                final ReducerFactory<K2, C2, V2> reducer) {
            super(tracker);
            this.mapper = mapper;
            this.combiner = combiner;
            this.reducer = reducer;
        }

        @Override
        protected ReducingSubmittableJob<K1, K2, V2> mapReduceJob(final Job<K1, V1> job) {
            return job.mapper(mapper).combiner(combiner).reducer(reducer);
        }

    }

}
