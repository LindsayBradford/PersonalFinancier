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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import blacksmyth.general.file.IFileHandler;
import blacksmyth.general.swing.Utilities;
import blacksmyth.general.swing.FontIconProvider;

import blacksmyth.personalfinancier.control.FileHandlerBuilder;
import blacksmyth.personalfinancier.control.inflation.InfllationObserver;
import blacksmyth.personalfinancier.control.inflation.InflationConversionController;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.model.inflation.InflationEntry;
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

  public static IPersonalFinancierComponentView createComponent(PersonalFinancierView view) {
    inflationModel = new InflationModel();
    inflationFileController = buildInflationFileController(view);
    
    InflationComponent newComponent = new InflationComponent(
        JSplitPane.VERTICAL_SPLIT, 
        buildBaseInflationPanel(),
        createInflationSummaryPanel()
    );

    newComponent.putClientProperty("AppMessage", "Explore money value changing with inflation in this tab.");
    newComponent.putClientProperty("TabName", "Inflation");

    newComponent.setOneTouchExpandable(true);
    newComponent.setResizeWeight(0.5);

    return newComponent;
  }
 
  private static IFileHandler<InflationFileContent> buildInflationFileController(PersonalFinancierView view) {
    IFileHandler<InflationFileContent> controller = FileHandlerBuilder.buildInflationHandler(view.getWindowFrame(), inflationModel);
    controller.addObserver(view.getMessageViewer());
    
    return controller;
  }
  
  private static JPanel buildBaseInflationPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.add(createInflationTablePanel(), BorderLayout.CENTER);
    panel.add(createInflationToolbar(), BorderLayout.PAGE_START);

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

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_folder_open_o);

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

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
        SaveInflationAction);

    // TODO: assign non-conflicting mnemonic
    button.setMnemonic(KeyEvent.VK_S);

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_save);

    button.setToolTipText(" Save the inflation data ");
    return button;
  }

  private static JButton createRemoveInflationEntriesButton() {
    JButton removeInflationEntriesButton = WidgetFactory.createMultiSelectedtRowEnabledButton(inflationTable);

    removeInflationEntriesButton.setMnemonic(KeyEvent.VK_DELETE);

    removeInflationEntriesButton.setForeground(Color.RED.brighter());

    Utilities.setGlyphAsText(removeInflationEntriesButton, FontIconProvider.FontIcon.fa_minus);

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

    Utilities.setGlyphAsText(addInflationEntryButton, FontIconProvider.FontIcon.fa_plus);

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

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_undo);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
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

    Utilities.setGlyphAsText(button, FontIconProvider.FontIcon.fa_repeat);

    Utilities.bindKeyStrokeToAction(button, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK),
        redoAction);

    button.setForeground(Color.GREEN);

    button.setToolTipText(" Redo ");

    return button;
  }

  private static Component createInflationTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
      new CompoundBorder(
        WidgetFactory.createColoredTitledBorder(" Inflation Entries ", Color.GRAY.brighter()),
        new EmptyBorder(0, 3, 5, 4)
      )
    );

    inflationTable = new InflationTable(inflationModel);
    panel.add(new JScrollPane(inflationTable), BorderLayout.CENTER);

    return panel;
  }

  private static JComponent createInflationSummaryPanel() {
    JSplitPane splitPane = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT, 
        createInflationConversionPanel(), 
        createInflationGraphPanel()
    );
    splitPane.setOneTouchExpandable(true);
    
    return splitPane;
  }
  
  private static Component createInflationConversionPanel() {
    JPanel borderPanel = new JPanel(new BorderLayout());

    borderPanel.setBorder(
      new CompoundBorder(
        WidgetFactory.createColoredTitledBorder(" Value Conversion ", Color.GRAY.brighter()),
        new EmptyBorder(0, 3, 5, 4)
      )
    );

    borderPanel.add(
        buildBaseConversionPanel()
    );

    return borderPanel;
  }

  private static InflationConversionPanel buildBaseConversionPanel() {
    InflationConversionModel conversionModel = new InflationConversionModel(inflationModel);
    InflationConversionPanel conversionPanel = new InflationConversionPanel(
        new InflationConversionController(conversionModel)
    );
    
    conversionPanel.setMinimumSize(conversionPanel.getPreferredSize());
    conversionModel.addListener(conversionPanel);

    return conversionPanel;
  }

  private static Component createInflationGraphPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBorder(
      new CompoundBorder(
        WidgetFactory.createColoredTitledBorder(" Inflation Graph ", Color.GRAY.brighter()),
         new EmptyBorder(0, 3, 5, 4)
      )
    );

    panel.add(new InflationChartPanel(inflationModel), BorderLayout.CENTER);

    return panel;
  }
}

@SuppressWarnings("serial")
class InflationChartPanel extends JPanel implements InfllationObserver {
  private static Logger LOG = LogManager.getLogger(InflationChartPanel.class);

  private InflationModel model;
  
  static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
  
  class XYChartData {
    public List<Date> xData;
    public List<Double> yData;
    
    public XYChartData() {
      this.xData = new LinkedList<Date>();
      this.yData = new LinkedList<Double>();
    }
  };
  
  public InflationChartPanel (InflationModel model) {
    super(new BorderLayout());

    this.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.setBackground(Color.WHITE);

    setModel(model);
  }

  private void setModel(InflationModel model) {
    this.model = model;
    model.addListener(this);
  }

  protected InflationModel getModel() {
    return this.model;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    forceUpdate();
  }

  private void forceUpdate() {
    displayChart(
      createChart(createTitleText(), createChartData())
    );
  }
  
  private String createTitleText() {
    return "Inflation Over Time";
  };

