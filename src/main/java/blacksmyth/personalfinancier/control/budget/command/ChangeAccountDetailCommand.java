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

public class ChangeAccountDetailCommand extends AbstractBudgetCommand {
  
  private AccountModel model;
  private int accountIndex;
  private String preCommandDetail;
  private String postCommandDetail;
  private String nickname;
  
  public static ChangeAccountDetailCommand doCmd(AccountModel model, int accountIndex, 
                                                 String postCommandDetail) {
    
    ChangeAccountDetailCommand command = new ChangeAccountDetailCommand(
        model, 
        accountIndex, 
        model.getAccount(accountIndex).getDetail(),
        postCommandDetail
    );
    
    command.redo();
    
    return command;
  }
  
  protected ChangeAccountDetailCommand(AccountModel model, int accountIndex, 
                                         String preCommandDetail, String postCommandDetail) {
    this.model = model;
    this.accountIndex = accountIndex;
    this.nickname = model.getAccount(accountIndex).getNickname();
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
        accountIndex, 
        postCommandDetail
    );
  }

  @Override
  public void undo() throws CannotUndoException {
    model.setAccountDetail(
        accountIndex, 
        preCommandDetail
    );
  }
  
  @Override
  public String getPresentationName() {
    return getRedoPresentationName();
  }

  @Override
  public String getUndoPresentationName() {
    return String.format("Account [%s] detail changed to [%s]", this.nickname, this.preCommandDetail);
  }

  @Override
  public String getRedoPresentationName() {
    return String.format("Account [%s] detail changed to [%s]", this.nickname, this.postCommandDetail);
  }

}
