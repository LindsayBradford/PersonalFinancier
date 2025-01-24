/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model.budget;

public class BudgetEvent {
  
  // TODO: Views are still doing too much work. Record indexes of changed items in the event too.
  
  public enum ItemType {
    IncomeItems, ExpenseItems, AllItems, 
    IncomeCategories, ExpenseCategories, Accounts
  }
  
  private ItemType itemType;
  
  public BudgetEvent() {
    this.setItemType(ItemType.AllItems);
  }
  
  public BudgetEvent(ItemType itemType) {
    setItemType(itemType);
  }
  
  public ItemType getItemType() {
    return this.itemType;
  }
  
  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }
}
