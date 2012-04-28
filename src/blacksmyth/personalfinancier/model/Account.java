package blacksmyth.personalfinancier.model;

public class Account {
  private String nickname;
  //TODO: fill this out.

  public static final Account DEFAULT = new Account("Default");

  public Account(String nickname) {
    this.setNickname(nickname);
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

}
