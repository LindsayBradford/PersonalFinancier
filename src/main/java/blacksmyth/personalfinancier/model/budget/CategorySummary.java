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

import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequencyUtility;

public class CategorySummary implements Comparable<CategorySummary> {
  
  private String budgetCategory;
  private Money   budgettedAmount;
  private CashFlowFrequency budgettedFrequency;
  
  public CategorySummary(String category) {
    this.budgetCategory = category;
    this.budgettedAmount = MoneyFactory.createAmount(0);
    // TODO: drive frequency off preferences.
    this.budgettedFrequency = CashFlowFrequency.Fortnightly;
  }
  
  public String getBudgetCategory() {
    return this.budgetCategory;
  }
  
  protected void setBudgetCategory(String category) {
    this.budgetCategory = category;
  }
  
  public Money getBudgettedAmount() {
    return budgettedAmount;
  }
  
  protected void setBudgettedAmount(Money budgettedAmount) {
    this.budgettedAmount = budgettedAmount;
  }
  
  public CashFlowFrequency getBudgettedFrequency() {
    return budgettedFrequency;
  }

  public void setBudgettedFrequency(CashFlowFrequency budgettedFrequency) {
    this.budgettedFrequency = budgettedFrequency;
  }

  public Object getBudgettedAmountAtFrequency(CashFlowFrequency newFrequency) {
    return CashFlowFrequencyUtility.convertFrequencyAmount(
        this.getBudgettedAmount().getTotal(), 
        getBudgettedFrequency(), 
        newFrequency
     );
  }


  @Override
  public int compareTo(CategorySummary otherCategory) {
    // TODO: This needs to be more intelligent.
    // Right now, comparison of CategorySummary instances
    // is on budgeted amount total, regardless of exchange rates.
    return this.getBudgettedAmount().getTotal().compareTo(
        otherCategory.getBudgettedAmount().getTotal()
    );
  }
}
