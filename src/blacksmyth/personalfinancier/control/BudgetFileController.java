package blacksmyth.personalfinancier.control;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.reflect.TypeToken;

import blacksmyth.personalfinancier.io.json.GSonAdapter;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class BudgetFileController implements Observer {
  private static final String fileName = "test.json";
  
  private BudgetModel model;

  public BudgetFileController (BudgetModel model) {
    setModel(model);
  }
  
  public BudgetModel getBudgetModel() {
    return this.model;
  }

  public void setModel(BudgetModel model) {
    model.addObserver(this);
    this.model = model;
  }

  public void save() {
    GSonAdapter.getInstance().save(
        this.getBudgetModel().getBudgetItems(),
        fileName
    );
  }

  @SuppressWarnings("unchecked")
  public void load() {
    Type budgetItemsType = new TypeToken<ArrayList<BudgetItem>>() {}.getType();
    this.getBudgetModel().setBudgetItems(
        (ArrayList<BudgetItem>) GSonAdapter.getInstance().load(fileName, budgetItemsType)
    );
  }

  public void update(Observable o, Object arg) {
    System.out.println("File controller ready.");
  }
}
