/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier.view.budget;

import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.AbstractFinancierTableModel;

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
abstract class AbstractBudgetTableModel<T extends Enum<T>> extends AbstractFinancierTableModel<T>
    implements IBudgetObserver {

  private BudgetModel budgetModel;

  /**
   * Returns a reference to the budget model this TableModel observers.
   * 
   * @return
   */
  protected final BudgetModel getBudgetModel() {
    return this.budgetModel;
  }

  /**
   * Registers this TableModel as an observer of <tt>budgetModel</tt> and stores a
   * reference to it.
   * 
   * @param budgetModel
   */
  protected final void setBudgetModel(BudgetModel budgetModel) {
    budgetModel.addObserver(this);
    this.budgetModel = budgetModel;
  }
}
