import cs2030s.fp.BooleanCondition;
import cs2030s.fp.Consumer;
import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * An ordered collection of room bookings.
 *
 * @author
 */
public class Schedule {
  private List<Booking> bookings;

  public Schedule(List<Booking> bookings) {
    this.bookings = new ArrayList<>(bookings);
  }

  public List<Booking> getBookings() {
    return List.copyOf(this.bookings);
  }

  public void cancel(BooleanCondition<? super Booking> condition) {
    List<Booking> kept = new ArrayList<>();
    for (Booking booking : this.bookings) {
      if (!condition.test(booking)) {
        kept.add(booking);
      }
    }
    this.bookings = kept;
  }

  public void revise(BooleanCondition<? super Booking> condition,
      Transformer<? super Booking, ? extends Booking> change) {
    List<Booking> revised = new ArrayList<>();
    for (Booking booking : this.bookings) {
      if (condition.test(booking)) {
        revised.add(change.transform(booking));
      } else {
        revised.add(booking);
      }
    }
    this.bookings = revised;
  }

  public void forEachBooking(Consumer<? super Booking> action) {
    for (Booking booking : this.bookings) {
      action.consume(booking);
    }
  }

  @Override
  public String toString() {
    return this.bookings.stream().map(b -> "[" + b + "]")
        .reduce("", (a, b) -> a + b);
  }
}
