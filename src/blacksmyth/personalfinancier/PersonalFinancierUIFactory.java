/**
 * PersonalFinancier by Lindsay Bradford is licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *
 * Year: 2012 
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import blacksmyth.general.FontIconProvider;
import blacksmyth.general.SwingUtilities;
import blacksmyth.personalfinancier.control.BudgetFileController;
import blacksmyth.personalfinancier.control.BudgetUndoManager;
import blacksmyth.personalfinancier.control.gui.RunnableQueueThread;
import blacksmyth.personalfinancier.model.budget.BudgetModel;

public class PersonalFinancierUIFactory {
  
  
  public static JFrame createJFrame() {
    UIComponents.budgetModel = new BudgetModel();
    
    RunnableQueueThread.getInstance().start();
    
    UIComponents.windowFrame  = new JFrame("Personal Financier");
    
    UIComponents.windowFrame.setDefaultCloseOperation(
        WindowConstants.EXIT_ON_CLOSE
    );
    
    Toolkit.getDefaultToolkit().setDynamicLayout(false);
    
    UIComponents.windowFrame.setJMenuBar(
        createMenu()
    );
    
    UIComponents.windowFrame.getContentPane().add(
        createMainToolbar(),
        BorderLayout.PAGE_START
    );
    
    UIComponents.windowFrame.getContentPane().add(
        createContentPane(), 
        BorderLayout.CENTER
    );
    
    UIComponents.windowFrame.getContentPane().add(
      createMessageBar(),
      BorderLayout.PAGE_END
    );
   
    UIComponents.windowFrame.setBounds(
        Toolkit.getDefaultToolkit().getScreenSize().width/8, 
        Toolkit.getDefaultToolkit().getScreenSize().height/8, 
        Toolkit.getDefaultToolkit().getScreenSize().width/4*3, 
        Toolkit.getDefaultToolkit().getScreenSize().height/4*3
    );
    
    return UIComponents.windowFrame;
  }
  
  private static Component createMainToolbar() {
    JToolBar toolbar = new JToolBar();
    
    UIComponents.budgetFileController = new BudgetFileController(UIComponents.budgetModel);
    
    toolbar.add(
        createLoadButton()
    );

    toolbar.add(
        createSaveButton()    
    );

    toolbar.addSeparator();

    toolbar.add(
        createPreferencesButton()
    );

    toolbar.addSeparator();
    
    toolbar.add(
        createAboutButton(UIComponents.windowFrame)
    );
    
    return toolbar;
  }
  
  private static JButton createAboutButton(final JFrame baseFrame) {
    JButton button = new JButton();
    
    button.setForeground(Color.GRAY);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_info_sign
    );
    
    button.setToolTipText(" About ");
    
    button.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(
                baseFrame,
                "<html><body>" +
                "This application depends on unmodified binary distributions of the following libraries:" + 
                 "<ul>" +
                 "<li> GSson (http://code.google.com/p/google-gson/) for Java JSon serialisation" +
                 "<li> JTattoo (http://www.jtattoo.net/) for a radically different Swing Look & Feel" +
                 "<li> FontAwesome (http://fortawesome.github.com/Font-Awesome) for icon-friendly font glyphs" +
                 "<li> GRAL (http://trac.erichseifert.de/gral/) for charting." +
                 "</ul>" +
                 "Many thanks for their excellent libraries." +
                "</body></html>"
            );
          }
        }
    );
    return button;
  }

  private static JButton createPreferencesButton() {
    JButton button = new JButton();

    button.setForeground(Color.GRAY);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_cogs
    );
    
    button.setToolTipText(" Preferences ");
    return button;
  }

  private static JButton createSaveButton() {
    
    JButton button = new JButton(UIComponents.SaveBudgetAction);

    button.setForeground(Color.GREEN.darker());

    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_S, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveBudgetAction
    );
    
    button.setMnemonic(KeyEvent.VK_S);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_save
    );

    button.setToolTipText(" Save the current budget ");
    return button;
  }

  private static JButton createLoadButton() {
    
    JButton button = new JButton(
        UIComponents.LoadBudgetAction
    );
    
    button.setMnemonic(KeyEvent.VK_O);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_download_alt
    );
    
    button.setForeground(Color.GREEN.darker());
    
    button.setToolTipText(" Load a budget ");
    
    return button;
  }

  private static JComponent createContentPane() {
    JTabbedPane pane = new JTabbedPane();
    pane.setBorder(new EmptyBorder(5,5,5,5));
    
    pane.addTab(
        "Budget", 
        BudgetUIFactory.createBudgetComponent()
    );
    
    pane.addTab(
        "Ledgers", 
        LedgersUIFactory.createLedgersComponent()
    );
    
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

    fileMenu.add(
        createLoadMenuItem()
    );

    fileMenu.add(
        createSaveMenuItem()
    );

    fileMenu.add(
        createSaveAsMenuItem()
    );
    
    fileMenu.addSeparator();
    
    fileMenu.add(
        new JMenuItem("Preferences")
    );

    fileMenu.addSeparator();

    fileMenu.add(
        createExitMenuItem()
    );
    
    menuBar.add(fileMenu);
  
    return menuBar;
  }
  
  private static JMenuItem createLoadMenuItem() {
    UIComponents.LoadBudgetAction = new AbstractAction("Open...") {
      
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        UIComponents.budgetFileController.load(
            UIComponents.windowFrame
        );
      }
    };
    
   JMenuItem menuItem = new JMenuItem(
          UIComponents.LoadBudgetAction    
   );

    SwingUtilities.bindKeyStrokeToAction(
        menuItem, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_O, 
            Event.CTRL_MASK
        ), 
        UIComponents.LoadBudgetAction
    );

    return menuItem;
  }
  
  private static JMenuItem createSaveMenuItem() {
    
    UIComponents.SaveBudgetAction = new AbstractAction("Save") {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        UIComponents.budgetFileController.save(
            UIComponents.windowFrame
        );
      }
    };

    JMenuItem menuItem = new JMenuItem(
        UIComponents.SaveBudgetAction    
    );

    SwingUtilities.bindKeyStrokeToAction(
        menuItem, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_S, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveBudgetAction
    );

    return menuItem;
  }
  
  private static JMenuItem createSaveAsMenuItem() {
    UIComponents.SaveAsBudgetAction = new AbstractAction("Save As...") {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        UIComponents.budgetFileController.saveAs(
            UIComponents.windowFrame
        );
      }
    };
    
    JMenuItem menuItem = new JMenuItem(
        UIComponents.SaveAsBudgetAction    
    );

    SwingUtilities.bindKeyStrokeToAction(
        menuItem, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_A, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveAsBudgetAction
    );

    return menuItem;
  }
  
  private static JMenuItem createExitMenuItem() {
    UIComponents.ExitAction = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        UIComponents.budgetFileController.save(
            UIComponents.windowFrame
        );
        System.exit(0);
      }
    };
    
    JMenuItem menuItem = new JMenuItem(
        UIComponents.ExitAction    
    );

    SwingUtilities.bindKeyStrokeToAction(
        menuItem, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_X, 
            Event.CTRL_MASK
        ), 
        UIComponents.ExitAction
    );

    return menuItem;
  }
}

class LedgersUIFactory {
  public static JComponent createLedgersComponent() {
    return new JLabel("Ledgers Component");
  }
}
