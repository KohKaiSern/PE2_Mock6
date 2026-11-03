package cs2030s.fp;

/**
 * A functional interface that consumes a value without returning a result.
 *
 * @param <T> the type of the input argument
 */
public interface Consumer<T> {
  /**
   * Consumes the given argument.
   *
   * @param arg the argument to consume
   */
  void consume(T arg);
}