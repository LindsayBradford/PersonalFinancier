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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationEntry;
import blacksmyth.personalfinancier.model.inflation.InflationModel;

public class AddInflationEntryCommand extends AbstractInflationCommand {
  
  private InflationModel inflationModel;
  private InflationEntry postCommandEntry;
  
  public static AddInflationEntryCommand doCmd(InflationModel model) {
    return new AddInflationEntryCommand(model);
  }
  
  protected AddInflationEntryCommand(InflationModel model) {
    this.inflationModel = model;
    this.postCommandEntry = model.addEntry();
  }

  @Override
  public void redo() throws CannotRedoException {
    inflationModel.addEntry(this.postCommandEntry);
  }

  @Override
  public void undo() throws CannotUndoException {
    inflationModel.removeEntry(this.postCommandEntry);
  }
}
