package blacksmyth.personalfinancier.view.inflation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import blacksmyth.general.BlacksmythSwingUtilities;
import blacksmyth.personalfinancier.control.inflation.InflationConversionController;
import blacksmyth.personalfinancier.model.inflation.InflationConversionModel;
import blacksmyth.personalfinancier.view.WidgetFactory;

public class InflationConversionPanel extends JPanel implements Observer {
  
  private GregorianCalendar earliestDate;
  private GregorianCalendar latestDate;
  
  private GridBagLayout      gbl = new GridBagLayout();
  private GridBagConstraints gbc = new GridBagConstraints();
  
  private Vector<JComponent> labels = new Vector<JComponent>();
  private Vector<JComponent> fields = new Vector<JComponent>();
  
  private JFormattedTextField initialDateField = WidgetFactory.createDateTextField();
  private JFormattedTextField  initialValueField = WidgetFactory.createAmountTextField();

  private JFormattedTextField conversionDateField = WidgetFactory.createDateTextField();
  private JFormattedTextField  conversionValueField = WidgetFactory.createAmountTextField();
  
  private JFormattedTextField  inflationOverPeriodField = WidgetFactory.createPercentTextField();
  private JFormattedTextField  inflationPerAnnumField = WidgetFactory.createPercentTextField();
  
  private JLabel dateRangeLabel;
  
  private InflationConversionController controller;
  
  public InflationConversionPanel(InflationConversionController controller) {
    super();
    this.controller = controller;
    setLayout(gbl);
    createView();
  }
  
  private void createView() {
    gbc.insets = new Insets(0,5,5,5);

    gbc.gridwidth    = 1;
    gbc.gridheight   = 1;
    gbc.weightx      = 0.25;
    gbc.weighty      = 0;
    
    addFieldPair(
      new JLabel("Initial Date :"),
      initialDateField,
      0,0
    );

    final JFormattedTextField initialDateFieldFinal = this.initialDateField;
    
    this.initialDateField.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          controller.setInitialDate(
              initialDateFieldFinal.getText()
          );
        }
      }
    );
    
    addFieldPair(
        new JLabel("Initial Value :"),
        initialValueField,
        0,1
    );
    
    final JFormattedTextField initialValueFieldFinal = this.initialValueField;
    
    this.initialValueField.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          controller.setInitialValue(
              initialValueFieldFinal.getText()
          );
        }
      }
    );

    addFieldPair(
        new JLabel("Conversion Date :"),
        conversionDateField,
        2,0
    );

    final JFormattedTextField conversionDateFieldFinal = this.conversionDateField;
    
    this.conversionDateField.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          controller.setConversionDate(
              conversionDateFieldFinal.getText()
          );
        }
      }
    );
    
    addFieldPair(
        new JLabel("Conversion Value :"),
        conversionValueField,
        2,1
    );
    
    final JFormattedTextField conversionValueFieldFinal = this.conversionValueField;
    
    this.conversionValueField.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          controller.setConversionValue(
              conversionValueFieldFinal.getText()
          );
        }
      }
    );
    
    gbc.insets = new Insets(10,5,5,5);

    addFieldPair(
        new JLabel("Inflation :"),
        inflationOverPeriodField,
        0,3
    );
    
    inflationOverPeriodField.setEnabled(false);
    fields.add(inflationOverPeriodField);

    addFieldPair(
        new JLabel("Inflation / Annum :"),
        inflationPerAnnumField,
        2,3
    );

    inflationPerAnnumField.setEnabled(false);
    fields.add(inflationPerAnnumField);
    
    gbc.insets = new Insets(10,5,5,5);

    BlacksmythSwingUtilities.equalizeComponentSizes(labels);
    BlacksmythSwingUtilities.equalizeComponentSizes(fields);
    
    labels.clear(); fields.clear();

    dateRangeLabel = new JLabel(getDateRangeMessage());
    dateRangeLabel.setHorizontalAlignment(SwingConstants.CENTER);

    gbc.insets = new Insets(12,0,0,0);

    gbc.fill         = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth    = GridBagConstraints.REMAINDER;
    gbc.gridx        = 0;
    gbc.gridy        = 4;
    gbc.weightx      = 1;

    add(dateRangeLabel, gbc);

  }
  
  private String getDateRangeMessage() {
    final String earliestDateString = dateToString(earliestDate);
    final String latestDateString   = dateToString(latestDate);
    
   return "CPI calculations cover the date range " + earliestDateString + " - " + latestDateString + ".";
  }
  
  private String dateToString(GregorianCalendar date) {
    if (date == null) {
      return "";
      
    }
    return WidgetFactory.DATE_FORMAT.format(
        date.getTime()
    );
  }

  protected void addFieldPair(JLabel label, JFormattedTextField field, int gridX, int gridY) {

    label.setHorizontalAlignment(SwingConstants.RIGHT);
    
    gbc.anchor       = GridBagConstraints.EAST;
    gbc.gridx        = gridX;
    gbc.gridy        = gridY;

    add(label, gbc);

    gbc.anchor       = GridBagConstraints.WEST;
    gbc.gridx        = gridX + 1;

    add(field, gbc);
    
    labels.add(label);
    fields.add(field);
  }
  
  @Override
  public void update(Observable o, Object arg) {
    assert o.getClass().equals(InflationConversionModel.class) : "View/Model mismatch.";
    
    InflationConversionModel model = (InflationConversionModel) o;

    this.initialDateField.setValue(
          model.getInitialDate().getTime()
    );

    this.initialValueField.setValue(
          model.getInitialValue().getTotal().doubleValue()
    );

    this.conversionDateField.setValue(
        model.getConversionDate().getTime()
    );
    
    this.conversionValueField.setValue(
          model.getConversionValue().getTotal().doubleValue()
    );

    this.inflationOverPeriodField.setValue(
          model.getInflationOverPeriod()
    );

    this.inflationPerAnnumField.setValue(
          model.getInflationPerAnnum()
    );

    this.earliestDate = (GregorianCalendar) model.getEarliestDate();
    this.latestDate = (GregorianCalendar) model.getLatestLatestDate();
    
    dateRangeLabel.setText(
        getDateRangeMessage()    
    );
  }
}
