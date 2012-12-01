/**
 * (c) 2012, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier.control.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class MoveIncomeItemDownCommand extends AbstractBudgetCommand {
  
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
  public void redo() throws CannotRedoException {
    model.moveIncomeItemDown(this.itemIndex);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.moveIncomeItemUp(this.itemIndex + 1);
  }
}
