package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class MoveExpenseItemDownCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  
  public static MoveExpenseItemDownCommand doCmd(BudgetModel model, int itemIndex) {
    MoveExpenseItemDownCommand command = new MoveExpenseItemDownCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveExpenseItemDownCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveExpenseItemDown(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveExpenseItemUp(this.itemIndex + 1);
  }
}
