package blacksmyth.personalfinancier.control.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Shape;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.plots.PiePlot;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.PiePlot.PieSliceRenderer;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Location;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

/**
 * An abstract Pie Chart that summarises and displays budget model data.
 * subclasses need only implement the abstract methods
 * {@see #createTitleText()} and {@see #createPiePlotData()} to get a 
 * completely functional Budget Pie Chart.  
 * @author linds
 *
 */
public abstract class AbstractBudgetPieChart extends JPanel implements IBudgetObserver {
  
  private static final int IS_NEGATIVE_COLUMN = 1;
  private static final int LABEL_COLUMN = 2;
  
  private BudgetModel model;

  public AbstractBudgetPieChart(BudgetModel model) {
    super(new BorderLayout());
    
    this.setBorder(new EmptyBorder(5,5,5,5));
    this.setBackground(Color.WHITE);
    
    setModel(model);
  }
  
  private void setModel(BudgetModel model) {
    this.model = model;
    model.addObserver(this);
  }
  
  protected BudgetModel getModel() {
    return this.model;
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    displayPiePlot(
        createPiePlot(
            createTitleText(),
            createPiePlotData()
         )
     );
  }
  
  abstract protected String createTitleText();
  
  abstract protected DataTable createPiePlotData();
 
  protected void displayPiePlot(PiePlot plot) {
    this.removeAll();  // destroy old PiePlot

    this.add(
        new InteractivePanel(plot), 
        BorderLayout.CENTER
    );
    
    this.validate();  // need to force a redraw
  }
  
  private PiePlot createPiePlot(String title, DataTable cashFlowData) {
    PiePlot plot = new PiePlot(cashFlowData);
    
    configurePiePlot(plot, title);
    
    plot.setPointRenderer(
        cashFlowData, 
        new DualSliceRenderer(
            plot,
            Color.BLACK,  // positive render colour
            Color.RED     // negative render colour
        )
    );
    
    return plot;
  }
  
  private void configurePiePlot(PiePlot plot, String title) {
    plot.setSetting(PiePlot.TITLE, title);
    
    plot.getPlotArea().setSetting(PlotArea.BORDER,null);

    plot.setInsets(new Insets2D.Double(1.0, 1.0, 1.0, 1.0));
    
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

    public DualSliceRenderer(PiePlot plot, Color positiveRenderColor, 
                                           Color negativeRenderColor) {
      super(plot);
      this.plot = plot;
      this.positiveSliceRenderer = createPieSliceRenderer(positiveRenderColor);
      this.negativeSliceRenderer = createPieSliceRenderer(negativeRenderColor);
    }
    
    /**
     * Utility function that creates a number of PieSliceRendeer instances
     * configured to differ only in rendering a single colour.
     * @param renderer
     * @param color
     */
    private PieSliceRenderer createPieSliceRenderer(Color color) {
      PieSliceRenderer renderer = new PieSliceRenderer(plot);
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
      
      return renderer;
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
