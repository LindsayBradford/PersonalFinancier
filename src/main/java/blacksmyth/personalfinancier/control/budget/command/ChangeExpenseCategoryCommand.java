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

public class ChangeExpenseCategoryCommand extends AbstractBudgetCommand {
  
  private BudgetModel model;
  private int itemIndex;
  private String preCommandCategory;
  private String postCommandCategory;
  private String itemDescription;
  
  public static ChangeExpenseCategoryCommand doCmd(BudgetModel model, int expenseItemIndex, 
                                                   String postCommandCategory) {
    
    ChangeExpenseCategoryCommand command = new ChangeExpenseCategoryCommand(
        model, 
        expenseItemIndex, 
        model.getExpenseItems().get(expenseItemIndex).getCategory(),
        postCommandCategory
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseCategoryCommand(BudgetModel model, int itemIndex, 
                                         String preCommandCategory, String postCommandCategory) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.itemDescription = model.getExpenseItems().get(itemIndex).getDescription();
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
    model.setExpenseItemCategory(
        itemIndex, 
        postCommandCategory
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemCategory(
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
    return String.format("Expense item [%s] category changed to [%s]", this.itemDescription, this.preCommandCategory);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Expense item [%s] category changed to [%s]", this.itemDescription, this.postCommandCategory);
  }
}
