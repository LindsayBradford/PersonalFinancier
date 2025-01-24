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

import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class RemoveIncomeItemCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private BudgetItem postCommandItem;
  private String preCommandDescription;

  
  public static RemoveIncomeItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new RemoveIncomeItemCommand(model, itemIndex);
  }
  
  protected RemoveIncomeItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.preCommandDescription = model.getIncomeItems().get(itemIndex).getDescription();
    this.postCommandItem = model.removeIncomeItem(itemIndex);
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeIncomeItem(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addIncomeItem(itemIndex, this.postCommandItem);
  }
  
  @Override
  public String getPresentationName() {
    return String.format("Removed income item [%s] ", preCommandDescription);
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Undid removal of income item [%s]", preCommandDescription);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Removed income item [%s] ", preCommandDescription);
  }
}
