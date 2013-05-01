package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeFrequencyCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private CashFlowFrequency preCommandFrequency;
  private CashFlowFrequency postCommandFrequency;
  
  public static ChangeIncomeFrequencyCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                   CashFlowFrequency postCommandFrequency) {
    
    ChangeIncomeFrequencyCommand command = new ChangeIncomeFrequencyCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getFrequency(),
        postCommandFrequency
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeFrequencyCommand(BudgetModel model, int incomeItemIndex, 
                                         CashFlowFrequency preCommandFrequency, CashFlowFrequency postCommandFrequency) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
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
    model.setIncomeItemFrequency(
        incomeItemIndex, 
        postCommandFrequency
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemFrequency(
        incomeItemIndex, 
        preCommandFrequency
    );
  }
}