  private XYChartData createChartData() {
    XYChartData chartData = new XYChartData();

    for (InflationEntry entry : model.getInflationList()) {
      chartData.xData.add(
          toLegacyDate(entry.getDate())
      );

      chartData.yData.add(
          entry.getCPIValue()
      );
    }
    
    return chartData;
  }
  
  private Date toLegacyDate(LocalDate localDate) {
    ZoneId localZone = ZoneId.systemDefault();
    Instant localDateAsInstant = localDate.atStartOfDay().atZone(localZone).toInstant();
    return Date.from(localDateAsInstant);
  }
  
  private XYChartData createTargetChartData(XYChartData actualData) {
    
    // Sets Inflation targets by first sampling the initial 1996 RBA CPI figure, and
    // applying a 2.5% increase per year, ignoring the actual fluctuations per year.
    
    // This is a simplification of what the RBA actually does, but suits my purposes well enough.
    
    Date targetCurrentDate = toLegacyDate("31/12/1995");  // RBA officially starts targeting inflation in 1996. 
    double currentTargetCPI = deriveInitialTargetCPI(actualData, targetCurrentDate);
    
    XYChartData targetData = new XYChartData();

    Date targetFinalDate = progressLegacyDateOneYear(actualData.xData.getLast());
    while (targetCurrentDate.before(targetFinalDate)) {

      targetData.xData.add(targetCurrentDate);
      targetData.yData.add(currentTargetCPI);
      
      currentTargetCPI =  deriveNewTargetCPIFrom(currentTargetCPI);
      targetCurrentDate = progressLegacyDateOneYear(targetCurrentDate);
    }

    return targetData;
  }
  
  private double deriveNewTargetCPIFrom(double currentTargetCPI) {
    return (double) Math.round(currentTargetCPI * 10.25) / 10;   // return 2.5% of current (midway in 2-3% range)
  }
  
  private Date progressLegacyDateOneYear(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR,  1);
    return calendar.getTime();
  }

  private double deriveInitialTargetCPI(XYChartData actualData, Date targetStartDate) {
    double initialTargetCPI = 0;
    
    for (int index = 0; index < actualData.xData.size(); index++) {
      if (actualData.xData.get(index).before(targetStartDate)) {
        continue;
      } else {
        initialTargetCPI = deriveNewTargetCPIFrom(actualData.yData.get(index));
        break;
      }
    }
    return initialTargetCPI;
  }
  
  private Date toLegacyDate(String dateAsString) {
    Date legacyDate = nowAsLegacyDate();
    try {
      legacyDate = new SimpleDateFormat(WidgetFactory.DATE_FORMAT_PATTERN).parse(dateAsString);
    } catch (ParseException e) {
      LOG.error("Failed parsing date string [{}] with format [{}]. Using today.",dateAsString, WidgetFactory.DATE_FORMAT_PATTERN);
    }
    return legacyDate;
  }
  
  private Date nowAsLegacyDate() {
    return Date.from(Instant.now());
  }

  private XYChart createChart(String title, XYChartData data) {
    XYChart chart =
        new XYChartBuilder()
            .title(title + deriveDateRangeString(data))
            .yAxisTitle("Consumer Price Index")
            .build();
    
    applyDesiredStyle(chart.getStyler());
    
    addTargetChartData(data, chart);
    addActualChartData(data, chart);
    
    return chart;
  }

  private void addTargetChartData(XYChartData data, XYChart chart) {
    XYChartData targetData = createTargetChartData(data);
    XYSeries targetCpiSeries = chart.addSeries("Target", targetData.xData, targetData.yData);
    
    targetCpiSeries.setLineColor(Color.BLUE);
    targetCpiSeries.setLineWidth(2);
    targetCpiSeries.setMarker(SeriesMarkers.DIAMOND);
    targetCpiSeries.setMarkerColor(Color.BLUE);
  }
  
  private void addActualChartData(XYChartData data, XYChart chart) {
    XYSeries actualCpiSeries = chart.addSeries("Actual", data.xData, data.yData);
    
    actualCpiSeries.setLineWidth(2);
    actualCpiSeries.setLineColor(Color.green.darker());
    actualCpiSeries.setMarker(SeriesMarkers.CIRCLE);
    actualCpiSeries.setMarkerColor(Color.green.darker());
  }

  
  private void applyDesiredStyle(XYStyler styler) {
    styler.setZoomEnabled(true);
    styler.setLegendPosition(LegendPosition.InsideNW);
    styler.setXAxisTitleVisible(false);
    
    styler.setYAxisDecimalPattern("0.0");
    styler.setChartBackgroundColor(Color.WHITE);
    styler.setDatePattern("dd/MM/yyyy");
    
    styler.setToolTipsEnabled(true);
    styler.setToolTipFont(new Font("Courier New", Font.BOLD, 12));
    styler.setToolTipHighlightColor(Color.RED.darker());
    styler.setToolTipHighlightColor(Color.RED.darker());
  }
  
  private String deriveDateRangeString(XYChartData data) {
    return new StringBuilder(": ")
        .append(dateFormatter.format(data.xData.getFirst()))
        .append(" - ")
        .append(dateFormatter.format(data.xData.getLast()))
        .toString();
  }
 
  private void displayChart(XYChart chart) {
    this.removeAll(); 
    this.add(new XChartPanel<XYChart>(chart), BorderLayout.CENTER);
    Utilities.refreshLater(this);
  }
  
}

@SuppressWarnings("serial")
final class InflationComponent extends JSplitPane implements IPersonalFinancierComponentView {

  public InflationComponent(int verticalSplit, JComponent inflationItemPanel, JComponent inflationSummaryPanel) {
    super(verticalSplit, inflationItemPanel, inflationSummaryPanel);
    this.setOneTouchExpandable(true);
  }
}

