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
5. Do not add imports. Remove the `java.util.ArrayList` and `java.util.List` imports from `Schedule.java` when completing Question 2.
6. Use JDK 17 or later. Compile with:

   ```shell
   javac -Xlint:unchecked -Xlint:rawtypes *.java
   ```

7. Run the tests with:

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
- `Maybe.md` and `InfiniteList.md` — abridged API references

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

Keep these source files unchanged. Use the supplied `InfiniteList` for sequence processing, `Maybe` for optional values, and the package's functional interfaces.

## Constraints

Your completed answers must satisfy all of the following:

- **Every method body must contain exactly one `return` statement and nothing else.**
- Do not declare local variables. Lambda parameters are allowed; declarations inside lambda bodies are not.
- Do not use loops, `if`, `switch`, conditional expressions (`?:`), or recursion.
- Do not use block-bodied lambdas such as `x -> { ... }`.
- Do not use arrays, Java collection types such as `List`, or conversions through `toList()` or `fromList()`.
- Do not use `Stream`, `IntStream`, `Optional`, or types from `java.util.function`.
- Do not assign to fields, mutate objects or collections, or use assignment, compound assignment, increment, or decrement expressions inside a return expression or lambda.
- Do not use casts, raw types, `@SuppressWarnings`, explicit null values, or exception-based control flow.
- Do not change any given method's name, parameters, or return type.
- Do not add fields, constructors, methods, classes, anonymous classes, or initialiser blocks. This also prohibits private helper methods.
- Do not add imports or use fully qualified names to access prohibited APIs.

The supplied constructors are not methods; leave their bodies unchanged. Make only the class and field modifier changes requested in Question 1. The supplied `cs2030s.fp` implementations are not subject to the answer restrictions.

The imperative starter methods are code to rewrite. The restrictions apply to your completed versions. All methods you are asked to implement return a value.

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

Rewrite `cancel`, `revise`, and `describe` in `Schedule.java` in functional style, using only a single return statement in each method. Remove all use of `java.util.List` and `java.util.ArrayList` from this class.

The class has one field, `bookings`, of type `InfiniteList<Booking>`. Keep the supplied field, constructor, `getBookings`, and `toString` unchanged. Every sequence in this question is finite, and its entries and the supplied callbacks are non-null.

`cancel` and `revise` return **new schedules**. Neither operation modifies the original schedule or any of its bookings. The same booking object may occur more than once, and each occurrence must be processed. Use the supplied lazy operations; the entries of a returned schedule may be computed when it is traversed.

### `cancel`

```java
public Schedule cancel(BooleanCondition<? super Booking> condition)
```

Return a new schedule containing only the entries that do **not** satisfy `condition`. Preserve the order and object references of the retained entries.

### `revise`

```java
public Schedule revise(BooleanCondition<? super Booking> condition,
    Transformer<? super Booking, ? extends Booking> change)
```

Return a new schedule in the original entry order. For each entry that satisfies `condition`, use the booking returned by `change`. Retain each nonmatching entry unchanged. Do not invoke `change` for a nonmatching entry.

A revision returns a non-null valid booking. It may change its room, times, or both. Do not remove or adjust other entries when a revision introduces an overlap. Repeated occurrences are separate entries and are each subject to the condition and transformation.

### `describe`

```java
public String describe(Transformer<? super Booking, String> formatter)
```

Apply `formatter` to every entry in order, enclose each result in square brackets, and concatenate the results without separators or a newline. The formatter returns a non-null string. Invoke it exactly once per entry on each call to `describe`, even when the same booking object occurs repeatedly.

For an empty schedule, `cancel` and `revise` return empty schedules, and `describe` returns `""`. No callback is invoked for an empty schedule.

The supplied imperative versions already produce the required results. This question assesses their functional rewrite.

### Sample Run

```text
jshell> Booking a = new Booking("R1", 10, 20)
a ==> R1 [10, 20)
jshell> Booking b = new Booking("R2", 15, 25)
b ==> R2 [15, 25)
jshell> Booking c = new Booking("R1", 30, 40)
c ==> R1 [30, 40)

jshell> Schedule original = new Schedule(InfiniteList.of(a, b, c))
original ==> [R1 [10, 20)][R2 [15, 25)][R1 [30, 40)]

jshell> Schedule kept = original.cancel(x -> x.getRoom().equals("R2"))
kept ==> [R1 [10, 20)][R1 [30, 40)]

jshell> Schedule revised = kept.revise(x -> x.getStart() >= 20, x -> x.shift(-5))
revised ==> [R1 [10, 20)][R1 [25, 35)]

jshell> original
original ==> [R1 [10, 20)][R2 [15, 25)][R1 [30, 40)]

jshell> kept
kept ==> [R1 [10, 20)][R1 [30, 40)]

jshell> c
c ==> R1 [30, 40)

jshell> revised.describe(x -> x.getRoom() + ":" + (x.getEnd() - x.getStart()))
$.. ==> "[R1:10][R1:10]"

jshell> revised.revise(x -> true, x -> x.relocate("R3"))
$.. ==> [R3 [10, 20)][R3 [25, 35)]

jshell> revised.cancel(x -> true).toString()
$.. ==> ""
```

