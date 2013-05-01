package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class MoveIncomeItemUpCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  
  public static MoveIncomeItemUpCommand doCmd(BudgetModel model, int itemIndex) {
    MoveIncomeItemUpCommand command = new MoveIncomeItemUpCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveIncomeItemUpCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveIncomeItemUp(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveIncomeItemDown(this.itemIndex - 1);
  }
}
