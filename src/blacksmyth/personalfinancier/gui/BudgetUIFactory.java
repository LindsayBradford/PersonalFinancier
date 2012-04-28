/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

class BudgetUIFactory {
  private static BudgetModel budgetModel = new BudgetModel(new AccountModel());
  
  public static JComponent createBudgetComponent() {
    
    JPanel budgetPanel = new JPanel(new BorderLayout());
    
    budgetPanel.add(
        createBudgetItemPanel(), 
        BorderLayout.CENTER
    );

    budgetPanel.add(
        createBudgetSummaryPanel(), 
        BorderLayout.PAGE_END
    );

    return budgetPanel;
  }
  
  private static JComponent createBudgetItemPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    panel.setBorder(new TitledBorder("Budget Items"));
    
    panel.add(
        new JScrollPane(
            new BudgetDetailTable(budgetModel)
        ),
        BorderLayout.CENTER
    );
    
    return panel;    
  }
  
  private static JComponent createBudgetSummaryPanel() {
    JPanel panel  = new JPanel(new BorderLayout());
    
    panel.setBorder(new TitledBorder("Budget Summary"));
    
    panel.add(
        new JScrollPane(
            new BudgetAccountSummaryTable(budgetModel)
        ),
        BorderLayout.LINE_END
    );
    
    return panel;    
  }
}