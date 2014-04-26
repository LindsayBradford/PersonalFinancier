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
 * An MVP model interface for any model object whose state can be "serialized".
 * The class T should be a POJO containing only the state to be serialised.
 * Other typical model state, such as subscriber lists, derived data, etc 
 * should not be included. 
 */
public interface IFileHandlerModel<T> {

  /**
   * Calling this method takes the serializable state from within t, 
   * and 'plugs' it into the model implementing this interface.
   * @param t
   */
  public void fromSerializable(T t);
  
  /**
   * Calling this method sees the model producing an instance of type T 
   * from its state, and returning it to the caller for serialisation.
   * @return serialisable state of the model implementing this interface.
   */
  public T toSerializable();
}
