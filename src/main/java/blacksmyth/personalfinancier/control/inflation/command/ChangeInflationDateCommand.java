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

import java.time.LocalDate;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationModel;

public class ChangeInflationDateCommand extends AbstractInflationCommand {
  
  private InflationModel model;
  private int inflationEntryIndex;
  private LocalDate preCommandDate;
  private LocalDate postCommandDate;
  
  public static ChangeInflationDateCommand doCmd(InflationModel model, int inflationEntryIndex, 
                                                 LocalDate postCommandDate) {
    
    ChangeInflationDateCommand command = new ChangeInflationDateCommand(
        model, 
        inflationEntryIndex, 
        model.getInflationList().get(inflationEntryIndex).getDate(),
        postCommandDate
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeInflationDateCommand(InflationModel model, int inflationEntryIndex, 
                                       LocalDate preCommandDate, LocalDate postCommandDate) {
    this.model = model;
    this.inflationEntryIndex = inflationEntryIndex;
    this.preCommandDate = preCommandDate;
    this.postCommandDate = postCommandDate;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandDate.equals(this.postCommandDate)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setInflationEntryDate(
        inflationEntryIndex, 
        postCommandDate
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setInflationEntryDate(
        inflationEntryIndex, 
        preCommandDate
    );
  }
}
