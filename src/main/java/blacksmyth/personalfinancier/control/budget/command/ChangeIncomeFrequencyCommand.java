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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeFrequencyCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private CashFlowFrequency preCommandFrequency;
  private CashFlowFrequency postCommandFrequency;
  private String itemDescription;
  
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
  
  protected ChangeIncomeFrequencyCommand(BudgetModel model, int itemIndex, 
                                         CashFlowFrequency preCommandFrequency, CashFlowFrequency postCommandFrequency) {
    this.model = model;
    this.incomeItemIndex = itemIndex;
    this.itemDescription = model.getIncomeItems().get(itemIndex).getDescription();
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
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Income item [%s] frequency changed to [%s]", this.itemDescription, this.preCommandFrequency);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Income item [%s] frequency changed to [%s]", this.itemDescription, this.postCommandFrequency);
  }
}
