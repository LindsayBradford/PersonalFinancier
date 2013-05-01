package blacksmyth.personalfinancier.control.inflation.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.inflation.AbstractInflationCommand;

public class ChangeInflationNotesCommand extends AbstractInflationCommand {
  
  private InflationModel model;
  private int inflationEntryIndex;
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
    this.model = model;
    this.inflationEntryIndex = inflationEntryIndex;
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
}
