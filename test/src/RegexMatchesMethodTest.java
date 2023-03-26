import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import ru.study.Constants;
import ru.study.Main;
import ru.study.RegexMatchesTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

public class RegexMatchesMethodTest {
    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenTextIsNull() {
        Optional<Boolean> result = Main.matches(null, "b");

        assertTrue(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenPatternIsNull() {
        Optional<Boolean> result = Main.matches("a", null);

        assertTrue(result.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenBothArgumentsAreNull() {
        Optional<Boolean> result = Main.matches(null, null);

        assertTrue(result.isPresent());
    }

    @Test
    public void ShouldMatchSameReference() {
        final String text = "abacaba";
        final String pattern = text;

        Optional<Boolean> result = Main.matches(text, pattern);

        assertTrue(result.isPresent());
        assertTrue(result.get());
    }

    @Test
    public void ShouldNotMatchWhenPatternIsEmpty() {
        final String text = "abacaba";
        final String pattern = "";

        Optional<Boolean> result = Main.matches(text, pattern);

        assertTrue(result.isPresent());
        assertFalse(result.get());
    }

    @Test
    public void ShouldNotMatchWhenTextIsEmpty() {
        final String text = "";
        final String pattern = "abacaba";

        Optional<Boolean> result = Main.matches(text, pattern);

        assertTrue(result.isPresent());
        assertFalse(result.get());
    }

    @Test
    public void ShouldMatchWhenTextAndPatternAreEmpty() {
        final String text = "";
        final String pattern = "";

        Optional<Boolean> result = Main.matches(text, pattern);

        assertTrue(result.isPresent());
        assertTrue(result.get());
    }

    @Test
    public void ShouldReturnsEmptyWhenRegexIsLarge() throws IOException {
        Logger logger = (Logger) LoggerFactory.getLogger(RegexMatchesTask.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        logger.addAppender(listAppender);
        listAppender.start();
        BufferedReader bufferedInputStream = new BufferedReader(new FileReader("./resources/large_text.txt"));
        final String text = "help";
        final String pattern = bufferedInputStream.readLine();

        Optional<Boolean> result = Main.matches(text, pattern);

        assertEquals(listAppender.list.get(0).getFormattedMessage(), Constants.REGEX_TOO_LARGE_MESSAGE);
        assertTrue(result.isEmpty());
    }
}


