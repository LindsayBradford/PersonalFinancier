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
  private InflationEntry postCommandEntry;
  
  public static AddInflationEntryCommand doCmd(InflationModel model) {
    return new AddInflationEntryCommand(model);
  }
  
  protected AddInflationEntryCommand(InflationModel model) {
    super(model, model.getInflationList().size());
    this.postCommandEntry = model.addEntry();
  }

  @Override
  public void redo() throws CannotRedoException {
    model.addEntry(this.inflationEntryIndex, this.postCommandEntry);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeEntry(this.inflationEntryIndex);
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Undid addition of inflation item [%s]", dateAsString());
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Added new inflation item [%s] ", dateAsString());
  }

}
