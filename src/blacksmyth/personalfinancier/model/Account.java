/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.model;

public class Account {
  private String nickname;
  private String detail;

  private boolean isBudgetAccount;

  public static final Account DEFAULT = new Account("Bills", "Bills Account Detail");
  
  public static Account Create() {
    return new Account("New Account", "New Account Detail");
  }

  public Account(String nickname) {
    init(nickname, null, true);
  }

  public Account(String nickname, String detail) {
    init(nickname, detail, true);
  }
  
  public Account(String nickname, boolean isBudgetAccount) {
    init(nickname, null, isBudgetAccount);
  }

  public Account(String nickname, String detail, boolean isBudgetAccount) {
    init(nickname, detail, isBudgetAccount);
  }

  private void init(String nickname, String detail, boolean isBudgetAccount) {
    this.setNickname(nickname);
    this.setDetail(detail);
    this.setBudgetAccount(true);
  }

  /**
   * returns a human understandable nickname for the account. 
   * @return
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * Allows a human-understandable nickname to be set for the account.
   * @param nickname
   */
  protected void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getDetail() {
    return detail;
  }
  
  protected void setDetail(String detail) {
    this.detail = detail;
  }
  
  /**
   * indicated whether the account is used in budgetting.
   * @return
   */
 public boolean isBudgetAccount() {
    return isBudgetAccount;
 }
 
 /**
  * Allows the account to be set as a budget account or not. 
  * If it is a budget account it will be selectable as an
  * account for receiving budget item monies.
  * @param isBudgetAccount
  */

  protected void setBudgetAccount(boolean isBudgetAccount) {
    this.isBudgetAccount = isBudgetAccount;
  }

}
