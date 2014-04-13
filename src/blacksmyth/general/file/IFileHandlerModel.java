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
 * Any stateful model object whose state can be serialized should implement this interface.
 * The class T should be a POJO containing only the state to be serialised. Other typical model state, such as 
 * subscriber lists, derived data, etc should not be included. 
 */
public interface IFileHandlerModel<T> {

  /**
   * Calling this method takes the content of t, and 'plugs' it into the model that implements this interface.
   * @param t
   */
  public void fromSerializable(T t);
  
  /**
   * Calling this method sees the model produces an instance of type T of the serializable state of the model.
   * @return serialisable state of the model that implementes this interface.
   */
  public T toSerializable();
}
