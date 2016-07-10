/**
 * @author rgettys
 */
public final class ArgUtil {

    private ArgUtil() {
    }

    public static void checkNull(Object arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " argument must not be null.");
        }
    }

}
