import cs2030s.fp.InfiniteList;
import cs2030s.fp.Maybe;

/**
 * Queries on room schedules.
 *
 * @author
 */
public class RoomPlanner {
  public static Maybe<Booking> firstConflict(Schedule schedule, Booking candidate) {
    return Maybe.none();
  }

  public static InfiniteList<String> availableRooms(Schedule schedule,
      InfiniteList<String> rooms, int start, int end) {
    return InfiniteList.sentinel();
  }

  public static Schedule intersections(Schedule first, Schedule second) {
    return new Schedule(InfiniteList.sentinel());
  }
}
