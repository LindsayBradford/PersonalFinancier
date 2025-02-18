/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.general;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import blacksmyth.personalfinancier.control.IApplicationMessagePresenter;

public class ApplicationMessagePresenter  
            implements IApplicationMessagePresenter {

  private static ApplicationMessagePresenter instance = null;

  protected ApplicationMessagePresenter() {
    support = new PropertyChangeSupport(this);
  }
  
  public static ApplicationMessagePresenter getInstance() {
     if(instance == null) {
        instance = new ApplicationMessagePresenter();
     }
     return instance;
  }
  
  private PropertyChangeSupport support;
  
  public void addObserver(PropertyChangeListener o) {
    support.addPropertyChangeListener(o);
  }
  
  
  @Override
  public void setMessage(String message) {
    support.firePropertyChange("ApplicaitonMessage", null, message);
  }
}
