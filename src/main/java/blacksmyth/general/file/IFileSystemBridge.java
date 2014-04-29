package blacksmyth.general.file;

import java.io.File;

public interface IFileSystemBridge {

  /**
   * Attempts to save the text <tt>content</tt> to <tt>fileName</tt>.
   * @param fileName
   * @param content
   */
  public abstract void saveTextFile(String fileName, String content);

  /**
   * Attempts to save the text <tt>content</tt> to <tt>fileHandle</tt>.
   * @param fileName
   * @param content
   */
  public abstract void saveTextFile(File fileHandle, String content);

  /**
   * Returns the text content of in file <tt>fileName</tt>
   * @param fileName
   * @return
   */
  public abstract String loadTextFile(String fileName);

  /**
   * Returns the text content in <tt>fileHandle</tt>
   * @param fileHandle
   * @return
   */
  public abstract String loadTextFile(File fileHandle);

  /**
   * Attempts to save the byte array <tt>content</tt> to binary file <tt>fileName</tt>.
   * @param fileName
   * @param content
   */
  public abstract void saveBinaryFile(String fileName, byte[] content);

  /**
   * Attempts to save the byte array <tt>content</tt> to binary file <tt>fileHandle</tt>.
   * @param fileName
   * @param content
   */
  public abstract void saveBinaryFile(File fileHandle, byte[] content);

  /**
   * Returns the byte arrray content of in binary file <tt>fileName</tt>
   * @param fileName
   * @return
   */
  public abstract byte[] loadBinaryFile(String fileName);

  /**
   * Returns the text content in <tt>fileHandle</tt>
   * @param fileHandle
   * @return
   */
  public abstract byte[] loadBinaryFile(File fileHandle);

}