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

class BudgetUIFactory {
  
  public static JComponent createBudgetComponent() {
    JPanel budgetPanel = new JPanel(new BorderLayout());
    
    budgetPanel.add(
        createBudgetItemPanel(), 
        BorderLayout.CENTER
    );

    budgetPanel.add(
        createBudgetSummariesPanel(), 
        BorderLayout.PAGE_END
    );

    return budgetPanel;
  }
  
  private static JComponent createBudgetItemPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    panel.setBorder(new TitledBorder("Budget Items"));
    
    panel.add(
        new JScrollPane(
            new BudgetDetailTable()
        ),
        BorderLayout.CENTER
    );
    
    return panel;    
  }

  private static JComponent createBudgetSummariesPanel() {
    JPanel panel = new JPanel(new GridLayout(0,2));
    
    panel.add(
        createBudgetedAccountExpensesPanel()
    ); 

    panel.add(
        createSalaryDistributionPanel()
    ); 

    return panel;    
  }
  
  private static JComponent createBudgetedAccountExpensesPanel() {
    JPanel panel  = new JPanel(new BorderLayout());
    
    panel.setBorder(new TitledBorder("Budgeted Account Expenses"));
    panel.add(new JLabel("Budgeted Account Expenses"));
    
    return panel;    
  }

  private static JComponent createSalaryDistributionPanel() {
    JPanel panel  = new JPanel(new BorderLayout());
    
    panel.setBorder(new TitledBorder("Salary Distribution"));
    
    panel.add(new JLabel("Salary Distribution"));
    
    return panel;    
  }
}