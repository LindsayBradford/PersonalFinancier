package blacksmyth.personalfinancier.control.inflation.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationEntry;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.inflation.AbstractInflationCommand;

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
