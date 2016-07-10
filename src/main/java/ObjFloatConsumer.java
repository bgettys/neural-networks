import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts an object-valued and a
 * {@code float}-valued argument, and returns no result.  This is the
 * {@code (reference, float)} specialization of {@link BiConsumer}.
 * Unlike most other functional interfaces, {@code ObjFloatConsumer} is
 * expected to operate via side-effects.
 * <p>
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, float)}.
 *
 * @param <T> the type of the object argument to the operation
 * @author rgettys
 * @see BiConsumer
 * @since 1.8
 */
@FunctionalInterface
public interface ObjFloatConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t     the first input argument
     * @param value the second input argument
     */
    void accept(T t, float value);
}
