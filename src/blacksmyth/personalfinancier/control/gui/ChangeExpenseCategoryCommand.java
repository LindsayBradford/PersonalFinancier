package blacksmyth.personalfinancier.control.gui;


import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.ExpenseCategory;

public class ChangeExpenseCategoryCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int expenseItemIndex;
  private ExpenseCategory preCommandCategory;
  private ExpenseCategory postCommandCategory;
  
  public static ChangeExpenseCategoryCommand doCmd(BudgetModel model, int expenseItemIndex, 
                                                   ExpenseCategory postCommandCategory) {
    
    ChangeExpenseCategoryCommand command = new ChangeExpenseCategoryCommand(
        model, 
        expenseItemIndex, 
        model.getExpenseItems().get(expenseItemIndex).getCategory(),
        postCommandCategory
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseCategoryCommand(BudgetModel model, int expenseItemIndex, 
                                         ExpenseCategory preCommandCategory, ExpenseCategory postCommandCategory) {
    this.model = model;
    this.expenseItemIndex = expenseItemIndex;
    this.preCommandCategory = preCommandCategory;
    this.postCommandCategory = postCommandCategory;
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
    if (this.preCommandCategory.equals(this.postCommandCategory)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setExpenseItemCategory(
        expenseItemIndex, 
        postCommandCategory
    );
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemCategory(
        expenseItemIndex, 
        preCommandCategory
    );
  }
}
