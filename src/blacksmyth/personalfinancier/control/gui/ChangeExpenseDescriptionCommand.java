package blacksmyth.personalfinancier.control.gui;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.UndoableBudgetCommand;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseDescriptionCommand implements UndoableBudgetCommand, IBudgetController {
  
  private BudgetModel model;
  private int itemIndex;
  private String preCommandDescription;
  private String postCommandDescription;
  
  public static ChangeExpenseDescriptionCommand doCmd(BudgetModel model, int itemIndex, 
                                                 String postCommandDescription) {
    
    ChangeExpenseDescriptionCommand command = new ChangeExpenseDescriptionCommand(
        model, 
        itemIndex, 
        model.getExpenseItems().get(itemIndex).getDescription(),
        postCommandDescription
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeExpenseDescriptionCommand(BudgetModel model, int itemIndex, 
                                       String preCommandDescription, String postCommandDescription) {
    this.model = model;
    this.itemIndex = itemIndex;
    this.preCommandDescription = preCommandDescription;
    this.postCommandDescription = postCommandDescription;
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
    return " Change expense item description back to '" +
        this.postCommandDescription + "'. ";
  }

  @Override
  public String getUndoPresentationName() {
    return " Change expense item description back to '" +
    		this.preCommandDescription + "'. ";
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
    model.setExpenseItemDescription(
        itemIndex, 
        postCommandDescription
    );
  }

  @Override
  public boolean replaceEdit(UndoableEdit arg0) {
    return false;
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setExpenseItemDescription(
        itemIndex, 
        preCommandDescription
    );
  }
}
