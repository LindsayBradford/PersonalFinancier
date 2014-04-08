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

import blacksmyth.general.FileUtilities;
import blacksmyth.personalfinancier.dependencies.IJSonSerialisationBridge;
import blacksmyth.personalfinancier.dependencies.JSonBridge;

/**
 * An adapter class that transfers state between instantiated objects and file-serialised
 * object state via a 3rd-party JSON Serialization library.
 */

public class JSonFileAdapter<T> implements IPersonalFinancierFileAdapter<T> {
  
  private IJSonSerialisationBridge<T> jsonBridge;
  
  public JSonFileAdapter() {
    init(new JSonBridge<T>());
  }

  public JSonFileAdapter(IJSonSerialisationBridge<T> bridge) {
    init(bridge);
  }
  
  private void init(IJSonSerialisationBridge<T> bridge) {
    this.jsonBridge = bridge;
  }
  
  @Override
  public void toFileFromObject(String filePath, T t) {
    FileUtilities.saveTextFile(
        filePath, 
        jsonBridge.toJSon(t)
    );
  }

  @Override
  public T toObjectFromFile(String filePath) {
    return jsonBridge.fromJson(
      FileUtilities.loadTextFile(filePath)
    );
  }
}
