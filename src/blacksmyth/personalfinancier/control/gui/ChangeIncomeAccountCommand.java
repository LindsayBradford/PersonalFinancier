package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeAccountCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private String preCommandAccount;
  private String postCommandAccount;
  
  public static ChangeIncomeAccountCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                 String postCommandAccount) {
    
    ChangeIncomeAccountCommand command = new ChangeIncomeAccountCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getBudgetAccount().getNickname(),
        postCommandAccount
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeAccountCommand(BudgetModel model, int incomeItemIndex, 
                                       String preCommandAccount, String postCommandAccount) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.preCommandAccount = preCommandAccount;
    this.postCommandAccount = postCommandAccount;
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
    if (this.preCommandAccount.equals(this.postCommandAccount)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemAccount(
        incomeItemIndex, 
        postCommandAccount
    );
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemAccount(
        incomeItemIndex, 
        preCommandAccount
    );
  }
}
