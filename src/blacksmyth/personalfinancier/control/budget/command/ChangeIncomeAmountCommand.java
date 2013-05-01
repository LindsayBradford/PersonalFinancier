package blacksmyth.personalfinancier.control.budget.command;

import java.math.BigDecimal;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeAmountCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private BigDecimal preCommandAmount;
  private BigDecimal postCommandAmount;
  
  public static ChangeIncomeAmountCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                BigDecimal postCommandAmount) {
    
    ChangeIncomeAmountCommand command = new ChangeIncomeAmountCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getBudgettedAmount().getTotal(), 
        postCommandAmount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeAmountCommand(BudgetModel model, int incomeItemIndex, 
                                      BigDecimal preCommandAmount, BigDecimal postCommandAmount) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.preCommandAmount = preCommandAmount;
    this.postCommandAmount = postCommandAmount;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandAmount.equals(this.postCommandAmount)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemTotal(
        incomeItemIndex, 
        postCommandAmount
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemTotal(
        incomeItemIndex, 
        preCommandAmount
    );
  }
}
