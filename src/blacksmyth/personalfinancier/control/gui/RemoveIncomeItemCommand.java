package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.IncomeItem;

public class RemoveIncomeItemCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int itemIndex;
  private IncomeItem postCommandItem;
  
  public static RemoveIncomeItemCommand doCmd(BudgetModel model, int itemIndex) {
    return new RemoveIncomeItemCommand(model, itemIndex);
  }
  
  protected RemoveIncomeItemCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.postCommandItem = model.removeIncomeItem(itemIndex);
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
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.removeIncomeItem(this.itemIndex);
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.addIncomeItem(itemIndex, this.postCommandItem);
  }
}
