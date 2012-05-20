package blacksmyth.personalfinancier.control.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

import blacksmyth.personalfinancier.control.IBudgetObserver;
import blacksmyth.personalfinancier.model.budget.AccountSummary;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class CashFlowPieChart extends JLabel implements IBudgetObserver, Runnable {
  private BudgetModel budgetModel;
  
  public CashFlowPieChart(BudgetModel model) {
    this.budgetModel = model;
    model.addObserver(this);
    
    this.setVerticalAlignment(CENTER);
    this.setHorizontalAlignment(CENTER);

    final CashFlowPieChart chart = this;
    
    this.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        RunnableQueueThread.getInstance().push(chart);
      }
    });
  }

  public void update(Observable observable, Object arg1) {
    RunnableQueueThread.getInstance().push(this);
  }
  
  public void run() {
    refreshIcon();
  }
  
  private void refreshIcon() {
    
    try {
      ImageIcon graphIcon = new ImageIcon(
          ImageIO.read(
              buildGraphURL(budgetModel)
          )
      );
      this.setIcon(graphIcon);
    } catch (IOException e) {
      e.printStackTrace();
      this.setIcon(null);
    }
    
  }
  
  private URL buildGraphURL(BudgetModel model) {
    try {
      return new URL(
          buildGraphURLString(model)
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private String buildGraphURLString(BudgetModel model) {
    ArrayList<Slice> slices = new ArrayList<Slice>();
    
    int blackOpacity = 100;
    int redOpacity = 100;
    
    com.googlecode.charts4j.Color blackColor = com.googlecode.charts4j.Color.BLACK;
    com.googlecode.charts4j.Color redColor = com.googlecode.charts4j.Color.RED;
    
    double netCashFlow = 0;
    double total = 0;
    double incomeTotal = 0;
    
    double minValue = 0;
    double maxValue = 0;
    
    for (AccountSummary account : model.getAccountSummaries()) {
      
      double summaryFigure = account.getBudgettedAmount().getTotal().doubleValue();
      
      if (summaryFigure < minValue) {
        minValue = summaryFigure;
      }
      
      if (summaryFigure > maxValue) {
        maxValue = summaryFigure;
      }
      
      netCashFlow = netCashFlow + summaryFigure;
      
      double absSummaryFigure = Math.abs(summaryFigure);
      
      total = total + absSummaryFigure;
      if (summaryFigure > 0) {
        incomeTotal = incomeTotal + summaryFigure;
      }
    }
    
    for (AccountSummary account : model.getAccountSummaries()) {
      
      double summaryFigure = account.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      int percentageAsInteger = (int) Math.round(((absSummaryFigure / total) * 100));
      
      com.googlecode.charts4j.Color sliceColor = summaryFigure < 0 ? redColor : blackColor;

      sliceColor = com.googlecode.charts4j.Color.newColor(
          sliceColor, 
          summaryFigure < 0 ? redOpacity : blackOpacity
      );

      Slice accountSlice = Slice.newSlice(
          percentageAsInteger, 
          sliceColor,
          account.getAccountNickname() + ", " + WidgetFactory.DECIMAL_FORMAT.format(summaryFigure)
      );

      if (summaryFigure < 0) {
        redOpacity -= 20;
      } else {
        blackOpacity -= 20;
      }

      slices.add(accountSlice);
    }
    
    String surplusOrDeficit = netCashFlow >= 0 ? "Surplus" : "Deficit";
    com.googlecode.charts4j.Color surplusOrDeficitColor = netCashFlow >= 0 ? com.googlecode.charts4j.Color.BLACK : com.googlecode.charts4j.Color.RED;
    
    PieChart chart = GCharts.newPieChart(slices);
    chart.setTitle(
        "Net Cash Flow " + surplusOrDeficit + ": " + WidgetFactory.DECIMAL_FORMAT.format(netCashFlow), 
        surplusOrDeficitColor, 
        16
    );
    
    chart.setTransparency(85);

    try {
      chart.setSize(
          this.getPreferredSize().width,
          this.getPreferredSize().height
      );
    } catch (Exception e) {
      chart.setSize(500,200);
    }
    
    return chart.toURLString();
  }
  
  @Override
  public Dimension getPreferredSize() {
    Dimension theSize = this.getParent().getSize();
    theSize.width = Math.max(theSize.width - 5, 500);
    theSize.height = Math.max(theSize.height - 5, 200);
    
    return theSize;
  }


}
