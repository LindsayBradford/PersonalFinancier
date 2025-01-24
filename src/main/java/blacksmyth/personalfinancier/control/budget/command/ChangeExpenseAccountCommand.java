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

public class ChangeExpenseAccountCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private String preCommandAccount;
  private String postCommandAccount;
  private String itemDescription;
  
  public static ChangeExpenseAccountCommand doCmd(BudgetModel model, int itemIndex, 
                                                 String postCommandAccount) {
    
    ChangeExpenseAccountCommand command = new ChangeExpenseAccountCommand(
        model, 
        itemIndex, 
        model.getExpenseItems().get(itemIndex).getBudgetAccount().getNickname(),
        postCommandAccount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseAccountCommand(BudgetModel model, int itemIndex, 
                                       String preCommandAccount, String postCommandAccount) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.itemDescription = model.getExpenseItems().get(itemIndex).getDescription();
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
    model.setExpenseItemAccount(
        itemIndex, 
        postCommandAccount
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemAccount(
        itemIndex, 
        preCommandAccount
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Expense item [%s] account changed to [%s]", this.itemDescription, this.preCommandAccount);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Expense item [%s] account changed to [%s]", this.itemDescription, this.postCommandAccount);
  }

}
