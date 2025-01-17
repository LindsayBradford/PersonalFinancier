/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

/**
 *  An interface for showing messages at an application level.
 */
public interface IApplicationMessageView extends PropertyChangeListener {
  
  public void bindViewComponent(JComponent component);
  
  /**
   * Shows a message based on a notification from an Observable that 
   * implementers of this interface watch.
   * @param message
   */
  public void showMessage(String message);

  
  /**
   * Sets a message based on a notification from an Observable that 
   * implementers of this interface watch. Message will display for
   * timeInMilliseconds, or until the next showMessage(), whichever 
   * happens first.
   * @param message
   */
  public void showMessage(String message, int timeInMilliseconds);
  
  /**
   * Clears the current message.
   */
  public void clearMessage();
}
