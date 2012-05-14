package blacksmyth.personalfinancier.control.gui;

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
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.model.budget.CategorySummary;

public class CategoryPieChart extends JLabel implements IBudgetObserver {
  private BudgetModel budgetModel;
  
  public CategoryPieChart(BudgetModel model) {
    this.budgetModel = model;
    model.addObserver(this);
    
    this.setVerticalAlignment(CENTER);
    this.setHorizontalAlignment(CENTER);

    final JLabel chart = this;
    
    this.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        chart.validate();
        refreshIcon();
      }
    });
  }

  public void update(Observable observable, Object arg1) {
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

    for (CategorySummary category : model.getCategorySummaries()) {
      
      double summaryFigure = category.getBudgettedAmount().getTotal().doubleValue();
      
      double absSummaryFigure = Math.abs(summaryFigure);
      int percentageAsInteger = (int) Math.round(((absSummaryFigure / total) * 100));
      
      int percentageOfIncome = (int) Math.round(((absSummaryFigure / incomeTotal) * 100));

      com.googlecode.charts4j.Color sliceColor = summaryFigure < 0 ? redColor : blackColor;

      sliceColor = com.googlecode.charts4j.Color.newColor(
          sliceColor, 
          summaryFigure < 0 ? redOpacity : blackOpacity
      );

      Slice categorySlice = Slice.newSlice(
          percentageAsInteger, 
          sliceColor,
          category.getBudgetCategory() + ", " + String.valueOf(percentageOfIncome) + "%"
      );

      if (summaryFigure < 0) {
        redOpacity -= 20;
      } else {
        blackOpacity -= 20;
      }

      slices.add(categorySlice);
    }
    
    PieChart chart = GCharts.newPieChart(slices);
    // chart.setThreeD(true);
    chart.setTitle(
        "Budget Category as % of Total Income", 
        com.googlecode.charts4j.Color.BLACK, 
        16
    );
    
    chart.setTransparency(85);
    
    try {
      chart.setSize(
          this.getParent().getPreferredSize().width,
          this.getParent().getPreferredSize().height
      );
    } catch (Exception e) {
      chart.setSize(500,200);
    }
    
    return chart.toURLString();
  }

}
