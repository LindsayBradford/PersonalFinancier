/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies.json;

import java.io.IOException;

import com.cedarsoftware.util.io.JsonWriter;
import com.cedarsoftware.util.io.JsonReader;

/**
 * A class implementing the 'Concrete Implementor class' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and the open-source JSON library json-io 
 * (https://code.google.com/p/json-io/)
 *
 * @param <T> The type of objects that this class will bridge JSON serialisation for.
 */

final class JSonIoBridge<T> implements IJSonSerialisationBridge<T> {

  @Override
  public String toJSon(T object) {
    try {
      return JsonWriter.formatJson(
        JsonWriter.objectToJson(object)
      );
    } catch (IOException ioe) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T fromJson(String jsonContent) {
    if (jsonContent == null) {
      return null;
    }
    
    try {
      return (T) JsonReader.jsonToJava(
          jsonContent
      );
    } catch (IOException e) {
      return null;
    }
  }
}
