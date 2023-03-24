package ru.study;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static ru.study.Constants.REGEX_MAX_LENGTH;

class RegexMatchesTask implements Callable<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(RegexMatchesTask.class.getName());

    private final InterruptableCharSequence text;

    private final String regex;

    private RegexMatchesTask(@NotNull String text, @NotNull String regex) {
        if (regex.length() > REGEX_MAX_LENGTH) {
            throw new IllegalArgumentException(Constants.REGEX_TOO_LARGE_MESSAGE);
        }
        this.text = new InterruptableCharSequence(text);
        this.regex = regex;
    }

    @Override
    public Boolean call() {
        return Pattern.compile(regex).matcher(text).matches();
    }

    static class RegexMatchesTaskFactory {
        public static Optional<RegexMatchesTask> build(@NotNull String text, @NotNull String regex) {
            try {
                return Optional.of(new RegexMatchesTask(text, regex));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                return Optional.empty();
            }
        }
    }
}