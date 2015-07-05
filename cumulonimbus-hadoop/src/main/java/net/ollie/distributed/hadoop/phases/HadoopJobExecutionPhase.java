package net.ollie.distributed.hadoop.phases;

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
        implements FuturePhase<Job, Void> {

    private final boolean verbose;
    private final Executor executor;

    public HadoopJobExecutionPhase(final boolean verbose, final Executor executor) {
        this.verbose = verbose;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Void> transform(@Nonnull final Job job) {
        requireNonNull(job);
        final CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        executor.execute(() -> {
            try {
                job.waitForCompletion(verbose);
                future.complete(null);
            } catch (final Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

}
