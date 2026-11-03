package cs2030s.fp;

/**
 * A functional interface that tests a boolean condition on a value.
 *
 * @param <T> the type of the input argument
 */
public interface BooleanCondition<T> {
  /**
   * Tests the given argument and returns a boolean result.
   *
   * @param arg the argument to test
   * @return true if the condition holds, false otherwise
   */
  boolean test(T arg);
}