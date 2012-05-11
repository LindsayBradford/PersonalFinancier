package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class MoveIncomeItemDownCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int itemIndex;
  
  public static MoveIncomeItemDownCommand doCmd(BudgetModel model, int itemIndex) {
    MoveIncomeItemDownCommand command = new MoveIncomeItemDownCommand(model, itemIndex);
    
    command.redo();
    
    return command;
  }
  
  protected MoveIncomeItemDownCommand(BudgetModel model, int itemIndex) {
    this.model = model;
    this.itemIndex = itemIndex;
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
    model.moveIncomeItemDown(this.itemIndex);
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveIncomeItemUp(this.itemIndex + 1);
  }
}
