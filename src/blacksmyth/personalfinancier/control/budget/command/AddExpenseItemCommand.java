package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetItem;

public class AddExpenseItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private BudgetItem postCommandItem;
  
  public static AddExpenseItemCommand doCmd(BudgetModel model) {
    return new AddExpenseItemCommand(model);
  }
  
  protected AddExpenseItemCommand(BudgetModel model) {
    this.model = model;
    this.postCommandItem = model.addExpenseItem();
  }

  @Override
  public void redo() throws CannotRedoException {
    model.addExpenseItem(this.postCommandItem);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeExpenseItem(this.postCommandItem);
  }
}
