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
    saveTextFile(new File(fileName), content);
  }

  @Override
  public void saveTextFile(File fileHandle, String content) {
    try (BufferedWriter output = new BufferedWriter(new FileWriter(fileHandle))) {
      output.write(content);
    } catch (IOException ioe) {
      System.out.println(ioe);
    }

    // System.out.println(fileHandle.getAbsolutePath() + " saved.");
  }

  @Override
  public String loadTextFile(String fileName) {
    return loadTextFile(new File(fileName));
  }

  @Override
  public String loadTextFile(File fileHandle) {

    try (BufferedReader input = new BufferedReader(new FileReader(fileHandle))) {
      String currentLine;
      StringBuffer fileContent = new StringBuffer();

      while ((currentLine = input.readLine()) != null) {
        fileContent.append(currentLine);
      }
      return fileContent.toString();
    } catch (IOException ioe) {
      System.out.println(ioe);
      return "";
    }
  }

  @Override
  public void saveBinaryFile(String fileName, byte[] content) {
    saveBinaryFile(new File(fileName), content);
  }

  @Override
  public void saveBinaryFile(File fileHandle, byte[] content) {
    try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(fileHandle))) {
      outputStream.write(content);
    } catch (IOException e) {
      // Deliberately do nothing
    }
  }

  @Override
  public byte[] loadBinaryFile(String fileName) {
    return loadBinaryFile(new File(fileName));
  }

  @Override
  public byte[] loadBinaryFile(File fileHandle) {
    try (DataInputStream inputStream = new DataInputStream(new FileInputStream(fileHandle))) {
      byte[] content = new byte[(int) fileHandle.length()];
      inputStream.readFully(content);
      return content;
    } catch (IOException ioe) {
      return null;
    }
  }
}
