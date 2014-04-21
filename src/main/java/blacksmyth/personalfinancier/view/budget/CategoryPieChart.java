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

import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;
import blacksmyth.personalfinancier.view.WidgetFactory;

import de.erichseifert.gral.data.DataTable;

final public class CategoryPieChart extends AbstractBudgetPieChart {
  
  public CategoryPieChart(BudgetModel model) {
    super(model);
  }

  @SuppressWarnings("unchecked")
  protected DataTable createPiePlotData() {

    DataTable categoryData = new DataTable(Double.class, Boolean.class, String.class);
    
    double total = 0;
    double incomeTotal = 0;
    
    for (CategorySummary category : getModel().getCategorySummaries()) {
      
      double summaryFigure = category.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      
      total = total + absSummaryFigure;
      if (summaryFigure > 0) {
        incomeTotal = incomeTotal + summaryFigure;
      }
    }
    
    for(CategorySummary summary : getModel().getCategorySummaries()) {
      
      double summaryFigure = summary.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      
      double percentageOfIncome = (absSummaryFigure / incomeTotal) * 100;
      
      categoryData.add(
          absSummaryFigure,  // negative values in PiePlot render as no slice
          summaryFigure >= 0 ? false : true,
          summary.getBudgetCategory() + ", " + WidgetFactory.DECIMAL_FORMAT.format(percentageOfIncome) + "%"
      );
    }
    
    return categoryData;
  }
  
  protected String createTitleText() {
    return "Budget Category as % of Total Income";
  }
}
