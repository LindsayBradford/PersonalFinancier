package blacksmyth.personalfinancier.control;

import java.lang.reflect.Type;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.reflect.TypeToken;

import blacksmyth.general.FileUtilities;
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
    FileUtilities.saveTextFile(
        fileName, 
        JSonAdapter.getInstance().toJSonFromObject(
            this.getBudgetModel().getState()
        )
    );
  }
  
  public void load() {
    Type budgetItemsType = new TypeToken<BudgetModel.SerializableState>() {}.getType();
    this.getBudgetModel().setState(
        (BudgetModel.SerializableState) this.loadJSon(fileName, budgetItemsType)
    );
  }

  private Object loadJSon(String fileName, Type typeToExpect) {
    return JSonAdapter.getInstance().toObjectFromJSon(
        FileUtilities.loadTextFile(fileName), 
        typeToExpect
    );
  }
  
  public void update(Observable o, Object arg) {}
}
