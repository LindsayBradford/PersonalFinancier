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

import java.util.Observer;

/**
 * Implementations of this interface are expected to extend the Observable
 * class. This interface allows for the setting of application messages.
 * Observers will see message changes and render them appropriately at an
 * application level.
 */
public interface IApplicationMessagePresenter {

  /**
   * Adds observer o to the list of observers watching this message presenter.
   * @param o
   */
  public void addObserver(Observer o);
  
  /**
   * Allows the setting of an application message. 
   * @param message
   */
  public void setMessage(String message);
}
