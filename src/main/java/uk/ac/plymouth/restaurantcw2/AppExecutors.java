package uk.ac.plymouth.restaurantcw2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final ExecutorService networkIO = Executors.newFixedThreadPool(3);
    private static final ExecutorService diskIO = Executors.newSingleThreadExecutor();

    public static ExecutorService networkIO() {
        return networkIO;
    }

    public static ExecutorService diskIO() {
        return diskIO;
    }
}