Run Question 2's tests with:

```shell
java -cp test.jar:. Test2
```

---

## Question 3 [5 marks]

Implement the three static methods in `RoomPlanner.java` in functional style. Each method body must be a single return statement. All input schedules and sequences are finite.

### `firstConflict` [2 marks]

| Item | Description |
| --- | --- |
| Parameters | `Schedule schedule`, `Booking candidate` |
| Return type | `Maybe<Booking>` |
| Result | The first stored booking, in the schedule's existing order, that overlaps `candidate`, wrapped in `Maybe`. Return `Maybe.none()` if there is no conflict. |

Return the actual stored object. Do not select by earliest start time, and do not add the candidate to the schedule.

### `availableRooms` [1 mark]

| Item | Description |
| --- | --- |
| Parameters | `Schedule schedule`, `InfiniteList<String> rooms`, `int start`, `int end` |
| Return type | `InfiniteList<String>` |
| Result | The entries from `rooms` for which a booking over `[start, end)` would not overlap any booking in the schedule. |

Room names are non-null and `0 <= start < end`. Preserve input order and repeated room names. A room with no bookings is available. An empty room sequence gives an empty result; an empty schedule makes every listed room available.

### `intersections` [2 marks]

| Item | Description |
| --- | --- |
| Parameters | `Schedule first`, `Schedule second` |
| Return type | `Schedule` |
| Result | A new schedule containing the common time interval of every overlapping pair, where one booking comes from `first` and the other comes from `second`. |

For each booking in `first`, consider the bookings in `second` in their original order. Include a result only when the pair overlaps. Its room is the pair's common room, its start is the larger start time, and its end is the smaller end time.

Each result must be a new `Booking`, even if it has the same values as an input booking. Preserve the order in which the pairs are considered: first-schedule order, then second-schedule order. Keep repeated results. Do not sort, merge, or remove duplicates.

Bookings in different rooms and bookings that merely touch at an endpoint contribute no result. If either input schedule is empty, return an empty schedule. Leave both input schedules and all their bookings unchanged.

### Sample Run

```text
jshell> Booking a = new Booking("R1", 10, 20)
a ==> R1 [10, 20)
jshell> Booking b = new Booking("R2", 15, 25)
b ==> R2 [15, 25)
jshell> Booking c = new Booking("R1", 30, 40)
c ==> R1 [30, 40)
jshell> Booking d = new Booking("R1", 18, 22)
d ==> R1 [18, 22)

jshell> Schedule schedule = new Schedule(InfiniteList.of(c, b, a, d))
schedule ==> [R1 [30, 40)][R2 [15, 25)][R1 [10, 20)][R1 [18, 22)]

jshell> RoomPlanner.firstConflict(schedule, new Booking("R1", 19, 31))
$.. ==> [R1 [30, 40)]

jshell> RoomPlanner.firstConflict(schedule, new Booking("R1", 22, 30))
$.. ==> []

jshell> RoomPlanner.firstConflict(schedule, new Booking("R3", 10, 50))
$.. ==> []

jshell> RoomPlanner.availableRooms(schedule, InfiniteList.of("R3", "R1", "R2"), 20, 30).toList()
$.. ==> [R3]

jshell> RoomPlanner.availableRooms(schedule, InfiniteList.of("R3", "R1", "R2"), 25, 30).toList()
$.. ==> [R3, R1, R2]

jshell> RoomPlanner.availableRooms(schedule, InfiniteList.of("R3", "R1", "R3"), 20, 30).toList()
$.. ==> [R3, R3]

jshell> Schedule windows = new Schedule(InfiniteList.of(new Booking("R1", 15, 35),
   ...>     new Booking("R2", 20, 30), new Booking("R1", 40, 50)))
windows ==> [R1 [15, 35)][R2 [20, 30)][R1 [40, 50)]

jshell> RoomPlanner.intersections(schedule, windows)
$.. ==> [R1 [30, 35)][R2 [20, 25)][R1 [15, 20)][R1 [18, 22)]

jshell> RoomPlanner.intersections(schedule, new Schedule(InfiniteList.sentinel())).toString()
$.. ==> ""

jshell> schedule
schedule ==> [R1 [30, 40)][R2 [15, 25)][R1 [10, 20)][R1 [18, 22)]
```

Multiple results may overlap each other:

```text
jshell> Schedule one = new Schedule(InfiniteList.of(new Booking("R1", 10, 30)))
one ==> [R1 [10, 30)]

jshell> Schedule two = new Schedule(InfiniteList.of(new Booking("R1", 12, 18),
   ...>     new Booking("R1", 16, 22)))
two ==> [R1 [12, 18)][R1 [16, 22)]

jshell> RoomPlanner.intersections(one, two)
$.. ==> [R1 [12, 18)][R1 [16, 22)]
```

Run Question 3's tests with:

```shell
java -cp test.jar:. Test3
```
