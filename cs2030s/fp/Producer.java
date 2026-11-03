package cs2030s.fp;

/**
 * A functional interface that produces a value without taking any input.
 *
 * @param <T> the type of the produced value
 */
public interface Producer<T> {
  /**
   * Produces and returns a value.
   *
   * @return the produced value
   */
  T produce();
}