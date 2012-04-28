package blacksmyth.personalfinancier.model;

public class Account {
  private String nickname;
  private boolean isBudgetAccount;

  //TODO: fill this out.

  public static final Account DEFAULT = new Account("Default");

  public Account(String nickname) {
    init(nickname, true);
  }
  
  public Account(String nickname, boolean isBudgetAccount) {
    init(nickname, isBudgetAccount);
  }

  private void init(String nickname, boolean isBudgetAccount) {
    this.setNickname(nickname);
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
