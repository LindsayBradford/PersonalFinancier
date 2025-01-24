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

import java.util.TreeSet;

public class Categories {
  public static String DISCRETIONARY =  "Discretionary";
  
  public static TreeSet<String> defaultExpenseCategories() {
    TreeSet<String> categories = new TreeSet<String>();

    categories.add(DISCRETIONARY);
    categories.add("Fixed Essential");
    categories.add("Variable Essential");
    categories.add("Tax Liability");

    return categories;
  }

  public static String SALARY =  "Salary";

  public static TreeSet<String> defaultIncomeCategories() {
    TreeSet<String> categories = new TreeSet<String>();

    categories.add(SALARY);
    categories.add("Bank Interest");
    categories.add("Fixed Interest");
    categories.add("Dividends");
    categories.add("Tax Return");
    
    return categories;
  }
}