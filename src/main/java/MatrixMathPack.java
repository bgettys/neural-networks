/**
 * @author rgettys
 */
public interface MatrixMathPack<T, U> {

    @FunctionalInterface
    public interface CostFunction<T, U> {

        T[] getCostMatrices(MatrixMathPack<T, U> mathPack, T[] input, T[] output, T[] trainingExamples, T[][] z, T[][] a, T[][] w);

    }

    T autoScaleInPlace(T[] input);

    void rescaleInPlace(T[] input, T scaleFactors);

    T[] rescale(T[] input, T scaleFactors);

    T[] clone(T[] input);

    T[] dotProduct(T[] multiplicand, T[] multiplier);

    T[] hadamardProduct(T[] multiplicand, T[] multiplier);

    void subtractInPlace(T[][] left, T[][] right);

    T[][] generateHigherDim(int size);

    void activateInPlace(T[] input);

    T[] activate(T[] input);

    void activatePrimeInPlace(T[] input);

    T[] activatePrime(T[] input);

    T[] initRandom(int width, int height);

    T[] subtract(T[] minuend, T[] subtrahend);

    T[] transpose(T[] input);

    T squaredError(T[] target, T[] output);

    T transform(T input, U operator);

    void mutate(T input, U operator);

    T[] transform(T[] input, U operator);

    void mutate(T[] input, U operator);

    T[][] transform(T[][] input, U operator);

    void mutate(T[][] input, U operator);

}
