/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import blacksmyth.general.ReflectionUtilities;

/**
 * An abstract TableModel using generics for collecting all common behaviours of
 * the Budget TableModels. An enumeration representing the columns of the table
 * model is required as part of the class definition.
 * 
 * @author linds
 *
 * @param <T>
 */

@SuppressWarnings("serial")
public abstract class AbstractFinancierTableModel<T extends Enum<T>> extends AbstractTableModel implements Observer {

  /**
   * Derives the Class of the columns enumeration supplied as the Gemeric type at
   * subclass definition time.
   * 
   * @return The Class of the columns enumeration.
   */
  @SuppressWarnings("unchecked")
  private final Class<T> getColEnumClass() {
    return (Class<T>) ReflectionUtilities.getParamatisedTypeOfGenericClass(this.getClass());
  }

  /**
   * Forces a table refresh whenever the BudgetModel this TableModel observes
   * sends an update.
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
   * Returns the String of the column enumeration value at position
   * <tt>colNum</tt>.
   */
  @Override
  public String getColumnName(int colNum) {
    return getColEnumClass().getEnumConstants()[colNum].toString();
  }

  /**
   * A convenience method that returns the set of enumeration values for the
   * column enumeration.
   * 
   * @return
   */
  protected final T[] getColumnEnumValues() {
    return getColEnumClass().getEnumConstants();
  }

  /**
   * Returns the enumeration value of the columns enumeration at position
   * <tt>index</tt>.
   * 
   * @param index
   * @return
   */
  protected final T getColumnEnumValueAt(int index) {
    return getColEnumClass().getEnumConstants()[index];
  }
}
