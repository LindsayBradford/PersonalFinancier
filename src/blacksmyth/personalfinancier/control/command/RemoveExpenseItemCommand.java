package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.ExpenseItem;

public class RemoveExpenseItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private ExpenseItem postCommandItem;
  
  public static RemoveExpenseItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new RemoveExpenseItemCommand(model, itemIndex);
  }
  
  protected RemoveExpenseItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.postCommandItem = model.removeExpenseItem(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeExpenseItem(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addExpenseItem(itemIndex, this.postCommandItem);
  }
}
