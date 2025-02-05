package blacksmyth.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

public class MaskedLogger {
  private Logger log;
  
  // Below, an educated-guess at possible sensitive numbers such as account, bsb and credit card numbers.
  private static Pattern pattern = Pattern.compile("\\d([\\d\\-\\s]+)\\d"); // any sequence of digits, space or hyphens book-ended by digits.

  public MaskedLogger() {}
  
  public static MaskedLogger wrap(Logger logger) {
    MaskedLogger newLogger = new MaskedLogger();
    newLogger.log = logger;
    
    return newLogger;
  }

  public void debug(String message) {
    log.debug(mask(message));
  }
  
  public void info(String message) {
    log.info(mask(message));
  }
  
  public void warn(String message) {
    log.warn(mask(message));
  }
  
  public void error(String message) {
    log.error(mask(message));
  }
  
  private String mask(String message) {
    Matcher m = pattern.matcher(message);
    while(m.find()) {
      String guts = m.group(1);
      String maskedGuts = guts.replaceAll("\\d", "*");
      message = message.replace(guts,maskedGuts);
    }
    return message;
  }
}
