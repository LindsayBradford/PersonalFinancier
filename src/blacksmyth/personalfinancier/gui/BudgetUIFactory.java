/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

class BudgetUIFactory {
  
  // TODO: making these static forces a model of only 1 budget.  Loosen up so we can copy budgets 
  private static final BudgetModel budgetModel = new BudgetModel(new AccountModel());
  private static final BudgetDetailTable budgetDetailTable = new BudgetDetailTable(budgetModel);
  
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
    
    panel.add(createBudgetItemToolbar(), BorderLayout.PAGE_START);
    
    panel.add(
        new JScrollPane(
            budgetDetailTable
        ),
        BorderLayout.CENTER
    );
    
    return panel;    
  }
  
  // TODO: eye-bending JButtons.  Make it more accessible to mortals.
  // TODO: replace JButton text with an embeeded images from a resource archive.
  
  @SuppressWarnings("serial")
  private static JToolBar createBudgetItemToolbar() {
    JToolBar toolbar = new JToolBar();
    
    toolbar.add(new JButton() {
      { 
        this.setText(" + ");
        this.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            budgetModel.addBudgetItem();
          }
        });
      }
    });

    // TODO: enable remove button only whenever a row is selected.
    toolbar.add(new JButton() {
      { 
        this.setText(" - ");
        this.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            int selectedRow = budgetDetailTable.getSelectedRow();
            if (selectedRow > -1) {
              budgetModel.removeBudgetItem(selectedRow);
            }
            // TODO: make selection stay as near as deleted row as possible.
          }
        });
      }
    });
    
    return toolbar;
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