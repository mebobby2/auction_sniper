package auctionsniper.util;

/**
 * Created by bob on 22/07/2017.
 */
public class Defect extends RuntimeException {

  public Defect() {
    super();
  }

  public Defect(String message, Throwable cause) {
    super(message, cause);
  }

  public Defect(String message) {
    super(message);
  }

  public Defect(Throwable cause) {
    super(cause);
  }
}
