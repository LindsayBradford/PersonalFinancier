package blacksmyth.personalfinancier.control.budget.command;

import java.math.BigDecimal;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseAmountCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private BigDecimal preCommandAmount;
  private BigDecimal postCommandAmount;
  
  public static ChangeExpenseAmountCommand doCmd(BudgetModel model, int itemIndex, 
                                                BigDecimal postCommandAmount) {
    
    ChangeExpenseAmountCommand command = new ChangeExpenseAmountCommand(
        model, 
        itemIndex, 
        model.getExpenseItems().get(itemIndex).getBudgettedAmount().getTotal(), 
        postCommandAmount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseAmountCommand(BudgetModel model, int itemIndex, 
                                      BigDecimal preCommandAmount, BigDecimal postCommandAmount) {
    this.model = model;
    this.itemIndex = itemIndex;
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
    model.setExpenseItemTotal(
        itemIndex, 
        postCommandAmount
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemTotal(
        itemIndex, 
        preCommandAmount
    );
  }
}
