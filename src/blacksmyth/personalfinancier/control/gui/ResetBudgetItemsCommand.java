package blacksmyth.personalfinancier.control.gui;

import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.ExpenseItem;
import blacksmyth.personalfinancier.model.budget.IncomeItem;

public class ResetBudgetItemsCommand implements UndoableBudgetCommand, IBudgetController {
  
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
  public boolean addEdit(UndoableEdit arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean canRedo() {
    return true;
  }

  @Override
  public boolean canUndo() {
    return true;
  }

  @Override
  public void die() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getRedoPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUndoPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSignificant() {
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setExpenseItems(new ArrayList<ExpenseItem>());
    model.setIncomeItems(new ArrayList<IncomeItem>());
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItems(this.preCommandExpenseItems);
    model.setIncomeItems(this.preCommandIncomeItems);
  }
}
