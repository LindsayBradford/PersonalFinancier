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

public class AddIncomeItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel budgetModel;
  private BudgetItem postCommandItem;
  private int itemIndex;
  private String postCommandDescription;
  
  public static AddIncomeItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new AddIncomeItemCommand(model, itemIndex);
  }
  
  protected AddIncomeItemCommand(BudgetModel model, int itemIndex) {
    this.budgetModel = model;
    this.itemIndex = itemIndex;
    this.postCommandItem = model.addIncomeItem(itemIndex);
    this.postCommandDescription = budgetModel.getIncomeItems().get(this.itemIndex).getDescription();
  }

  @Override
  public void redo() throws CannotRedoException {
    budgetModel.addIncomeItem(this.itemIndex, this.postCommandItem);
  }

  @Override
  public void undo() throws CannotUndoException {
    budgetModel.removeIncomeItem(this.itemIndex);
  }
  
  @Override
  public String getPresentationName() {
    return String.format("Added new imcome item [%s] ", postCommandDescription);
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Undid addition of imcome item [%s]", postCommandDescription);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Added new imcome item [%s] ", postCommandDescription);
  }
}
