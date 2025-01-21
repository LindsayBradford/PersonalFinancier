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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
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

import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.general.FontIconProvider;
import blacksmyth.general.file.IFileHandler;
import blacksmyth.personalfinancier.control.FileHandlerBuilder;
import blacksmyth.personalfinancier.control.inflation.InflationConversionController;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.model.inflation.InflationFileContent;
import blacksmyth.personalfinancier.model.inflation.InflationModel;
import blacksmyth.personalfinancier.view.IPersonalFinancierComponentView;
import blacksmyth.personalfinancier.view.JUndoListeningButton;
import blacksmyth.personalfinancier.view.PersonalFinancierView;
import blacksmyth.personalfinancier.view.WidgetFactory;
import blacksmyth.personalfinancier.view.inflation.InflationConversionPanel;
import blacksmyth.personalfinancier.view.inflation.InflationTable;

class InflationUIFactory {

  private static InflationModel inflationModel;
  private static InflationTable inflationTable;

  private static Action LoadInflationAction;
  private static Action SaveInflationAction;

  private static IFileHandler<InflationFileContent> inflationFileController;

  public static IPersonalFinancierComponentView createInflationComponent(PersonalFinancierView view) {

    inflationModel = new InflationModel();

    InflationComponent newComponent = new InflationComponent(JSplitPane.VERTICAL_SPLIT, createInflationItemPanel(view),
        createInflationSummaryPanel());

    newComponent.putClientProperty("AppMessage", "Explore money value changing with inflation in this tab.");

    newComponent.putClientProperty("TabName", "Inflation");

    newComponent.setOneTouchExpandable(true);
    newComponent.setResizeWeight(0.5);

    return newComponent;
  }

  private static JComponent createInflationItemPanel(PersonalFinancierView view) {
    JPanel panel = new JPanel(new BorderLayout());

    inflationTable = new InflationTable(inflationModel);
    inflationFileController = FileHandlerBuilder.buildInflationHandler(view.getWindowFrame(), inflationModel);

    inflationFileController.addObserver(view.getMessageViewer());

    panel.add(createInflationToolbar(), BorderLayout.PAGE_START);

    panel.add(createInflationTablePanel(), BorderLayout.CENTER);

    return panel;
  }

  private static JToolBar createInflationToolbar() {

    JToolBar toolbar = new JToolBar();

    toolbar.add(createLoadButton());

    toolbar.add(createSaveButton());

    toolbar.addSeparator();

    toolbar.add(createAddInflationButton());

    toolbar.add(createRemoveInflationEntriesButton());

    toolbar.addSeparator();

    toolbar.add(createUndoButton());

    toolbar.add(createRedoButton());

    return toolbar;
  }

  @SuppressWarnings("serial")
  private static JButton createLoadButton() {
    LoadInflationAction = new AbstractAction("Open Inflation Data...") {

      public void actionPerformed(ActionEvent e) {
        inflationModel.getUndoManager().discardAllEdits();
        inflationFileController.load();
      }
    };

    JButton button = new JButton(LoadInflationAction);

    // TODO: assign non-conflicting mnemonic
    button.setMnemonic(KeyEvent.VK_O);

    BlacksmythSwingUtilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_folder_open_o);

    button.setForeground(Color.GREEN.darker());

    button.setToolTipText(" Load Inflation Data");

