/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.dependencies;

/**
 * A class implementing the 'refined abstraction class' of the Bridge pattern, allowing a 
 * bridge all needed functionality between the application and the open-source JSON library json-io 
 * (https://code.google.com/p/json-io/)
 *
 * @param <T> The type of objects that this class will bridge JSON serialisation for.
 */

public class JSonBridge<T> implements IJSonSerialisationBridge<T> {
  
  private IJSonSerialisationBridge<T> bridge; 
  
  public JSonBridge() {
    this.bridge = new JSonIoBridge<T>();
  }

  @Override
  public String toJSon(T object) {
    return bridge.toJSon(object);
  }

  @Override
  public T fromJson(String jsonContent) {
    return bridge.fromJson(jsonContent);
  }
}
