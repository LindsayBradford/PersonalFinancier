package blacksmyth.personalfinancier.control.inflation.command;

import java.util.Calendar;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.inflation.AbstractInflationCommand;

public class ChangeInflationDateCommand extends AbstractInflationCommand {
  
  private InflationModel model;
  private int inflationEntryIndex;
  private Calendar preCommandDate;
  private Calendar postCommandDate;
  
  public static ChangeInflationDateCommand doCmd(InflationModel model, int inflationEntryIndex, 
                                                 Calendar postCommandDate) {
    
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
                                       Calendar preCommandDate, Calendar postCommandDate) {
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
