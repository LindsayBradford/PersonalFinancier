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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A collection of general utilities for file handling.
 * @author linds
 *
 */
public class FileUtilities {
  
  /**
   * Attempts to save the text <tt>content</tt> to <tt>fileName</tt>.
   * @param fileName
   * @param content
   */
  public static void saveTextFile(String fileName, String content) {
    saveTextFile(
        new File(fileName), 
        content
    );
  }

  /**
   * Attempts to save the text <tt>content</tt> to <tt>fileHandle</tt>.
   * @param fileName
   * @param content
   */
  public static void saveTextFile(File fileHandle, String content) {
    BufferedWriter output = null;

    try {
      output = new BufferedWriter(
          new FileWriter(fileHandle)
      );
      output.write(content);
      output.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(
        fileHandle.getAbsolutePath() + " saved."
    );
  }
  
  /**
   * Returns the text content of in file <tt>fileName</tt>
   * @param fileName
   * @return
   */
  public static String loadTextFile(String fileName) {
    return loadTextFile(
        new File(fileName)
    );
  }
  
  /**
   * Returns the text content in <tt>fileHandle</tt>
   * @param fileHandle
   * @return
   */
  public static String loadTextFile(File fileHandle) {
    BufferedReader input = null;

    StringBuffer fileContent = new StringBuffer();
    String currentLine;
    try {
      input = new BufferedReader(
          new FileReader(fileHandle)
      );
      
      while ((currentLine = input.readLine()) != null) {
        fileContent.append(currentLine);
      }
      input.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
    
    System.out.println(fileHandle.getAbsolutePath() + " loaded.");

    return fileContent.toString();
  }
}
