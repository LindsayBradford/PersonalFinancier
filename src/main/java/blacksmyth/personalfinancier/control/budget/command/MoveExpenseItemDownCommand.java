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

public class MoveExpenseItemDownCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int preItemIndex;
  private int postItemIndex;
  
  public static MoveExpenseItemDownCommand doCmd(BudgetModel model, int itemIndex) {
    MoveExpenseItemDownCommand command = new MoveExpenseItemDownCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveExpenseItemDownCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.preItemIndex = itemIndex;
    this.postItemIndex = itemIndex + 1;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveExpenseItemDown(preItemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveExpenseItemUp(postItemIndex);
  }
  
  @Override
  public String getPresentationName() {
    return String.format("Expense item [%s] moved down", model.getExpenseItems().get(this.postItemIndex).getDescription());
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Expense item [%s] moved up", model.getExpenseItems().get(this.postItemIndex).getDescription());
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Expense item [%s] moved down", model.getExpenseItems().get(this.preItemIndex).getDescription());
  }
}
