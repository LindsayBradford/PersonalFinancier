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

public class ChangeExpenseFrequencyCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private CashFlowFrequency preCommandFrequency;
  private CashFlowFrequency postCommandFrequency;
  private String itemDescription;
  
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
  
  protected ChangeExpenseFrequencyCommand(BudgetModel model, int itemIndex, 
                                         CashFlowFrequency preCommandFrequency, CashFlowFrequency postCommandFrequency) {
    this.model = model;
    this.itemIndex = itemIndex;
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
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Expense item [%s] frequency changed to [%s]", this.itemDescription, this.preCommandFrequency);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Expense item [%s] frequency changed to [%s]", this.itemDescription, this.postCommandFrequency);
  }
}
