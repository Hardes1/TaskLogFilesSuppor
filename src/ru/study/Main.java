package ru.study;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

class RegexMatchesTask implements Callable<Boolean> {
    static final int TASK_TIME_LIMIT = 7000;
    private final String text;
    private final String regex;

    RegexMatchesTask(@NotNull String text, @NotNull String regex) {
        this.text = text;
        this.regex = regex;
    }

    @Override
    public Boolean call() {
        return Pattern.compile(regex).matcher(text).matches();
    }
}

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static Optional<Boolean> matches(@NotNull String text, @NotNull String regex) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(new RegexMatchesTask(text, regex));
        try {
            return Optional.of(future.get(RegexMatchesTask.TASK_TIME_LIMIT, TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            future.cancel(true);
            LOGGER.log(Level.INFO, "Matches function call timeout exceeded!");
            return Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return Optional.empty();
        } finally {
            executorService.shutdownNow();
        }
    }

    private static void setUpLogger() {
        Handler handler = new ConsoleHandler();
        LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
    }

    public static void main(String[] args) {
        setUpLogger();
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String pattern = scanner.nextLine();
        Optional<Boolean> result = matches(text, pattern);
        result.ifPresentOrElse(
                valueGetter -> System.out.println(valueGetter.booleanValue()),
                () -> LOGGER.log(Level.WARNING, "Matches function internal error!")
        );
    }
}