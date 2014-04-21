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

public class ChangeIncomeDescriptionCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private String preCommandDescription;
  private String postCommandDescription;
  
  public static ChangeIncomeDescriptionCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                 String postCommandAccount) {
    
    ChangeIncomeDescriptionCommand command = new ChangeIncomeDescriptionCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getDescription(),
        postCommandAccount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeDescriptionCommand(BudgetModel model, int incomeItemIndex, 
                                       String preCommandAccount, String postCommandAccount) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.preCommandDescription = preCommandAccount;
    this.postCommandDescription = postCommandAccount;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandDescription.equals(this.postCommandDescription)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemDescription(
        incomeItemIndex, 
        postCommandDescription
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemDescription(
        incomeItemIndex, 
        preCommandDescription
    );
  }
}
