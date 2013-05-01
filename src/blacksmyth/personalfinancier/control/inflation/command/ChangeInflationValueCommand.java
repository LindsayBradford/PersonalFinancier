package blacksmyth.personalfinancier.control.inflation.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.inflation.AbstractInflationCommand;

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
