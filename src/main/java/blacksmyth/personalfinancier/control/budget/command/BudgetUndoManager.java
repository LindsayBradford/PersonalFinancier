/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control.budget.command;

import org.apache.logging.log4j.LogManager;

import blacksmyth.general.MaskedLogger;
import blacksmyth.personalfinancier.control.AbstractUndoManager;

@SuppressWarnings("serial")
public class BudgetUndoManager extends AbstractUndoManager {
  
  private static MaskedLogger LOG = MaskedLogger.wrap(LogManager.getLogger(BudgetUndoManager.class));
  
  public BudgetUndoManager() {
    super();
  }
  
  @Override
  protected void fireUndoableEvent() {
    observableDelegate.firePropertyChange("Undoable Budget Change",null,null);
  }

  @Override
  protected void sendMessage(String commandMessage) {
    LOG.info(commandMessage);
    super.sendMessage(commandMessage);
  }
}


