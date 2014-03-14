package blacksmyth.personalfinancier.view.budget;

import java.util.Observable;

import blacksmyth.personalfinancier.control.UndoManagers;
import blacksmyth.personalfinancier.control.budget.command.AddAccountCommand;
import blacksmyth.personalfinancier.control.budget.command.ChangeAccountNicknameCommand;
import blacksmyth.personalfinancier.control.budget.command.ChangeAccountDetailCommand;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.AccountSummary;

enum ACCOUNT_SUMMARY_COLUMNS {
  Account, Detail, CashFlow
}

@SuppressWarnings("serial")
public class BudgetCashFlowSummaryTableModel extends AbstractBudgetTableModel<ACCOUNT_SUMMARY_COLUMNS> {

  public BudgetCashFlowSummaryTableModel(BudgetModel budgetModel, AccountModel accountModel) {
    super();
    this.setBudgetModel(budgetModel);
    this.setAccountModel(accountModel);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return String.class;
      case Detail:
        return String.class;
      case CashFlow: 
        return Money.class;
    }
    return Object.class;
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
      case Account: case Detail: 
        return true;
      default:
        return false;
    }
  }
  
  public void setValueAt(Object value, int rowNum, int colNum) {
    switch (this.getColumnEnumValueAt(colNum)) {
    case Account:
      UndoManagers.BUDGET_UNDO_MANAGER.addEdit(
          ChangeAccountNicknameCommand.doCmd(
              getAccountModel(),
              getAccountModel().getAccountIndex(
                  getSummaryAtRow(rowNum).getAccountNickname()
              ),
              (String) value
          )
      );
      break;
    case Detail:
      UndoManagers.BUDGET_UNDO_MANAGER.addEdit(
          ChangeAccountDetailCommand.doCmd(
              getAccountModel(), 
              getAccountModel().getAccountIndex(
                  getSummaryAtRow(rowNum).getAccountNickname()
              ),
              (String) value
          )
      );
      break;
    }
  }
  
  protected AccountSummary getSummaryAtRow(int rowNum) {
    return getBudgetModel().getCashFlowSummaries().get(rowNum);
  }

  public int getRowCount() {
    return getBudgetModel().getCashFlowSummaries().size() + 1;
  }

  public Object getValueAt(int rowNum, int colNum) {
    
    if (rowNum == this.getRowCount() - 1) {
      switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return null;
      case Detail:
        return "Net Cash Flow: ";
      case CashFlow: 
        return getBudgetModel().getNetCashFlow().getTotal();
      default:
           return null;
      }
    }
    
    AccountSummary summary = getSummaryAtRow(rowNum);

    switch (this.getColumnEnumValueAt(colNum)) {
      case Account:
        return summary.getAccountNickname();
      case Detail:
        return summary.getAccountDetail();
      case CashFlow: 
        return summary.getBudgettedAmountAtFrequency(CashFlowFrequency.Fortnightly);
      default:
           return null;
    }
  }
  
  public void addAccount() {    
    UndoManagers.BUDGET_UNDO_MANAGER.addEdit(
      AddAccountCommand.doCmd(
          getAccountModel()
      )
    );
  }
  
  @Override
  public void update(Observable arg0, Object arg1) {
    this.fireTableDataChanged();
  }
  
  private AccountModel accountModel;

  /**
   * Returns a reference to the budget model this TableModel observers.
   * @return
   */
  public final AccountModel getAccountModel() {
    return this.accountModel;
  }

  /**
   * Registers this TableModel as an observer of <tt>budgetModel</tt>
   * and stores a reference to it.
   * @param budgetModel
   */
  protected final void setAccountModel(AccountModel accountModel) {
    accountModel.addObserver(this);
    this.accountModel = accountModel;
  }


}