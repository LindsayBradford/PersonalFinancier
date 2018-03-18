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

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a concrete implementation of IObjectFileConverter<T> that
 * interrogates the file extension  of the file specified, and uses that
 * extension to decide which of the adapter's added to its internal  map 
 * to delegate operations to.
 * @param <T>
 */

public class StrategicFileConverter<T> implements IObjectFileConverter<T> {
  
  private static final Logger LOG = LogManager.getLogger(StrategicFileConverter.class);

  private HashMap<String, IObjectFileConverter<T>> adapterMap;
  
  public void setAdapterMap(HashMap<String, IObjectFileConverter<T>> inputMap) {
    this.adapterMap = inputMap;
  }

  @Override
  public T toObjectFromFile(String filePath) {
    IObjectFileConverter<T> converter = getMatchingConverter(filePath);
    return converter.toObjectFromFile(filePath);
  }

  @Override
  public void toFileFromObject(String filePath, T t) {
    IObjectFileConverter<T> converter = getMatchingConverter(filePath);
    
    converter.toFileFromObject(filePath, t);
  }

  private IObjectFileConverter<T> getMatchingConverter(String filePath) {
    String fileExt = getFileExtension(filePath);

    IObjectFileConverter<T> converter = adapterMap.get(fileExt);
    
    
    LOG.info("Converter [{}] can process file [{}]",converter.getClass().getSimpleName(),  filePath);
    
    assert converter != null : "No converter maps to supplied file (" + filePath + ")";

    return converter;
  }
  
  private String getFileExtension(String filePath) {
	  return filePath.replaceAll("^.*\\.", "").toLowerCase();
  }
}
