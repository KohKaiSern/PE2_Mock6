# Stream and List API (Abridged)

## Stream<T>

| Operation | Behaviour |
| --- | --- |
| `list.stream()` | Create a sequential stream in list order. |
| `filter(condition)` | Keep elements that pass the condition. |
| `map(f)` | Transform each element. |
| `flatMap(f)` | Flatten the streams produced by the transformation. |
| `limit(n)` | Keep at most the first `n` elements. |
| `sorted(comparator)` | Sort by the comparator; preserve encounter order when comparisons are equal. |
| `reduce(identity, accumulator)` | Combine elements using an associative operation with the given identity. |
| `anyMatch(condition)` | Whether any element matches; false for an empty stream. |
| `noneMatch(condition)` | Whether no element matches; true for an empty stream. |
| `toList()` | Return an unmodifiable list in encounter order. |
| `forEach(action)` | Invoke an action on every element; a sequential ordered stream visits elements in order. |

Streams are consumed by terminal operations. Obtain a fresh stream for each traversal.

Stream methods accept Java's functional interfaces. Pass lambdas or method references directly. For a `cs2030s.fp.Transformer<T, R> f`, use `stream.map(f::transform)`; for a `BooleanCondition<T> c`, use `stream.filter(c::test)`; for a `Consumer<T> c`, use `stream.forEach(c::consume)`.

## IntStream

| Operation | Behaviour |
| --- | --- |
| `IntStream.range(start, end)` | Integers from `start` inclusive to `end` exclusive; empty if `start >= end`. |
| `mapToObj(f)` | Map each integer to an object and return a `Stream`. |

## Comparator

`Comparator.comparingInt(f)` compares objects by an integer key, in ascending order.

## List<T>

| Method | Behaviour |
| --- | --- |
| `List.of(...)` | Create an unmodifiable list. |
| `List.copyOf(collection)` | Create an unmodifiable snapshot of the elements. |
| `get(index)` | Return the element at the zero-based index. |
| `size()` | Number of elements. |
| `isEmpty()` | Whether the list is empty. |

`List.copyOf` preserves element references; it does not clone the elements.
