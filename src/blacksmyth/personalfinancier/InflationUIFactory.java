/**
 * (c) 2013, Lindsay Bradford. licensed under a 
 * Creative Commons Attribution-ShareAlike 3.0 
 * Unported License.
 */

package blacksmyth.personalfinancier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import blacksmyth.general.FontIconProvider;
import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.personalfinancier.control.inflation.InflationConversionController;
import blacksmyth.personalfinancier.control.inflation.InflationFileController;
import blacksmyth.personalfinancier.control.inflation.InflationUndoManager;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.JUndoListeningButton;
import blacksmyth.personalfinancier.view.WidgetFactory;
import blacksmyth.personalfinancier.view.inflation.InflationConversionPanel;
import blacksmyth.personalfinancier.view.inflation.InflationTable;

class InflationUIFactory {

  public static JComponent createInflationComponent() {

    UIComponents.inflationModel = new InflationModel();
    
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.VERTICAL_SPLIT,
        createInflationItemPanel(), 
        createInflationSummaryPanel()
    );
    
    splitPane.setOneTouchExpandable(true);
    splitPane.setResizeWeight(0.5);
    
    return splitPane;
  }
  
  private static JComponent createInflationItemPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    UIComponents.inflationTable = new InflationTable(UIComponents.inflationModel);
    UIComponents.inflationFileController = new InflationFileController(UIComponents.inflationModel);

    panel.add(
        createInflationToolbar(),
        BorderLayout.PAGE_START
    );
    
    panel.add(
        createInflationTablePanel(),
        BorderLayout.CENTER
    );

    return panel;    
  }

  private static JToolBar createInflationToolbar() {
    
    JToolBar toolbar = new JToolBar();
    
    toolbar.add(
        createLoadButton()
    );

    toolbar.add(
        createSaveButton()    
    );

    toolbar.addSeparator();

    toolbar.add(
        createAddInflationButton()
    );

    toolbar.add(
        createRemoveInflationEntriesButton()
    );

    toolbar.addSeparator();
    
    toolbar.add(
        createUndoButton()    
    );

    toolbar.add(
        createRedoButton()    
    );

    return toolbar;
  }
  
  private static JButton createLoadButton() {
    UIComponents.LoadInflationAction = new AbstractAction("Open Inflation Data...") {
      
      public void actionPerformed(ActionEvent e) {
        InflationUndoManager.getInstance().discardAllEdits();
        UIComponents.inflationFileController.load(
            UIComponents.windowFrame
        );
      }
    };
    
    JButton button = new JButton(
        UIComponents.LoadInflationAction
    );
    
    //TODO: assign non-conflicting mnemonic
    button.setMnemonic(KeyEvent.VK_O);
    
    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_folder_open_alt
    );
    
    button.setForeground(Color.GREEN.darker());
    
    button.setToolTipText(" Load Inflation Data");
    
    return button;
  }

  private static JButton createSaveButton() {
    
    UIComponents.SaveInflationAction = new AbstractAction("Save Inflation Data") {
      public void actionPerformed(ActionEvent e) {
        InflationUndoManager.getInstance().discardAllEdits();
        UIComponents.inflationFileController.save(
            UIComponents.windowFrame
        );
      }
    };
    
    JButton button = new JButton(UIComponents.SaveInflationAction);

    button.setForeground(Color.GREEN.darker());

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_S, 
            Event.CTRL_MASK
        ), 
        UIComponents.SaveInflationAction
    );

    //TODO: assign non-conflicting mnemonic
    button.setMnemonic(KeyEvent.VK_S);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_save
    );

    button.setToolTipText(" Save the inflation data ");
    return button;
  }

  private static JButton createRemoveInflationEntriesButton() {
    JButton removeInflationEntriesButton = 
        WidgetFactory.createMultiSelectedtRowEnabledButton(UIComponents.inflationTable);

    removeInflationEntriesButton.setMnemonic(KeyEvent.VK_DELETE);
    
    removeInflationEntriesButton.setForeground(
        Color.RED.brighter()
   );
    
    FontIconProvider.getInstance().setGlyphAsText(
        removeInflationEntriesButton, 
        FontIconProvider.icon_minus
    );

    removeInflationEntriesButton.setToolTipText(
        "Remove selected inflation entry"
    );
    
    removeInflationEntriesButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.inflationTable.removeInflationEntries();
            UIComponents.inflationTable.requestFocus();
          }
        }
    );
    
    return removeInflationEntriesButton;
  }

  private static JButton createAddInflationButton() {
    JButton addInflationEntryButton = new JButton();
    
    addInflationEntryButton.setMnemonic(KeyEvent.VK_INSERT);

    FontIconProvider.getInstance().setGlyphAsText(
        addInflationEntryButton, 
        FontIconProvider.icon_plus
    );
    
    addInflationEntryButton.setToolTipText(
        "Add a new Inflation Entry"
    );
    
    addInflationEntryButton.setForeground(
        Color.GREEN.brighter()
   );
    
    addInflationEntryButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            UIComponents.inflationTable.addInflationEntry();
            UIComponents.inflationTable.requestFocus();
          }
        }
    );

    return addInflationEntryButton;
  }

  private static JButton createUndoButton() {
    AbstractAction undoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (InflationUndoManager.getInstance().canUndo()) {
           InflationUndoManager.getInstance().undo();
        }
      }
    };
    
    JUndoListeningButton button = new JUndoListeningButton(undoAction) {
      
      protected void handleCantUndoState() {
        this.setEnabled(false);
      }
      
      protected void handleCanUndoState() {
        this.setEnabled(true);
      }
    };
    
    InflationUndoManager.getInstance().addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_undo
    );
    
    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, 
            Event.CTRL_MASK
        ), 
        undoAction
    );
    
    button.setForeground(Color.GREEN);

    button.setToolTipText(" Undo ");

    return button;
  }

  private static JButton createRedoButton() {
    AbstractAction redoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (InflationUndoManager.getInstance().canRedo()) {
           InflationUndoManager.getInstance().redo();
        }
      }
    };

    JUndoListeningButton button = new JUndoListeningButton(redoAction) {
      
      protected void handleCantRedoState() {
        this.setEnabled(false);
      }
      
      protected void handleCanRedoState() {
        this.setEnabled(true);
      }
    };
    
    InflationUndoManager.getInstance().addObserver(button);

    FontIconProvider.getInstance().setGlyphAsText(
        button, 
        FontIconProvider.icon_repeat
    );

    BlacksmythSwingUtilities.bindKeyStrokeToAction(
        button, 
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Y, 
            Event.CTRL_MASK
        ), 
        redoAction
    );
    
    button.setForeground(Color.GREEN);

    button.setToolTipText(" Redo ");

    return button;
  }
  
  private static Component createInflationTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " Inflation Entries ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );

    panel.add(
        new JScrollPane(UIComponents.inflationTable),
        BorderLayout.CENTER
    );
    
    return panel;
  }

  private static JComponent createInflationSummaryPanel() {
    JPanel panel  = new JPanel(new GridLayout(1,2));
    
    panel.add(
        createInflationConversionPanel()
    );

    panel.add(
        createInflationGraphPanel()
    );

    return panel;    
  }

  private static Component createInflationConversionPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " Value Conversion ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );

    final InflationConversionModel conversionModel = new InflationConversionModel(
        UIComponents.inflationModel    
    );

    final InflationConversionPanel conversionPanel = 
        new InflationConversionPanel(
            new InflationConversionController(
                conversionModel
            )       
    );
    
    conversionModel.addObserver(conversionPanel);

    panel.add(
        new JScrollPane(conversionPanel),
        BorderLayout.CENTER
    );
    
    return panel;
  }

  private static Component createInflationGraphPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(
            WidgetFactory.createColoredTitledBorder(
                " Inflation Graph ",
                Color.GRAY.brighter()
            ),
            new EmptyBorder(0,3,5,4)
        )
    );
    
    panel.add(
        new JLabel("A pretty graph goes here!"),
        BorderLayout.CENTER
    );
    
    return panel;
  }

}
