package blacksmyth.personalfinancier;

import javax.swing.Action;
import javax.swing.JFrame;

import blacksmyth.personalfinancier.control.BudgetFileController;
import blacksmyth.personalfinancier.control.gui.ExpenseItemTable;
import blacksmyth.personalfinancier.control.gui.IncomeItemTable;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class UIComponents {
  public static JFrame      windowFrame;

  public static BudgetModel budgetModel;
  
  public static ExpenseItemTable expenseTable;
  public static IncomeItemTable incomeTable;
  
  public static BudgetFileController budgetFileController;
  
  public static Action LoadBudgetAction;
  public static Action SaveBudgetAction;
  public static Action SaveAsBudgetAction;

}
