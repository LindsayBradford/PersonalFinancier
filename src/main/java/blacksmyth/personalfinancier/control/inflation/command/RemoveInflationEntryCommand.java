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

public class RemoveInflationEntryCommand extends AbstractInflationCommand {
  
  private InflationModel model;
  private int entryIndex;
  private InflationEntry postCommandItem;
  
  public static RemoveInflationEntryCommand doCmd(InflationModel model, int entryIndex) {
    return new RemoveInflationEntryCommand(model, entryIndex);
  }
  
  protected RemoveInflationEntryCommand(InflationModel model, int itemIndex) {
    this.model = model;
    this.entryIndex = itemIndex;
    this.postCommandItem = model.removeEntry(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeEntry(this.entryIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addEntry(entryIndex, this.postCommandItem);
  }
}
