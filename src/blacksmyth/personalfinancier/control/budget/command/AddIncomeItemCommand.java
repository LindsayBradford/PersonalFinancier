package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetItem;

public class AddIncomeItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel budgetModel;
  private BudgetItem postCommandItem;
  
  public static AddIncomeItemCommand doCmd(BudgetModel model) {
    return new AddIncomeItemCommand(model);
  }
  
  protected AddIncomeItemCommand(BudgetModel model) {
    this.budgetModel = model;
    this.postCommandItem = model.addIncomeItem();
  }

  @Override
  public void redo() throws CannotRedoException {
    budgetModel.addIncomeItem(this.postCommandItem);
  }

  @Override
  public void undo() throws CannotUndoException {
    budgetModel.removeIncomeItem(this.postCommandItem);
  }
}
