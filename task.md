# Mock Practice PE 2 — Room Planner
CS2030S Programming Methodology II

Time Allowed: **90 minutes**  
Total: **15 marks**

---

## INSTRUCTIONS TO CANDIDATES

1. Marks for each question are indicated below. Style and design form part of the assessment.
2. This is a closed-book assessment. You may refer to one double-sided A4-size paper and the supplied API references.
3. Edit `Booking.java`, `Schedule.java`, and `RoomPlanner.java`. Keep your answers directly in the home directory.
4. Write your student number in the `@author` tag of each file you edit.
5. Do not add imports. You may remove unused imports. All imports needed for the questions are already provided.
6. Do not change any given public method's name, parameters, or return type. Do not add classes, constructors, fields, or methods.
7. Use JDK 17 or later. Compile your code with:

   ```shell
   javac -Xlint:unchecked -Xlint:rawtypes *.java
   ```

8. Run the tests with:

   ```shell
   java -cp test.jar:. Test1
   java -cp test.jar:. Test2
   java -cp test.jar:. Test3
   ```

   Recompile after changing your Java files.

---

## Preliminary

Your home directory contains:

- `Booking.java`
- `Schedule.java`
- `RoomPlanner.java`
- `cs2030s/fp/` — the supplied package source files
- `pristine/` — clean copies of the three question files
- `task.md` — this paper
- `test.jar` — compiled tests for Questions 1–3
- `Maybe.md` and `Stream.md` — abridged API references

## Provided Classes

The `cs2030s.fp` package contains:

- `BooleanCondition<T>`
- `Combiner<S, T, R>`
- `Consumer<T>`
- `InfiniteList<T>`
- `Lazy<T>`
- `Maybe<T>`
- `Producer<T>`
- `Transformer<T, R>`

Keep the package unchanged. This paper uses Java streams for collection processing and the supplied `Maybe` and functional interfaces for conditions, transformations, and actions.

## Constraints

Questions 2 and 3 require **functional style**. In those questions:

- Do not use loops, `if`, `switch`, or conditional expressions, including `?:`.
- Do not use arrays, recursion, or block-bodied lambdas such as `x -> { ... }`.
- Do not use `Optional` or declare types from `java.util.function`.
- Do not use casts, raw types, or `@SuppressWarnings`.
- Do not mutate collections with operations such as `add`, `remove`, `set`, or `sort`, including inside lambdas.
- Use sequential streams. Do not use parallel streams.
- You may declare local variables and call the supplied methods. Method bodies need not consist of a single return statement.

In Question 2, the `bookings` field may be reassigned to the result of a stream operation. Calling the supplied callbacks is part of the required behaviour. Question 3 must leave the schedule, its bookings, and the input lists unchanged.

These restrictions apply to the methods you rewrite or implement. Leave the other supplied methods unchanged, except for the class and field modifiers required by Question 1.

---

## Question 1 [5 marks]

Rewrite `Booking` as an **immutable** class.

A booking identifies a room and a time interval. The interval `[start, end)` includes `start` and excludes `end`. For example, bookings `[10, 20)` and `[20, 30)` in the same room do not overlap.

The class has exactly three fields:

- `room` of type `String`
- `start` of type `int`
- `end` of type `int`

The constructor takes these three values in that order. A room name is non-null, and `0 <= start < end`. All shifts used in this question preserve these bounds and fit in an `int`; you do not need to add input validation.

Make the class `final` and its fields `private final`. Re-implement:

| Method | Required behaviour |
| --- | --- |
| `Booking shift(int minutes)` | Return a new booking in the same room, with both endpoints increased by `minutes`. Negative shifts are allowed. |
| `Booking relocate(String room)` | Return a new booking in the specified room, preserving both endpoints. |

Both methods must leave the original booking unchanged and return a new object even when its values are unchanged.

Retain the supplied constructor, getters, `overlaps`, and `toString`. The supplied `overlaps` method returns true exactly when the bookings have equal room names and their intervals intersect. Room names are compared by their string values and are case-sensitive.

### Sample Run

