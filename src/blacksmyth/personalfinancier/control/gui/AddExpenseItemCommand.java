package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.ExpenseItem;

public class AddExpenseItemCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private ExpenseItem postCommandItem;
  
  public static AddExpenseItemCommand doCmd(BudgetModel model) {
    return new AddExpenseItemCommand(model);
  }
  
  protected AddExpenseItemCommand(BudgetModel model) {
    this.model = model;
    this.postCommandItem = model.addExpenseItem();
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
    model.addExpenseItem(this.postCommandItem);
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeExpenseItem(this.postCommandItem);
  }
}
