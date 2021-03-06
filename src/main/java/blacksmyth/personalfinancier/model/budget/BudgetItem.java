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

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;

public class BudgetItem {

  private String category;
  protected String description;
  protected Money budgettedAmount;
  protected CashFlowFrequency frequency;
  protected Account budgetAccount;
  
  public BudgetItem(String category, String description, Money amount, 
                    CashFlowFrequency frequency, Account account) {
    this.category = category;
    this.description = description;
    this.budgettedAmount = amount;
    this.frequency = frequency;
    this.budgetAccount = account;
  }

  public String getCategory() {
    return this.category;
  }
  
  protected void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  protected void setDescription(String description) {
    this.description = description;
  }

  public Money getBudgettedAmount() {
    return budgettedAmount;
  }

  protected void setBudgettedAmount(Money amount) {
    this.budgettedAmount = amount;
  }

  public CashFlowFrequency getFrequency() {
    return frequency;
  }

  protected void setFrequency(CashFlowFrequency frequency) {
    this.frequency = frequency;
  }

  public Account getBudgetAccount() {
    return budgetAccount;
  }

  protected void setBudgetAccount(Account account) {
    this.budgetAccount = account;
  }

}