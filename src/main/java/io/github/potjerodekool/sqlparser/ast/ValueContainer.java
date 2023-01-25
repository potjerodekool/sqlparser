package io.github.potjerodekool.sqlparser.ast;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ValueContainer<T> {

    private final T value;
    private final boolean hasValue;

    private static final ValueContainer<Object> ABSENT = new ValueContainer<>(null, false);

    private ValueContainer(final T value,
                           final boolean hasValue) {
        this.value = value;
        this.hasValue = hasValue;
    }

    public static <E> ValueContainer<E> of(final E value) {
        return new ValueContainer<>(value, true);
    }

    public static <E> ValueContainer<E> absent() {
        return (ValueContainer<E>) ABSENT;
    }

    public boolean isHasValue() {
        return hasValue;
    }

    public T getValue() {
        if (!hasValue) {
            throw new IllegalStateException();
        } else {
            return value;
        }
    }

    public T getValueOrElse(final T defaultValue) {
        if (hasValue) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public T getValueOrElseGet(final Supplier<T> supplier) {
        if (hasValue) {
            return value;
        } else {
            return supplier.get();
        }
    }

    public <R> ValueContainer<R> map(final Function<T,R> mapper) {
        if (!hasValue) {
            return (ValueContainer<R>) this;
        } else {
            return new ValueContainer<>(mapper.apply(value), true);
        }
    }

}
