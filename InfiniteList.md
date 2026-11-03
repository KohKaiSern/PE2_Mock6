# InfiniteList API (Abridged)

The supplied `cs2030s.fp.InfiniteList<T>` stores an ordered sequence. Transformations are lazy, and computed nodes are cached.

| Method | Behaviour |
| --- | --- |
| `static <T> InfiniteList<T> sentinel()` | An empty sequence. |
| `static <T> InfiniteList<T> of(T... values)` | A sequence containing the given arguments in order. |
| `static <T> InfiniteList<T> generate(Producer<T> producer)` | Generate values on demand. |
| `static <T> InfiniteList<T> iterate(T seed, Transformer<T, T> next)` | Generate `seed`, `next(seed)`, and so on. |
| `<R> InfiniteList<R> map(Transformer<? super T, ? extends R> f)` | Transform each element in order. |
| `InfiniteList<T> filter(BooleanCondition<? super T> condition)` | Retain elements that pass the test, in order. |
| `<R> InfiniteList<R> flatMap(Transformer<? super T, ? extends InfiniteList<? extends R>> f)` | Replace each element by a sequence and flatten the resulting sequences in order. |
| `InfiniteList<T> limit(long n)` | Retain at most the first `n` elements. |
| `InfiniteList<T> takeWhile(BooleanCondition<? super T> condition)` | Retain the initial elements while the condition holds. |
| `<U> U reduce(U identity, Combiner<U, ? super T, U> accumulator)` | Process each retained element from left to right, updating the accumulator; return the identity for an empty sequence. |
| `long count()` | Count the retained elements. |
| `T head()` | Return the first retained element. |
| `InfiniteList<T> tail()` | The sequence after the first retained element. |
| `boolean isSentinel()` | Whether this object is the sentinel. |

`of` can be called with separate arguments, for example `InfiniteList.of(2, 4, 6)`.

For the sample runs, `toList()` displays the retained elements as a Java list. Do not use `toList()` or `fromList()` in your answers.

## Integer operations

`Math.max(a, b)` returns the larger of two integers. `Math.min(a, b)` returns the smaller.
