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

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A collection of general utilities around byte array processing.
 */
public class ByteUtilities {
  
  public static Charset ENCODING = Charset.forName("UTF-8");
  
  /**
   * Converts the supplied byte array to an UTF-8 encoded string.
   * @param bytes
   * @return UTF-8 encoded string of the bytes supplied.
   */
  public static String BytesToString(byte[] bytes) {
    return new String(bytes, ENCODING);
  }

  /**
   * Converts the supplied strung to a byte-array of the string, encoded in UTF-8..
   * @param bytes
   * @return byte array of string.
   */
  public static byte[] StringToBytes(String string) {
    return string.getBytes(ENCODING);
  }

  /**
   * Returns a copy the supplied array with all trailing 0 characters
   * trimmed.
   * @param bytes
   * @return byte array of string.
   */
  public static byte[] TrimBytes(byte[] bytes)
  {
      int i = bytes.length - 1;
      while (i >= 0 && bytes[i] == 0) {
          --i;
      }
      return Arrays.copyOf(bytes, i + 1);
  }

}
