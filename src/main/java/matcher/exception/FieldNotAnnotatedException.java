package matcher.exception;

public class FieldNotAnnotatedException extends Exception{

  public FieldNotAnnotatedException(String message) {
    super(message);
  }
  public FieldNotAnnotatedException(String message, Throwable cause) {
    super(message, cause);
  }
}
