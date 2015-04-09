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

public interface IPersonalFinancierView {

  public enum Events {
    BoundsChanged,
    ExitRequested,
  }
  
  public void raiseEvent(Events event);

  public void setBounds(Rectangle windowBounds);
  public Rectangle getBounds();

  public IApplicationMessageView getMessageViewer();
  public void setMessageViewer(IApplicationMessageView messageView);

  public void addComponent(IPersonalFinancierComponentView componentView);
  public void display();

}
