package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeCategoryCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private String preCommandCategory;
  private String postCommandCategory;
  
  public static ChangeIncomeCategoryCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                   String postCommandCategory) {
    
    ChangeIncomeCategoryCommand command = new ChangeIncomeCategoryCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getCategory(),
        postCommandCategory
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeCategoryCommand(BudgetModel model, int incomeItemIndex, 
                                         String preCommandCategory, String postCommandCategory) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
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
    model.setIncomeItemCategory(
        incomeItemIndex, 
        postCommandCategory
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemCategory(
        incomeItemIndex, 
        preCommandCategory
    );
  }
}
