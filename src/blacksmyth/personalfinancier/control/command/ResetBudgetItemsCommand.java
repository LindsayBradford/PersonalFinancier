package blacksmyth.personalfinancier.control.command;

import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.ExpenseItem;
import blacksmyth.personalfinancier.model.budget.IncomeItem;

public class ResetBudgetItemsCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private ArrayList<IncomeItem> preCommandIncomeItems;
  private ArrayList<ExpenseItem> preCommandExpenseItems;
  
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
    model.setExpenseItems(new ArrayList<ExpenseItem>());
    model.setIncomeItems(new ArrayList<IncomeItem>());
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItems(this.preCommandExpenseItems);
    model.setIncomeItems(this.preCommandIncomeItems);
  }
}
