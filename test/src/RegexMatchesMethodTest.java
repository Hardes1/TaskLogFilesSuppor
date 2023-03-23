import org.junit.Test;
import ru.study.Main;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
