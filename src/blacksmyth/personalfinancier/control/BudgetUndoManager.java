package blacksmyth.personalfinancier.control;

import javax.swing.undo.UndoManager;

public class BudgetUndoManager extends UndoManager {
  
  private static BudgetUndoManager instance;
  
  protected BudgetUndoManager() {
    super();
  }
  
  public static BudgetUndoManager getInstance() {
    if (instance == null) {
      instance = new BudgetUndoManager();
    }
    return instance;
  }

}
