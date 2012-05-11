package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ChangeExpenseDescriptionCommand extends AbstractBudgetCommand {
  
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
  public void undo() throws CannotUndoException {
    model.setExpenseItemDescription(
        itemIndex, 
        preCommandDescription
    );
  }
}
