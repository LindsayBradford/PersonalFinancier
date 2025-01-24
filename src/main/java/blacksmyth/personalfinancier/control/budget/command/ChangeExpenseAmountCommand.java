/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

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
  private String itemDescription;
  
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
    this.itemDescription = model.getExpenseItems().get(itemIndex).getDescription();
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
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Expense item [%s] amount changed to [%s]", this.itemDescription, this.preCommandAmount);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Expense item [%s] amount changed to [%s]", this.itemDescription, this.postCommandAmount);
  }

}
