import cs2030s.fp.Maybe;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Queries on room schedules.
 *
 * @author
 */
public class RoomPlanner {
  public static Maybe<Booking> firstConflict(Schedule schedule, Booking candidate) {
    return Maybe.none();
  }

  public static List<String> availableRooms(Schedule schedule, List<String> rooms,
      int start, int end) {
    return List.of();
  }

  public static List<Integer> turnaroundTimes(Schedule schedule, String room) {
    return List.of();
  }
}
