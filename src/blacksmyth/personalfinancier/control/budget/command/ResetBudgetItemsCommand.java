package blacksmyth.personalfinancier.control.budget.command;

import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ResetBudgetItemsCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private ArrayList<BudgetItem> preCommandIncomeItems;
  private ArrayList<BudgetItem> preCommandExpenseItems;
  
  public static ResetBudgetItemsCommand doCmd(BudgetModel model) {
    ResetBudgetItemsCommand command = new ResetBudgetItemsCommand(model);
    command.redo();
    return command;    
  }
  
  protected ResetBudgetItemsCommand(BudgetModel model) {
    this.model = model;
    this.preCommandIncomeItems = model.getIncomeItems();
    this.preCommandExpenseItems = model.getExpenseItems();
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setExpenseItems(new ArrayList<BudgetItem>());
    model.setIncomeItems(new ArrayList<BudgetItem>());
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItems(this.preCommandExpenseItems);
    model.setIncomeItems(this.preCommandIncomeItems);
  }
}
