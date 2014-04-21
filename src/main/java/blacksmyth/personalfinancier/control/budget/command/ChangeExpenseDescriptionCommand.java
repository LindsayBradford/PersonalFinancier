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

public class ChangeExpenseDescriptionCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private String preCommandDescription;
  private String postCommandDescription;
  
  public static ChangeExpenseDescriptionCommand doCmd(BudgetModel model, int itemIndex, 
                                                 String postCommandDescription) {
    
    ChangeExpenseDescriptionCommand command = new ChangeExpenseDescriptionCommand(
        model, 
        itemIndex, 
        model.getExpenseItems().get(itemIndex).getDescription(),
        postCommandDescription
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseDescriptionCommand(BudgetModel model, int itemIndex, 
                                       String preCommandDescription, String postCommandDescription) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.preCommandDescription = preCommandDescription;
    this.postCommandDescription = postCommandDescription;
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
    model.setExpenseItemDescription(
        itemIndex, 
        postCommandDescription
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemDescription(
        itemIndex, 
        preCommandDescription
    );
  }
}
