package matcher.exception;

public class MatchingException extends Exception{

  public MatchingException(String message) {
    super(message);
  }

  public MatchingException(String message, Exception e) {
  }
}
