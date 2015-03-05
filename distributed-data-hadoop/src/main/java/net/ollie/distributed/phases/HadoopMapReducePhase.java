package net.ollie.distributed.phases;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author Ollie
 */
public class HadoopMapReducePhase<K1, V1, KI, VI, K2, V2>
        implements FuturePhase<Map<K1, V1>, Map<K2, V2>> {

    private final Supplier<Job> jobSupplier;
    private final Class<Mapper<K1, V1, KI, VI>> mapper;
    private final Class<Reducer<KI, VI, K2, V2>> reducer;

    public HadoopMapReducePhase(
            final Supplier<Job> jobSupplier,
            final Class<Mapper<K1, V1, KI, VI>> mapper,
            final Class<Reducer<KI, VI, K2, V2>> reducer) {
        this.jobSupplier = jobSupplier;
        this.mapper = mapper;
        this.reducer = reducer;
    }

    @Override
    public CompletableFuture<Map<K2, V2>> transform(final Map<K1, V1> from) {
        final Job job = jobSupplier.get();
        job.setMapperClass(mapper);
        job.setReducerClass(reducer);
        throw new UnsupportedOperationException();
    }

}
