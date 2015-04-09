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

import blacksmyth.personalfinancier.view.IPersonalFinancierView;

public interface IPersoanalFinancierPresenter extends Observer {
  
  public void setView(IPersonalFinancierView view);
  public void processEvent(IPersonalFinancierView.Events event);

}
