# Maybe API (Abridged)

`cs2030s.fp.Maybe<T>` represents a present or absent value.

| Method | Behaviour |
| --- | --- |
| `static <T> Maybe<T> some(T value)` | Wrap a present value. |
| `static <T> Maybe<T> none()` | Return an absent value. |
| `static <T> Maybe<T> of(T value)` | Return `none()` for null, otherwise `some(value)`. |
| `<R> Maybe<R> map(Transformer<? super T, ? extends R> f)` | Apply `f` to a present value and wrap its result. |
| `<R> Maybe<R> flatMap(Transformer<? super T, ? extends Maybe<? extends R>> f)` | Apply a transformation that returns a `Maybe`. |
| `Maybe<T> filter(BooleanCondition<? super T> condition)` | Keep a present non-null value if it passes the test; otherwise return `none()`. A present null is retained without testing. |
| `T orElse(T replacement)` | Return the present value or the replacement. |
| `T orElseGet(Producer<? extends T> replacement)` | Produce a replacement only when absent. |
| `void ifPresent(Consumer<? super T> action)` | Invoke the action only when a value is present. |

`map`, `flatMap`, and `filter` do not invoke their callbacks on an absent value.
`get()` is protected. Use the public methods above.

`Maybe.some(7).toString()` is `"[7]"`; `Maybe.none().toString()` is `"[]"`.

## Functional interfaces

| Interface | Abstract method |
| --- | --- |
| `Transformer<T, R>` | `R transform(T value)` |
| `Combiner<S, T, R>` | `R combine(S first, T second)` |
| `BooleanCondition<T>` | `boolean test(T value)` |
| `Consumer<T>` | `void consume(T value)` |
| `Producer<T>` | `T produce()` |
