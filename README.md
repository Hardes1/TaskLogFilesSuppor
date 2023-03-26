# Solution to the "Log files support" task from JetBrains internships

## Usage

Reads 2 strings - text and parser (each in separate line) and then check whether the first one satisfies
the second one.

## Main ideas about the refactoring

- I don't like null paradigm in programming languages, so instead this I
  prefer [Result\<T\>](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) (
  or [Optional\<T\>](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) in Java).
- Moreover, I use [\@NotNull](https://www.jetbrains.com/help/idea/nullable-and-notnull-annotations.html#notnull)
  annotation to avoid as maximum as possible nullable values.
- To limit the time execution of **matches** function, I
  use [Executors API](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html). It
  has several benefits:
    - You can move an execution of function to a separate thread pool.
    - It provides beautiful API to stop the running tasks (instead of `while(true)` or its analogues)
- Logging errors (TLE and etc) via [Logger](https://logback.qos.ch/apidocs/index.html).

### One big remark

Before the deadline, I had decided to write some unit tests for my refactored solution and was faced with one interesting
problem:
if the input consists of any text and some big regex (let's say $10^6$ length), then despite
invocation `Future<T>.cancel(true)` when the time is up, my program is still running. After some researching, I realised
that `Future<T>` uses a simple mechanism of stopping the current thread by invoking `Thread.interrupt()`. This means that
our
API should stop execution by itself by checking `Thread.interrupted()`. Unfortunately, the longest execution time
belongs
to the `Pattern.compile(String)` method, which is internal Java API and can't be modified. With the help of google I have
figured out some ideas which might help to get the desired behaviour:

- To make `Matcher.matches()` work we also need a text on which we will apply this method. This text is created using
  `pattern.matcher(CharSequence)` method. So here we can extend CharSequence class and make it interruptible (I
  have `InterruptableCharSequence`). So now, every time we need to access a char of our text, we check whether
  the current Thread is interrupted and if true, then throw an `RuntimeException`.
  Alas, this will only affect the verification of the membership of the NFA string, and when building the NFA of the
  regular expression itself, we will still have to wait a long time.
- Another pretty stupid idea is just limit the length of regex string and this way `Pattern.compile` will never be
  long (I have done it too).
- Third approach consists of modernizing a Pattern class by adding constructions like in the first
  idea (`if(Thread.interrupted()) throw new RuntimeException()`).
- And the last idea which comes into my mind is to write custom regular expressions. But it seems to me, that it is too long and
too hard problem.)

All in all, I understood that this problem, possibly, much more important rather than some architecture drawbacks of code.
### Another remark

Probably, it was expected that I should refactor this function to kotlin, but I haven't thought about it (P.S. I can do
it too).