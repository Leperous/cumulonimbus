package net.ollie.distributed.hadoop.phases;

import java.io.IOException;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

import org.apache.hadoop.mapreduce.Job;

import net.ollie.distributed.phases.FuturePhase;

/**
 * Execute a job.
 *
 * @author Ollie
 */
public class HadoopJobExecutionPhase
        implements FuturePhase<Job, Boolean> {

    private final boolean verbose;
    private final Executor executor;

    public HadoopJobExecutionPhase(final boolean verbose, final Executor executor) {
        this.verbose = verbose;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Boolean> transform(@Nonnull final Job job) {
        requireNonNull(job);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return job.waitForCompletion(verbose);
            } catch (final IOException | ClassNotFoundException | InterruptedException t) {
                throw new RuntimeException(t);
            }
        }, executor);
    }

}
