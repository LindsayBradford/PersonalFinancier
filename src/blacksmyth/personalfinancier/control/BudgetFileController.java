package blacksmyth.personalfinancier.control;

import java.lang.reflect.Type;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.reflect.TypeToken;

import blacksmyth.general.JSonAdapter;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
  

public class BudgetFileController implements Observer, IBudgetController, IBudgetObserver {
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
    JSonAdapter.getInstance().save(
        this.getBudgetModel().getState(),
        fileName
    );
  }

  public void load() {
    Type budgetItemsType = new TypeToken<BudgetModel.SerializableState>() {}.getType();
    this.getBudgetModel().setState(
        (BudgetModel.SerializableState) JSonAdapter.getInstance().load(fileName, budgetItemsType)
    );
  }

  public void update(Observable o, Object arg) {}
}
