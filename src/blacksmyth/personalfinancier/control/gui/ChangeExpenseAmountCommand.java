package blacksmyth.personalfinancier.control.gui;

import java.math.BigDecimal;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseAmountCommand implements UndoableBudgetCommand, IBudgetController {
  
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
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemTotal(
        itemIndex, 
        preCommandAmount
    );
  }
}
