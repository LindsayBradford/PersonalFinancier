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

import blacksmyth.general.file.IFileSystemBridge;
import blacksmyth.general.file.IObjectFileConverter;

/**
 * An adapter class that transfers state between instantiated objects and file-serialised
 * object state via a 3rd-party JSON Serialization library.
 */

public class JSonObjectFileConverter<T> implements IObjectFileConverter<T> {
  
  private IJSonSerialisationBridge<T> jsonBridge;
  private IFileSystemBridge fileSystemBridge;
  
  public void setJSonBridge(IJSonSerialisationBridge<T> bridge) {
    this.jsonBridge = bridge;
  }

  public void setFileSystemBridge(IFileSystemBridge bridge) {
    this.fileSystemBridge = bridge;
  }
  
  private boolean hasValidConfig() {
    if (jsonBridge == null || fileSystemBridge == null) {
      return false;
    }
    return true;
  }
  
  @Override
  public void toFileFromObject(String filePath, T t) {
    assert hasValidConfig();
    
    fileSystemBridge.saveTextFile(
        filePath, 
        jsonBridge.toJSon(t)
    );
  }

  @Override
  public T toObjectFromFile(String filePath) {
    assert hasValidConfig();

    return jsonBridge.fromJSon(
      fileSystemBridge.loadTextFile(filePath)
    );
  }
}
