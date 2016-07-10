import java.util.Arrays;


/**
 * @author rgettys
 */

public class Main {

    public static void main2() {
        MatrixMathPack<float[], FloatUnaryOperator> floatMathPack = new JavaFloatMatrixMathPack(Main::stableFloatSigmoid, Main::floatSigmoidPrime);
        float[][][] weights = new float[][][]{
                new float[][]{
                        new float[]{0.15f, 0.25f},
                        new float[]{0.2f, 0.3f}
                },
                new float[][]{
                        new float[]{0.4f, 0.5f},
                        new float[]{0.45f, 0.55f}
                }
        };
        float[][] inputs = new float[][]{
                new float[]{0.05f, 0.10f}
        };
        float[][] outputs = new float[][]{
                new float[]{0.01f, 0.99f}
        };

        long time = System.nanoTime();
        Network<float[], FloatUnaryOperator> network = new Network<>(floatMathPack, input -> input * 0.5f, weights);
        float[][] newoutputs = network.forward(inputs);
        System.out.println(Arrays.deepToString(newoutputs));
        for (int i = 0; i < 10000; ++i) {
            network.train(inputs, outputs);
        }
        System.out.printf("%.2f seconds.\n", (System.nanoTime() - time) / 1000000000.0);
        newoutputs = network.forward(inputs);
        System.out.println(Arrays.deepToString(newoutputs));
    }

    public static void main3() {
        MatrixMathPack<float[], FloatUnaryOperator> floatMathPack = new JavaFloatMatrixMathPack(Main::stableFloatSigmoid, Main::floatSigmoidPrime);
        float[][] inputs = new float[][]{
                new float[]{0.0f},
                new float[]{15.0f},
                new float[]{30.0f},
                new float[]{45.0f},
                new float[]{60.0f},
                new float[]{75.0f},
                new float[]{90.0f},
                new float[]{105.0f},
                new float[]{120.0f},
                new float[]{135.0f},
                new float[]{150.0f},
                new float[]{165.0f},
                new float[]{180.0f},
                new float[]{195.0f},
                new float[]{210.0f},
                new float[]{225.0f},
                new float[]{240.0f},
                new float[]{255.0f},
                new float[]{270.0f},
                new float[]{285.0f},
                new float[]{300.0f},
                new float[]{315.0f},
                new float[]{330.0f},
                new float[]{345.0f},
                new float[]{360.0f},
        };
        floatMathPack.autoScaleInPlace(inputs);
        float[][] outputs = new float[][]{
                new float[]{0.0f},
                new float[]{0.258819045102521f},
                new float[]{0.5f},
                new float[]{0.707106781186548f},
                new float[]{0.866025403784439f},
                new float[]{0.965925826289068f},
                new float[]{1.0f},
                new float[]{0.965925826289068f},
                new float[]{0.866025403784439f},
                new float[]{0.707106781186548f},
                new float[]{0.5f},
                new float[]{0.258819045102521f},
                new float[]{0.0f},
                new float[]{-0.258819045102521f},
                new float[]{-0.5f},
                new float[]{-0.707106781186548f},
                new float[]{-0.866025403784439f},
                new float[]{-0.965925826289068f},
                new float[]{-1.0f},
                new float[]{-0.965925826289068f},
                new float[]{-0.866025403784439f},
                new float[]{-0.707106781186548f},
                new float[]{-0.5f},
                new float[]{-0.258819045102521f},
                new float[]{0.0f},
        };
        for (int i = 0; i < outputs.length; ++i) {
            outputs[i][0] = (outputs[i][0] * 0.5f) + 0.5f;
        }
        System.out.println("rescaled outputs: " + Arrays.deepToString(outputs));

        long time = System.nanoTime();
        Network<float[], FloatUnaryOperator> network = new Network<>(1, new int[]{10}, 1, floatMathPack, input -> input * 1.1f);
        float[][] newoutputs = network.forward(inputs);
        System.out.println(Arrays.deepToString(newoutputs));
        for (int i = 0; i < 100000; ++i) {
            network.train(inputs, outputs);
        }
        System.out.printf("%.2f seconds.\n", (System.nanoTime() - time) / 1000000000.0);
        newoutputs = network.forward(inputs);
        System.out.println(Arrays.deepToString(newoutputs));
    }

    public static void main(String[] args) {
        main2();
        System.exit(0);
        
        MatrixMathPack<float[], FloatUnaryOperator> mathPack = new JavaFloatMatrixMathPack(Main::stableFloatSigmoid, Main::floatSigmoidPrime);
        Network<float[], FloatUnaryOperator> network = new Network<>(2, new int[]{3}, 1, mathPack, input -> input * 0.5f);

        float[][] input = new float[][]{
                new float[]{1.0f, 2.0f},
                new float[]{2.0f, 3.0f},
                new float[]{6.0f, 7.0f},
                new float[]{18.0f, 7.0f},
                new float[]{3.0f, 3.0f}
        };

        float[][] output = new float[][]{
                new float[]{4.0f},
                new float[]{7.0f},
                new float[]{19.0f},
                new float[]{43.0f},
                new float[]{9.0f}
        };

        mathPack.autoScaleInPlace(input);
        float[] outputScaleFactors = mathPack.autoScaleInPlace(output);
        float[][] preTrainOutput = network.forward(input);
        long time = System.nanoTime();
        for (int i = 0; i < 300000; ++i) {
            network.train(input, output);
        }
        System.out.printf("%.2f seconds.\n", (System.nanoTime() - time) / 1000000000.0);

        float[][] newOutput = network.forward(input);
        mathPack.rescaleInPlace(newOutput, outputScaleFactors);
        mathPack.rescaleInPlace(preTrainOutput, outputScaleFactors);

        System.out.println("Before: " + java.util.Arrays.deepToString(preTrainOutput));
        System.out.println("After: " + java.util.Arrays.deepToString(newOutput));
    }

    private static float stableFloatSigmoid(float input) {
        if (input >= 0.0f) {
            return 1.0f / (1.0f + (float) Math.exp(-input));
        } else {
            float epowx = (float) Math.exp(input);
            return epowx / (1.0f + epowx);
        }
    }

    private static float floatSigmoidPrime(float input) {
        float sigmoid = stableFloatSigmoid(input);
        return sigmoid * (1 - sigmoid);
    }

}
