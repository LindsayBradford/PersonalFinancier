package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.BudgetItem;

public class AddIncomeItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private BudgetItem postCommandItem;
  
  public static AddIncomeItemCommand doCmd(BudgetModel model) {
    return new AddIncomeItemCommand(model);
  }
  
  protected AddIncomeItemCommand(BudgetModel model) {
    this.model = model;
    this.postCommandItem = model.addIncomeItem();
  }

  @Override
  public void redo() throws CannotRedoException {
    model.addIncomeItem(this.postCommandItem);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeIncomeItem(this.postCommandItem);
  }
}
