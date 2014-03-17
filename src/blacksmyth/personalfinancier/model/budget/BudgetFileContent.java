package blacksmyth.personalfinancier.model.budget;

import java.util.ArrayList;
import java.util.HashSet;

import blacksmyth.personalfinancier.model.Account;

public class BudgetFileContent {
  public HashSet<String> expenseCategories;
  public HashSet<String> incomeCategories;
  public ArrayList<Account> accounts;
  public ArrayList<BudgetItem> incomeItems;
  public ArrayList<BudgetItem> expenseItems;
}