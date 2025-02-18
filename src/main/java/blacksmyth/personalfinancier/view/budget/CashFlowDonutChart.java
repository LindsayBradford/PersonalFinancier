package blacksmyth.personalfinancier.view.budget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.PieStyler.LabelType;


import blacksmyth.general.swing.Utilities;
import blacksmyth.personalfinancier.control.budget.IBudgetObserver;
import blacksmyth.personalfinancier.model.BigDecimalFactory;
import blacksmyth.personalfinancier.model.budget.AccountSummary;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.ViewPreferences;
import blacksmyth.personalfinancier.view.WidgetFactory;

@SuppressWarnings("serial")
public class CashFlowDonutChart extends JPanel implements IBudgetObserver  {
  
  private enum Cashflow {
    Positive,
    Neurtral,
    Negative
  }
  
  class DonutData {
    public String Title;
    public TreeMap<String,Double> Slices;
    public TreeMap<String,Cashflow> CashFlowDirection;

    public DonutData() {
      this.Title = "<Undefined>";
      this.Slices = new TreeMap<String,Double>();
      this.CashFlowDirection = new TreeMap<String,Cashflow>();
    }
  }
  
  private BudgetModel model;

  public CashFlowDonutChart(BudgetModel model) {
    super(new BorderLayout());

    this.setBackground(Color.BLACK);

    setModel(model);  
  }
  
  private void setModel(BudgetModel model) {
    this.model = model;
    model.addObserver(this);
  }
  
  private PieChart buildCashflowChart(DonutData data) {
    PieChart chart = new PieChartBuilder()
        .title(data.Title)
        .theme(Styler.ChartTheme.Matlab)
        .build();
    
    data.Slices.entrySet().stream()
      .forEach(slice -> chart.addSeries(slice.getKey(),slice.getValue())
    );
    
    applyDesiredStyle(chart.getStyler(), data);
 
    return chart; 
  }
  
  private void applyDesiredStyle(PieStyler styler, DonutData data) {
    styler.setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
    styler.setLegendVisible(false);
    styler.setChartPadding(5);

//  styler.setSliceBorderWidth(5);  // Bugged @ 3.8.8, phantom borders appear.
    
    styler.setPlotBorderVisible(false);
    styler.setPlotBackgroundColor(getBackground());
    styler.setChartBackgroundColor(getBackground());
    styler.setChartFontColor(deriveTitleColor(data));
    
    styler.setPlotContentSize(0.666);
    
    styler.setSeriesColors(
        deriveSeriesColors(data)
    );
    
    styler.setLabelType(LabelType.NameAndValue);
    styler.setLabelsVisible(true);
    styler.setForceAllLabelsVisible(true);
    styler.setLabelsDistance(1.2);
    styler.setLabelsFontColor(Color.WHITE);
    styler.setLabelsFontColorAutomaticEnabled(false);
    styler.setLabelsFont(getFont().deriveFont(Font.BOLD));
    
    styler.setClockwiseDirectionType(ClockwiseDirectionType.CLOCKWISE);
    styler.setStartAngleInDegrees(90);
    
    styler.setShowWithinAreaPoint(false);
    
    styler.setDecimalPattern("#,###,###,##0.00");
  }
  
  private Color deriveTitleColor(DonutData data) {
    return data.Title.contains("Surplus") ? 
        ViewPreferences.getInstance().getPreferredPositiveCashFlowColor(): 
        ViewPreferences.getInstance().getPreferredNegativeCashFlowColor();
  }

  private Color[] deriveSeriesColors(DonutData data) {
    LinkedList<Color> colors = new LinkedList<Color>();
    
    data.CashFlowDirection.sequencedEntrySet().stream().forEach(
        entry -> colors.add(deriveColorFromDirection(entry.getValue()))
    );

    return colors.toArray(Color[]::new);
  }
  
    private Color deriveColorFromDirection(Cashflow direction) {
    switch(direction) {
    case Negative:
      return ViewPreferences.getInstance().getPreferredNegativeCashFlowColor();
    case Positive:
      return ViewPreferences.getInstance().getPreferredPositiveCashFlowColor();
    default:
      return ViewPreferences.getInstance().getPreferredUnEditableCellColor();
    }
  }
  
  private DonutData createCashflowData() {
    DonutData data = new DonutData();

    data.Title = createTitleText();
    
    this.model.getCashFlowSummaries().stream()
      .filter(summary -> summary.getBudgettedAmount().getTotal().doubleValue() != 0)
      .forEach(
        summary -> { 
          double cashflow = summary.getBudgettedAmount().getTotal().doubleValue();
          data.Slices.put(
            summary.getAccountNickname(),
            Math.abs(cashflow)
          );
          data.CashFlowDirection.put(
              summary.getAccountNickname(), 
              deriveDirection(cashflow)
          );
        });
    
    return data;
  }
  
  private Cashflow deriveDirection(double amount) {
    if (amount < 0) {
      return Cashflow.Negative;
    }
    if (amount > 0) {
      return Cashflow.Positive;
    }

    return Cashflow.Neurtral;
  }
  
  protected String createTitleText() {
    BigDecimal netCashFlow = BigDecimalFactory.create(0);
    
    for (AccountSummary summary : this.model.getCashFlowSummaries()) {
      netCashFlow = netCashFlow.add(summary.getBudgettedAmount().getTotal());
    }

    String surplusOrDeficit = netCashFlow.doubleValue() >= 0 ? "Surplus" : "Deficit";
    return "Net Cash Flow " + surplusOrDeficit + ": " + WidgetFactory.DECIMAL_FORMAT.format(netCashFlow.doubleValue());
  }

  private void displayChart(PieChart accountCashflowChart) {
    this.removeAll(); 
    this.add(new XChartPanel<PieChart>(accountCashflowChart));
    Utilities.refreshLater(this);
  }

  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    displayChart(
        buildCashflowChart(
            createCashflowData()
        )
    );
  }

}
