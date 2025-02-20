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

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeAccountCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private String preCommandAccount;
  private String postCommandAccount;
  private String itemDescription;
  
  public static ChangeIncomeAccountCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                 String postCommandAccount) {
    
    ChangeIncomeAccountCommand command = new ChangeIncomeAccountCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getBudgetAccount().getNickname(),
        postCommandAccount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeAccountCommand(BudgetModel model, int incomeItemIndex, 
                                       String preCommandAccount, String postCommandAccount) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.itemDescription = model.getIncomeItems().get(incomeItemIndex).getDescription();
    this.preCommandAccount = preCommandAccount;
    this.postCommandAccount = postCommandAccount;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandAccount.equals(this.postCommandAccount)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemAccount(
        incomeItemIndex, 
        postCommandAccount
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemAccount(
        incomeItemIndex, 
        preCommandAccount
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Income item [%s] account changed to [%s]", this.itemDescription, this.preCommandAccount);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Income item [%s] account changed to [%s]", this.itemDescription, this.postCommandAccount);
  }
}
