/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import blacksmyth.general.FontIconProvider;
import blacksmyth.general.RunnableQueueThread;
import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.personalfinancier.control.FileHandlerBuilder;
import blacksmyth.personalfinancier.control.UndoManagers;
import blacksmyth.personalfinancier.model.AccountModel;
import blacksmyth.personalfinancier.model.PreferencesModel;
import blacksmyth.personalfinancier.model.budget.BudgetModel;
import blacksmyth.personalfinancier.view.ApplicationMessageView;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class PersonalFinancierUIFactory {
  
  public static JFrame createJFrame() {
    UIComponents.accountModel = new AccountModel();
    UIComponents.budgetModel = new BudgetModel(UIComponents.accountModel);
    
    RunnableQueueThread.getInstance().start();
    
    UIComponents.windowFrame  = new JFrame("Personal Financier");
    
    UIComponents.windowFrame.setDefaultCloseOperation(
        WindowConstants.EXIT_ON_CLOSE
    );
    
    UIComponents.messageBar = createMessageBar();

    UIComponents.windowFrame.getContentPane().add(
        UIComponents.messageBar.getPanel(),
      BorderLayout.PAGE_END
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
        
    
    UIComponents.windowFrame.setBounds(
      PreferencesModel.getInstance().getWindowBounds()
    );
    
    UIComponents.windowFrame.addComponentListener(
      new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          updatePreferences();
        }
        
        public void componentMoved(ComponentEvent e) {
          updatePreferences();
        }
        
        private void updatePreferences() {
      	  PreferencesModel.getInstance().setWindowBounds(
      		  UIComponents.windowFrame.getBounds()
          );
        }
      }
    );
    
    return UIComponents.windowFrame;
  }
  
  private static Component createMainToolbar() {
    JToolBar toolbar = new JToolBar();
    
    UIComponents.budgetFileController = 
        FileHandlerBuilder.buildBudgetHandler(
            UIComponents.windowFrame,
            UIComponents.budgetModel
        );
    
    UIComponents.budgetFileController.addObserver(
        UIComponents.messageBar
    );
    
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
        FontIconProvider.icon_info_circle
    );
    
    button.setToolTipText(" About ");
    
    button.putClientProperty("AppMessage", "About this application...");
    UIComponents.messageBar.bindViewComponent(button);
    
    button.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(
                baseFrame,
                "<html><body>" +
                "This application depends on unmodified binary distributions of the following libraries:" + 
                 "<ul>" +
                 "<li> JTattoo (http://www.jtattoo.net/) for a radically different Swing Look & Feel" +
                 "<li> FontAwesome (http://fortawesome.github.com/Font-Awesome) for icon-friendly font glyphs" +
                 "<li> GRAL (http://trac.erichseifert.de/gral/) for charting." +
                 "<li> Json-io (http://code.google.com/p/json-io/) for Java JSon serialisation" +
                 "</ul>" +
                 "This project wouldn't be possible without these excellent libraries." +
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
    
    button.putClientProperty("AppMessage", "Set your preferences (not yet implemented)...");
    UIComponents.messageBar.bindViewComponent(button);

    
    return button;
  }

  private static JButton createSaveButton() {
    
    JButton button = new JButton(UIComponents.SaveBudgetAction);

    button.setForeground(Color.GREEN.darker());

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
    
    button.putClientProperty("AppMessage", "Save your personal budget...");
    UIComponents.messageBar.bindViewComponent(button);

    return button;
  }

  private static JButton createLoadButton() {
    
    JButton button = new JButton(
        UIComponents.LoadBudgetAction
    );
    
    button.setMnemonic(KeyEvent.VK_O);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_folder_open_o
    );
    
    button.setForeground(Color.GREEN.darker());
    
    button.setToolTipText(" Load a budget ");

    button.putClientProperty("AppMessage", "Load your personal budget...");
    UIComponents.messageBar.bindViewComponent(button);
    
    return button;
  }

  private static JComponent createContentPane() {
    final JTabbedPane pane = new JTabbedPane();
    final JComponent budgetComponent = BudgetUIFactory.createBudgetComponent();
    
    pane.addTab(
        "Budget", 
        budgetComponent
    );

    budgetComponent.putClientProperty(
        "AppMessage", 
        "Manage your personal budget in this tab."
    );
    
    final JComponent inflationComponent = InflationUIFactory.createInflationComponent();

    inflationComponent.putClientProperty(
        "AppMessage", 
        "Explore money value changing with inflation in this tab."
    );

    pane.addTab(
        "Inflation", 
        inflationComponent
    );
    
    pane.addMouseMotionListener(
        new MouseMotionAdapter() {
          
          @Override
          public void mouseMoved(MouseEvent e) {
            for (int i = 0; i < pane.getTabCount(); i++){
              if (pane.getBoundsAt(i).contains(e.getPoint())) { 
                JComponent component = (JComponent) pane.getComponentAt(i);
               
                UIComponents.messageBar.showMessage(
                    (String) component.getClientProperty("AppMessage"),
                    PreferencesModel.getInstance().getAppMessageTimeout()
                );
                
              } // if event point is within tab's bounds
            } // for all tab pane indices
          }
    });
    
    WidgetFactory.enableSelectionHilightedTabPane(pane);
    
    return pane;
  }
  
  private static ApplicationMessageView createMessageBar() {
    return new ApplicationMessageView();
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
        UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
        UIComponents.budgetFileController.load();
      }
    };
    
   JMenuItem menuItem = new JMenuItem(
          UIComponents.LoadBudgetAction
          
   );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
        UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
        UIComponents.budgetFileController.save();
      }
    };

    JMenuItem menuItem = new JMenuItem(
        UIComponents.SaveBudgetAction    
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
        UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
        UIComponents.budgetFileController.saveAs();
      }
    };
    
    JMenuItem menuItem = new JMenuItem(
        UIComponents.SaveAsBudgetAction    
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
        UndoManagers.BUDGET_UNDO_MANAGER.discardAllEdits();
        UIComponents.budgetFileController.save();
        System.exit(0);
      }
    };
    
    JMenuItem menuItem = new JMenuItem(
        UIComponents.ExitAction    
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
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