```text
jshell> Booking a = new Booking("R1", 10, 20)
a ==> R1 [10, 20)

jshell> Booking shifted = a.shift(5)
shifted ==> R1 [15, 25)

jshell> Booking moved = a.relocate("R2")
moved ==> R2 [10, 20)

jshell> a
a ==> R1 [10, 20)

jshell> shifted.shift(-5)
$.. ==> R1 [10, 20)

jshell> a.getRoom()
$.. ==> "R1"

jshell> a.getStart()
$.. ==> 10

jshell> a.getEnd()
$.. ==> 20

jshell> a.overlaps(new Booking("R1", 19, 25))
$.. ==> true

jshell> a.overlaps(new Booking("R1", 20, 25))
$.. ==> false

jshell> a.overlaps(new Booking("R2", 12, 18))
$.. ==> false

jshell> a.shift(0) == a
$.. ==> false
```

Run Question 1's tests with:

```shell
java -cp test.jar:. Test1
```

---

## Question 2 [5 marks]

Rewrite the following three methods of `Schedule` in functional style. There is no need to make `Schedule` immutable.

The class has one field, `bookings`, of type `List<Booking>`. The supplied constructor copies the input list. `getBookings()` returns an unmodifiable snapshot of its current entries, and `toString()` encloses each booking in square brackets, without separators. Leave these three methods unchanged.

Bookings and callbacks are non-null. The same booking object may appear more than once. Each occurrence is a separate entry, and the order of entries matters.

| Method | Required behaviour |
| --- | --- |
| `void cancel(BooleanCondition<? super Booking> condition)` | Remove every entry that satisfies `condition`, preserving the order and object references of the remaining entries. Test each original entry exactly once, in order. |
| `void revise(BooleanCondition<? super Booking> condition, Transformer<? super Booking, ? extends Booking> change)` | Replace each matching entry with the result of `change`. Keep each nonmatching entry unchanged. Test each entry exactly once, and apply `change` exactly once to each matching entry only. Preserve entry order. |
| `void forEachBooking(Consumer<? super Booking> action)` | Invoke `action` exactly once on each entry, in order, without changing the schedule. |

A revision callback returns a non-null valid booking. For each entry, test the condition and perform any selected revision before processing the next entry. The replacement may change the room, times, or both. It does not cause any other booking to be removed or adjusted, even if the revised schedule contains overlapping bookings.

For an empty schedule, all three methods do nothing and invoke no callbacks. The supplied imperative versions already implement the required behaviour. This question assesses their functional rewrite.

### Sample Run

```text
jshell> Booking a = new Booking("R1", 10, 20)
a ==> R1 [10, 20)
jshell> Booking b = new Booking("R2", 15, 25)
b ==> R2 [15, 25)
jshell> Booking c = new Booking("R1", 30, 40)
c ==> R1 [30, 40)

jshell> Schedule schedule = new Schedule(List.of(a, b, c))
schedule ==> [R1 [10, 20)][R2 [15, 25)][R1 [30, 40)]

jshell> schedule.cancel(x -> x.getRoom().equals("R2"))
jshell> schedule
schedule ==> [R1 [10, 20)][R1 [30, 40)]

jshell> schedule.revise(x -> x.getStart() >= 20, x -> x.shift(-5))
jshell> schedule
schedule ==> [R1 [10, 20)][R1 [25, 35)]

jshell> c
c ==> R1 [30, 40)

jshell> schedule.forEachBooking(System.out::println)
R1 [10, 20)
R1 [25, 35)

jshell> schedule.revise(x -> true, x -> x.relocate("R3"))
jshell> schedule
schedule ==> [R3 [10, 20)][R3 [25, 35)]

jshell> schedule.cancel(x -> true)
jshell> schedule.toString()
$.. ==> ""
```

Run Question 2's tests with:

```shell
java -cp test.jar:. Test2
```

---

## Question 3 [5 marks]

Implement the three static methods in `RoomPlanner.java` in functional style. Use `cs2030s.fp.Maybe` to represent an absent result.

### `firstConflict` [2 marks]

