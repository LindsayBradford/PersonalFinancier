/**
 * Copyright (c) 2015, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */
package blacksmyth.personalfinancier.control;

import java.beans.PropertyChangeListener;

import blacksmyth.personalfinancier.view.IPersonalFinancierView;
import blacksmyth.personalfinancier.view.IPersonalFinancierView.Events;

import blacksmyth.personalfinancier.model.IPersomalFinancierModel;

/**
 * The interface Personal Financier Presenter classes should implement.
 * It is a Presenter as per the MVP design pattern. 
 *
 */
public interface IPersoanalFinancierPresenter extends PropertyChangeListener {
  
  /**
   * Binds the supplied view to this presenter, so the presenter can issue
   * user-interface updates to the view, and receive user event notifications from it.
   * @see blacksmyth.personalfinancier.view.IPersonalFinancierView
   * @param view
   */
  public void addView(IPersonalFinancierView view);
  
  /**
   * Responds to the user-triggered event raised by the supplied view.
   * @see blacksmyth.personalfinancier.view.IPersonalFinancierView
   * @see blacksmyth.personalfinancier.view.IPersonalFinancierView.Events
   * @param view
   * @param event
   */
  public void processEvent(IPersonalFinancierView view, Events event);
  
  public void setModel(IPersomalFinancierModel model);

}
