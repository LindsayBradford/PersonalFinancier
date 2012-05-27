package blacksmyth.personalfinancier.control.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Shape;
import java.math.BigDecimal;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.plots.PiePlot;
import de.erichseifert.gral.plots.PiePlot.PieSliceRenderer;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Location;

public class CategoryPieChart extends JPanel implements IBudgetObserver {
  
  private static final int IS_NEGATIVE_COLUMN = 1;
  private static final int LABEL_COLUMN = 2;
  
  BudgetModel model;
  
  public CategoryPieChart(BudgetModel model) {
    super(new BorderLayout());
    
    this.setBorder(new EmptyBorder(5,5,5,5));
    this.setBackground(Color.WHITE);
    
    setModel(model);
  }
  
  private void setModel(BudgetModel model) {
    this.model = model;
    model.addObserver(this);
  }
  
  @Override
  public void update(Observable arg0, Object arg1) {
    refreshSelf();
  }
  
  @SuppressWarnings("unchecked")
  private void refreshSelf() {

    DataTable categoryData = new DataTable(Double.class, Boolean.class, String.class);
    
    double total = 0;
    double incomeTotal = 0;
    
    for (CategorySummary category : model.getCategorySummaries()) {
      
      double summaryFigure = category.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      
      total = total + absSummaryFigure;
      if (summaryFigure > 0) {
        incomeTotal = incomeTotal + summaryFigure;
      }
    }
    
    for(CategorySummary summary : model.getCategorySummaries()) {
      
      double summaryFigure = summary.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      
      double percentageOfIncome = (absSummaryFigure / incomeTotal) * 100;
      
      categoryData.add(
          absSummaryFigure,  // negative values in PiePlot render as no slice
          summaryFigure >= 0 ? false : true,
          summary.getBudgetCategory() + ", " + WidgetFactory.DECIMAL_FORMAT.format(percentageOfIncome) + "%"
      );
    }
    
    displayPiePlot(
        createPiePlot(
            categoryData,
            getTitleText()
         )
     );
  }
  
  private String getTitleText() {
    return "Budget Category as % of Total Income";
  }
  
  private void displayPiePlot(PiePlot plot) {
    this.removeAll();  // destroy old PiePlot

    this.add(
        new InteractivePanel(plot), 
        BorderLayout.CENTER
    );
    
    this.validate();  // need to force a redraw
  }
  
  private PiePlot createPiePlot(DataTable cashFlowData, String title) {
    PiePlot plot = new PiePlot(cashFlowData);
    
    plot.setSetting(PiePlot.TITLE, title);
    
    plot.getPlotArea().setSetting(PlotArea.BORDER,null);

    plot.setInsets(new Insets2D.Double(1.0, 1.0, 1.0, 1.0));
    
    PieSliceRenderer positivePointRenderer = new PiePlot.PieSliceRenderer(plot);
    
    configurePieSliceRenderer(positivePointRenderer, Color.BLACK);
    
    PieSliceRenderer negativePointRenderer = new PiePlot.PieSliceRenderer(plot);

    configurePieSliceRenderer(negativePointRenderer, Color.RED);

    plot.setPointRenderer(
        cashFlowData, 
        new DualSliceRenderer(
            plot,
            positivePointRenderer,
            negativePointRenderer
        )
    );
    
    return plot;
  }
  
  /**
   * Utility function that ensures a number of related PieSliceRenderer
   * instances are configured to differ only in rendering a single colour.
   * @param renderer
   * @param color
   */
  private void configurePieSliceRenderer(PieSliceRenderer renderer, Color color) {
    renderer.setSetting(PieSliceRenderer.GAP, 0.1);

    renderer.setSetting(
        PointRenderer.COLOR,
        new LinearGradientPaint(
            0f,0f, 0f,1f,
            new float[] { 0.0f, 1.0f },
            new Color[] { color, Color.GRAY }
        )
    );

    renderer.setSetting(PointRenderer.VALUE_DISPLAYED, true);
    renderer.setSetting(PointRenderer.VALUE_LOCATION, Location.NORTH);
    
    renderer.setSetting(PointRenderer.VALUE_COLUMN, LABEL_COLUMN);
    renderer.setSetting(PointRenderer.VALUE_COLOR, color);
  }
  
  /**
   * A PieSliceRenderer that delegates rendering to a positive or a
   * negative PieSliceRenderer based on whether the data point to 
   * render is a positive or negative number. 
   * @author linds
   *
   */
  private class DualSliceRenderer extends PieSliceRenderer {
    
    private PiePlot plot;

    private PieSliceRenderer positiveSliceRenderer;
    private PieSliceRenderer negativeSliceRenderer;

    public DualSliceRenderer(PiePlot plot, PieSliceRenderer positiveSliceRenderer, 
                                           PieSliceRenderer negativeSliceRenderer) {
      super(plot);
      this.plot = plot;
      this.positiveSliceRenderer = positiveSliceRenderer;
      this.negativeSliceRenderer = negativeSliceRenderer;
    }

    @Override
    public Drawable getPoint(PointData data, Shape shape) {
      if (valueIsNegative(data)) {
        return negativeSliceRenderer.getPoint(data, shape);
      }
      return positiveSliceRenderer.getPoint(data, shape);
    }
    
    @Override 
    public Shape getPointShape(PointData data) {
      if (valueIsNegative(data)) {
        return negativeSliceRenderer.getPointShape(data);
      }
      return positiveSliceRenderer.getPointShape(data);
    }
    
    /**
     * Interrogates the current plot's data value
     * for the given <tt>data</tt> entry.  If the 
     * value is negative, returns <tt>true</tt>.
     * @param data
     * @return
     */
    private boolean valueIsNegative(PointData data) {
      return (Boolean) plot.getData().get(0).get(
          IS_NEGATIVE_COLUMN, 
          data.row.getIndex()
      );
    }
  }
}
