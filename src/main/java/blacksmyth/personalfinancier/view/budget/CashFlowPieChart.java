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

import java.math.BigDecimal;

import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.budget.AccountSummary;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.WidgetFactory;
import de.erichseifert.gral.data.DataTable;

@SuppressWarnings("serial")
final public class CashFlowPieChart extends AbstractBudgetPieChart {

  public CashFlowPieChart(BudgetModel model) {
    super(model);
  }

  @SuppressWarnings("unchecked")
  protected DataTable createPiePlotData() {
    DataTable accountCashFlows = new DataTable(Double.class, Boolean.class, String.class);

    for (AccountSummary summary : getModel().getCashFlowSummaries()) {

      double cashFlow = summary.getBudgettedAmount().getTotal().doubleValue();

      accountCashFlows.add(Math.abs(cashFlow), // negative values in PiePlot render as no slice
          cashFlow >= 0 ? false : true,
          summary.getAccountNickname() + ", " + WidgetFactory.DECIMAL_FORMAT.format(cashFlow));
    }
    return accountCashFlows;
  }

  protected String createTitleText() {
    BigDecimal netCashFlow = BigDecimalFactory.create(0);

    for (AccountSummary summary : getModel().getCashFlowSummaries()) {
      netCashFlow = netCashFlow.add(summary.getBudgettedAmount().getTotal());
    }

    String surplusOrDeficit = netCashFlow.doubleValue() >= 0 ? "Surplus" : "Deficit";
    return "Net Cash Flow " + surplusOrDeficit + ": " + WidgetFactory.DECIMAL_FORMAT.format(netCashFlow.doubleValue());
  }

}
