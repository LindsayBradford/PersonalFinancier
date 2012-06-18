package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class RemoveIncomeItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private BudgetItem postCommandItem;
  
  public static RemoveIncomeItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new RemoveIncomeItemCommand(model, itemIndex);
  }
  
  protected RemoveIncomeItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.postCommandItem = model.removeIncomeItem(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeIncomeItem(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addIncomeItem(itemIndex, this.postCommandItem);
  }
}
