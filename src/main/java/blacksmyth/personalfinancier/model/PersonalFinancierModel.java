/**
 * Copyright (c) 2015, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */
package blacksmyth.personalfinancier.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The default implementation of the Personal Financier Model.
 */

public final class PersonalFinancierModel implements IPersomalFinancierModel, PropertyChangeListener {
  
  private PropertyChangeSupport support;
  
  public PersonalFinancierModel() {
    support = new PropertyChangeSupport(this);
    ModelPreferences.getInstance().addObserver(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    support.firePropertyChange(evt);
  }
}
