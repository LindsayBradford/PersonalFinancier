package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeDescriptionCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private String preCommandDescription;
  private String postCommandDescription;
  
  public static ChangeIncomeDescriptionCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                 String postCommandAccount) {
    
    ChangeIncomeDescriptionCommand command = new ChangeIncomeDescriptionCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getDescription(),
        postCommandAccount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeDescriptionCommand(BudgetModel model, int incomeItemIndex, 
                                       String preCommandAccount, String postCommandAccount) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.preCommandDescription = preCommandAccount;
    this.postCommandDescription = postCommandAccount;
  }

  @Override
  public boolean addEdit(UndoableEdit arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean canRedo() {
    return true;
  }

  @Override
  public boolean canUndo() {
    return true;
  }

  @Override
  public void die() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getRedoPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUndoPresentationName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandDescription.equals(this.postCommandDescription)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemDescription(
        incomeItemIndex, 
        postCommandDescription
    );
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemDescription(
        incomeItemIndex, 
        preCommandDescription
    );
  }
}
