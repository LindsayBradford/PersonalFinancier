/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import blacksmyth.general.ResourceBridge;
import blacksmyth.personalfinancier.control.BudgetFileController;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class PersonalFinancierUIFactory {
  
  public static JFrame createJFrame() {
    
    final BudgetModel  budgetModel = new BudgetModel();
    
    JFrame frame = new JFrame("Personal Financier");
    
    frame.setDefaultCloseOperation(
        WindowConstants.EXIT_ON_CLOSE
    );
    
    frame.setJMenuBar(
        createMenu()
    );
    
    frame.getContentPane().add(
        getMainToolbar(budgetModel),
        BorderLayout.PAGE_START
    );
    
    frame.getContentPane().add(
        createContentPane(budgetModel), 
        BorderLayout.CENTER
    );
    
    frame.getContentPane().add(
      createMessageBar(),
      BorderLayout.PAGE_END
    );
   
    frame.setBounds(
        Toolkit.getDefaultToolkit().getScreenSize().width/8, 
        Toolkit.getDefaultToolkit().getScreenSize().height/8, 
        Toolkit.getDefaultToolkit().getScreenSize().width/4*3, 
        Toolkit.getDefaultToolkit().getScreenSize().height/4*3
    );
    
    return frame;
  }
  
  
  private static Component getMainToolbar(BudgetModel model) {
    JToolBar toolbar = new JToolBar();
    
    final BudgetFileController fileController = new BudgetFileController(model);
    
    JButton loadButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        loadButton, 
        FontIconProvider.icon_download_alt
    );
    
    loadButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            fileController.load();
          }
        }
    );
    
    loadButton.setForeground(Color.GREEN.darker());
    
    loadButton.setToolTipText(
        " Load a budget "
    );

    toolbar.add(loadButton);

    JButton saveButton = new JButton();

    saveButton.setForeground(Color.GREEN.darker());

    FontIconProvider.getInstance().configureButton(
        saveButton, 
        FontIconProvider.icon_upload_alt
    );

    
    saveButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            fileController.save();
          }
        }
    );

    saveButton.setToolTipText(
        " Save the budget "
    );

    toolbar.add(saveButton);

    JButton configButton = new JButton();
    
    FontIconProvider.getInstance().configureButton(
        configButton, 
        FontIconProvider.icon_cogs
    );
    
    configButton.setToolTipText(
        " Preferences "
    );

    toolbar.addSeparator();
    
    toolbar.add(configButton);
    
    return toolbar;
  }


  private static JComponent createContentPane(BudgetModel model) {
    JTabbedPane pane = new JTabbedPane();
    pane.setBorder(new EmptyBorder(5,5,5,5));
    
    pane.addTab(
        "Budget", 
        BudgetUIFactory.createBudgetComponent(model)
    );
    pane.addTab("Ledgers", LedgersUIFactory.createLedgersComponent());
    
    return pane;
  }
  
  private static JComponent createMessageBar() {
    
    JPanel messagePanel = new JPanel(new BorderLayout());
    messagePanel.setBorder(new EmptyBorder(2,5,5,5));
    
    JLabel messageLabel = new JLabel("test");
   
    messageLabel.setBorder(
       new CompoundBorder(
           new BevelBorder(BevelBorder.LOWERED),
           new EmptyBorder(2,5,5,5)
       )
    );
   
    messagePanel.add(messageLabel, BorderLayout.CENTER);
  
    return messagePanel;
  }

  private static JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();
  
    JMenu fileMenu = new JMenu("File");
    fileMenu.add(new JMenuItem("Preferences"));
  
    menuBar.add(fileMenu);
  
    return menuBar;
  }
}

class LedgersUIFactory {
  public static JComponent createLedgersComponent() {
    return new JLabel("Ledgers Component");
  }
}
