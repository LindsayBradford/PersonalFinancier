package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseCategoryCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int expenseItemIndex;
  private String preCommandCategory;
  private String postCommandCategory;
  
  public static ChangeExpenseCategoryCommand doCmd(BudgetModel model, int expenseItemIndex, 
                                                   String postCommandCategory) {
    
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
                                         String preCommandCategory, String postCommandCategory) {
    this.model = model;
    this.expenseItemIndex = expenseItemIndex;
    this.preCommandCategory = preCommandCategory;
    this.postCommandCategory = postCommandCategory;
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
  public void undo() throws CannotUndoException {
    model.setExpenseItemCategory(
        expenseItemIndex, 
        preCommandCategory
    );
  }
}
