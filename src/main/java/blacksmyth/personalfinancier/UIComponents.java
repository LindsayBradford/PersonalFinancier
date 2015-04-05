/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import blacksmyth.general.file.IFileHandler;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetFileContent;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.inflation.InflationFileContent;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.ApplicationMessageView;
import blacksmyth.personalfinancier.view.budget.ExpenseItemTable;
import blacksmyth.personalfinancier.view.budget.IncomeItemTable;
import blacksmyth.personalfinancier.view.inflation.InflationTable;

public class UIComponents {
  public static JFrame      windowFrame;
  
  public static ApplicationMessageView messageBar;

  public static BudgetModel budgetModel;
  public static AccountModel accountModel;
  
  public static JToolBar expenseItemToolbar;
  public static ExpenseItemTable expenseItemTable;

  public static JToolBar incomeItemToolbar;
  public static IncomeItemTable incomeItemTable;
  
  public static IFileHandler<BudgetFileContent> budgetFileController;
  
  public static Action LoadBudgetAction;
  public static Action SaveBudgetAction;
  public static Action SaveAsBudgetAction;
  public static Action ExitAction;

  public static InflationModel inflationModel;
  public static InflationTable inflationTable;

  public static Action LoadInflationAction;
  public static Action SaveInflationAction;

  public static IFileHandler<InflationFileContent> inflationFileController;

}
