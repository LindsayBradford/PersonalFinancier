package blacksmyth.personalfinancier.control.gui;

import java.util.Observable;

import javax.swing.table.AbstractTableModel;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

/**
 * An abstract TableModel using generics for collecting all common behaviours of the Budget TableModels. An enumeration representing
 * the columns of the table model is required as part of the class definition. 
 * @author linds
 *
 * @param <T>
 */
abstract class AbstractBudgetTableModel<T extends Enum<T>> extends AbstractTableModel implements IBudgetObserver {

  private BudgetModel budgetModel;

  /**
   * Derives the Class of the columns enumeration supplied as the Gemeric type at subclass
   * definition time.
   * @return The Class of the columns enumeration.
   */
  @SuppressWarnings("unchecked")
  private final Class<T> getColEnumClass() {
    // TODO: Is there a more elegant way of automatically deriving T's class?
    String genericSuperclass = this.getClass().getGenericSuperclass().toString();

    String classNameOfT  = genericSuperclass.substring(
        genericSuperclass.indexOf('<') + 1,
        genericSuperclass.indexOf('>')
    );

    try {
      return (Class<T>) Class.forName(classNameOfT);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns a reference to the budget model this TableModel observers.
   * @return
   */
  protected final BudgetModel getBudgetModel() {
    return this.budgetModel;
  }

  /**
   * Registers this TableModel as an observer of <tt>budgetModel</tt>
   * and stores a reference to it.
   * @param budgetModel
   */
  protected final void setBudgetModel(BudgetModel budgetModel) {
    budgetModel.addObserver(this);
    this.budgetModel = budgetModel;
  }

  /**
   * Forces a table refresh whenever the BudgetModel this TableModel
   * observes sends an update.
   */
  @Override
  public void update(Observable arg0, Object arg1) {
    this.fireTableDataChanged();
  }
  
  @Override
  public final int getColumnCount() {
    return getColEnumClass().getEnumConstants().length;
  }
  
  /**
   * Returns the String of the column enumeration value 
   * at position <tt>colNum</tt>.
   */
  @Override
  public String getColumnName(int colNum) {
    return getColEnumClass().getEnumConstants()[colNum].toString();
  }
  
  /**
   * A convenience method that returns the set of 
   * enumeration values for the column enumeration.
   * @return
   */
  protected final T[] getColumnEnumValues() {
    return getColEnumClass().getEnumConstants();
  }
  
  /**
   * Returns the enumeration value of the columns enumeration
   * at position <tt>index</tt>.
   * @param index
   * @return
   */
  protected final T getColumnEnumValueAt(int index) {
    return getColEnumClass().getEnumConstants()[index];
    
  }
}
