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

import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

/**
 * The default implementation of the Personal Financier Model.
 */

public final class PersonalFinancierModel extends Observable implements IPersomalFinancierModel, Observer {
  
  public PersonalFinancierModel() {
    PreferencesModel.getInstance().addObserver(this);
  }

  @Override
  public Rectangle getBounds() {
    return PreferencesModel.getInstance().getWindowBounds();
  }

  @Override
  public void setBounds(Rectangle bounds) {
    PreferencesModel.getInstance().setWindowBounds(bounds);
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    this.setChanged();
    this.notifyObservers(arg1);
  }
}
