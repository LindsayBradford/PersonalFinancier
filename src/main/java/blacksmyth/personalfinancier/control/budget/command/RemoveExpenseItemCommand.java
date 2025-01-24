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

public class RemoveExpenseItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private BudgetItem postCommandItem;
  private String preCommandDescription;
  
  public static RemoveExpenseItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new RemoveExpenseItemCommand(model, itemIndex);
  }
  
  protected RemoveExpenseItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.preCommandDescription = model.getExpenseItems().get(itemIndex).getDescription();
    this.postCommandItem = model.removeExpenseItem(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeExpenseItem(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addExpenseItem(itemIndex, this.postCommandItem);
  }
  
  @Override
  public String getPresentationName() {
    return String.format("Removed expense item [%s] ", preCommandDescription);
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Undid removal of expense item [%s]", preCommandDescription);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Removed expense item [%s] ", preCommandDescription);
  }
}
