package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class MoveExpenseItemUpCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  
  public static MoveExpenseItemUpCommand doCmd(BudgetModel model, int itemIndex) {
    MoveExpenseItemUpCommand command = new MoveExpenseItemUpCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveExpenseItemUpCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveExpenseItemUp(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveExpenseItemDown(this.itemIndex - 1);
  }
}
