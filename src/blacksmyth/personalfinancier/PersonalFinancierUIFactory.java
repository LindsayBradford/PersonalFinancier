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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        createMainToolbar(budgetModel),
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
  
  private static Component createMainToolbar(BudgetModel model) {
    JToolBar toolbar = new JToolBar();
    
    final BudgetFileController fileController = new BudgetFileController(model);
    
    toolbar.add(
        createLoadButton(fileController)
    );

    toolbar.add(
        createSaveButton(fileController)    
    );

    toolbar.addSeparator();

    toolbar.add(
        createPreferencesButton()
    );

    toolbar.addSeparator();
    
    toolbar.add(
        createAboutButton()
    );
    
    return toolbar;
  }
  
  private static JButton createAboutButton() {
    JButton button = new JButton();
    
    button.setForeground(Color.GRAY);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_info_sign
    );
    
    button.setToolTipText(" About ");
    
    // TODO: clean this hackjob up:
    button.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JDialog dialog = new JDialog();
            
            JLabel infoLabel = new JLabel(
                "<html><body>" +
                "This application depends on the following packages:" + 
                 "<ul>" +
                 "<li> GSson (http://code.google.com/p/google-gson/) for Java JSon serialisation" +
                 "<li> JTattoo (http://www.jtattoo.net/) for a radically different Swing Look & Feel" +
                 "<li> FontAwesome (http://fortawesome.github.com/Font-Awesome) for icon-friendly font glyphs" +
                "</ul>" + 
                "</body></html>"
            );

            infoLabel.setForeground(Color.BLACK);
            
            dialog.setTitle("Info");
            dialog.setContentPane(infoLabel);
            
            dialog.setSize(
                dialog.getPreferredSize()
            );
            
            dialog.setBounds(
                Toolkit.getDefaultToolkit().getScreenSize().width/4, 
                Toolkit.getDefaultToolkit().getScreenSize().height/4, 
                Toolkit.getDefaultToolkit().getScreenSize().width/2, 
                (int) dialog.getPreferredSize().getHeight() + 20
            );
            
            dialog.setResizable(false);
            
            dialog.setVisible(true);
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

  private static JButton createSaveButton(final BudgetFileController controller) {
    AbstractAction saveAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        controller.save();
      }
    };
    
    JButton button = new JButton(saveAction);

    button.setForeground(Color.GREEN.darker());

    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_S, 
            Event.CTRL_MASK
        ), 
        saveAction
    );
    
    button.setMnemonic(KeyEvent.VK_S);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_upload_alt
    );

    button.setToolTipText(" Save the budget ");
    return button;
  }

  private static JButton createLoadButton(final BudgetFileController controller) {
    AbstractAction loadAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        BudgetUndoManager.getInstance().discardAllEdits();
        controller.load();
      }
    };
    
    JButton button = new JButton(loadAction);
    
    SwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_O, 
            Event.CTRL_MASK
        ), 
        loadAction
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

  private static JComponent createContentPane(BudgetModel model) {
    JTabbedPane pane = new JTabbedPane();
    pane.setBorder(new EmptyBorder(5,5,5,5));
    
    pane.addTab(
        "Budget", 
        BudgetUIFactory.createBudgetComponent(model)
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
