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

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cedarsoftware.util.io.JsonIoException;
import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

/**
 * A class implementing the 'Concrete Implementor class' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and the open-source JSON library json-io 
 * (https://code.google.com/p/json-io/)
 *
 * @param <T> The type of objects that this class will bridge JSON serialisation for.
 */

final class JSonIoBridge<T> implements IJSonSerialisationBridge<T> {
  
  private static final Logger LOG = LogManager.getLogger(JSonIoBridge.class);

  @Override
  public String toJSon(T object) {
    LOG.info("Converting object hierarchy to JSON");
    return JsonWriter.formatJson(
      JsonWriter.objectToJson(object)
    );
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public T fromJSon(String jsonContent) {
    LOG.info("Converting JSON to object hierarchy");

    if (jsonContent == null) { 
      LOG.warn("Content supplied was empty. Nothing to convert.");
      return null; 
    }
    
    try {
    	
    	HashMap<String, Object> args = new HashMap<>();
    	args.put(JsonReader.FAIL_ON_UNKNOWN_TYPE, Boolean.TRUE);
    	
    	Object objectOfJsonContent = JsonReader.jsonToJava(jsonContent, args);

      // Java generics erasure means we're capable of receiving a json-io JsonObject from the JsonReader
      // that will not cause a ClassCastException when we cast back to <T> below for objects that couldn't 
      // be deserialised into <T> but are still valid Json (no json-io serialisation additions). 
      // Instead of forcing the fromJson() caller to deal with this possibility, we explicitly throw away 
      // these attempts.
      
      if (objectOfJsonContent instanceof JsonObject) {
    	  return null;
      }

      return (T) objectOfJsonContent;
    } catch (JsonIoException e) {
      LOG.error(e);
      return null;
    }
  }
}
