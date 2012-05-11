package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseFrequencyCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private CashFlowFrequency preCommandFrequency;
  private CashFlowFrequency postCommandFrequency;
  
  public static ChangeExpenseFrequencyCommand doCmd(BudgetModel model, int itemIndex, 
                                                   CashFlowFrequency postCommandFrequency) {
    
    ChangeExpenseFrequencyCommand command = new ChangeExpenseFrequencyCommand(
        model, 
        itemIndex, 
        model.getExpenseItems().get(itemIndex).getFrequency(),
        postCommandFrequency
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseFrequencyCommand(BudgetModel model, int incomeItemIndex, 
                                         CashFlowFrequency preCommandFrequency, CashFlowFrequency postCommandFrequency) {
    this.model = model;
    this.itemIndex = incomeItemIndex;
    this.preCommandFrequency = preCommandFrequency;
    this.postCommandFrequency = postCommandFrequency;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandFrequency.equals(this.postCommandFrequency)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setExpenseItemFrequency(
        itemIndex, 
        postCommandFrequency
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemFrequency(
        itemIndex, 
        preCommandFrequency
    );
  }
}
