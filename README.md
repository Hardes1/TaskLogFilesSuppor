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
- Logging errors (TLE and etc) via [Logger](https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html).
