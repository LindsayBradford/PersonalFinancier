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

/**
 * A collection of general utilities for file handling.
 */
public class FileSystemBridge implements IFileSystemBridge {
  
  @Override
  public void saveTextFile(String fileName, String content) {
    saveTextFile(
        new File(fileName), 
        content
    );
  }

  @Override
  public void saveTextFile(File fileHandle, String content) {
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
  
  @Override
  public String loadTextFile(String fileName) {
    return loadTextFile(
        new File(fileName)
    );
  }
  
  @Override
  public String loadTextFile(File fileHandle) {
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
    
    return fileContent.toString();
  }
  
  @Override
  public void saveBinaryFile(String fileName, byte[] content) {
    saveBinaryFile(
        new File(fileName), 
        content
    );
  }

  @Override
  public void saveBinaryFile(File fileHandle, byte[] content) {
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
  
  @Override
  public byte[] loadBinaryFile(String fileName) {
    return loadBinaryFile(
        new File(fileName)
    );
  }
  
  @Override
  public byte[] loadBinaryFile(File fileHandle) {
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
}
