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

import java.util.HashMap;

import blacksmyth.general.file.IObjectFileConverter;

/**
 * This is a concrete implementation of IObjectFileConverter<T> that
 * interrogates the file extension  of the file specified, and uses that
 * extension to decide which of the adapter's added to its internal  map 
 * to delegate operations to.
 * @param <T>
 */
public final class StrategicFileAdapter<T> implements IObjectFileConverter<T> {
  
  private HashMap<String, IObjectFileConverter<T>> adapterMap = new HashMap<String, IObjectFileConverter<T>>();

  /**
   * Adds the supplied adapter to it's internal map of adapters against which file extensions
   * they process.
   * @param fileExtension
   * @param adapter
   */
  public void add(String fileExtension, IObjectFileConverter<T> adapter) {
    adapterMap.put(fileExtension, adapter);
  }

  @Override
  public T toObjectFromFile(String filePath) {
    IObjectFileConverter<T> adapter = getMatchingAdapter(filePath);
    if (adapter != null) {
      return adapter.toObjectFromFile(filePath);
    }
    return null;
  }

  @Override
  public void toFileFromObject(String filePath, T t) {
    IObjectFileConverter<T> adapter = getMatchingAdapter(filePath);
    if (adapter != null) {
      adapter.toFileFromObject(filePath, t);
    }
  }

  private IObjectFileConverter<T> getMatchingAdapter(String filePath) {
    String fileExt = getFileExtension(filePath);
    
    IObjectFileConverter<T> adapter = adapterMap.get(fileExt);
    
    return adapter;
  }
  
  private String getFileExtension(String filePath) {
    int i = filePath.lastIndexOf('.');
    if (i >= 0) {
        return filePath.substring(i+1);
    }
    return null;
  }
}
