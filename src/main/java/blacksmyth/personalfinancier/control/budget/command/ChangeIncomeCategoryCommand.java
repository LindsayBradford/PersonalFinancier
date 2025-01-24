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

public class ChangeIncomeCategoryCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private String preCommandCategory;
  private String postCommandCategory;
  private String itemDescription;
  
  public static ChangeIncomeCategoryCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                   String postCommandCategory) {
    
    ChangeIncomeCategoryCommand command = new ChangeIncomeCategoryCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getCategory(),
        postCommandCategory
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeCategoryCommand(BudgetModel model, int itemIndex, 
                                         String preCommandCategory, String postCommandCategory) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.itemDescription = model.getIncomeItems().get(itemIndex).getDescription();
    this.preCommandCategory = preCommandCategory;
    this.postCommandCategory = postCommandCategory;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandCategory.equals(this.postCommandCategory)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemCategory(
        itemIndex, 
        postCommandCategory
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemCategory(
        itemIndex, 
        preCommandCategory
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Income item [%s] category changed to [%s]", this.itemDescription, this.preCommandCategory);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Income item [%s] category changed to [%s]", this.itemDescription, this.postCommandCategory);
  }
}
