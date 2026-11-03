package cs2030s.fp;

import java.util.NoSuchElementException;

/**
 * Represent a box with possibly missing value.
 * 
 * @author XXX
 * @version 2026-2027 Sem 1
 */
public final class Maybe<T> {
  private static final Maybe<?> NONE = new Maybe<>();

  private T val;
  private boolean has;

  private Maybe() {
    this(null, false);
  }

  private Maybe(T val) {
    this(val, true);
  }

  private Maybe(T val, boolean has) {
    this.val = val;
    this.has = has;
  }

  public static <T> Maybe<T> none() {
    @SuppressWarnings("unchecked")
    Maybe<T> res = (Maybe<T>) NONE;
    return res;
  }

  public static <T> Maybe<T> some(T val) {
    return new Maybe<>(val);
  }

  public static <T> Maybe<T> of(T val) {
    if (val == null) {
      return Maybe.none();
    } else {
      return Maybe.some(val);
    }
  }

  protected T get() {
    if (this.has) {
      return this.val;
    } else {
      throw new NoSuchElementException();
    }
  }

  public Maybe<T> filter(BooleanCondition<? super T> cond) {
    if (this.has) {
      if (this.val == null) {
        return Maybe.<T>some(null);
      } else if (!cond.test(this.val)) {
        return Maybe.<T>none();
      } else {
        return this;
      }
    } else {
      return Maybe.<T>none();
    }
  }

  public <R> Maybe<R> map(Transformer<? super T, ? extends R> func) {
    if (this.has) {
      return Maybe.<R>some(func.transform(this.val));
    } else {
      return Maybe.<R>none();
    }
  }

  public <R> Maybe<R> flatMap(Transformer<? super T, ? extends Maybe<? extends R>> func) {
    if (this.has) {
      @SuppressWarnings("unchecked")
      Maybe<R> res = (Maybe<R>) func.transform(this.val);
      return res;
    } else {
      return Maybe.<R>none();
    }
  }

  public T orElse(T val) {
    if (this.has) {
      return this.val;
    } else {
      return val;
    }
  }

  public T orElseGet(Producer<? extends T> prod) {
    if (this.has) {
      return this.val;
    } else {
      return prod.produce();
    }
  }

  public void ifPresent(Consumer<? super T> cons) {
    if (this.has) {
      cons.consume(this.val);
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Maybe<?>) {
      Maybe<?> maybe = (Maybe<?>) obj;
      if (this.has != maybe.has) {
        return false;
      }
      if (this.has && maybe.has) {
        if (this.val == null) {
          return maybe.val == null;
        }
        return this.val.equals(maybe.val);
      } else {
        return !maybe.has;
      }
    }
    return false;
  }
}