| Item | Description |
| --- | --- |
| Parameters | `Schedule schedule`, `Booking candidate` |
| Return type | `Maybe<Booking>` |
| Result | The first booking in the schedule's existing order that overlaps `candidate`, wrapped in `Maybe`. Return `Maybe.none()` if none overlaps. |

Return the actual stored booking object. Do not sort the schedule or select by earliest start time. The candidate is used for comparison and is not added to the schedule.

### `availableRooms` [1 mark]

| Item | Description |
| --- | --- |
| Parameters | `Schedule schedule`, `List<String> rooms`, `int start`, `int end` |
| Return type | `List<String>` |
| Result | The entries from `rooms` for which a booking over `[start, end)` would have no conflict with the schedule. |

Room names are non-null and `0 <= start < end`. Preserve the input room order and any repeated room names. A room with no bookings is available. An empty room list gives an empty result; an empty schedule makes every listed room available.

### `turnaroundTimes` [2 marks]

| Item | Description |
| --- | --- |
| Parameters | `Schedule schedule`, `String room` |
| Return type | `List<Integer>` |
| Result | The gap between each consecutive pair of bookings for `room`, after ordering those bookings by ascending start time. |

If two bookings have the same start time, retain their relative order from the schedule. For each consecutive pair, calculate:

```text
next booking's start - previous booking's end
```

A positive result is an idle gap, zero means the bookings meet at an endpoint, and a negative result means they overlap. Compare each booking with its immediate predecessor in the sorted order; do not merge bookings or track the largest preceding end time.

Return the gaps in that order. With zero or one booking in the requested room, return an empty list. The schedule itself must retain its original order.

### Sample Run

These examples start with a fresh schedule:

```text
jshell> Booking a = new Booking("R1", 10, 20)
a ==> R1 [10, 20)
jshell> Booking b = new Booking("R2", 15, 25)
b ==> R2 [15, 25)
jshell> Booking c = new Booking("R1", 30, 40)
c ==> R1 [30, 40)
jshell> Booking d = new Booking("R1", 18, 22)
d ==> R1 [18, 22)

jshell> Schedule schedule = new Schedule(List.of(c, b, a, d))
schedule ==> [R1 [30, 40)][R2 [15, 25)][R1 [10, 20)][R1 [18, 22)]

jshell> RoomPlanner.firstConflict(schedule, new Booking("R1", 19, 31))
$.. ==> [R1 [30, 40)]

jshell> RoomPlanner.firstConflict(schedule, new Booking("R1", 22, 30))
$.. ==> []

jshell> RoomPlanner.firstConflict(schedule, new Booking("R3", 10, 50))
$.. ==> []

jshell> RoomPlanner.availableRooms(schedule, List.of("R3", "R1", "R2"), 20, 30)
$.. ==> [R3]

jshell> RoomPlanner.availableRooms(schedule, List.of("R3", "R1", "R2"), 25, 30)
$.. ==> [R3, R1, R2]

jshell> RoomPlanner.availableRooms(schedule, List.of("R3", "R1", "R3"), 20, 30)
$.. ==> [R3, R3]

jshell> RoomPlanner.turnaroundTimes(schedule, "R1")
$.. ==> [-2, 8]

jshell> RoomPlanner.turnaroundTimes(schedule, "R2")
$.. ==> []

jshell> RoomPlanner.turnaroundTimes(schedule, "R3")
$.. ==> []

jshell> schedule
schedule ==> [R1 [30, 40)][R2 [15, 25)][R1 [10, 20)][R1 [18, 22)]
```

For equal start times:

```text
jshell> Schedule tied = new Schedule(List.of(new Booking("R1", 10, 12),
   ...>     new Booking("R1", 10, 16), new Booking("R1", 20, 21)))
tied ==> [R1 [10, 12)][R1 [10, 16)][R1 [20, 21)]

jshell> RoomPlanner.turnaroundTimes(tied, "R1")
$.. ==> [-2, 4]
```

Run Question 3's tests with:

```shell
java -cp test.jar:. Test3
```
