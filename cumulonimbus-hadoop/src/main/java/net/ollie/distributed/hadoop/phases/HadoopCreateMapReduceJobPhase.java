package net.ollie.distributed.hadoop.phases;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import net.ollie.distributed.phases.SupplyPhase.ExceptionalSupplyPhase;

/**
 *
 * @author Ollie
 */
public abstract class HadoopCreateMapReduceJobPhase implements ExceptionalSupplyPhase<Job, IOException> {

    private final JobConf config;

    protected HadoopCreateMapReduceJobPhase(final JobConf config) {
        checkArgument(config.get(FileInputFormat.INPUT_DIR) != null);
        checkArgument(config.get(FileOutputFormat.OUTDIR) != null);
        this.config = config;
    }

    protected Job createJob() throws IOException {
        return Job.getInstance(config);
    }

    static class MapReduce<K1, V1, K2, V2, K3, V3> extends HadoopCreateMapReduceJobPhase {

        private final Class<? extends Mapper<K1, V1, K2, V2>> mapper;
        private final Class<? extends Reducer<K2, V2, K3, V3>> reducer;
        private final Class<K3> outKeyClass;
        private final Class<V3> outValueClass;

        MapReduce(
                final JobConf config,
                final Class<? extends Mapper<K1, V1, K2, V2>> mapper,
                final Class<? extends Reducer<K2, V2, K3, V3>> reducer,
                final Class<K3> outKeyClass,
                final Class<V3> outValueClass) {
            super(config);
            this.mapper = mapper;
            this.reducer = reducer;
            this.outKeyClass = outKeyClass;
            this.outValueClass = outValueClass;
        }

        @Override
        public Job getExceptionally() throws IOException {
            final Job job = this.createJob();
            job.setJarByClass(mapper);
            job.setMapperClass(mapper);
            job.setReducerClass(reducer);
            job.setOutputKeyClass(outKeyClass);
            job.setOutputValueClass(outValueClass);
            return job;
        }

    }

}
