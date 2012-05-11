package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.IncomeCategory;

public class ChangeIncomeCategoryCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private IncomeCategory preCommandCategory;
  private IncomeCategory postCommandCategory;
  
  public static ChangeIncomeCategoryCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                   IncomeCategory postCommandCategory) {
    
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
                                         IncomeCategory preCommandCategory, IncomeCategory postCommandCategory) {
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
