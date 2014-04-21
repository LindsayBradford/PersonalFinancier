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

public class MoveIncomeItemDownCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  
  public static MoveIncomeItemDownCommand doCmd(BudgetModel model, int itemIndex) {
    MoveIncomeItemDownCommand command = new MoveIncomeItemDownCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveIncomeItemDownCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveIncomeItemDown(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveIncomeItemUp(this.itemIndex + 1);
  }
}
