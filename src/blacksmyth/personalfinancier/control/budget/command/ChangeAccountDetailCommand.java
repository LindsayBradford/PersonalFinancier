package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.AccountModel;

public class ChangeAccountDetailCommand extends AbstractBudgetCommand {
  
  private AccountModel model;
  private int accountItemIndex;
  private String preCommandDetail;
  private String postCommandDetail;
  
  public static ChangeAccountDetailCommand doCmd(AccountModel model, int accountItemIndex, 
                                                 String postCommandDetail) {
    
    ChangeAccountDetailCommand command = new ChangeAccountDetailCommand(
        model, 
        accountItemIndex, 
        model.getAccount(accountItemIndex).getDetail(),
        postCommandDetail
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeAccountDetailCommand(AccountModel model, int accountItemIndex, 
                                         String preCommandDetail, String postCommandDetail) {
    this.model = model;
    this.accountItemIndex = accountItemIndex;
    this.preCommandDetail = preCommandDetail;
    this.postCommandDetail = postCommandDetail;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandDetail.equals(this.postCommandDetail)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setAccountDetail(
        accountItemIndex, 
        postCommandDetail
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setAccountDetail(
        accountItemIndex, 
        preCommandDetail
    );
  }
}
