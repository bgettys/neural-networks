import java.util.concurrent.ThreadLocalRandom;

/**
 * @author rgettys
 */
public class JavaFloatMatrixMathPack implements MatrixMathPack<float[], FloatUnaryOperator> {

    private final FloatUnaryOperator activationFunction, activationFunctionDerivative;

    public JavaFloatMatrixMathPack() {
        this(input -> input, input -> input);
    }

    public JavaFloatMatrixMathPack(FloatUnaryOperator activationFunction, FloatUnaryOperator activationFunctionDerivative) {
        this.activationFunction = activationFunction;
        this.activationFunctionDerivative = activationFunctionDerivative;
    }

    @Override
    public void rescaleInPlace(float[][] input, float[] scaleFactors) {
        for (int i = 0; i < input.length; ++i) {
            for (int j = 0; j < scaleFactors.length; ++j) {
                input[i][j] *= scaleFactors[j];
            }
        }
    }

    @Override
    public float[][] rescale(float[][] input, float[] scaleFactors) {
        float[][] output = new float[input.length][];
        int width = input[0].length;
        for (float[] inputRow : input) {
            float[] outputRow = new float[width];
            for (int i = 0; i < width; ++i) {
                outputRow[i] = inputRow[i] * scaleFactors[i];
            }
        }
        return output;
    }

    @Override
    public float[][] clone(float[][] input) {
        float[][] clone = new float[input.length][];
        for (int i = 0; i < input.length; ++i) {
            float[] inputRow = input[i];
            clone[i] = new float[inputRow.length];
            for (int j = 0; j < inputRow.length; ++j) {
                clone[i][j] = inputRow[j];
            }
        }
        return clone;
    }

    @Override
    public float[][] dotProduct(float[][] multiplicand, float[][] multiplier) {
        // No null checks because SERIOUSLY?
        int height = multiplicand.length;
        // If you provide a multiplier without a single row in it, then you deserve the
        // ArrayIndexOutOfBoundsException and I feel no remorse about throwing it here.
        int width = multiplier[0].length;
        int dotProductSize = multiplicand[0].length;
        float[][] product = new float[height][];
        for (int i = 0; i < height; ++i) {
            float[] multiplicandRow = multiplicand[i];
            float[] productRow = product[i] = new float[width];
            for (int j = 0; j < dotProductSize; ++j) {
                for (int k = 0; k < width; ++k) {
                    productRow[k] += multiplicandRow[j] * multiplier[j][k];
                }
            }
        }
        return product;
    }

    @Override
    public float[][] hadamardProduct(float[][] multiplicand, float[][] multiplier) {
        int height = multiplicand.length;
        int width = multiplicand[0].length;
        float[][] product = new float[height][];
        for (int i = 0; i < height; ++i) {
            float[] productRow = product[i] = new float[width];
            float[] multiplicandRow = multiplicand[i];
            float[] multiplierRow = multiplier[i];
            for (int j = 0; j < width; ++j) {
                productRow[j] = multiplicandRow[j] * multiplierRow[j];
            }
        }
        return product;
    }

    @Override
    public float[] autoScaleInPlace(float[][] input) {
        int inputSize = input[0].length;
        float[] maxes = new float[inputSize];
        for (float[] inputRow : input) {
            for (int i = 0; i < inputSize; ++i) {
                maxes[i] = Math.max(maxes[i], inputRow[i]);
            }
        }
        for (float[] inputRow : input) {
            for (int i = 0; i < inputSize; ++i) {
                inputRow[i] /= maxes[i];
            }
        }
        return maxes;
    }

    @Override
    public void subtractInPlace(float[][][] left, float[][][] right) {
        for (int i = 0; i < left.length; ++i) {
            for (int j = 0; j < left[i].length; ++j) {
                for (int k = 0; k < left[i][j].length; ++k) {
                    left[i][j][k] -= right[i][j][k];
                }
            }
        }

    }

    @Override
    public float[][][] generateHigherDim(int size) {
        return new float[size][][];
    }

    @Override
    public void activateInPlace(float[][] input) {
        mutate(input, activationFunction);
    }

    @Override
    public float[][] activate(float[][] input) {
        return transform(input, activationFunction);
    }

    @Override
    public void activatePrimeInPlace(float[][] input) {
        mutate(input, activationFunctionDerivative);
    }

    @Override
    public float[][] activatePrime(float[][] input) {
        return transform(input, activationFunctionDerivative);
    }

    @Override
    public void mutate(float[] input, FloatUnaryOperator f) {
        for (int i = 0; i < input.length; ++i) {
            input[i] = f.applyAsFloat(input[i]);
        }
    }

    @Override
    public void mutate(float[][] input, FloatUnaryOperator f) {
        for (float[] vector : input) {
            mutate(vector, f);
        }
    }

    @Override
    public void mutate(float[][][] input, FloatUnaryOperator f) {
        for (float[][] vector : input) {
            mutate(vector, f);
        }
    }

    @Override
    public float[] transform(float[] input, FloatUnaryOperator f) {
        int size = input.length;
        float[] output = new float[size];
        for (int j = 0; j < size; ++j) {
            output[j] = f.applyAsFloat(input[j]);
        }
        return output;
    }

    @Override
    public float[][] transform(float[][] input, FloatUnaryOperator f) {
        int height = input.length;
        float[][] output = new float[height][];
        for (int i = 0; i < height; ++i) {
            output[i] = transform(input[i], f);
        }
        return output;
    }

    @Override
    public float[][][] transform(float[][][] input, FloatUnaryOperator f) {
        int size = input.length;
        float[][][] output = new float[size][][];
        for (int i = 0; i < size; ++i) {
            output[i] = transform(input[i], f);
        }
        return output;
    }

    public float[][] initRandom(int width, int height) {
        float[][] result = new float[height][];
        for (int i = 0; i < height; ++i) {
            float[] resultRow = result[i] = new float[width];
            for (int j = 0; j < resultRow.length; ++j) {
                resultRow[j] = ThreadLocalRandom.current().nextFloat();
            }
        }
        return result;
    }

    @Override
    public float[][] subtract(float[][] minuend, float[][] subtrahend) {
        int height = minuend.length;
        int width = minuend[0].length;
        float[][] result = new float[height][];
        for (int i = 0; i < height; ++i) {
            float[] resultRow = result[i] = new float[width];
            for (int j = 0; j < width; ++j) {
                resultRow[j] = minuend[i][j] - subtrahend[i][j];
            }
        }
        return result;
    }

    @Override
    public float[][] transpose(float[][] input) {
        int width = input.length;
        int height = input[0].length;
        float[][] result = new float[height][];
        for (int i = 0; i < height; ++i) {
            result[i] = new float[width];
        }
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                result[j][i] = input[i][j];
            }
        }
        return result;
    }

    @Override
    public float[] squaredError(float[][] target, float[][] output) {
        int height = target.length;
        float[] result = new float[height];
        int width = target[0].length;
        for (int i = 0; i < height; ++i) {
            float[] targetRow = target[i];
            float[] outputRow = output[i];
            for (int j = 0; j < width; ++j) {
                float diff = targetRow[j] - outputRow[j];
                result[i] += 0.5f * diff * diff;
            }
        }
        return result;
    }

}
