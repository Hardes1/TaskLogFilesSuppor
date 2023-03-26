package ru.study;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.*;


public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static Optional<Boolean> matches(@NotNull String text, @NotNull String regex) {
        return matches(text, regex, Constants.TASK_TIME_LIMIT);
    }

    public static Optional<Boolean> matches(@NotNull String text, @NotNull String regex, long timeLimitMicroseconds) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Optional<RegexMatchesTask> task = RegexMatchesTask.RegexMatchesTaskFactory.build(text, regex);
        if (task.isEmpty()) {
            return Optional.empty();
        }
        Future<Boolean> future = executorService.submit(task.get());
        try {
            return Optional.of(future.get(timeLimitMicroseconds, TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            future.cancel(true);
            LOGGER.info(Constants.TIMEOUT_EXCEEDED_MESSAGE);
            return Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.info(e.getMessage());
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return Optional.empty();
        } finally {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            String pattern = scanner.nextLine();
            Optional<Boolean> result = matches(text, pattern);
            result.ifPresentOrElse(
                    valueGetter -> System.out.println(valueGetter.booleanValue()),
                    () -> LOGGER.warn(Constants.INTERNAL_ERROR_MESSAGE));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}