package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

public class DatabaseThreadPool {
    private static final int THREAD_POOL_SIZE = 5;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void shutdown() {
        executorService.shutdown();
    }

    // Utility method to run database operations asynchronously
    public static <T> CompletableFuture<T> submitTask(DatabaseTask<T> task) {
        return CompletableFuture.supplyAsync(task::execute, executorService);
    }
}

@FunctionalInterface
public interface DatabaseTask<T> {
    T execute();
}
