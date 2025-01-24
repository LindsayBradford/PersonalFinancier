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

import java.util.ArrayList;
import java.util.TreeSet;

import blacksmyth.personalfinancier.model.Account;

public class BudgetFileContent {
  public TreeSet<String> expenseCategories;
  public TreeSet<String> incomeCategories;
  public ArrayList<Account> accounts;
  public ArrayList<BudgetItem> incomeItems;
  public ArrayList<BudgetItem> expenseItems;
}