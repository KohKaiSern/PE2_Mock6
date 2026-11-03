package cs2030s.fp;

import java.util.ArrayList;
import java.util.List;

public final class InfiniteList<T> {
  private static class Node<T> {
    private T val;
    private boolean has;

    private Node(T val, boolean has) {
      this.val = val;
      this.has = has;
    }

    public boolean has() {
      return this.has;
    }

    public T val() {
      return this.val;
    }

    public Node<T> filter(BooleanCondition<? super T> pred) {
      if (this.has) {
        return new Node<>(this.val, pred.test(this.val));
      } else {
        return new Node<>(this.val, this.has);
      }
    }

    public <R> Node<R> map(Transformer<? super T, ? extends R> fn) {
      if (this.has) {
        return new Node<>(fn.transform(this.val), this.has);
      } else {
        return new Node<>(null, this.has);
      }
    }

    @Override
    public String toString() {
      if (this.has) {
        return "[" + this.val + "]";
      } else {
        return "[]";
      }
    }
  }

  private static final InfiniteList<?> SENTINEL = new InfiniteList<>();

  private Lazy<Node<T>> head;
  private Lazy<InfiniteList<T>> tail;

  private InfiniteList() {
    this.head = null;
    this.tail = null;
  }

  private InfiniteList(T head, Producer<InfiniteList<T>> tail) {
    this.head = Lazy.of(new Node<>(head, true));
    this.tail = Lazy.of(tail);
  }

  private InfiniteList(Lazy<Node<T>> head, Lazy<InfiniteList<T>> tail) {
    this.head = head;
    this.tail = tail;
  }

  public static <T> InfiniteList<T> fromList(List<T> list) {
    final int n = list.size();
    return InfiniteList.iterate(0, x -> x + 1)
        .limit(n)
        .map(i -> list.get(i));
  }

  @SafeVarargs 
  public static <T> InfiniteList<T> of(T... elems) {
    final int n = elems.length;
    return InfiniteList.iterate(0, x -> x + 1)
        .limit(n)
        .map(i -> elems[i]);
  }

  public static <T> InfiniteList<T> generate(Producer<T> prod) {
    return new InfiniteList<>(
          Lazy.of(() -> new Node<>(prod.produce(), true)),
          Lazy.of(() -> InfiniteList.generate(prod))
        );
  }

  public static <T> InfiniteList<T> iterate(T seed, Transformer<T, T> next) {
    return new InfiniteList<>(
          seed,
          () -> InfiniteList.iterate(next.transform(seed), next)
        );
  }

  public T head() {
    if (this.isSentinel()) {
      throw new java.util.NoSuchElementException();
    } else if (this.head.get().has()) {
      return this.head.get().val();
    } else {
      return this.tail.get().head();
    }
  }

  public InfiniteList<T> tail() {
    if (this.isSentinel()) {
      throw new java.util.NoSuchElementException();
    } else if (this.head.get().has()) {
      return this.tail.get();
    } else {
      return this.tail.get().tail();
    }
  }

  public <R> InfiniteList<R> map(Transformer<? super T, ? extends R> fn) {
    if (this.isSentinel()) {
      return InfiniteList.sentinel();
    }
    return new InfiniteList<>(
          this.head.map(node -> node.map(fn)),
          this.tail.map(inf -> inf.map(fn))
        );
  }

  public InfiniteList<T> filter(BooleanCondition<? super T> pred) {
    if (this.isSentinel()) {
      return InfiniteList.sentinel();
    }
    return new InfiniteList<>(
          this.head.map(node -> node.filter(pred)),
          this.tail.map(inf -> inf.filter(pred))
        );
  }

  public static <T> InfiniteList<T> sentinel() {
    @SuppressWarnings("unchecked")
    InfiniteList<T> res = (InfiniteList<T>) InfiniteList.SENTINEL;
    return res;
  }

  public InfiniteList<T> limit(long n) {
    if (this.isSentinel()) {
      return InfiniteList.sentinel();
    } else if (n <= 0) {
      return InfiniteList.sentinel();
    } else {
      return new InfiniteList<>(
            this.head,
            Lazy.of(() -> this.head.get().has()
                          ? this.tail.get().limit(n - 1)
                          : this.tail.get().limit(n))
          );
    }
  }

  public List<T> toList() {
    List<T> list = new ArrayList<>();
    InfiniteList<T> curr = this;
    while (!curr.isSentinel()) {
      Node<T> node = curr.head.get();
      if (node.has()) {
        list.add(node.val());
      }
      curr = curr.tail.get();
    }
    return list;
  }

  public InfiniteList<T> takeWhile(BooleanCondition<? super T> pred) {
    if (this.isSentinel()) {
      return InfiniteList.sentinel();
    }
    return new InfiniteList<>(
          Lazy.of(() -> {
            Node<T> node = this.head.get();
            if (node.has()) {
              Node<T> test = node.filter(pred);
              return test;
            } else {
              return this.head.get().filter(pred);
            }
          }),
          Lazy.of(() -> {
            Node<T> node = this.head.get();
            if (node.has()) {
              Node<T> test = this.head.get().filter(pred);
              if (test.has()) {
                return this.tail.get().takeWhile(pred);
              } else {
                return InfiniteList.sentinel();
              }
            } else {
              return this.tail.get().takeWhile(pred);
            }
          })
        );
  }

  public boolean isSentinel() {
    return this.tail == null;
  }

  public <U> U reduce(U identity, Combiner<U, ? super T, U> acc) {
    U res = identity;
    InfiniteList<T> curr = this;
    while (!curr.isSentinel()) {
      if (curr.head.get().has()) {
        res = acc.combine(res, curr.head.get().val());
      }
      curr = curr.tail.get();
    }
    return res;
  }

  public long count() {
    return this.reduce(0L, (acc, elem) -> acc + 1L);
  }

  protected InfiniteList<T> append(InfiniteList<T> list) {
    if (this.isSentinel()) {
      return list;
    }
    return new InfiniteList<>(
          this.head,
          this.tail.map(inf -> inf.append(list))
        );
  }

  public <U> InfiniteList<U> flatMap(Transformer<? super T, ? extends InfiniteList<? extends U>> fn) {
    if (this.isSentinel()) {
      return InfiniteList.sentinel();
    }
    return new InfiniteList<>(
          Lazy.of(() -> new Node<>(null, false)),
          Lazy.of(() -> {
            Node<T> node = this.head.get();
            if (node.has()) {
              InfiniteList<U> list = node.map(fn).val().<U>map(x -> x);
              return InfiniteList.<U>sentinel().append(list).append(new InfiniteList<>(
                    Lazy.of(() -> new Node<>(null, false)),
                    Lazy.of(() -> this.tail.get().flatMap(fn))
                  ));
            } else {
              return this.tail.get().flatMap(fn);
            }
          })
        );
  }

  public String toString() {
    return "[" + this.head + " " + this.tail + "]";
  }
}
