import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * An ordered sequence of room bookings.
 *
 * @author
 */
public final class Schedule {
  private final InfiniteList<Booking> bookings;

  public Schedule(InfiniteList<Booking> bookings) {
    this.bookings = bookings;
  }

  public InfiniteList<Booking> getBookings() {
    return this.bookings;
  }

  public Schedule cancel(BooleanCondition<? super Booking> condition) {
    List<Booking> kept = new ArrayList<>();
    for (Booking booking : this.bookings.toList()) {
      if (!condition.test(booking)) {
        kept.add(booking);
      }
    }
    return new Schedule(InfiniteList.fromList(kept));
  }

  public Schedule revise(BooleanCondition<? super Booking> condition,
      Transformer<? super Booking, ? extends Booking> change) {
    List<Booking> revised = new ArrayList<>();
    for (Booking booking : this.bookings.toList()) {
      if (condition.test(booking)) {
        revised.add(change.transform(booking));
      } else {
        revised.add(booking);
      }
    }
    return new Schedule(InfiniteList.fromList(revised));
  }

  public String describe(Transformer<? super Booking, String> formatter) {
    String result = "";
    for (Booking booking : this.bookings.toList()) {
      result += "[" + formatter.transform(booking) + "]";
    }
    return result;
  }

  @Override
  public String toString() {
    return this.bookings.reduce("", (text, b) -> text + "[" + b + "]");
  }
}
