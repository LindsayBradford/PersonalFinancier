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

import java.awt.Rectangle;

/**
 * The interface all implementations of a view for the Personal Financier utility must implement.
 */

public interface IPersonalFinancierView {

  /**
   * An enumeration of all user-triggered events supported by a Personal Financier view:
   * <li>{@link #ExitRequested}</li>
   */
  public enum Events {
    /**
     * Represents a user-event where the user wishes to exit the application.
     */
    ExitRequested,
  }
  
  /**
   * Informs any listeners (typically a matching presenter) of the user-triggered event.
   * @see blacksmyth.personalfinancier.presenter.IPersonalFiancnierPresenter
   * @see Events
   * @param event
   */
  public void raiseEvent(Events event);

  /**
   * Allows other objects to set the screen bounds the view's window should occupy.
   * @param windowBounds
   */
  public void setBounds(Rectangle windowBounds);
  
  /**
   * Returns the screen bounds occupied by the view's window on-screen.
   * @return A rectangle defining the window bounds occupied.
   */
  public Rectangle getBounds();

  /**
   * Returns a reference to an internal component dedicated to showing user
   * messages of various components or events in a centralised location.
   * @see IApplicationMessageView
   * @return
   */
  public IApplicationMessageView getMessageViewer();
  
  /**
   * Sets the supplied messageView as the dedicated user-messaging component
   * of the application.
   * @param messageView
   */
  public void setMessageViewer(IApplicationMessageView messageView);

  /**
   * Ensures the supplied <tt>componentView</tt> is integrated into this view for display.
   * @see IPersonalFinancierComponentView 
   * @param componentView
   */
  public void addComponentView(IPersonalFinancierComponentView componentView);

  /**
   * Ensures the view is being displayed to the user.
   */
  public void display();

}
