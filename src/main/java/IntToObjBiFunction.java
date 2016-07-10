/**
 * @author rgettys
 */
@FunctionalInterface
public interface IntToObjBiFunction<T> {

    T apply(int left, int right);

}
