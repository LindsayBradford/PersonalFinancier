package blacksmyth.personalfinancier.view.budget;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.PieStyler.LabelType;


import blacksmyth.general.swing.Utilities;
import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.ViewPreferences;

@SuppressWarnings("serial")
public class CategoryDonutChart extends JPanel implements IBudgetObserver  {
  
  class DonutData {
    public String Title;
    public TreeMap<String,Double> Slices;
    public Color BaseColor;
    
    public DonutData() {
      this.Title = "<Undefined>";
      this.Slices = new TreeMap<String,Double>();
      this.BaseColor = Color.WHITE;
    }
  }
  
  private BudgetModel model;

  public CategoryDonutChart(BudgetModel model) {
    super(new GridLayout(1,2));

    this.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.setBackground(Color.BLACK);

    setModel(model);  
  }
  
  private void setModel(BudgetModel model) {
    this.model = model;
    model.addObserver(this);
  }
  
  private PieChart buildCategoryChart(DonutData data) {
    PieChart chart = new PieChartBuilder()
        .title(data.Title)
        .theme(Styler.ChartTheme.Matlab)
        .build();
   
    data.Slices.entrySet().stream().forEach(
        slice -> chart.addSeries(slice.getKey(),slice.getValue())
    );
    
    applyDesiredStyle(chart.getStyler(), data);
 
    return chart; 
  }
  
  private void applyDesiredStyle(PieStyler styler, DonutData data) {
    styler.setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
    styler.setLegendVisible(false);
    styler.setChartPadding(5);

//  styler.setSliceBorderWidth(5);  // Bugged @ 3.8.8, phantom borders appear.
    
    styler.setSumVisible(true);
    
    styler.setPlotBorderVisible(false);
    styler.setPlotBackgroundColor(getBackground());
    styler.setChartBackgroundColor(getBackground());
    styler.setChartFontColor(Color.WHITE);
    
    styler.setPlotContentSize(0.666);
    
    styler.setSeriesColors(
        deriveSeriesColors(data)
    );
    
    styler.setLabelType(LabelType.NameAndPercentage);
    styler.setLabelsVisible(true);
    styler.setForceAllLabelsVisible(true);
    styler.setLabelsDistance(1.2);
    styler.setLabelsFontColor(Color.WHITE);
    styler.setLabelsFontColorAutomaticEnabled(false);
    styler.setLabelsFont(getFont().deriveFont(Font.BOLD));
    
    styler.setToolTipsEnabled(true);
    styler.setToolTipBackgroundColor(getBackground());
    styler.setToolTipBorderColor(Color.GRAY);
    styler.setToolTipHighlightColor(
        ViewPreferences.getInstance().getPreferredEditableCellColor()
    );
    
    styler.setClockwiseDirectionType(ClockwiseDirectionType.CLOCKWISE);
    styler.setStartAngleInDegrees(90);
    
    styler.setShowWithinAreaPoint(false);
    
    styler.setDecimalPattern("#,###,###,##0.00");
  }

  private Color[] deriveSeriesColors(DonutData data) {
    Color current = data.BaseColor;

    Color[] colors = new Color[data.Slices.size()];
    for(int counter = colors.length - 1; counter >= 0; counter--) {
      colors[counter] = current;
      current = current.darker().darker().darker();
    }

    return colors;
  }
  
  private DonutData createIncomeData() {
    DonutData data = new DonutData();
    data.Title = "Income Categories";
    data.BaseColor = Utilities.POSITIVE_CASH_COLOUR;

    this.model.getIncomeCategorySummaries().stream().forEach(
        summary -> {
            data.Slices.put(
              summary.getBudgetCategory(), 
              summary.getBudgettedAmount().getTotal().doubleValue()
            );
        }
    );

    return data;
  }

  
  private DonutData createExpenseData() {
    DonutData data = new DonutData();

    data.Title = "Expense Categories";
    data.BaseColor = Utilities.NEGATIVE_CASH_COLOUR;
    
    this.model.getExpenseCategorySummaries().stream().forEach(
        summary -> { 
          data.Slices.put(
            summary.getBudgetCategory(), 
            summary.getBudgettedAmount().getTotal().doubleValue() * -1
          );
        }   
    );

    return data;
  }

  private void displayCharts(PieChart incomeChart, PieChart expenseChart) {
    this.removeAll(); 
    this.add(new XChartPanel<PieChart>(incomeChart));
    this.add(new XChartPanel<PieChart>(expenseChart));
    Utilities.refreshLater(this);
  }

  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    displayCharts(
        buildCategoryChart(
            createIncomeData()
        ),
        buildCategoryChart(
            createExpenseData()
        )
    );
  }

}
