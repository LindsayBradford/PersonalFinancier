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

public class ChangeIncomeAmountCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private BigDecimal preCommandAmount;
  private BigDecimal postCommandAmount;
  private String itemDescription;
  
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
    this.itemDescription = model.getIncomeItems().get(incomeItemIndex).getDescription();
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
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Income item [%s] amount changed to [%s]", this.itemDescription, this.preCommandAmount);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Income item [%s] amount changed to [%s]", this.itemDescription, this.postCommandAmount);
  }
}
