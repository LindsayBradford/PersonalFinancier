package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeIncomeFrequencyCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int incomeItemIndex;
  private CashFlowFrequency preCommandFrequency;
  private CashFlowFrequency postCommandFrequency;
  
  public static ChangeIncomeFrequencyCommand doCmd(BudgetModel model, int incomeItemIndex, 
                                                   CashFlowFrequency postCommandFrequency) {
    
    ChangeIncomeFrequencyCommand command = new ChangeIncomeFrequencyCommand(
        model, 
        incomeItemIndex, 
        model.getIncomeItems().get(incomeItemIndex).getFrequency(),
        postCommandFrequency
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeIncomeFrequencyCommand(BudgetModel model, int incomeItemIndex, 
                                         CashFlowFrequency preCommandFrequency, CashFlowFrequency postCommandFrequency) {
    this.model = model;
    this.incomeItemIndex = incomeItemIndex;
    this.preCommandFrequency = preCommandFrequency;
    this.postCommandFrequency = postCommandFrequency;
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
    if (this.preCommandFrequency.equals(this.postCommandFrequency)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setIncomeItemFrequency(
        incomeItemIndex, 
        postCommandFrequency
    );
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setIncomeItemFrequency(
        incomeItemIndex, 
        preCommandFrequency
    );
  }
}
