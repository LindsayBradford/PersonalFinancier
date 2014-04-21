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

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.AccountModel;

public class AddAccountCommand extends AbstractBudgetCommand {
  
  private AccountModel model;
  private Account postCommandAccount;
  
  public static AddAccountCommand doCmd(AccountModel model) {
    return new AddAccountCommand(model);
  }
  
  protected AddAccountCommand(AccountModel model) {
    this.model = model;
    this.postCommandAccount= model.addAccount();
  }

  @Override
  public void redo() throws CannotRedoException {
    model.addAccount(this.postCommandAccount);
  }

  @Override
  public void undo() throws CannotUndoException {
    model.removeAccount(this.postCommandAccount);
  }
}
