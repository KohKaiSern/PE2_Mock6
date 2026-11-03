package cs2030s.fp;

/**
 * A functional interface that transforms a value from one type to another.
 *
 * @param <T> the type of the input argument
 * @param <R> the type of the result
 */
public interface Transformer<T, R> {
  /**
   * Transforms the given argument into a result.
   *
   * @param arg the argument to transform
   * @return the transformed result
   */
  R transform(T arg);
}