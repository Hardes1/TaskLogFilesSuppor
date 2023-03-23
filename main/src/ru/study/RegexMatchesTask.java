package ru.study;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
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