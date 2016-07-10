import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleUnaryOperator;

/**
 * @author rgettys
 */
public class JavaDoubleMatrixMathPack implements MatrixMathPack<double[], DoubleUnaryOperator> {

    private final DoubleUnaryOperator activationFunction, activationFunctionDerivative;

    public JavaDoubleMatrixMathPack() {
        this(input -> input, input -> input);
    }

    public JavaDoubleMatrixMathPack(DoubleUnaryOperator activationFunction, DoubleUnaryOperator activationFunctionDerivative) {
        this.activationFunction = activationFunction;
        this.activationFunctionDerivative = activationFunctionDerivative;
    }

    @Override
    public void rescaleInPlace(double[][] input, double[] scaleFactors) {
        for (int i = 0; i < input.length; ++i) {
            for (int j = 0; j < scaleFactors.length; ++j) {
                input[i][j] *= scaleFactors[j];
            }
        }
    }

    @Override
    public double[][] rescale(double[][] input, double[] scaleFactors) {
        double[][] output = new double[input.length][];
        int width = input[0].length;
        for (double[] inputRow : input) {
            double[] outputRow = new double[width];
            for (int i = 0; i < width; ++i) {
                outputRow[i] = inputRow[i] * scaleFactors[i];
            }
        }
        return output;
    }

    @Override
    public double[][] clone(double[][] input) {
        double[][] clone = new double[input.length][];
        for (int i = 0; i < input.length; ++i) {
            double[] inputRow = input[i];
            clone[i] = new double[inputRow.length];
            for (int j = 0; j < inputRow.length; ++j) {
                clone[i][j] = inputRow[j];
            }
        }
        return clone;
    }

    @Override
    public double[][] dotProduct(double[][] multiplicand, double[][] multiplier) {
        // No null checks because SERIOUSLY?
        int height = multiplicand.length;
        // If you provide a multiplier without a single row in it, then you deserve the
        // ArrayIndexOutOfBoundsException and I feel no remorse about throwing it here.
        int width = multiplier[0].length;
        int dotProductSize = multiplicand[0].length;
        double[][] product = new double[height][];
        for (int i = 0; i < height; ++i) {
            double[] multiplicandRow = multiplicand[i];
            double[] productRow = product[i] = new double[width];
            for (int j = 0; j < dotProductSize; ++j) {
                for (int k = 0; k < width; ++k) {
                    productRow[k] += multiplicandRow[j] * multiplier[j][k];
                }
            }
        }
        return product;
    }

    @Override
    public double[][] hadamardProduct(double[][] multiplicand, double[][] multiplier) {
        int height = multiplicand.length;
        int width = multiplicand[0].length;
        double[][] product = new double[height][];
        for (int i = 0; i < height; ++i) {
            double[] productRow = product[i] = new double[width];
            double[] multiplicandRow = multiplicand[i];
            double[] multiplierRow = multiplier[i];
            for (int j = 0; j < width; ++j) {
                productRow[j] = multiplicandRow[j] * multiplierRow[j];
            }
        }
        return product;
    }

    @Override
    public double[] autoScaleInPlace(double[][] input) {
        int inputSize = input[0].length;
        double[] maxes = new double[inputSize];
        for (double[] inputRow : input) {
            for (int i = 0; i < inputSize; ++i) {
                maxes[i] = Math.max(maxes[i], inputRow[i]);
            }
        }
        for (double[] inputRow : input) {
            for (int i = 0; i < inputSize; ++i) {
                inputRow[i] /= maxes[i];
            }
        }
        return maxes;
    }

    @Override
    public void subtractInPlace(double[][][] left, double[][][] right) {
        for (int i = 0; i < left.length; ++i) {
            for (int j = 0; j < left[i].length; ++j) {
                for (int k = 0; k < left[i][j].length; ++k) {
                    left[i][j][k] -= right[i][j][k];
                }
            }
        }

    }

    @Override
    public double[][][] generateHigherDim(int size) {
        return new double[size][][];
    }

    @Override
    public void activateInPlace(double[][] input) {
        mutate(input, activationFunction);
    }

    @Override
    public double[][] activate(double[][] input) {
        return transform(input, activationFunction);
    }

    @Override
    public void activatePrimeInPlace(double[][] input) {
        mutate(input, activationFunctionDerivative);
    }

    @Override
    public double[][] activatePrime(double[][] input) {
        return transform(input, activationFunctionDerivative);
    }

    @Override
    public void mutate(double[] input, DoubleUnaryOperator f) {
        for (int i = 0; i < input.length; ++i) {
            input[i] = f.applyAsDouble(input[i]);
        }
    }

    @Override
    public void mutate(double[][] input, DoubleUnaryOperator f) {
        for (double[] vector : input) {
            mutate(vector, f);
        }
    }

    @Override
    public void mutate(double[][][] input, DoubleUnaryOperator f) {
        for (double[][] vector : input) {
            mutate(vector, f);
        }
    }

    @Override
    public double[] transform(double[] input, DoubleUnaryOperator f) {
        int size = input.length;
        double[] output = new double[size];
        for (int j = 0; j < size; ++j) {
            output[j] = f.applyAsDouble(input[j]);
        }
        return output;
    }

    @Override
    public double[][] transform(double[][] input, DoubleUnaryOperator f) {
        int height = input.length;
        double[][] output = new double[height][];
        for (int i = 0; i < height; ++i) {
            output[i] = transform(input[i], f);
        }
        return output;
    }

    @Override
    public double[][][] transform(double[][][] input, DoubleUnaryOperator f) {
        int height = input.length;
        double[][][] output = new double[height][][];
        for (int i = 0; i < height; ++i) {
            output[i] = transform(input[i], f);
        }
        return output;
    }

    @Override
    public double[][] initRandom(int width, int height) {
        double[][] result = new double[height][];
        for (int i = 0; i < height; ++i) {
            double[] resultRow = result[i] = new double[width];
            for (int j = 0; j < resultRow.length; ++j) {
                resultRow[j] = ThreadLocalRandom.current().nextFloat();
            }
        }
        return result;
    }

    @Override
    public double[][] subtract(double[][] minuend, double[][] subtrahend) {
        int height = minuend.length;
        int width = minuend[0].length;
        double[][] result = new double[height][];
        for (int i = 0; i < height; ++i) {
            double[] resultRow = result[i] = new double[width];
            for (int j = 0; j < width; ++j) {
                resultRow[j] = minuend[i][j] - subtrahend[i][j];
            }
        }
        return result;
    }

    @Override
    public double[][] transpose(double[][] input) {
        int width = input.length;
        int height = input[0].length;
        double[][] result = new double[height][];
        for (int i = 0; i < height; ++i) {
            result[i] = new double[width];
        }
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                result[j][i] = input[i][j];
            }
        }
        return result;
    }

    @Override
    public double[] squaredError(double[][] target, double[][] output) {
        int height = target.length;
        double[] result = new double[height];
        int width = target[0].length;
        for (int i = 0; i < height; ++i) {
            double[] targetRow = target[i];
            double[] outputRow = output[i];
            for (int j = 0; j < width; ++j) {
                double diff = targetRow[j] - outputRow[j];
                result[i] += 0.5 * diff * diff;
            }
        }
        return result;
    }

}
