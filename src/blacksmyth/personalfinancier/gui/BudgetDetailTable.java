package blacksmyth.personalfinancier.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.model.Account;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.MoneyAmount;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class BudgetDetailTable extends JTable {
  private static final long serialVersionUID = 1L;

  // TODO: align with BudgetModel;
  
  public BudgetDetailTable() {
    super(
        new BudgetDetailTableModel()
    );
  }

  public void update(Observable model, Object modelArgs) {
  }
}


class BudgetDetailTableModel extends AbstractTableModel implements Observer {
  private static final long serialVersionUID = 1L;

  private BudgetModel baseModel;
  
  private static enum COLUMNS {
    Description,
    Amount,
    Frequency,
    Account
  }
  
  private static final String[] COL_NAMES = { 
      COLUMNS.Description.toString(),
      COLUMNS.Amount.toString(),
      COLUMNS.Frequency.toString(),
      COLUMNS.Account.toString(),
  };
  
  public BudgetDetailTableModel() {
    super();
    baseModel = new BudgetModel();

    // TODO: we need a BudgetModel controller.
    
    baseModel.addObserver(this);
    
    baseModel.addBudgetItem("test");
    baseModel.addBudgetItem("another test");
    baseModel.addBudgetItem("yet another test");
  }

  public int getColumnCount() {
    return COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return COL_NAMES[colNum];
  }
  
  public Class getColumnClass(int colNum) {
    switch (colNum) {
    case 0:
      return String.class;
    case 1:
      return MoneyAmount.class;
    case 2:
      return CashFlowFrequency.class;
    case 3:
      return Account.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return baseModel.getBudgetItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    return true;
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    BudgetItem item = (BudgetItem) baseModel.getBudgetItems().get(rowNum);
    switch (colNum) {
    case 0:
      return item.getDescription();
    case 1:
      return item.getAmount().getAmount();
    case 2:
      return item.getFrequency();
    case 3:
       return item.getAccount();
    }
    return null;
  }

  public void update(Observable o, Object arg) {
    this.fireTableDataChanged();
  }
}