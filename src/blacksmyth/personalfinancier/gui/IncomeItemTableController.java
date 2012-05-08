package blacksmyth.personalfinancier.gui;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.IncomeCategory;
import blacksmyth.personalfinancier.model.budget.IncomeItem;

public class IncomeItemTableController extends AbstractTableModel 
                implements IBudgetObserver, IBudgetController {
  private static final long serialVersionUID = 1L;

  private BudgetModel budgetMmodel;
 
  public IncomeItemTableController(BudgetModel budgetModel) {
    super();
    setBudgetModel(budgetModel);
  }

  public int getColumnCount() {
    return IncomeItemTable.TABLE_COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return IncomeItemTable.TABLE_COLUMNS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (IncomeItemTable.TABLE_COLUMNS.values()[colNum]) {
      case Category:
        return IncomeCategory.class;
      case Description:
        return String.class;
      case Amount: 
        return Money.class;
      case Frequency:
        return CashFlowFrequency.class;
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return BigDecimal.class;
      case Account:
        return String.class;
    }
    return Object.class;
  }

  public int getRowCount() {
    return getBudgetModel().getIncomeItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (IncomeItemTable.TABLE_COLUMNS.values()[colNum]) {
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return false;
      default:
        return true;
    }
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    IncomeItem item = (IncomeItem) getBudgetModel().getIncomeItems().get(rowNum);
    
    switch (IncomeItemTable.TABLE_COLUMNS.values()[colNum]) {
      case Category:
        return item.getCategory();
      case Description:
        return item.getDescription();
      case Amount: 
        return item.getBudgettedAmount().getTotal();
      case Daily:
        return convertAmount(item, CashFlowFrequency.Daily);
      case Weekly:
        return convertAmount(item, CashFlowFrequency.Weekly);
      case Fortnightly:
        return convertAmount(item, CashFlowFrequency.Fortnightly);
      case Monthly:
        return convertAmount(item, CashFlowFrequency.Monthly);
      case Quarterly:
        return convertAmount(item, CashFlowFrequency.Quarterly);
      case Yearly:
        return convertAmount(item, CashFlowFrequency.Yearly);
      case Frequency:
        return item.getFrequency();
      case Account:
         return item.getBudgetAccount().getNickname();
       default:
         return null;
    }
  }
  
  private BigDecimal convertAmount(BudgetItem item, CashFlowFrequency newFrequency) {
    return MoneyUtilties.convertFrequencyAmount(
        item.getBudgettedAmount().getTotal(), 
        item.getFrequency(), 
        newFrequency
    );
  }
  
  public void setValueAt(Object value, int rowNum, int colNum) {
    switch (IncomeItemTable.TABLE_COLUMNS.values()[colNum]) {
    case Category:
      getBudgetModel().setIncomeItemCategory(
          rowNum, 
          IncomeCategory.valueOf((String) value)
      );
      break;
    case Description:
      getBudgetModel().setIncomeItemDescription(rowNum, (String) value);
      break;
    case Amount:
      getBudgetModel().setIncomeItemTotal(
          rowNum,
          BigDecimalFactory.create((String) value)
      );
      break;
    case Frequency:
      getBudgetModel().setIncomeItemFrequency(
          rowNum,
          CashFlowFrequency.valueOf((String) value)
      );
      break;
    case Account:
      getBudgetModel().setIncomeItemAccount(
          rowNum,
          (String) value
      );
      break;
    }
    this.fireTableRowsUpdated(rowNum, rowNum);
  }

  public void update(Observable o, Object arg) {
    this.fireTableDataChanged();
  }
  
  private void setBudgetModel(BudgetModel model) {
    this.budgetMmodel = model;
    this.budgetMmodel.addObserver(this);
  }
  
  public BudgetModel getBudgetModel() {
    return this.budgetMmodel;
  }
  
  public void addModelObserver(Observer observer) {
    this.getBudgetModel().addObserver(observer);
  }

  public void addBudgetItem() {
    this.getBudgetModel().addIncomeItem();
  }

  public void removeItem(int row) {
    this.getBudgetModel().removeIncomeItem(row);
  }

  public void removeAllBudgetItems() {
    this.getBudgetModel().removeAllIncomeItems();
  }
}