    return button;
  }

  @SuppressWarnings("serial")
  private static JButton createSaveButton() {

    SaveInflationAction = new AbstractAction("Save Inflation Data") {
      public void actionPerformed(ActionEvent e) {
        inflationFileController.save();
      }
    };

    JButton button = new JButton(SaveInflationAction);

    button.setForeground(Color.GREEN.darker());

    BlacksmythSwingUtilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
        SaveInflationAction);

    // TODO: assign non-conflicting mnemonic
    button.setMnemonic(KeyEvent.VK_S);

    BlacksmythSwingUtilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_save);

    button.setToolTipText(" Save the inflation data ");
    return button;
  }

  private static JButton createRemoveInflationEntriesButton() {
    JButton removeInflationEntriesButton = WidgetFactory.createMultiSelectedtRowEnabledButton(inflationTable);

    removeInflationEntriesButton.setMnemonic(KeyEvent.VK_DELETE);

    removeInflationEntriesButton.setForeground(Color.RED.brighter());

    BlacksmythSwingUtilities.setGlyphAsText(removeInflationEntriesButton, FontIconProvider.FontIcon.fa_minus);

    removeInflationEntriesButton.setToolTipText("Remove selected inflation entry");

    removeInflationEntriesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        inflationTable.removeInflationEntries();
        inflationTable.requestFocus();
      }
    });

    return removeInflationEntriesButton;
  }

  private static JButton createAddInflationButton() {
    JButton addInflationEntryButton = new JButton();

    addInflationEntryButton.setMnemonic(KeyEvent.VK_INSERT);

    BlacksmythSwingUtilities.setGlyphAsText(addInflationEntryButton, FontIconProvider.FontIcon.fa_plus);

    addInflationEntryButton.setToolTipText("Add a new Inflation Entry");

    addInflationEntryButton.setForeground(Color.GREEN.brighter());

    addInflationEntryButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        inflationTable.addInflationEntry();
        inflationTable.requestFocus();
      }
    });

    return addInflationEntryButton;
  }

  @SuppressWarnings("serial")
  private static JButton createUndoButton() {
    AbstractAction undoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (inflationModel.getUndoManager().canUndo()) {
          inflationModel.getUndoManager().undo();
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

    inflationModel.getUndoManager().addObserver(button);

    BlacksmythSwingUtilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_undo);

    BlacksmythSwingUtilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
        undoAction);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Undo ");

    return button;
  }

  @SuppressWarnings("serial")
  private static JButton createRedoButton() {
    AbstractAction redoAction = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (inflationModel.getUndoManager().canRedo()) {
          inflationModel.getUndoManager().redo();
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

    inflationModel.getUndoManager().addObserver(button);

    BlacksmythSwingUtilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_repeat);

    BlacksmythSwingUtilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK),
        redoAction);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Redo ");

    return button;
  }

  private static Component createInflationTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(WidgetFactory.createColoredTitledBorder(" Inflation Entries ", Color.GRAY.brighter()),
            new EmptyBorder(0, 3, 5, 4)));

    panel.add(new JScrollPane(inflationTable), BorderLayout.CENTER);

    return panel;
  }

  private static JComponent createInflationSummaryPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 2));

    panel.add(createInflationConversionPanel());

    panel.add(createInflationGraphPanel());

    return panel;
  }

  private static Component createInflationConversionPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(WidgetFactory.createColoredTitledBorder(" Value Conversion ", Color.GRAY.brighter()),
            new EmptyBorder(0, 3, 5, 4)));

    final InflationConversionModel conversionModel = new InflationConversionModel(inflationModel);

    final InflationConversionPanel conversionPanel = new InflationConversionPanel(
        new InflationConversionController(conversionModel));

    conversionModel.addListener(conversionPanel);

    panel.add(new JScrollPane(conversionPanel), BorderLayout.CENTER);

    return panel;
  }

  private static Component createInflationGraphPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
        new CompoundBorder(WidgetFactory.createColoredTitledBorder(" Inflation Graph ", Color.GRAY.brighter()),
            new EmptyBorder(0, 3, 5, 4)));

    panel.add(new JLabel("A pretty graph goes here!"), BorderLayout.CENTER);

    return panel;
  }
}

@SuppressWarnings("serial")
final class InflationComponent extends JSplitPane implements IPersonalFinancierComponentView {

  public InflationComponent(int verticalSplit, JComponent inflationItemPanel, JComponent inflationSummaryPanel) {
    super(verticalSplit, inflationItemPanel, inflationSummaryPanel);
  }
}
