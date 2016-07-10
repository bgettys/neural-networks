/**
 * @author rgettys
 */
public class Network<T, U> {

    private final T[][] weights;
    private final MatrixMathPack<T, U> mathPack;
    private final U learningRateScaler;

    public Network(MatrixMathPack<T, U> mathPack, U learningRateScaler, T[][] initialWeights) {
        this.mathPack = mathPack;
        this.learningRateScaler = learningRateScaler;
        weights = initialWeights;
    }

    public Network(int inputSize, int[] hiddenLayerSizes, int outputSize, MatrixMathPack<T, U> mathPack, U learningRateScaler) {
        this(mathPack, learningRateScaler, initRandomWeights(mathPack, inputSize, hiddenLayerSizes, outputSize));
    }

    private static <T, U> T[][] initRandomWeights(MatrixMathPack<T, U> mathPack, int inputSize, int[] hiddenLayerSizes, int outputSize) {
        T[][] output = mathPack.generateHigherDim(hiddenLayerSizes.length + 1);
        int prevSize = inputSize;
        int i = 0;
        for (; i < hiddenLayerSizes.length; ++i) {
            output[i] = mathPack.initRandom(hiddenLayerSizes[i], prevSize);
            prevSize = hiddenLayerSizes[i];
        }
        output[hiddenLayerSizes.length] = mathPack.initRandom(outputSize, prevSize);
        return output;
    }


    public T[] forward(T[] input) {
        T[] activation = input;
        for (T[] layerWeights : weights) {
            activation = mathPack.dotProduct(activation, layerWeights);
            mathPack.activateInPlace(activation);
        }
        return activation;
    }

    public void train(T[] input, T[] exampleResult) {
        ArgUtil.checkNull(input, "input");
        ArgUtil.checkNull(exampleResult, "exampleResult");
        T[][] activationTotals = mathPack.generateHigherDim(weights.length);
        T[][] postActivations = mathPack.generateHigherDim(weights.length);
        T[] lastA = input;
        for (int i = 0; i < weights.length; ++i) {
            T[] w = weights[i];
            activationTotals[i] = mathPack.dotProduct(lastA, w);
            lastA = postActivations[i] = mathPack.activate(activationTotals[i]);
        }
        T[] yhat = postActivations[weights.length - 1];
        T[][] djdw = gradient(input, yhat, exampleResult, activationTotals, postActivations);
        mathPack.mutate(djdw, learningRateScaler);
        mathPack.subtractInPlace(weights, djdw);
    }

    private T[][] gradient(T[] input, T[] output, T[] trainingExamples, T[][] activationTotals, T[][] postActivations) {
        T[][] djdw = mathPack.generateHigherDim(postActivations.length);
        T[] yDiff = mathPack.subtract(output, trainingExamples);
        int i = activationTotals.length - 1;
        mathPack.activatePrimeInPlace(activationTotals[i]);
        T[] lastDelta = mathPack.hadamardProduct(yDiff, activationTotals[i]);
        djdw[i] = mathPack.dotProduct(mathPack.transpose(postActivations[i - 1]), lastDelta);
        for (i = i - 1; i > 0; --i) {
            mathPack.activatePrimeInPlace(activationTotals[i]);
            lastDelta = mathPack.dotProduct(mathPack.dotProduct(lastDelta, mathPack.transpose(weights[i + 1])), activationTotals[i]);
            djdw[i] = mathPack.dotProduct(mathPack.transpose(postActivations[i - 1]), lastDelta);
        }
        mathPack.activatePrimeInPlace(activationTotals[i]);
        lastDelta = mathPack.hadamardProduct(mathPack.dotProduct(lastDelta, mathPack.transpose(weights[i + 1])), activationTotals[i]);
        djdw[i] = mathPack.dotProduct(mathPack.transpose(input), lastDelta);
        return djdw;
    }

}
