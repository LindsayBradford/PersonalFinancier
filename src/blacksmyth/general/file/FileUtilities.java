/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A collection of general utilities for file handling.
 * @author linds
 *
 */
public class FileUtilities {
  
  private static Charset ENCODING = Charset.forName("UTF-8");
  
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
    
    // System.out.println(fileHandle.getAbsolutePath() + " saved.");
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
    
    //System.out.println(fileHandle.getAbsolutePath() + " loaded.");

    return fileContent.toString();
  }
  
  /**
   * Attempts to save the byte array <tt>content</tt> to binary file <tt>fileName</tt>.
   * @param fileName
   * @param content
   */
  public static void saveBinaryFile(String fileName, byte[] content) {
    saveBinaryFile(
        new File(fileName), 
        content
    );
  }

  /**
   * Attempts to save the byte array <tt>content</tt> to binary file <tt>fileHandle</tt>.
   * @param fileName
   * @param content
   */
  public static void saveBinaryFile(File fileHandle, byte[] content) {
    DataOutputStream outputStream;
    try {
      outputStream = new DataOutputStream(new FileOutputStream(fileHandle));
      outputStream.write(content);
      outputStream.close();
    } catch (FileNotFoundException fnfe) {
      return;
    } catch (IOException ioe) {
      return;
    }
  }
  
  /**
   * Returns the byte arrray content of in binary file <tt>fileName</tt>
   * @param fileName
   * @return
   */
  public static byte[] loadBinaryFile(String fileName) {
    return loadBinaryFile(
        new File(fileName)
    );
  }
  
  /**
   * Returns the text content in <tt>fileHandle</tt>
   * @param fileHandle
   * @return
   */
  public static byte[] loadBinaryFile(File fileHandle) {
    byte[] content = new byte[(int)fileHandle.length()];
    DataInputStream inputStream;
    try {
      inputStream = new DataInputStream(new FileInputStream(fileHandle));
      inputStream.readFully(content);
      inputStream.close();
    } catch (FileNotFoundException fnfe){
      return null;
    } catch (IOException ioe) {
      return null;
    }
    return content;
  }
  
  public static String BytesToString(byte[] bytes) {
    return new String(bytes, ENCODING);
  }
  
  public static byte[] StringToBytes(String string) {
    return string.getBytes(ENCODING);
  }
  
  public static byte[] TrimBytes(byte[] bytes)
  {
      int i = bytes.length - 1;
      while (i >= 0 && bytes[i] == 0){
          --i;
      }
      return Arrays.copyOf(bytes, i + 1);
  }

}
