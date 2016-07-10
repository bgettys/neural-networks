/**
 * @author rgettys
 */
public class NotImplementedException extends org.apache.commons.lang3.NotImplementedException {

    private static final long serialVersionUID = 1L;

	public NotImplementedException() {
        super(Constants.DEFAULT_NOT_IMPLEMENTED_MESSAGE);
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
