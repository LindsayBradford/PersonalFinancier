package blacksmyth.personalfinancier.control.inflation.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.inflation.InflationEntry;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.inflation.AbstractInflationCommand;

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
