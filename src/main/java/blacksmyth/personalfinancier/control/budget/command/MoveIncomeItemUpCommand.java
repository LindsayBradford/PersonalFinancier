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

public class MoveIncomeItemUpCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int preItemIndex;
  private int postItemIndex;
  
  public static MoveIncomeItemUpCommand doCmd(BudgetModel model, int itemIndex) {
    MoveIncomeItemUpCommand command = new MoveIncomeItemUpCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveIncomeItemUpCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.preItemIndex = itemIndex;
    this.postItemIndex = itemIndex - 1;

  }

  @Override
  public void redo() throws CannotRedoException {
    model.moveIncomeItemUp(this.preItemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveIncomeItemDown(this.postItemIndex);
  }
  
  @Override
  public String getPresentationName() {
    return String.format("Income item [%s] moved up", model.getIncomeItems().get(this.postItemIndex).getDescription());
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Income item [%s] moved down", model.getIncomeItems().get(this.postItemIndex).getDescription());
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Income item [%s] moved up", model.getIncomeItems().get(this.preItemIndex).getDescription());
  }
}
