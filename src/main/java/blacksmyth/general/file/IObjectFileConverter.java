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

/**
 * An interface that all objects involved in converting objects to files (and vice-versa) should implement.
 * Note: this is to be considered looser than standard Java Serialization for those converters that do not use
 * the Java serialisation framework.
 * @param <T>
 */
public interface IObjectFileConverter<T> {

  /**
   * Saves the content of the object t to a file at filePath.
   * @param filePath
   * @param t
   */
  void toFileFromObject(String filePath, T t);
  
  /**
   * Convert the content at filePath to an object of type T, and returns it.
   * @param filePath
   * @return
   */
  public T toObjectFromFile(String filePath);
}
