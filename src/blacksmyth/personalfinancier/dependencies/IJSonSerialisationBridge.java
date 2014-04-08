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
 * An interface defining the 'Abstraction' of the Bridge pattern, allowing a bridge 
 * of all needed functionality between the application and some 3rd-party JSON library 
 * that the application depends on.
 *
 * @param <T> The type of objects that this class will bridge JSON serialisation for.
 */

public interface IJSonSerialisationBridge<T> {
  
    /**
     * Converts an object of type T to a JSON serialisation string of its content.
     * @param object
     * @return JSON serialisation of object state.
     */
  
    public String toJSon(T object);
    
    /**
     * Covnerts a JSON serialisation of an object of type T into an instance of that object.
     * @param jsonContent
     * @return an object instantiated with the state encoded in jsonContent.
     */
    public T fromJson(String jsonContent);
}
