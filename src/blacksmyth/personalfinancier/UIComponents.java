/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier;

import javax.swing.Action;
import javax.swing.JFrame;

import blacksmyth.personalfinancier.control.budget.BudgetFileController;
import blacksmyth.personalfinancier.control.inflation.InflationFileController;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.budget.ExpenseItemTable;
import blacksmyth.personalfinancier.view.budget.IncomeItemTable;
import blacksmyth.personalfinancier.view.inflation.InflationTable;

public class UIComponents {
  public static JFrame      windowFrame;

  public static BudgetModel budgetModel;
  public static AccountModel accountModel;
  
  public static ExpenseItemTable expenseTable;
  public static IncomeItemTable incomeTable;
  
  public static BudgetFileController budgetFileController;
  
  public static Action LoadBudgetAction;
  public static Action SaveBudgetAction;
  public static Action SaveAsBudgetAction;
  public static Action ExitAction;

  public static InflationModel inflationModel;
  public static InflationTable inflationTable;

  public static Action LoadInflationAction;
  public static Action SaveInflationAction;

  public static InflationFileController inflationFileController;

}
