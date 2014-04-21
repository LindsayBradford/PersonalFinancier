/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.control.budget.command;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import blacksmyth.personalfinancier.model.AccountModel;

public class ChangeAccountNicknameCommand extends AbstractBudgetCommand {
  
  private AccountModel model;
  private int accountItemIndex;
  private String preCommandNickname;
  private String postCommandNickname;
  
  public static ChangeAccountNicknameCommand doCmd(AccountModel model, int accountItemIndex, 
                                                   String postCommandNickname) {
    
    ChangeAccountNicknameCommand command = new ChangeAccountNicknameCommand(
        model, 
        accountItemIndex, 
        model.getAccount(accountItemIndex).getNickname(),
        postCommandNickname
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeAccountNicknameCommand(AccountModel model, int accountItemIndex, 
                                         String preCommandNickname, String postCommandNickname) {
    this.model = model;
    this.accountItemIndex = accountItemIndex;
    this.preCommandNickname = preCommandNickname;
    this.postCommandNickname = postCommandNickname;
  }

  @Override
  public boolean isSignificant() {
    if (this.preCommandNickname.equals(this.postCommandNickname)) {
      return false;
    }
    return true;
  }

  @Override
  public void redo() throws CannotRedoException {
    model.setAccountNickname(
        accountItemIndex, 
        postCommandNickname
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setAccountNickname(
        accountItemIndex, 
        preCommandNickname
    );
  }
}
