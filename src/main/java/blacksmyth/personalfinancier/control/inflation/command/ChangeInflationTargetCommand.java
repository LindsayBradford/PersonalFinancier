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

public class ChangeInflationTargetCommand extends AbstractInflationCommand {
  
  private Double preCommandValue;
  private Double postCommandValue;
  
  public static ChangeInflationTargetCommand doCmd(InflationModel model, int inflationEntryIndex, 
                                                  Double postCommandValue) {
    
    ChangeInflationTargetCommand command = new ChangeInflationTargetCommand(
        model, 
        inflationEntryIndex, 
        model.getInflationList().get(inflationEntryIndex).getTarget(),
        postCommandValue
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeInflationTargetCommand(InflationModel model, int inflationEntryIndex, 
                                      Double preCommandValue, Double postCommandValue) {

    super(model, inflationEntryIndex);
    
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
    model.setInflationEntryTarget(
        inflationEntryIndex, 
        postCommandValue
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setInflationEntryTarget(
        inflationEntryIndex, 
        preCommandValue
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Inflation item [%s] target changed from [%s] to [%s]", dateAsString(), this.postCommandValue, this.preCommandValue);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Item [%s] target changed to from [%s] to [%s]", dateAsString(), this.preCommandValue, this.postCommandValue);
  }
}
