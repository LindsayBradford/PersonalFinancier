/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

public class MaskedLogger {
  private Logger log;
  
  // Below, an educated-guess at possible sensitive numbers such as account, bsb and credit card numbers.
  // any sequence of digits, space or hyphens book-ended by a digit.
  private static Pattern pattern = Pattern.compile("\\d([\\d\\-\\s]+)\\d"); 

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
      message = message.replaceFirst(guts,maskedGuts);
    }
    return message;
  }
}
