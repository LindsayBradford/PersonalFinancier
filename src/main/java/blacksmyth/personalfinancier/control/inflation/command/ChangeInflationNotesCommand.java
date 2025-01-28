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

public class ChangeInflationNotesCommand extends AbstractInflationCommand {
  
  private String preCommandNotes;
  private String postCommandNotes;
  
  public static ChangeInflationNotesCommand doCmd(InflationModel model, int inflationEntryIndex, 
                                                  String postCommandNotes) {
    
    ChangeInflationNotesCommand command = new ChangeInflationNotesCommand(
        model, 
        inflationEntryIndex, 
        model.getInflationList().get(inflationEntryIndex).getNotes(),
        postCommandNotes
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeInflationNotesCommand(InflationModel model, int inflationEntryIndex, 
                                        String preCommandNotes, String postCommandNotes) {
    super(model, inflationEntryIndex);

    this.preCommandNotes = preCommandNotes;
    this.postCommandNotes = postCommandNotes;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandNotes.equals(this.postCommandNotes)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setInflationEntryNotes(
        inflationEntryIndex, 
        postCommandNotes
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setInflationEntryNotes(
        inflationEntryIndex, 
        preCommandNotes
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format(
        "Inflation entry [%s] notes [%s] changed to [%s]", dateAsString(), 
        this.postCommandNotes, this.preCommandNotes
    );
  }

  @Override
  public String getRedoPresentationName() {
    return String.format(
        "Inflation entry [%s] notes [%s] changed to [%s]", dateAsString(), 
        this.preCommandNotes, this.postCommandNotes
    );
  }
}
