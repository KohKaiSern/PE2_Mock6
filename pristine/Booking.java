/**
 * A booking of one room over a half-open time interval.
 *
 * @author
 */
public class Booking {
  private String room;
  private int start;
  private int end;

  public Booking(String room, int start, int end) {
    this.room = room;
    this.start = start;
    this.end = end;
  }

  public String getRoom() {
    return this.room;
  }

  public int getStart() {
    return this.start;
  }

  public int getEnd() {
    return this.end;
  }

  public Booking shift(int minutes) {
    this.start += minutes;
    this.end += minutes;
    return this;
  }

  public Booking relocate(String room) {
    this.room = room;
    return this;
  }

  public boolean overlaps(Booking other) {
    return this.room.equals(other.room)
        && this.start < other.end && other.start < this.end;
  }

  @Override
  public String toString() {
    return this.room + " [" + this.start + ", " + this.end + ")";
  }
}
