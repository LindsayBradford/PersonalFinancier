package blacksmyth.personalfinancier.control.gui;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.control.BudgetUndoManager;
import blacksmyth.personalfinancier.control.IBudgetController;
import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.control.command.AddExpenseItemCommand;
import blacksmyth.personalfinancier.control.command.ChangeExpenseAccountCommand;
import blacksmyth.personalfinancier.control.command.ChangeExpenseAmountCommand;
import blacksmyth.personalfinancier.control.command.ChangeExpenseCategoryCommand;
import blacksmyth.personalfinancier.control.command.ChangeExpenseDescriptionCommand;
import blacksmyth.personalfinancier.control.command.ChangeExpenseFrequencyCommand;
import blacksmyth.personalfinancier.control.command.MoveExpenseItemDownCommand;
import blacksmyth.personalfinancier.control.command.MoveExpenseItemUpCommand;
import blacksmyth.personalfinancier.control.command.RemoveExpenseItemCommand;
import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.CashFlowFrequency;
import blacksmyth.personalfinancier.model.Money;
import blacksmyth.personalfinancier.model.MoneyUtilties;
import blacksmyth.personalfinancier.model.budget.BudgetItem;
import blacksmyth.personalfinancier.model.budget.ExpenseCategory;
import blacksmyth.personalfinancier.model.budget.ExpenseItem;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class ExpenseItemTableController extends AbstractTableModel 
                implements IBudgetObserver, IBudgetController {
  private static final long serialVersionUID = 1L;

  private BudgetModel budgetMmodel;
 
  public ExpenseItemTableController(BudgetModel budgetModel) {
    super();
    setBudgetModel(budgetModel);
  }

  public int getColumnCount() {
    return ExpenseItemTable.TABLE_COLUMNS.values().length;
  }
  
  public String getColumnName(int colNum) {
    return ExpenseItemTable.TABLE_COLUMNS.values()[colNum].toString();
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Class getColumnClass(int colNum) {

    switch (ExpenseItemTable.TABLE_COLUMNS.values()[colNum]) {
      case Category:
        return ExpenseCategory.class;
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
    return getBudgetModel().getExpenseItems().size();
  }
  
  public boolean isCellEditable(int rowNum, int colNum) {
    switch (ExpenseItemTable.TABLE_COLUMNS.values()[colNum]) {
      case Daily: case Weekly: case Fortnightly: 
      case Monthly: case Quarterly: case Yearly:
        return false;
      default:
        return true;
    }
  }

  public Object getValueAt(int rowNum, int colNum) {
    @SuppressWarnings("cast")
    ExpenseItem item = (ExpenseItem) getBudgetModel().getExpenseItems().get(rowNum);
    
    switch (ExpenseItemTable.TABLE_COLUMNS.values()[colNum]) {
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
    switch (ExpenseItemTable.TABLE_COLUMNS.values()[colNum]) {
    case Category:
      BudgetUndoManager.getInstance().addEdit(
          ChangeExpenseCategoryCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              ExpenseCategory.valueOf((String) value)
          )
      );
      break;
    case Description:
      BudgetUndoManager.getInstance().addEdit(
          ChangeExpenseDescriptionCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              (String) value
          )
      );
      break;
    case Amount:
      BudgetUndoManager.getInstance().addEdit(
          ChangeExpenseAmountCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              BigDecimalFactory.create((String) value)
          )
      );
      break;
    case Frequency:
      BudgetUndoManager.getInstance().addEdit(
          ChangeExpenseFrequencyCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              CashFlowFrequency.valueOf((String) value)
          )
      );
      break;
    case Account:
      BudgetUndoManager.getInstance().addEdit(
          ChangeExpenseAccountCommand.doCmd(
              getBudgetModel(), 
              rowNum, 
              (String) value
          )
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

  public void addExpenseItem() {    
    BudgetUndoManager.getInstance().addEdit(
      AddExpenseItemCommand.doCmd(
          getBudgetModel()
      )
    );
  }

  public void removeItem(int row) {
    BudgetUndoManager.getInstance().addEdit(
        RemoveExpenseItemCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }
  

  public void moveExpenseItemDown(int row) {
    BudgetUndoManager.getInstance().addEdit(
        MoveExpenseItemDownCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }

  public void moveExpenseItemUp(int row) {
    BudgetUndoManager.getInstance().addEdit(
        MoveExpenseItemUpCommand.doCmd(
            getBudgetModel(),
            row
        )
    );
  }
}
