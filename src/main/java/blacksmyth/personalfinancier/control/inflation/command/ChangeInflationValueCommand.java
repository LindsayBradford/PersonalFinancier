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

import blacksmyth.personalfinancier.model.inflation.InflationModel;

public class ChangeInflationValueCommand extends AbstractInflationCommand {
  
  private InflationModel model;
  private int inflationEntryIndex;
  private double preCommandValue;
  private double postCommandValue;
  
  public static ChangeInflationValueCommand doCmd(InflationModel model, int inflationEntryIndex, 
                                                  double postCommandValue) {
    
    ChangeInflationValueCommand command = new ChangeInflationValueCommand(
        model, 
        inflationEntryIndex, 
        model.getInflationList().get(inflationEntryIndex).getCPIValue(),
        postCommandValue
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeInflationValueCommand(InflationModel model, int inflationEntryIndex, 
                                      double preCommandValue, double postCommandValue) {
    this.model = model;
    this.inflationEntryIndex = inflationEntryIndex;
    this.preCommandValue = preCommandValue;
    this.postCommandValue = postCommandValue;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandValue == this.postCommandValue) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setInflationEntryValue(
        inflationEntryIndex, 
        postCommandValue
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setInflationEntryValue(
        inflationEntryIndex, 
        preCommandValue
    );
  }
}
