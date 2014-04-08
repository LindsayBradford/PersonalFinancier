/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control;

import java.io.IOException;

import blacksmyth.general.FileUtilities;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

/**
 * An adapter through to a 3rd-party JSON Serialization library.
 * All access to JSson functionality should go through this adapter.
 * @author linds
 */
public class JSonFileAdapter<T> implements IPersonalFinancierFileAdapter<T> {
  
  @Override
  public void toFileFromObject(String filePath, T t) {
    try {
      FileUtilities.saveTextFile(
          filePath, 
          JsonWriter.formatJson(
              JsonWriter.objectToJson(t)
          )
      );

    } catch (IOException e) {
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public T toObjectFromFile(String filePath) {
    try {
      return (T) JsonReader.jsonToJava(
          FileUtilities.loadTextFile(filePath)
      );
    } catch (IOException e) {
      return null;
    }
  }
}
