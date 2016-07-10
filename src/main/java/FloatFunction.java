import java.util.function.Function;

/**
 * Represents a function that accepts a float-valued argument and produces a
 * result.  This is the {@code float}-consuming primitive specialization for
 * {@link Function}.
 * <p>
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(float)}.
 *
 * @param <R> the type of the result of the function
 * @author rgettys
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface FloatFunction<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    R apply(float value);
}
