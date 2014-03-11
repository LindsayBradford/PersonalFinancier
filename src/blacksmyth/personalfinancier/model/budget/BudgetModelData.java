package blacksmyth.personalfinancier.model.budget;

import java.util.ArrayList;
import java.util.HashSet;

public class BudgetModelData {
  public HashSet<String> expenseCategories;
  public HashSet<String> incomeCategories;
  public ArrayList<BudgetItem> incomeItems;
  public ArrayList<BudgetItem> expenseItems;
}