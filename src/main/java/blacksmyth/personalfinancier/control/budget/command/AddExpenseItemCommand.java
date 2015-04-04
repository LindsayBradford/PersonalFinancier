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
import blacksmyth.personalfinancier.model.budget.BudgetItem;

public class AddExpenseItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private BudgetItem postCommandItem;
  private int itemIndex;
  
  public static AddExpenseItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new AddExpenseItemCommand(model, itemIndex);
  }
  
  protected AddExpenseItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.postCommandItem = model.addExpenseItem(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.addExpenseItem(this.itemIndex, this.postCommandItem);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeExpenseItem(this.itemIndex);
  }
}
