/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control.inflation.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blacksmyth.personalfinancier.control.AbstractUndoManager;

@SuppressWarnings("serial")
public class InflationUndoManager extends AbstractUndoManager {
  
  private static Logger LOG = LogManager.getLogger(InflationUndoManager.class);
  
  public InflationUndoManager() {
    super();
  }

  @Override
  protected void fireUndoableEvent() {
    this.observableDelegate.firePropertyChange("Undoable Inflation Change",null,null);
  }

  @Override
  protected void sendMessage(String commandMessage) {
    LOG.info(commandMessage);
    super.sendMessage(commandMessage);
  }

}
